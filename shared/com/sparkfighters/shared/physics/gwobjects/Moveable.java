package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;
/**
 * A moveable object in the gameworld can move, and has a velocity and a position.
 *
 * Mutable. Private.
 *
 */
public abstract class Moveable implements Oriented {
	/**
	 * Invariant: Modifying returned vector cannot affect this objects velocity.
	 */
	public abstract Vector get_velocity();
	public abstract Moveable set_velocity(Vector v);
	
	/** 
	 * Move the object by t units of time.
	 * Updates it's position using get_velocity and set_velocity
	 * @param t
	 */
	public void move_by(double t) {
		this.set_position(this.get_position().add(
							this.get_velocity().multiply(t)
						 ));
	}
}
