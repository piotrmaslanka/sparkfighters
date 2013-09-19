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
import com.sparkfighters.shared.world.Manipulator;
import com.sparkfighters.shared.world.Team;
import com.sparkfighters.shared.world.World;

public class ExecutorThread extends Thread {

	static final int FORCED_START_ITERATION = 1200;
	static final int CINEMATICS_DURATION = 200;
	static final int FRAME_DURATION = 50;
	
	public World gameworld;
	boolean _terminating = false;
	public BridgeRoot br = null;
	public int iteration = 0;
	public boolean is_game_started = false;
	public int cine_started_on = 0;
	
	public Synchronizer sync;
	
	public HashMap<Integer, Boolean> is_online = new HashMap<>();	// by Actor ID
	/**
	 * Indexed by Team ID
	 * If given part is zero, team should be spawned
	 */
	public HashMap<Integer, Integer> team_death_counter = new HashMap<>();
	
	public ExecutorThread(World gameworld, BridgeRoot br, JSONBattleDTO bpf) {
		this.gameworld = gameworld;
		this.br = br;
		
		// Set up all onliners
		for (JSONUserDTO user : bpf.users)
			this.is_online.put(user.hero_id, false);
		
		// Set up all teams
		for (Team team : this.gameworld.teams) this.team_death_counter.put(team.id, 0);
		
		this.sync = new Synchronizer(this);
		
	}
	
	public ExecutorThread terminate() { this._terminating = true; return this; }

	
	/**
	 * Spawns a character. Relays messages to backend.
	 * @param actor_id ID of actor to spawn
	 */
	private void spawn_character(int actor_id) {
		Actor actor = this.gameworld.actor_by_id.get(actor_id);
		assert actor != null;
		new Manipulator(this.gameworld).spawn_character(actor_id);
		this.sync.on_actor_spawned(actor_id, actor.team_id, this.gameworld.spawnpoints_by_team.get(actor.team_id));
	}
	
	/**
	 * UnSpawns a character. Relays messages to backend.
	 * @param actor_id ID of actor to spawn
	 */
	private void unspawn_character(int actor_id) {
		Actor actor = this.gameworld.actor_by_id.get(actor_id);
		assert actor != null;
		new Manipulator(this.gameworld).unspawn_character(actor_id);
		this.sync.on_actor_unspawned(actor_id);
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
				
				if (nex instanceof PlayerConnected) {
					this.is_online.put(nex.player_id, true);
					if (this.is_game_started)
						this.spawn_character(nex.player_id);
				}
				if (nex instanceof PlayerDisconnected) {
					this.is_online.put(nex.player_id, false);
					if (this.gameworld.actor_by_id.get(nex.player_id).alive)
						this.unspawn_character(nex.player_id);
				}
				if (nex instanceof InputStatusChanged) {
					InputStatusChanged isc = (InputStatusChanged)nex;
					Actor a = this.gameworld.actor_by_id.get(isc.player_id); 
					// Does it make any sense to relay that to Synchronizer? We can do that only if
					// character is alive
					if (a.alive) {
						this.sync.on_input_status_changed(isc, a.physical.get_position());
						a.controller()
						    .set_mouse_position(new Vector(isc.mouse_x, isc.mouse_y))
						    .set_keyboard_status(isc.kbd_up, isc.kbd_right, isc.kbd_down, isc.kbd_left)
						    .set_mouse_status(isc.mouse_lmb, isc.mouse_rmb);	
					}
				}
			}		

			// Should we start cinematics?
			if (this.cine_started_on == 0) {
				boolean all_connected = true;
				for (Boolean b : this.is_online.values()) all_connected &= b;
				
				if ((this.iteration >= ExecutorThread.FORCED_START_ITERATION) || all_connected) {
					this.cine_started_on = this.iteration;
					this.send_to_network(new CinematicStarted());
				}
			}

			// Should we start the game?
			if ((this.cine_started_on != 0) && ((this.iteration - this.cine_started_on) == ExecutorThread.CINEMATICS_DURATION)) {
				this.is_game_started = true;
				this.send_to_network(new GameStarted());
				
				// Teams need to be spawned
				for (int actor_id : this.gameworld.actor_by_id.keySet())
					this.spawn_character(actor_id);
			}

			// Time spawn counters
			if ((this.iteration % 20) == 0) {
				for (int team_id : this.team_death_counter.keySet()) {
					int tp = this.team_death_counter.get(team_id);
					if (tp == 0) continue;
					if ((--tp) == 0) {
						Team team = this.gameworld.teams[team_id];
						for (Actor a : team.actors)
							if (this.is_online.get(a.id))
								this.spawn_character(a.id);
					}
					this.team_death_counter.put(team_id, tp);
				}
			}

			// Push the world an iteration further
			this.gameworld.advance(1);
			
			// Send forth LSD
			ExecutorToNetwork etn;
			boolean was_dispatched = false;
			while ((etn = this.sync.generate_dispatch()) != null) {
				this.br.send_to_network(etn);
				was_dispatched = true;
			}
			if (was_dispatched) System.out.println("Performed dispatch");
					
			// Increment exception, wait more
			try {
				long delta = System.currentTimeMillis() - started_on;
				if (delta < 0) throw new RuntimeException("Timer overrun");
				this.iteration++;
				if ((ExecutorThread.FRAME_DURATION-delta) > 0)
					Thread.sleep(ExecutorThread.FRAME_DURATION-delta);
			} catch (InterruptedException e) {};
		}
		
	}

}
