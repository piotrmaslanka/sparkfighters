package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.*;

/**
 * Rebound's static class job is providing facilites for
 * rebounding actors, ie. forcing them into coordinates and
 * velocities that will not cause collisions.
 * 
 * It is just a service class - you can call it's methods if you wish,
 * you are not forced to use it.
 * 
 * rebound() operator MODIFIES position and velocity of passed actor !!
 * 
 * @author Henrietta
 *
 */
final public class Rebound {
	
	/**
	 * Amount of vertical space to keep between actor and platform
	 */
	static double GRAVITY_EPSILON = 0.001;
	/**
	 * Amount of friction in px^2/t
	 */
	static double FRICTION = 0.001;
	
	/**
	 * Rebound an actor by a platform. Actor must collide with platform.
	 * 
	 * Can mutate both actor position and velocity. Will not touch anchor
	 * nor flags.
	 * 
	 * @param actor actor (can be mutated)
	 * @param hs platform
	 * @param dt time delta
	 */
	static void rebound(PhysicActor actor, HorizSegment hs, double dt) {
		double actor_x = actor.get_position().x;
		double actor_y = hs.y - actor.get().get_mbr().y1 + Rebound.GRAVITY_EPSILON;
		
		actor.set_position(new Vector(actor_x, actor_y));
		
		// Friction??
		if (actor.get_h_moving()) return; // Actor wants to move, no friction
		
		Vector vel = actor.get_velocity();
		vel.y = 0;
		
		if (Math.abs(vel.x) < Rebound.FRICTION) 
			vel.x = 0;
		else
			vel.x += -Math.abs(vel.x) / vel.x * Rebound.FRICTION * dt;
		
		actor.set_velocity(vel);
	}
	
}
