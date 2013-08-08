package com.sparkfighters.shard.network;

public class NetworkThread extends Thread {

	NetworkRoot root = null;
	boolean _terminating = false;
	
	public NetworkThread(NetworkRoot root) {
		this.root = root;
	}
	
	public NetworkThread terminate() {
		this._terminating = true;
		return this;
	}
	
	public void run() {
		while (!this._terminating) {
			
		}
	}


}
