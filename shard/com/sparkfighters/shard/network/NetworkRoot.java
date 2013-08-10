package com.sparkfighters.shard.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public class NetworkRoot {

	DatagramChannel channel = null;
	HashMap<SocketAddress, Connection> connections = new HashMap<>();
	ByteBuffer recvbuf = ByteBuffer.allocate(2048);
	
	/**
	 * @param netifc Network interface name
	 * @param port Port number
	 * @throws IOException 
	 */
	public NetworkRoot(String netifc, int port) throws IOException {
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
			// This is a brand new connection!!!
			this.on_connected(sa);
		} else {
			// This is an existing connection
			try {
				conn.on_received(Packet.from_bytes(this.recvbuf.array()));
			} catch (PacketMalformedError e) {
				this.on_disconnected(sa);
			}
		}
		
	}
	
	
	public void on_disconnected(SocketAddress sa) {
		// a new socket was disconnected
		Connection cn = this.connections.get(sa);
		if (!cn.is_logged_in) {
			// not logged in - no problem
			this.connections.remove(sa);
			return;
		}
	}
	
	public void on_connected(SocketAddress sa) {
		// new user connected
		Connection nc = new Connection();
		this.connections.put(sa, nc);
	}

}
