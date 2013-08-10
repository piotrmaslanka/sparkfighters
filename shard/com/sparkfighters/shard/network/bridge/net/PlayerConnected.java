package com.sparkfighters.shard.network.bridge.net;

import com.sparkfighters.shard.network.bridge.NetworkToExecutor;

public class PlayerConnected extends NetworkToExecutor {
	public PlayerConnected(int player_id) {
		super(player_id);
	}
}
