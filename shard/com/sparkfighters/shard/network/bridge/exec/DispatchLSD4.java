package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shared.lsd.LSDPacket;

/**
 * Order to dispatch a LSD fragment on channel 4
 * @author Henrietta
 *
 */
public class DispatchLSD4 extends ExecutorToNetwork {

	public int actor_id;
	public LSDPacket frag;
	
	public DispatchLSD4(int actor_id, LSDPacket frag) {
		this.actor_id = actor_id;
		this.frag = frag;
	}

}
