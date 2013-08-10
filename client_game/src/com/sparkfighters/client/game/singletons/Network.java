package com.sparkfighters.client.game.singletons;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

import pl.com.henrietta.lnx2.Connection;
import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public enum Network implements Runnable
{
	INSTANCE;
	
	DatagramChannel channel;
	SocketAddress connections;
	ByteBuffer recvbuf = ByteBuffer.allocate(2048);
	
	Thread thread;
	
	public void Connect()
	{
		if(thread.isAlive()==true) thread.stop();
		thread=new Thread
	}
	
	public void run() 
	{
		
	}

	
}
