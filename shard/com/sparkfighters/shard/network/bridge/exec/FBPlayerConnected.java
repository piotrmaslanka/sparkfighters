package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.FBNetworkToNetwork;

/**
 * Means that player of given ID has connected
 * @author Henrietta
 *
 */
public class FBPlayerConnected extends FBNetworkToNetwork {

	public int player_id;
	
	public FBPlayerConnected(int player_id) {
		this.player_id = player_id;
	}

}
