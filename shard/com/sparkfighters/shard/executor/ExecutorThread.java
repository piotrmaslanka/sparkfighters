package com.sparkfighters.shard.executor;

import com.sparkfighters.shared.world.World;

public class ExecutorThread extends Thread {

	World gameworld;
	boolean _terminating = false;
	
	public ExecutorThread(World gameworld) {
		this.gameworld = gameworld;
	}
	
	public ExecutorThread terminate() { this._terminating = true; return this; }

	public void run() {
		
	}

}
