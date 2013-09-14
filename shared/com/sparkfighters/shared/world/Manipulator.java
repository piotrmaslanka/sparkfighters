package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * A helper class for performing simple, predefined actions upon game world
 * 
 * Your modus operandii should be - create this class, perform operation,
 * delete this class. Do not keep an instance for too long - cloning will cause
 * this instance to become invalid. Usage will be undefined then.
 * 
 * @author Henrietta
 */
public class Manipulator {

	private World world;
	
	public Manipulator(World world) {
		this.world = world;
	}
	
	/**
	 * Call this if you want to spawn, or 'make-alive' a character
	 * @param actor_id Actor ID
	 */
	public void spawn_character(int actor_id) {
		Actor actor = this.world.actor_by_id.get(actor_id);
		
		Vector position = this.world.spawnpoints_by_team.get(actor.team_id);

		actor.physical = actor.actor_blueprint.create_physicactor(actor.id);
		this.world.physics_world.add_actor(actor.physical);
		actor.physical.set_position(position);
		actor.alive = true;
	}
	
	/**
	 * Call this if you want to unspawn a character
	 * @param actor_id Actor ID
	 */
	public void unspawn_character(int actor_id) {
		Actor actor = this.world.actor_by_id.get(actor_id);
		
		this.world.physics_world.remove_actor(actor.physical);
		actor.physical = null;
		actor.alive = false;
	}

}
