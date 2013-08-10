package com.sparkfighters.shard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.sparkfighters.shard.executor.ExecutorThread;
import com.sparkfighters.shard.loader.JSONBattleDTO;
import com.sparkfighters.shard.loader.WorldConstructor;
import com.sparkfighters.shared.world.World;

import com.sparkfighters.shard.network.*;
import com.sparkfighters.shard.network.bridge.BridgeRoot;
public class ShardApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		
		Gson gson = new Gson();
		JSONBattleDTO bpf = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[1]))));
			bpf = gson.fromJson(reader, JSONBattleDTO.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("BPF file not found");
		}
		
		// Init bridge
		BridgeRoot br = new BridgeRoot();
		
		// Init network
		NetworkThread thread_network = null;
		try {
			 thread_network = new NetworkThread(new NetworkRoot(bpf.netifc, bpf.netport, br, bpf), br);
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize network");
		}		
		
		thread_network.start();
		
		// Construct the world
		World gameworld = null;
		try {
			gameworld = (new WorldConstructor(bpf.content_fs_path)).load_from_dto(bpf);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load world");
		}
		
		// Init executor
		ExecutorThread thread_executor = null;
		thread_executor = new ExecutorThread(gameworld, br);
		
		thread_executor.start();
		
		
		// hang hang hang
		
		thread_network.terminate().join();
		thread_executor.terminate().join();

	}

}
