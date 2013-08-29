package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;

/**
 * Means that a team has been slain
 * @author Henrietta
 *
 */
public class TeamSlain extends ExecutorToNetwork {

	public int team_id;
	
	public TeamSlain(int team_id) {
		this.team_id = team_id;
	}

}
