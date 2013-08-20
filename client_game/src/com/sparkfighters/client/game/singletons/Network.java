package com.sparkfighters.client.game.singletons;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
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
	
	public void Init(String login, String password, String ip, String port)
	{
		this.ip=ip;
		this.port=Integer.valueOf(port);
		this.login=login;
		this.password=password;
	}
	
	public void Connect() throws IOException, NothingToSend, PacketMalformedError, NothingToRead, NoSuchAlgorithmException
	{
		//create real connection
		channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(0));
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
	
	
	
	public void Recive() throws IOException, PacketMalformedError, NothingToRead, NoSuchAlgorithmException, NothingToSend
	{
		ByteBuffer recvbuf = ByteBuffer.allocate(2048);
		this.channel.receive(recvbuf);
		
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
		ByteBuffer buf=ByteBuffer.wrap(p.to_bytes());	
		
		this.channel.send(buf, new InetSocketAddress(this.ip, this.port));
	}
	

	private void DoCommand(byte channel, String msg) throws NoSuchAlgorithmException, NothingToSend, IOException
	{
		if(channel==0)
		{
			byte[] pwd;
			pwd = this.password.getBytes("UTF-8");
			byte[] msg2;
			msg2=msg.getBytes("UTF-8");

			// Lol, why can't Java just concat two arrays?
			byte[] response = new byte[pwd.length + msg2.length];
			System.arraycopy(pwd, 0, response, 0, pwd.length);
			System.arraycopy(msg2, 0, response, pwd.length, msg2.length);
			
			// Compute SHA-1
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(response);
			
			Formatter formatter = new Formatter();
			for (byte b : md.digest()) formatter.format("%02x", b);
			
			response = formatter.toString().getBytes("UTF-8");
			formatter.close();
				
			Send((byte)0, new String(response, "UTF-8"));
		}
	}
	
	private void AuthorizeConnection() throws NothingToSend, IOException, PacketMalformedError, NothingToRead, NoSuchAlgorithmException
	{
		Send((byte) 0, login);
		Recive(); //for nonce
		Recive(); //for resposne OK/failed
	}
	
	
	
	
}
