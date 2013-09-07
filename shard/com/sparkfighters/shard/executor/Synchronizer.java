package com.sparkfighters.shard.executor;

import java.util.HashMap;
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
	ExecutorThread thrd;
	
	public Synchronizer(ExecutorThread thrd) {
		this.thrd = thrd;
		for (int actor_id : thrd.gameworld.actor_by_id.keySet()) {
			this.syncqueue.put(actor_id, new Vector<SyncUnit>());
		}
	}
	
	
	/**
	 * Information that actor has been spawned
	 * @return 
	 */
	public void on_actor_spawned(int actor_id, int team_id, com.sparkfighters.shared.physics.objects.Vector position) {
		CharacterSpawned cs = new CharacterSpawned(actor_id, this.thrd.iteration, position);
		for (Vector<SyncUnit> q: this.syncqueue.values()) q.add(cs);
	}
	
	
	/**
	 * Return a DispatchLSD4/DispatchLSD5 or null if nothing to send now
	 */
	public ExecutorToNetwork generate_dispatch() {
		for (int actor_id : this.syncqueue.keySet()) {
			if (this.syncqueue.get(actor_id).size() > 0) {
				SyncUnit u = this.syncqueue.get(actor_id).iterator().next();
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
		}		
		
		ExecutorToNetwork lsdp = null;
		if (dispatch == 4)
			lsdp = (ExecutorToNetwork)new DispatchLSD4(actor_id, frag);
		if (dispatch == 5)
			lsdp = (ExecutorToNetwork)new DispatchLSD5(actor_id, frag);
		
		System.out.println("Synchronizer: dispatch performed");
		return lsdp;
	}
	
	
	public void relay(ExecutorToNetwork etn) {
		
		
	}
	
	public void relay(NetworkToExecutor net) {
		
	}
}
