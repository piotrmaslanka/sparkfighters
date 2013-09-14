package com.sparkfighters.shard.executor.synchronizer;

public interface SyncUnit {

	/**
	 * Return iteration about which this entry speaks
	 */
	public int getIteration();
	/**
	 * Return LSD channel affiliation
	 * @return 4 or 5
	 */
	public int getChannelAffiliation();
	
}
