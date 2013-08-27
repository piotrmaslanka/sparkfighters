package com.sparkfighters.shard.network;

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
				
			}
				
				
			// Sleep if nothing to do 
			try {
				if (!done_anything) Thread.sleep(10);
			} catch (InterruptedException e) {}
			
		}
	}


}
