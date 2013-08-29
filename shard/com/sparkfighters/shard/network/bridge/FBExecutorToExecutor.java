package com.sparkfighters.shard.network.bridge;

/**
 * Executor feedback message
 * @author Henrietta
 *
 */
public class FBExecutorToExecutor extends NetworkToExecutor {

	public FBExecutorToExecutor(int player_id) {
		super(player_id);
	}
	
	public FBExecutorToExecutor() {
		super(-1);
	}

}
