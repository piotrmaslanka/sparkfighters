package com.sparkfighters.shard.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shard.network.bridge.exec.*;

public class NetworkThread extends Thread {

	NetworkRoot root = null;
	BridgeRoot br = null;
	boolean _terminating = false;
	
	public NetworkThread(NetworkRoot root, BridgeRoot br) {
		this.root = root;
		this.br = br;
	}
	
	public NetworkThread terminate() {
		this._terminating = true;
		return this;
	}
	
	public void run() {
		while (!this._terminating) {
			boolean done_anything = false;
			// Perform selection
			done_anything |= this.root.select();
			
			// Do we have any messages for us?
			ExecutorToNetwork etn = this.br.network_receive();
			if (etn != null) {				
				
				if (etn instanceof CinematicStarted) {
					// Tell waiting players that it is so
					for (Connection c : this.root.connections.values()) {
						byte[] rdy = {'1'};
						c.getChannel(0).write(rdy);
					}					
				}
				
				if (etn instanceof GameStarted) {
					// Instruct root that stuff is connected
					this.root.is_game_started = true;
					
					// Tell waiting players that it is so
					for (Connection c : this.root.connections.values()) {
						byte[] rdy = {'2'};
						c.getChannel(0).write(rdy);
					}
				}
				
				if (etn instanceof FBPlayerConnected) {
					FBPlayerConnected f = (FBPlayerConnected)etn;
					byte[] txe = {0, (byte)(f.player_id >> 8), (byte)(f.player_id & 255)};
					
					for (Connection c : this.root.connections.values())
						if (c.login_phase == 2)
							c.getChannel(3).write(txe);
				}
			
				if (etn instanceof FBPlayerDisconnected) {
					FBPlayerDisconnected f = (FBPlayerDisconnected)etn;
					byte[] txe = {1, (byte)(f.player_id >> 8), (byte)(f.player_id & 255)};
					
					for (Connection c : this.root.connections.values())
						if (c.login_phase == 2)
							c.getChannel(3).write(txe);
				}
				
				if (etn instanceof DispatchLSD4) {
					DispatchLSD4 lsd = (DispatchLSD4)etn;
					
					Connection c = this.root.connections.get(lsd.actor_id);
					if (c != null) if (c.login_phase == 2) {
						ByteArrayOutputStream bs = new ByteArrayOutputStream();
						try {
							lsd.frag.toStream(bs);
						} catch (IOException e) {
							throw new RuntimeException("Java plays ball");
						}
						c.getChannel(4).write(bs.toByteArray());
					}
				}
				
				if (etn instanceof DispatchLSD5) {
					DispatchLSD5 lsd = (DispatchLSD5)etn;

					Connection c = this.root.connection_by_pid.get(lsd.actor_id);
					if (c != null) if (c.login_phase == 2) {
						ByteArrayOutputStream bs = new ByteArrayOutputStream();
						try {
							lsd.frag.toStream(bs);
						} catch (IOException e) {
							throw new RuntimeException("Java plays ball");
						}
						c.getChannel(5).write(bs.toByteArray());
					}
				}
				
			}
								
			// Sleep if nothing to do 
			try {
				if (!done_anything) Thread.sleep(10);
			} catch (InterruptedException e) {}
			
		}
	}


}
