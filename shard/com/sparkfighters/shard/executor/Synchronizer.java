package com.sparkfighters.shard.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.sparkfighters.shard.executor.synchronizer.*;
import com.sparkfighters.shard.network.bridge.ExecutorToNetwork;
import com.sparkfighters.shard.network.bridge.NetworkToExecutor;
import com.sparkfighters.shard.network.bridge.exec.DispatchLSD4;
import com.sparkfighters.shard.network.bridge.exec.DispatchLSD5;
import com.sparkfighters.shared.lsd.LSDPacket;

/**
 * A LSD-based delegate that attempts to keep clients in sync
 * @author Henrietta
 *
 */
public class Synchronizer {

	HashMap<Integer, Vector<SyncUnit>> syncqueue = new HashMap<>();
	/**
	 * Current cache of input statuses. Used to detect whether 
	 * updates signalled to synchronizer should be put away to 
	 * LSD layer for update
	 */
	HashMap<Integer, InputStatusChanged> inputcache = new HashMap<>();
	ExecutorThread thrd;
	
	public Synchronizer(ExecutorThread thrd) {
		this.thrd = thrd;
		for (int actor_id : thrd.gameworld.actor_by_id.keySet())
			this.syncqueue.put(actor_id, new Vector<SyncUnit>());
	}	
	
	/**
	 * Information that input status has been changed for 
	 * particular player
	 */
	public void on_input_status_changed(com.sparkfighters.shard.network.bridge.net.InputStatusChanged base,
										com.sparkfighters.shared.physics.objects.Vector character_location) {
		// f_angle will be in range [pi; -pi] radians
		double f_angle = -Math.atan2(base.mouse_y - character_location.y, base.mouse_x - character_location.x);
		
		// f_angle will be in range [2pi; 0] radians
		if (f_angle < 0) f_angle += 2*Math.PI;
		
		// now f_angle will be in range [360; 0] degrees
		f_angle = f_angle * 180 / Math.PI;
		
		// secure negatives
		int angle = ((int)f_angle < 0) ? 0 : (int)f_angle;

		InputStatusChanged isc = new InputStatusChanged(
									  base.player_id,
									  this.thrd.iteration,
									  base.kbd_up, base.kbd_left, base.kbd_down, base.kbd_right,
									  (short)angle
								);

		InputStatusChanged prev = this.inputcache.get(base.player_id);
			
		// Check if updated input is markedly different from that which was
		
		boolean is_different = false;
		
		// Does exist?
		if (prev == null) is_different = true;

		// Is different?
		if (!is_different)
			if (!isc.controlEquals(prev))
				is_different = true;
		
		if (is_different) {
			System.out.format("Input has changed for %d\n", base.player_id);
			// Go ahead, notify everyone. That'll work.
			this.broadcast(isc);
			// Save this value to cache
			this.inputcache.put(base.player_id, isc);
		}
										
	}
	
	/**
	 * Information that actor has been spawned
	 */
	public void on_actor_spawned(int actor_id, int team_id, com.sparkfighters.shared.physics.objects.Vector position) {
		this.broadcast(new CharacterSpawned(actor_id, this.thrd.iteration, position));
	}
	
	/**
	 * Information that actor has been unspawned
	 */
	public void on_actor_unspawned(int actor_id) {
		this.broadcast(new CharacterUnspawned(actor_id, this.thrd.iteration));
	}	
	
	/**
	 * Information about input status of given actor changing
	 */
	public void on_input_changed(int player_id, boolean kbd_up, boolean kbd_left, boolean kbd_down, boolean kbd_right, int angle) {
		
	}
	
	
	/**
	 * Return a DispatchLSD4/DispatchLSD5 or null if nothing to send now
	 */
	public ExecutorToNetwork generate_dispatch() {
		for (int actor_id : this.syncqueue.keySet()) {
			if (this.syncqueue.get(actor_id).size() > 0) {
				SyncUnit u = this.syncqueue.get(actor_id).firstElement();
				return this.create_dispatch(actor_id, u.getChannelAffiliation(), u.getIteration());
			}
		}
		return null;
	}
	
	/**
	 * Generates a DispatchLSD4/DispatchLSD5, as need is
	 * @param actor_id Actor ID which fragment should be generated for
	 * @param dispatch 4 or 5
	 * @param iteration Iteration to generate entries about
	 */
	public ExecutorToNetwork create_dispatch(int actor_id, int dispatch, int iteration) {
		// filter out entries
		Vector<SyncUnit> my_list = this.syncqueue.get(actor_id);

		Vector<SyncUnit> filtered = new Vector<>();
		for (SyncUnit u : my_list)
			if ((u.getChannelAffiliation() == dispatch) && (u.getIteration() == iteration))
				filtered.add(u);
		// remove those filtered out
		for (SyncUnit u : filtered) my_list.remove(u);

		if (filtered.size() == 0) return null;	// nothing to do
		
		// create a fresh packet
		LSDPacket frag = new LSDPacket(iteration);
		
		// convert outstanding parameters to a LSD packet
		for (SyncUnit u : filtered) {
			if (u instanceof com.sparkfighters.shard.executor.synchronizer.CharacterSpawned) {
				com.sparkfighters.shard.executor.synchronizer.CharacterSpawned ts =	(com.sparkfighters.shard.executor.synchronizer.CharacterSpawned)u;
				frag.fragments.add(new com.sparkfighters.shared.lsd.fragments.CharacterSpawned(ts.actor_id, ts.position));
			}				
			else if (u instanceof com.sparkfighters.shard.executor.synchronizer.CharacterUnspawned) {
				com.sparkfighters.shard.executor.synchronizer.CharacterUnspawned ts = (com.sparkfighters.shard.executor.synchronizer.CharacterUnspawned)u;
				frag.fragments.add(new com.sparkfighters.shared.lsd.fragments.CharacterUnspawned(ts.actor_id));
			}				
			else if (u instanceof com.sparkfighters.shard.executor.synchronizer.InputStatusChanged) {
				com.sparkfighters.shard.executor.synchronizer.InputStatusChanged is = (com.sparkfighters.shard.executor.synchronizer.InputStatusChanged)u;
				frag.fragments.add(new com.sparkfighters.shared.lsd.fragments.CharacterInputUpdate(is.player_id, is.kbd_up, is.kbd_left, is.kbd_down, is.kbd_right, is.angle));				
			}
		}		
		
		ExecutorToNetwork lsdp = null;
		if (dispatch == 4)
			lsdp = (ExecutorToNetwork)new DispatchLSD4(actor_id, frag);
		if (dispatch == 5)
			lsdp = (ExecutorToNetwork)new DispatchLSD5(actor_id, frag);
		
		return lsdp;
	}
	
	/**
	 * Send this syncunit to everyone
	 * @param u SyncUnit to broadcast
	 */
	private void broadcast(SyncUnit u) {
		for (Vector<SyncUnit> q : this.syncqueue.values()) q.add(u);
	}
	
	public void relay(ExecutorToNetwork etn) {
		
		
	}
	
	public void relay(NetworkToExecutor net) {
		
	}
}
