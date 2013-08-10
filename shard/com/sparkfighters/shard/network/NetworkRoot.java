package com.sparkfighters.shard.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shard.network.bridge.net.PlayerDisconnected;

import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public class NetworkRoot {

	DatagramChannel channel = null;
	HashMap<SocketAddress, Connection> connections = new HashMap<>();
	ByteBuffer recvbuf = ByteBuffer.allocate(2048);
	BridgeRoot br = null;
	
	/**
	 * @param netifc Network interface name
	 * @param port Port number
	 * @throws IOException 
	 */
	public NetworkRoot(String netifc, int port, BridgeRoot br) throws IOException {
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(netifc, port));
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
		}
				
	}
	
	
	public void on_disconnected(SocketAddress sa) {
		// a new socket was disconnected
		Connection cn = this.connections.get(sa);
		this.connections.remove(sa);
		if (!cn.is_logged_in) return;	// not logged in - no problem
			
		this.br.send_to_executor(new PlayerDisconnected(cn.player_id));
	}
	
	public void on_connected(SocketAddress sa) {
		// new user connected
		Connection nc = new Connection();
		this.connections.put(sa, nc);
	}

}
