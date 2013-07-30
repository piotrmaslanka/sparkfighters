package com.sparkfighters.shared.world;

import java.util.HashMap;
import java.util.Vector;

import com.sparkfighters.shared.physics.gwobjects.PhysicActor;

/**
 * A god-class that represents the game world.
 * @author Henrietta
 */
public class World implements Cloneable {

	public com.sparkfighters.shared.physics.world.World physics_world = null;
	public HashMap<Integer, Actor> actor_by_id = null;
	
	/**
	 * Number of current iteration. May be fractional, if invoked with fractional dt's.
	 */
	public double iteration = 0;
	
	/**
	 * Initializes the world
	 * @param physics_world A blueprint-loaded physics world
	 * @param actors array of logical actors. Seeing as they are logical, not physical,
	 * they can be specified at the initialization.
	 */
	public World(com.sparkfighters.shared.physics.world.World physics_world,
				 Actor[] actors) {
		this.physics_world = physics_world;
		
		PhysicsWorldBridge bridge = new PhysicsWorldBridge(this);
		this.physics_world.set_processor(bridge);
		
		this.actor_by_id = new HashMap<>();
		for (Actor actor : actors) this.actor_by_id.put(actor.id, actor);
	}
	
	/**
	 * Returns an actor by given ID
	 * @param id
	 * @return Actor, or null if not found
	 */
	public Actor get_actor(int id) { return this.actor_by_id.get(id); }
	
	/**
	 * Advance the world state by dt
	 * @param dt
	 */
	public void advance(double dt) {
		this.iteration += dt;
		
		this.physics_world.pre_advance(dt);
		this.physics_world.advance_gravity(dt);
		this.physics_world.advance_collisions(dt);
		this.physics_world.advance_movement(dt);
		this.physics_world.post_advance(dt);
		
		for (Actor a : this.actor_by_id.values()) a.process_controller_input();
	}
	
	
	public World clone() {
		// prepare physic world
		HashMap<PhysicActor, PhysicActor> pailut = this.physics_world.clone_return_PAILUT();
		com.sparkfighters.shared.physics.world.World physworld = this.physics_world.clone();
		
		for (PhysicActor new_pactor : pailut.values()) physworld.add_actor(new_pactor);

		// prepare logic actors
		Actor[] new_actors = new Actor[this.actor_by_id.size()];
		int i=0;
		for (Actor act : this.actor_by_id.values()) {
			Actor na = act.clone();
			PhysicActor pca = pailut.get(act.physical);
			if (pca == null) pca = act.physical.clone();
			na.physical = pca;
			new_actors[i++] = na;
		}
		
		//prepare logic world
		World nworld = new World(physworld, new_actors);

		return nworld;
	}
}
