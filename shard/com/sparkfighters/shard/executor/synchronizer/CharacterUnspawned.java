package com.sparkfighters.shard.executor.synchronizer;

public class CharacterUnspawned implements SyncUnit {

	public int actor_id;
	public int iteration;
	
	public int getIteration() { return this.iteration; }
	public int getChannelAffiliation() { return 5; }
	
	public CharacterUnspawned(int actor_id, int iteration) {
		this.actor_id = actor_id;
		this.iteration = iteration;
	}


}