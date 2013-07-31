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
 * PRIVATE
 * 
 * @author Henrietta
 *
 */
final public class Rebound {
	
	/**
	 * Amount of vertical space to keep between actor and platform
	 */
	final static public double GRAVITY_EPSILON = 0.001;
	/**
	 * Amount of friction in px^2/t
	 */
	final static public double FRICTION = 10;
	
	/**
	 * Vertical comparision epsilon
	 */
	final static public double VERT_EPSILON = 0.0001;
	
	
	/**
	 * Rebound an actor against a game world box. Actor must not be wholly
	 * contained within rect.
	 * @param actor the actor
	 * @param rect rectangle describing the box game is taking place within
	 * @param dt time delta
	 */
	static public void rebound(PhysicActor actor, Rectangle rect, double dt) {
		Rectangle actor_mbr = actor.get().get_mbr().translate(
								actor.get_velocity().multiply(dt).add(
										actor.get_position()));
		
		if (actor_mbr.y2 >= rect.y2) {
			Rebound.actor_hit_roof(actor, rect.y2, dt);
		} else if (actor_mbr.y1 <= rect.y1) {
			Rebound.actor_hit_floor(actor, rect.y1, dt);
		} else if (actor_mbr.x1 <= rect.x1) {
			Rebound.actor_hit_leftside(actor, rect.x1, dt);
		} else if (actor_mbr.x2 >= rect.x2) {
			Rebound.actor_hit_rightside(actor, rect.x2, dt);
		} else assert(false); // this is invalid
	}
	
	/**
	 * Modify the actor, as it has hit a left side
	 */
	static private void actor_hit_leftside(PhysicActor actor, double fx, double dt) {
		actor.on_hbrake();
		actor.set_position(actor.get_position().force_x(
					fx - actor.get().get_mbr().x1 + Rebound.VERT_EPSILON
				));
		actor.set_velocity(actor.get_velocity().force_x(0));
		actor.set_h_braked(true);
	}
	
	/**
	 * Modify the actor, as it has hit a right side
	 */
	static private void actor_hit_rightside(PhysicActor actor, double fx, double dt) {
		actor.on_hbrake();
		actor.set_position(actor.get_position().force_x(
					fx - actor.get().get_mbr().x2 - Rebound.VERT_EPSILON
				));
		actor.set_velocity(actor.get_velocity().force_x(0));
		actor.set_h_braked(true);
			
	}
	
	/**
	 * Modify the actor, as it has hit the root
	 */
	static private void actor_hit_roof(PhysicActor actor, double fy, double dt) {
		actor.set_velocity(actor.get_velocity().force_y(0));
		actor.set_v_braked(true).on_vbrake();
		actor.set_position(actor.get_position().force_y(
					fy - actor.get().get_mbr().y2 - Rebound.GRAVITY_EPSILON
				));		
	}
	
	/**
	 * Modify the actor, as it has hit the floor
	 */
	static private void actor_hit_floor(PhysicActor actor, double fy, double dt) {
		actor.on_vbrake();
		actor.set_v_braked(true).set_velocity(actor.get_velocity().force_y(0));
		
		actor.set_position(actor.get_position().force_y(
				fy - actor.get().get_mbr().y1 + Rebound.GRAVITY_EPSILON
					));			
		
		if (!actor.get_h_moving()) {	// consider friction
			if (Math.abs(actor.get_velocity().x) < Rebound.FRICTION * dt) {
				actor.set_velocity(actor.get_velocity().force_x(0));
				actor.on_hbrake();
			} else {
				actor.set_velocity(actor.get_velocity().add_x(
	-Math.abs(actor.get_velocity().x)/actor.get_velocity().x * Rebound.FRICTION * dt
						));
			}
		}		
	}
	
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
	static public void rebound(PhysicActor actor, HorizSegment hs, double dt) {
		
		actor.set_v_braked(true).on_vbrake();
		
		double actor_x = actor.get_position().x;
		double actor_y = hs.y - actor.get().get_mbr().y1 + Rebound.GRAVITY_EPSILON;
		
		actor.set_position(new Vector(actor_x, actor_y));
		actor.set_velocity(actor.get_velocity().force_y(0));

		// Friction??
		if (actor.get_h_moving()) return; // Actor wants to move, no friction
		
		Vector vel = actor.get_velocity();
		
		if (Math.abs(vel.x) < Rebound.FRICTION * dt) 
			vel.x = 0;
		else
			vel.x += -Math.abs(vel.x) / vel.x * Rebound.FRICTION * dt;
		
		actor.set_velocity(vel);
	}
	
	
	static private void r_rebound(PhysicActor actor, Rectangle rect, double dt) {
		/*
		 * Employ a heuristic
		 * ------------
		 * |    0     |
		 * | 1 RECT 3 |
		 * |    2     |
		 * ------------
		 */
		actor.set_last_obstacle_collided(rect);
		
		Vector actor_pos = actor.get_position().add(actor.get_velocity().multiply(dt));
		Rectangle actor_mbr = actor.get().get_mbr();
		double h[] = {
			actor_pos.y + actor_mbr.y1 - rect.y2,
			actor_pos.y + actor_mbr.y2 - rect.y1,
			actor_pos.x + actor_mbr.x2 - rect.x1,
			actor_pos.x + actor_mbr.x1 - rect.x2
		};

		for (int i=0; i<4; i++) h[i] = Math.abs(h[i]);
				
		// now the question is - which of those is the smallest?
		
		double smallest_val = Double.POSITIVE_INFINITY;
		int smallest_index = -1;
		
		for (int i=0; i<4; i++)
			if (smallest_val > h[i]) {
				smallest_val = h[i];
				smallest_index = i;
			}
		
		assert(smallest_index != -1);
		
		// ok, smallest_index is that
		
		if (smallest_index == 0)	// colliding with floor
			Rebound.actor_hit_floor(actor, rect.y2, dt);		
		
		if (smallest_index == 1) 	// colliding with roof
			Rebound.actor_hit_roof(actor, rect.y1, dt);
		
		if (smallest_index == 2) {
			Rebound.actor_hit_rightside(actor, rect.x1, dt);
			if (actor.get_last_obstacle_collided() != rect) {
				actor.set_v_braked(true);
				actor.set_last_obstacle_collided(rect);
			}	
		}
			
		if (smallest_index == 3) {
			Rebound.actor_hit_leftside(actor, rect.x2, dt);
			if (actor.get_last_obstacle_collided() != rect) {
				actor.set_v_braked(true);
				actor.set_last_obstacle_collided(rect);
			}			
		}
		
	}
	
	/**
	 * Rebound an actor by a LSG. Actor must collide with LSG.
	 * 
	 * Can mutate both position, velocity and flags. Will not touch anchor.
	 */
	static public void rebound(PhysicActor actor, LargeStaticGeometry lsg, double dt) {
		// determine the colliding rect
		for (Rectangle rect : lsg) {
			if (actor.intersects(rect, dt)) {
				Rebound.r_rebound(actor,  rect, dt);
				break;
			}
		}
	}
}
