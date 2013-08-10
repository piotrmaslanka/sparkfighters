package com.sparkfighters.shard.network.bridge;

/**
 * A root class for network-to-executor messages
 * @author Henrietta
 *
 */
public class NetworkToExecutor {
	/**
	 * Player ID this message refers to
	 */
	public int player_id;
	
	public NetworkToExecutor(int player_id) {
		this.player_id = player_id;
	}
}
