package com.sparkfighters.shard.executor.synchronizer;

import com.sparkfighters.shared.physics.objects.Vector;

public class CharacterSpawned implements SyncUnit {

	public int actor_id;
	public int iteration;
	public Vector position;
	
	public int getIteration() { return this.iteration; }
	public int getChannelAffiliation() { return 5; }
	
	public CharacterSpawned(int actor_id, int iteration, Vector position) {
		this.actor_id = actor_id;
		this.iteration = iteration;
		this.position = position;
	}


}
