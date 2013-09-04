package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shared.lsd.fragments.LSDFragment;

/**
 * Order to dispatch a LSD fragment on channel 5
 * @author Henrietta
 *
 */
public class DispatchLSD5 extends ExecutorToNetwork {

	public int actor_id;
	public LSDFragment frag;
	
	public DispatchLSD5(int actor_id, LSDFragment frag) {
		this.actor_id = actor_id;
		this.frag = frag;
	}

}
