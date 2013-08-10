package com.sparkfighters.shard.network.bridge.net;

import com.sparkfighters.shard.network.bridge.NetworkToExecutor;

public class PlayerDisconnected extends NetworkToExecutor {
	public PlayerDisconnected(int player_id) {
		super(player_id);
	}
}
