package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Means that a team has been spawned
 * @author Henrietta
 *
 */
public class TeamSpawned extends ExecutorToNetwork {

	public int team_id;
	public long iteration;
	public Vector position;
	
	public TeamSpawned(long iteration, int team_id, Vector position) {
		this.team_id = team_id;
		this.iteration = iteration;
		this.position = position;
	}

}
