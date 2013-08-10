package com.sparkfighters.client.game.singletons;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import java.util.HashMap;
import java.util.Vector;

import pl.com.henrietta.lnx2.Channel;
import pl.com.henrietta.lnx2.Connection;
import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.RetransmissionMode;
import pl.com.henrietta.lnx2.exceptions.NothingToRead;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public enum Network
{
	INSTANCE;
	
	private String ip;
	private int port;
	private boolean blocking;
	
	private DatagramChannel channel;
	private Connection connection;
	
	public void Init(String ip, int port)
	{
		this.ip=ip;
		this.port=port;
	}
	
	public void setBlocking(boolean blocking) throws IOException
	{
		this.blocking=blocking;	
		channel.configureBlocking(this.blocking);
	}
	
	public void Connect() throws IOException
	{
		//create real connection
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(ip, port));
		setBlocking(true);
		
		//setting channels
		Vector<Channel> chanells=new Vector<Channel>();	
		Channel c0=new Channel((byte)0, RetransmissionMode.RTM_AUTO_ORDERED, 10, 60);
		chanells.add(c0);
		
		connection=new Connection(chanells, 15f);
			
	}
	
	public void Recive() throws IOException, PacketMalformedError, NothingToRead
	{
		ByteBuffer recvbuf = ByteBuffer.allocate(2048);
		this.channel.read(recvbuf);
		this.connection.on_received(Packet.from_bytes(recvbuf.array()));
		
		if(this.connection.has_new_data==true)
		{
			connection.getChannel(0).read();
		}
	}
	
	public void Send()
	{
		//this.connection.getChannel(0).write(byte[])
	}
	

	
}
