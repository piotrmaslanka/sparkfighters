package com.sparkfighters.client.game.singletons;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import pl.com.henrietta.lnx2.exceptions.NothingToSend;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public enum Network
{
	INSTANCE;
	private String login;
	private String password;
	
	private String ip;
	private int port;
	private boolean blocking;
	
	private DatagramChannel channel;
	private Connection connection;
	
	public void Connect(String login, String password, String ip, int port) throws IOException, NothingToSend, PacketMalformedError, NothingToRead
	{
		this.ip=ip;
		this.port=port;
		this.login=login;
		this.password=password;
		
		//create real connection
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(ip, port));
		setBlocking(true);
				
		//setting channels
		Vector<Channel> chanells=new Vector<Channel>();	
		Channel c0=new Channel((byte)0, RetransmissionMode.RTM_AUTO_ORDERED, 10, 60);
		chanells.add(c0);
				
		connection=new Connection(chanells, 15f);
		
		AuthorizeConnection();
	}
	
	public void setBlocking(boolean blocking) throws IOException
	{
		this.blocking=blocking;	
		channel.configureBlocking(this.blocking);
	}
	
	
	
	public void Recive() throws IOException, PacketMalformedError, NothingToRead
	{
		ByteBuffer recvbuf = ByteBuffer.allocate(2048);
		this.channel.read(recvbuf);
		this.connection.on_received(Packet.from_bytes(recvbuf.array()));
		
		if(this.connection.has_new_data==true)
		{
			 for (Channel c : connection.getChannels()) 
			 {
				 String msg = new String(connection.getChannel(c.channel_id).read(), "UTF-8");
				 DoCommand(c.channel_id, msg);
			 }
		}
	}
	
	public void Send(byte channel, String text) throws NothingToSend, IOException
	{
		this.connection.getChannel(channel).write(text.getBytes("UTF-8"));	
		Packet p=this.connection.on_sendable();	
		ByteBuffer buf=ByteBuffer.wrap(p.data);	
		this.channel.write(buf);
	}
	

	private void DoCommand(byte channel, String msg)
	{
		if(channel==0)
		{
			
		}
	}
	
	private void AuthorizeConnection() throws NothingToSend, IOException, PacketMalformedError, NothingToRead
	{
		Send((byte) 0, login);
		Recive(); //for nonce
		Recive(); //for resposne OK/failed
	}
	
	
	
	
}
