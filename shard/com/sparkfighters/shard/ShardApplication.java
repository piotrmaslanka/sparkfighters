package com.sparkfighters.shard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.sparkfighters.shard.loader.JSONBattleDTO;

import com.sparkfighters.shard.network.*;
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
		
		// Init network
		NetworkThread thread_network = null;
		try {
			 thread_network = new NetworkThread(new NetworkRoot(bpf.netifc, bpf.netport));
		} catch (IOException e) {
			throw new RuntimeException("Failed to initialize network");
		}		
		
		thread_network.start();
		
		// hang hang hang
		
		thread_network.terminate().join();
		

	}

}
