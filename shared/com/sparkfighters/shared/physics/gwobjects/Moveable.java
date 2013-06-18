package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;
/**
 * A moveable object in the gameworld can move, and has a velocity and a position
 */
public interface Moveable extends Oriented {
	public Vector get_velocity();
	public void set_velocity(Vector v);
}
