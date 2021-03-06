package com.sparkfighters.client.game.singletons;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Vector;

import com.sparkfighters.shared.lsd.LSDPacket;
import com.sparkfighters.shared.lsd.fragments.CharacterInputUpdate;
import com.sparkfighters.shared.lsd.fragments.CharacterSpawned;
import com.sparkfighters.shared.lsd.fragments.CharacterUnspawned;
import com.sparkfighters.shared.world.Manipulator;

import pl.com.henrietta.lnx2.Channel;
import pl.com.henrietta.lnx2.Connection;
import pl.com.henrietta.lnx2.Packet;
import pl.com.henrietta.lnx2.RetransmissionMode;
import pl.com.henrietta.lnx2.exceptions.NothingToRead;
import pl.com.henrietta.lnx2.exceptions.NothingToSend;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

public enum Network implements Runnable
{
	INSTANCE;
	public String login;
	private String password;
	
	private String ip;
	private int port;
	private boolean blocking;
	
	private DatagramChannel channel;
	private Connection connection;
	
	private Thread thread;
	
	public boolean Authorization=false;
	public boolean GameData=false;
	public com.sparkfighters.client.game.network.GameData GameDataMsg;
	public boolean StartGame=false;
	private long sendPing=System.currentTimeMillis();
	public long ping;
	
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
		setBlocking(false);
						
		//setting channels
		Vector<Channel> chanells=new Vector<Channel>();	
		
		Channel c0=new Channel((byte)0, RetransmissionMode.RTM_AUTO_ORDERED, 5, 60);
		chanells.add(c0);
		
		Channel c1=new Channel((byte)1, RetransmissionMode.RTM_MANUAL, 5, 1);
		chanells.add(c1);
		
		Channel c2=new Channel((byte)2, RetransmissionMode.RTM_MANUAL, 1, 1);
		chanells.add(c2);
		
		Channel c3=new Channel((byte)3, RetransmissionMode.RTM_AUTO, 10, 60);
		chanells.add(c3);
		
		Channel c4=new Channel((byte)4, RetransmissionMode.RTM_NONE, 0, 0); 
		chanells.add(c4);
		
		Channel c5=new Channel((byte)5, RetransmissionMode.RTM_AUTO, 5, 60); 
		chanells.add(c5);
		
		connection=new Connection(chanells, 15f);
		
		thread=new Thread(Network.INSTANCE);
		thread.start();
		
		AuthorizeConnection();
	}
	
	public void setBlocking(boolean blocking)
	{
		try
		{
			this.blocking=blocking;	
			channel.configureBlocking(this.blocking);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void Recive()
	{
		try
		{
			ByteBuffer recvbuf = ByteBuffer.allocate(2048);
			this.channel.receive(recvbuf);
			
			int how_much_readed = recvbuf.position();
			byte[] data_readed = new byte[how_much_readed];
			System.arraycopy(recvbuf.array(), 0, data_readed, 0, how_much_readed);
			
			if(how_much_readed>0)
				Logger.INSTANCE.write("Recive:"+Logger.bytesToHex(data_readed), Logger.LogType.INFO);
			
			this.connection.on_received(Packet.from_bytes(data_readed));
	
			if(this.connection.has_new_data==true)
			{			
				for (Channel c : connection.getChannels()) 
				{
					try
					{
						byte[] msg = connection.getChannel(c.channel_id).read();
						DoCommand(c.channel_id, msg);
					}
					catch(NothingToRead e)
					{
						continue;
					}
				}	
				this.connection.has_new_data=false;
			}
		}
		catch(Exception e)
		{
			
		}
	
	}
	
	public void Send(byte channel, String text)
	{
		try
		{
			//prepare to send data
			this.connection.getChannel(channel).write(text.getBytes("UTF-8"));	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void Send(byte channel, byte[] bytes)
	{
		try
		{
			//prepare to send data
			this.connection.getChannel(channel).write(bytes);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void DoCommand(byte channel, byte[] msg)
	{
		try
		{
			if(channel==0) Channel0(msg);
			if(channel==1) Channel1(msg);
			if(channel==2) Channel2(msg);
			if(channel==3) Channel3(msg);
			if(channel==4) Channel4(msg);
			if(channel==5) Channel5(msg);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	private void Channel5(byte[] msg) 
	{
		//channel 5 and 4 are very smillar so invoke channel4 function
		Channel4(msg);
	}

	private void Channel4(byte[] msg) 
	{
		ByteArrayInputStream bs = new ByteArrayInputStream(msg);
		LSDPacket lp = new LSDPacket(0);
		try 
		{
			lp.fromStream(bs);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
		
		for(int i=0;i<lp.fragments.size();i++)
		{
			if(lp.fragments.get(i) instanceof CharacterInputUpdate)
			{
				CharacterInputUpdate ciu=(CharacterInputUpdate) lp.fragments.get(i);
				WorldManager.INSTANCE.worldLogic.actor_by_id.get(ciu.player_id).controller().set_keyboard_status(ciu.kbd_up, ciu.kbd_right, ciu.kbd_down, ciu.kbd_left);
				
				for(int j=0;j<WorldManager.INSTANCE.actors.size();j++)
				{
					if(WorldManager.INSTANCE.actors.get(j).getId()==ciu.player_id)
					{
						WorldManager.INSTANCE.actors.get(j).setDegree(ciu.angle);
					}
				}
				
				//WorldManager.INSTANCE.worldLogic.actor_by_id.get(ciu.player_id).controller().set_mouse_position(new Vector(ciu.,y_absolute));
				
				//WorldManager.INSTANCE.worldLogic.actor_by_id.get(ciu.player_id).controller().set_mouse_status(lmb, rmb);
			}
			
			if(lp.fragments.get(i) instanceof CharacterSpawned)
			{
				CharacterSpawned cs=(CharacterSpawned) lp.fragments.get(i);
				
				new Manipulator(WorldManager.INSTANCE.worldLogic).spawn_character(cs.actor_id);
				
			}
			
			if(lp.fragments.get(i) instanceof CharacterUnspawned)
			{			
				CharacterUnspawned cu=(CharacterUnspawned) lp.fragments.get(i);
				
				new Manipulator(WorldManager.INSTANCE.worldLogic).unspawn_character(cu.actor_id);
			}
		}
	}

	private void Channel3(byte[] msg)
	{
		//player connected, disconnected on this channel
		
	}

	private void Channel2(byte[] msg) 
	{
		//Write only no read 		
	}

	private void Channel1(byte[] msg) 
	{
		//for ping
		long now_time=System.currentTimeMillis();		
		this.ping=now_time-sendPing;
	}

	private void Channel0(byte[] msg) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		String msg_s=new String(msg, "UTF-8");
		if(Authorization==false)
		{
			if(msg_s.equals("OK"))
			{
				this.Authorization=true;
			}
			else
			{
				if(msg_s.equals("FAIL"))
				{
					System.exit(0);
				}
				else
				{
					byte[] pwd;
					pwd = this.password.getBytes("UTF-8");
		
					// Lol, why can't Java just concat two arrays?
					byte[] response = new byte[pwd.length + msg.length];
					System.arraycopy(pwd, 0, response, 0, pwd.length);
					System.arraycopy(msg, 0, response, pwd.length, msg.length);
					
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
		}
		else
		{
			if(this.GameData==false)
			{
				this.GameDataMsg=new com.sparkfighters.client.game.network.GameData(new String(msg, "UTF-8"));
				this.GameData=true;	
			}
			else
			{
				if(msg_s.equals("0"))
				{
					//game not start yet
				}
				
				if(msg_s.equals("1"))
				{
					//play the cinematic sequence
				}
				
				if(msg_s.equals("2"))
				{
					//play the game
					this.StartGame=true;
				}
				
			}
			
		}
	}
	private void AuthorizeConnection()
	{
		Send((byte) 0, login);
	}

	public void run() 
	{
		while(true)
		{
			long now_time=System.currentTimeMillis();		
			long time=now_time-sendPing;
			
			if(time>=1000)
			{
				//send ping
				sendPing=System.currentTimeMillis();
				Send((byte)1,"P");		
			}


			//send data
			Packet p;
			try 
			{
				p = this.connection.on_sendable();
				ByteBuffer buf=ByteBuffer.wrap(p.to_bytes());	
				Logger.INSTANCE.write("Send:"+Logger.bytesToHex(buf.array()), Logger.LogType.INFO);
				this.channel.send(buf, new InetSocketAddress(this.ip, this.port));
			} 
			catch (NothingToSend e) 
			{

			} 
			catch (IOException e) 
			{

				e.printStackTrace();
			}	

			
			Recive();
		}
	}
	
	
	
	
}
