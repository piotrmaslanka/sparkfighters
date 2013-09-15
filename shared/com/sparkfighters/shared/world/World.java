package com.sparkfighters.shared.world;

import java.util.HashMap;
import com.sparkfighters.shared.physics.gwobjects.PhysicActor;
import com.sparkfighters.shared.physics.objects.Vector;

/**
 * A god-class that represents the game world.
 * @author Henrietta
 */
public class World implements Cloneable {

	public com.sparkfighters.shared.physics.world.World physics_world = null;
	public HashMap<Integer, Actor> actor_by_id = null; // by Actor ID
	public HashMap<Integer, Vector> spawnpoints_by_team = null; // immutable!
	public Team[] teams = null; // by Team ID
	
	/**
	 * Number of current iteration. May be fractional, if invoked with fractional dt's.
	 */
	public double iteration = 0;
	
	/**
	 * Initializes the world
	 * @param physics_world A blueprint-loaded physics world
	 * @param spawn_points Where players spawn (team => position)
	 * @param teams Array of teams. Borrows reference.
	 */
	public World(com.sparkfighters.shared.physics.world.World physics_world,
				 HashMap<Integer, Vector> spawn_points,
				 Team[] teams) {
		this.physics_world = physics_world;
		
		this.spawnpoints_by_team = spawn_points;
		
		PhysicsWorldBridge bridge = new PhysicsWorldBridge(this);
		this.physics_world.set_processor(bridge);
		
		this.teams = teams;
		this.actor_by_id = new HashMap<>();
		for (Team team : teams)
			for (Actor actor : team.actors)
				this.actor_by_id.put(actor.id, actor);
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

		// prepare teams. We WILL creates actors, by the way
		Team[] new_teams = new Team[this.teams.length];
		for(int i=0; i<new_teams.length; i++) {
			new_teams[i] = this.teams[i].clone();
			
			int j=0;
			for (Actor old_actor : this.teams[i].actors) {
				// Clone an actor
				Actor new_actor = old_actor.clone();
				if (new_actor.physical != null) {
					PhysicActor pca = pailut.get(new_actor.physical);
					if (pca == null) throw new RuntimeException("What allocated this?");
					new_actor.physical = pca;
				}
				// Store them into array
				new_teams[i].actors[j] = new_actor;
				j++;
			}
		}

		//prepare logic world
		World nworld = new World(physworld, this.spawnpoints_by_team, new_teams);

		return nworld;
	}
}
