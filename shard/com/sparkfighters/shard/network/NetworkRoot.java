package com.sparkfighters.shard.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

import com.sparkfighters.shard.loader.JSONBattleDTO;
import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shard.network.bridge.net.PlayerConnected;
import com.sparkfighters.shard.network.bridge.net.PlayerDisconnected;

import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.exceptions.NothingToRead;
import pl.com.henrietta.lnx2.exceptions.NothingToSend;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public class NetworkRoot {

	DatagramChannel channel = null;
	HashMap<SocketAddress, Connection> connections = new HashMap<>();
	HashMap<Integer, Connection> connection_by_pid = new HashMap<>();
	ByteBuffer recvbuf = ByteBuffer.allocate(2048);
	BridgeRoot br = null;
	JSONBattleDTO bpf = null;
	
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

	public void select() {
		SocketAddress sa = null;
		try {
			sa = this.channel.receive(this.recvbuf);
		} catch (IOException e) {
			return;
		}
		
		// is it an existing connection?
		Connection conn = this.connections.get(sa);
		if (conn == null) {
			// brand new connection!
			this.on_connected(sa);
			conn = this.connections.get(sa);
		}
			
		try {
			conn.on_received(Packet.from_bytes(this.recvbuf.array()));
		} catch (PacketMalformedError e) {
			this.on_disconnected(sa);
			return;
		}
		
		if (conn.has_new_data) {
			conn.has_new_data = false;
			this.on_has_data(sa, conn);
		}
		
		// Dispatch all packets
		for (SocketAddress csa : this.connections.keySet()) {
			try {
				Packet p = this.connections.get(csa).on_sendable();
				this.channel.send(ByteBuffer.wrap(p.to_bytes()), csa);
			} catch (NothingToSend e) {
				continue;
			} catch (IOException e) { 
			  	throw new RuntimeException("IO error in network ops");
			}
		}
		
		// Ok, roll through all connections, kill timeouters
		for (SocketAddress csa : this.connections.keySet())
			if (conn.has_timeouted()) {
				this.on_disconnected(csa);
				break;
			}
	}
	
	public void on_has_data(SocketAddress sa, Connection conn) {
		if (!conn.is_logged_in) {
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
				try {
					conn.username = new String(p, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Java does not support UTF-8, wtf");
				}
				
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
				try {
					pwd = conn.associated_dto.password.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException("Java does not support UTF-8, wtf");
				}

				// Lol, why can't Java just concat two arrays?
				byte[] response = new byte[pwd.length + conn.challenge_nonce.length];
				System.arraycopy(pwd, 0, response, 0, pwd.length);
				System.arraycopy(conn.challenge_nonce, 0, response, pwd.length, conn.challenge_nonce.length);
				
				// Compare the nonce...
				if (response.equals(p)) {	// It's All-Ok!
					
					// If the player was logged in right now, invalidate that connection
					Connection alrdy_logd = this.connection_by_pid.get(conn.player_id);
					if (alrdy_logd != null) {
						// set is_logged_in to false so that on_disconnect
						// doesn't annoy Executor
						alrdy_logd.is_logged_in = false;
						this.on_disconnected(alrdy_logd.address);
					}
					
					conn.player_id = conn.associated_dto.hero_id;
					conn.is_logged_in = true;
					
					this.connection_by_pid.put(conn.player_id, conn);
					this.br.send_to_executor(new PlayerConnected(conn.player_id));
				} else {
					// Failed nonce check.
					this.on_disconnected(sa);
					return;
				}
			}
			
		}
	}
	
	public void on_disconnected(SocketAddress sa) {
		// a new socket was disconnected
		Connection cn = this.connections.get(sa);
		this.connections.remove(sa);
		if (!cn.is_logged_in) return;	// not logged in - no problem
			
		this.connection_by_pid.remove(cn.player_id);
		this.br.send_to_executor(new PlayerDisconnected(cn.player_id));
	}
	
	public void on_connected(SocketAddress sa) {
		// new user connected
		Connection nc = new Connection();
		nc.address = sa;
		this.connections.put(sa, nc);
	}

}
