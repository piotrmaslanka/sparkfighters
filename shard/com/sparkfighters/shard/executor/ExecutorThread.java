package com.sparkfighters.shard.executor;

import java.util.HashMap;

import com.sparkfighters.shard.loader.JSONBattleDTO;
import com.sparkfighters.shard.loader.JSONUserDTO;
import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shard.network.bridge.NetworkToExecutor;
import com.sparkfighters.shard.network.bridge.exec.*;
import com.sparkfighters.shard.network.bridge.net.*;
import com.sparkfighters.shared.world.World;

public class ExecutorThread extends Thread {

	World gameworld;
	boolean _terminating = false;
	BridgeRoot br = null;
	long iteration = 0;
	boolean is_game_started = false;
	
	HashMap<Integer, Boolean> is_online = new HashMap<>();
	
	public ExecutorThread(World gameworld, BridgeRoot br, JSONBattleDTO bpf) {
		this.gameworld = gameworld;
		this.br = br;
		
		// Set up all onliners
		for (JSONUserDTO user : bpf.users)
			this.is_online.put(user.hero_id, false);
		
	}
	
	public ExecutorThread terminate() { this._terminating = true; return this; }

	public void run() {
		
		while (!this._terminating) {
			long started_on = System.currentTimeMillis();
			
			// Receive messages
			NetworkToExecutor nex;
			while ((nex = this.br.executor_receive()) != null) {
				
				if (nex instanceof PlayerConnected)
					this.is_online.put(nex.player_id, true);
				if (nex instanceof PlayerDisconnected)
					this.is_online.put(nex.player_id, false);				
			}		
			
			// Should we start the game?
			if (!this.is_game_started) {
				boolean all_connected = true;
				for (Boolean b : this.is_online.values()) all_connected &= b;
				
				if ((this.iteration > 240) || all_connected) {
					this.is_game_started = true;
					this.br.send_to_network(new GameStarted());
					System.out.println("Executor: starting the game");
				}
			}
			
			// Increment exception, wait more
			try {
				long delta = System.currentTimeMillis() - started_on;
				if (delta < 0) throw new RuntimeException("Timer overrun");
				iteration++;
				Thread.sleep(250-delta);
			} catch (InterruptedException e) {};
		}
		
	}

}
