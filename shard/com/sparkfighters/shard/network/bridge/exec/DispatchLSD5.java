package com.sparkfighters.shard.network.bridge.exec;

import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shared.lsd.LSDPacket;


/**
 * Order to dispatch a LSD fragment on channel 5
 * @author Henrietta
 *
 */
public class DispatchLSD5 extends ExecutorToNetwork {

	public int actor_id;
	public LSDPacket frag;
	
	public DispatchLSD5(int actor_id, LSDPacket frag) {
		this.actor_id = actor_id;
		this.frag = frag;
	}

}
