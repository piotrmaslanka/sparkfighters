package com.sparkfighters.shard.network;

import com.sparkfighters.shard.network.bridge.BridgeRoot;

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
			
		}
	}


}
