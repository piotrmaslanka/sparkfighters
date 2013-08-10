package com.sparkfighters.shard.network.bridge;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * A two-way comms exchange between Executor and Network.
 * Network provides Executor with information about game state changes,
 * and Executor relays significant game state info changes.
 * @author Henrietta
 */
public class BridgeRoot {

	LinkedBlockingDeque<NetworkToExecutor> network_to_executor = null;
	LinkedBlockingDeque<ExecutorToNetwork> executor_to_network = null;
	
	public BridgeRoot() {
		this.network_to_executor = new LinkedBlockingDeque<>();
		this.executor_to_network = new LinkedBlockingDeque<>();
	}

	public void send_to_network(ExecutorToNetwork msg) {
		this.executor_to_network.add(msg);
	}
	
	public void send_to_executor(NetworkToExecutor msg) {
		this.network_to_executor.add(msg);
	}
}
