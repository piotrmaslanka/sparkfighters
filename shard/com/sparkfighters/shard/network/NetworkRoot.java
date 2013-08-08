package com.sparkfighters.shard.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class NetworkRoot {

	DatagramChannel channel = null;
	
	/**
	 * @param netifc Network interface name
	 * @param port Port number
	 * @throws IOException 
	 */
	public NetworkRoot(String netifc, int port) throws IOException {
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(netifc, port));
	}

}
