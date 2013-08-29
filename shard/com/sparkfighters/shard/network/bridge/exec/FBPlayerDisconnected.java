package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.FBNetworkToNetwork;

/**
 * Means that player_id has disconnected
 * @author Henrietta
 *
 */
public class FBPlayerDisconnected extends FBNetworkToNetwork {

	public int player_id;
	
	public FBPlayerDisconnected(int player_id) {
		this.player_id = player_id;
	}

}
