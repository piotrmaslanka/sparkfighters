package com.sparkfighters.shard.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

import com.sparkfighters.shard.loader.JSONBattleDTO;
import com.sparkfighters.shard.loader.JSONUserDTO;
import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shard.network.bridge.exec.*;
import com.sparkfighters.shard.network.bridge.net.*;

import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.exceptions.NothingToRead;
import pl.com.henrietta.lnx2.exceptions.NothingToSend;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public class NetworkRoot {

	public DatagramChannel channel = null;
	public HashMap<SocketAddress, Connection> connections = new HashMap<>();
	public HashMap<Integer, Connection> connection_by_pid = new HashMap<>();
	public ByteBuffer recvbuf = ByteBuffer.allocate(2048);
	public BridgeRoot br = null;
	public JSONBattleDTO bpf = null;
	
	public boolean is_game_started = false;
	
	/**
	 * @param netifc Network interface name
	 * @param port Port number
	 * @throws IOException 
	 */
	public NetworkRoot(String netifc, int port, BridgeRoot br, JSONBattleDTO bpf) throws IOException {
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(netifc, port));
		channel.configureBlocking(false);
		this.bpf = bpf;
		this.br = br;
	}

	/**
	 * Runs an execution loop
	 * @return true if loop has done something
	 */
	public boolean select() {
		boolean work_was_done = false;
		boolean any_data_received = true;
		
		SocketAddress sa = null;
		try {
			sa = this.channel.receive(this.recvbuf);			
		} catch (IOException e) {
			any_data_received = false;
		}
		if (sa == null) any_data_received = false;
				
		if (any_data_received) {
			work_was_done = true;
			// strip out the readed bytes
			int how_much_readed = this.recvbuf.position();
			byte[] data_readed = new byte[how_much_readed];
			System.arraycopy(this.recvbuf.array(), 0, data_readed, 0, how_much_readed);
			this.recvbuf.clear();
			
			// is it an existing connection?
			Connection conn = this.connections.get(sa);
			if (conn == null) {
				// brand new connection!
				this.on_connected(sa);
				conn = this.connections.get(sa);
			}
				
			try {
				conn.on_received(Packet.from_bytes(data_readed));
			} catch (PacketMalformedError e) {
				this.on_disconnected(sa);
				return true;
			}
			
			if (conn.has_new_data) {
				conn.has_new_data = false;
				try {
					this.on_has_data(sa, conn);
				} catch (NoSuchAlgorithmException | IOException e) {
					throw new RuntimeException("Java plays ball");
				}
			}
		}
		
		// Dispatch all outbound packets
		for (SocketAddress csa : this.connections.keySet()) {
			try {
				while (true) {
					Packet p = this.connections.get(csa).on_sendable();
					this.channel.send(ByteBuffer.wrap(p.to_bytes()), csa);
					work_was_done = true;
				}
			} catch (NothingToSend e) {
				continue;
			} catch (IOException e) { 
			  	throw new RuntimeException("IO error in network ops");
			}
		}
		
		// Ok, roll through all connections, kill timeouters
		for (SocketAddress csa : this.connections.keySet()) {
			Connection conn = this.connections.get(csa);
			if (conn.has_timeouted()) {
				System.out.println("Connection killed due to timeout");
				this.on_disconnected(csa);
				work_was_done = true;
				break;
			}
		}
		
		return work_was_done;
	}
	
	/**
	 * Called upon data exists on given connection
	 * @param sa Address of sender
	 * @param conn Connection on which data exists
	 * @throws UnsupportedEncodingException Called upon Java sucking cock
	 * @throws NoSuchAlgorithmException Called upon Java sucking cock
	 */
	public void on_has_data(SocketAddress sa, Connection conn) throws UnsupportedEncodingException,
																      NoSuchAlgorithmException,
																      IOException {
		// handle ping
		try { 
			conn.getChannel(1).write(conn.getChannel(1).read());
		} catch (NothingToRead e) {}
		
		if (conn.login_phase == 0) {
			// it's either confirmation data or login request.
			// read it anyway
			byte[] p = null;
			try {
				p = conn.getChannel(0).read();
			} catch (NothingToRead e) {
				// If we were at login, data was received, and 0 is not readable, then this
				// is a clear violation of the protocol.
				this.on_disconnected(sa);
				return;
			}

			// ok, so what is this?
			if (conn.username == null) {
				// This is a login packet
				conn.username = new String(p, "UTF-8");
				
				// check it this does make sense, prime DTO if so
				conn.associated_dto = this.bpf.find_by_login(conn.username);
				if (conn.associated_dto == null) {
					// Invalid user!
					this.on_disconnected(sa);
					return;
				}
				// ok, we must send back the nonce
				conn.getChannel(0).write(conn.challenge_nonce);
			} else {
				// This is a nonce-confirmation
				// calculate SHA replica
				byte[] pwd;
				pwd = conn.associated_dto.password.getBytes("UTF-8");

				// Lol, why can't Java just concat two arrays?
				byte[] response = new byte[pwd.length + conn.challenge_nonce.length];
				System.arraycopy(pwd, 0, response, 0, pwd.length);
				System.arraycopy(conn.challenge_nonce, 0, response, pwd.length, conn.challenge_nonce.length);
				
				// Compute SHA-1
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(response);
				
				Formatter formatter = new Formatter();
				for (byte b : md.digest()) formatter.format("%02x", b);
				
				response = formatter.toString().getBytes("UTF-8");
				formatter.close();
				
				// Compare the nonce...
				if (Arrays.equals(response, p)) {	// It's All-Ok!
					// If the player was logged in right now, invalidate that connection
					Connection alrdy_logd = this.connection_by_pid.get(conn.player_id);
					if (alrdy_logd != null) {
						// set login_phase to 0 so that on_disconnect
						// doesn't annoy Executor
						alrdy_logd.login_phase = 0;
						this.on_disconnected(alrdy_logd.address);
						System.out.printf("CONN: Replacing %s\n", conn.username);
					} else
						System.out.printf("CONN: Connecting %s\n", conn.username);						
					
					conn.player_id = conn.associated_dto.id;
					conn.login_phase = 1;
					
					this.connection_by_pid.put(conn.player_id, conn);
					
					byte[] ok = {'O', 'K'};			// Send OK
					conn.getChannel(0).write(ok);
					
								// Send info about map and players
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bos.write(Integer.toString(this.bpf.map).getBytes("UTF-8"));
					
					bos.write(0);
					bos.write(Integer.toString(conn.player_id).getBytes("UTF-8"));
					
					for (JSONUserDTO user : this.bpf.users) {
						bos.write(0);
						bos.write(Integer.toString(user.hero_id).getBytes("UTF-8"));
						bos.write(0);
						bos.write(Integer.toString(user.weapon_id).getBytes("UTF-8"));
						bos.write(0);
						bos.write(Integer.toString(user.id).getBytes("UTF-8"));
						bos.write(0);
						bos.write(Integer.toString(user.team_id).getBytes("UTF-8"));
						bos.write(0);
						bos.write(user.username.getBytes("UTF-8"));						
					}
					conn.getChannel(0).write(bos.toByteArray());
					
					// Wait for RDY					
				} else {
					byte[] fail = {'F', 'A', 'I', 'L'};
					conn.getChannel(0).write(fail);	// send FAIL
					
					this.on_disconnected(sa);
					return;
				}
			}
			
		} else if (conn.login_phase == 1) {
			// Awaiting RDY
			byte[] dat_in;
			try {
				dat_in = conn.getChannel(0).read();
			} catch (NothingToRead e) {
				// too bad it hasn't arrived yet.
				return;
			}
			
			byte[] templ = {'R', 'D', 'Y'};	// what should arrive
			if (Arrays.equals(dat_in, templ)) {
				// he's ready
				conn.login_phase = 2;
				this.br.send_to_executor(new PlayerConnected(conn.player_id));
				this.br.feedback_network(new FBPlayerConnected(conn.player_id));
				
				// send '0' or '2' about game state
				byte[] gstate = { this.is_game_started ? (byte)'2' : (byte)'0' };
				conn.getChannel(0).write(gstate);
			}			
		} else {
			// Connection has been established
			
			// Handle controller update input
			try {
				byte[] data = conn.getChannel(2).read();
				if (data.length != 6) {
					System.out.format("NET: Protocol violation at ch2 by %d. Seen %d bytes.\n", conn.player_id, data.length);
					this.on_disconnected(sa);
					return;
				}
				
				int mousex = data[0]*256 + data[1];
				int mousey = data[2]*256 + data[3];
				
				boolean lmb = (data[4] & 16) > 0;
				boolean rmb = (data[4] & 32) > 0;
				
				boolean up = (data[4] & 1) > 0;
				boolean right = (data[4] & 2) > 0;
				boolean down = (data[4] & 4) > 0;
				boolean left = (data[4] & 8) > 0;
				
				conn.lag_state = (data[5] & 0xFF) * 4;
				
				this.br.send_to_executor(new InputStatusChanged(conn.player_id, mousex, mousey, 
																up, right, down, left, lmb, rmb));				
			} catch (NothingToRead e) {}
		}
	}
	
	public void on_disconnected(SocketAddress sa) {
		// a new socket was disconnected
		Connection cn = this.connections.get(sa);
		this.connections.remove(sa);

		if (cn.username == null)
			System.out.printf("CONN: Disconnecting <UNKNOWN>\n");
		else
			System.out.printf("CONN: Disconnecting %s\n", cn.username);
		
		if (cn.login_phase < 2) return;	// not logged in - no problem
			
		this.connection_by_pid.remove(cn.player_id);
		this.br.feedback_network(new FBPlayerDisconnected(cn.player_id));
		this.br.send_to_executor(new PlayerDisconnected(cn.player_id));
	}
	
	public void on_connected(SocketAddress sa) {
		// new user connected
		Connection nc = new Connection();
		nc.address = sa;
		this.connections.put(sa, nc);
	}

}
