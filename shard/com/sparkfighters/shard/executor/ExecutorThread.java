package com.sparkfighters.shard.executor;

import java.util.HashMap;
import java.util.Map.Entry;

import com.sparkfighters.shard.loader.JSONBattleDTO;
import com.sparkfighters.shard.loader.JSONUserDTO;
import com.sparkfighters.shard.network.bridge.BridgeRoot;
import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shard.network.bridge.NetworkToExecutor;
import com.sparkfighters.shard.network.bridge.exec.*;
import com.sparkfighters.shard.network.bridge.net.*;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.world.Actor;
import com.sparkfighters.shared.world.Team;
import com.sparkfighters.shared.world.World;

public class ExecutorThread extends Thread {

	static final long FORCED_START_ITERATION = 1200;
	static final long CINEMATICS_DURATION = 200;
	static final int FRAME_DURATION = 50;
	
	World gameworld;
	boolean _terminating = false;
	BridgeRoot br = null;
	long iteration = 0;
	boolean is_game_started = false;
	long game_started_on = 0;
	
	public Synchronizer sync;
	
	HashMap<Integer, Boolean> is_online = new HashMap<>();
	
	public ExecutorThread(World gameworld, BridgeRoot br, JSONBattleDTO bpf) {
		this.gameworld = gameworld;
		this.br = br;
		
		// Set up all onliners
		for (JSONUserDTO user : bpf.users)
			this.is_online.put(user.hero_id, false);
		
		this.sync = new Synchronizer(this);
		
	}
	
	public ExecutorThread terminate() { this._terminating = true; return this; }

	
	/**
	 * Spawns a team. Relays messages to backend.
	 * @param team_id ID of team to spawn
	 */
	private void spawn_team(int team_id) {
		Vector position = this.gameworld.spawnpoints_by_team.get(team_id);
		Team team = this.gameworld.teams[team_id];

		for (Actor actor : team.actors) {
			actor.physical = actor.actor_blueprint.create_physicactor(actor.id);
			this.gameworld.physics_world.add_actor(actor.physical);
			actor.physical.set_position(position);
		}
		
		this.send_to_network(new TeamSpawned(this.iteration, team_id, position));		
	}
	
	
	public void send_to_network(ExecutorToNetwork etn) {
		this.sync.relay(etn);
		this.br.send_to_network(etn);
	}
	
	public void run() {
		
		while (!this._terminating) {
			long started_on = System.currentTimeMillis();
			
			// Receive messages
			NetworkToExecutor nex;
			while ((nex = this.br.executor_receive()) != null) {
				
				this.sync.relay(nex);
				
				if (nex instanceof PlayerConnected)
					this.is_online.put(nex.player_id, true);
				if (nex instanceof PlayerDisconnected)
					this.is_online.put(nex.player_id, false);	
				if (nex instanceof InputStatusChanged) {
					InputStatusChanged isc = (InputStatusChanged)nex;
					gameworld.actor_by_id.get(isc.player_id).controller()
						.set_mouse_position(new Vector(isc.mouse_x, isc.mouse_y))
						.set_keyboard_status(isc.kbd_up, isc.kbd_right, isc.kbd_down, isc.kbd_left)
						.set_mouse_status(isc.mouse_lmb, isc.mouse_rmb);						
				}
			}		
			
			// Should we start cinematics?
			if (this.game_started_on == 0) {
				boolean all_connected = true;
				for (Boolean b : this.is_online.values()) all_connected &= b;
				
				if ((this.iteration >= ExecutorThread.FORCED_START_ITERATION) || all_connected) {
					this.game_started_on = this.iteration;
					this.send_to_network(new CinematicStarted());
					System.out.println("Executor: starting cinematics");
				}
			}
			
			// Should we start the game?
			if ((this.game_started_on != 0) && ((this.iteration - this.game_started_on) == ExecutorThread.CINEMATICS_DURATION)) {
				this.is_game_started = true;
				this.send_to_network(new GameStarted());
				System.out.println("Executor: starting gameplay");
				
				// Teams need to be spawned
				for (int key : this.gameworld.spawnpoints_by_team.keySet())
					this.spawn_team(key);
			}
			
			// Increment exception, wait more
			try {
				long delta = System.currentTimeMillis() - started_on;
				if (delta < 0) throw new RuntimeException("Timer overrun");
				iteration++;
				Thread.sleep(ExecutorThread.FRAME_DURATION-delta);
			} catch (InterruptedException e) {};
		}
		
	}

}
