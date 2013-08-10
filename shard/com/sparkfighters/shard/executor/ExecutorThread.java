package com.sparkfighters.shard.executor;

import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shared.world.World;

public class ExecutorThread extends Thread {

	World gameworld;
	boolean _terminating = false;
	BridgeRoot br = null;
	
	public ExecutorThread(World gameworld, BridgeRoot br) {
		this.gameworld = gameworld;
		this.br = br;
	}
	
	public ExecutorThread terminate() { this._terminating = true; return this; }

	public void run() {
		
	}

}
