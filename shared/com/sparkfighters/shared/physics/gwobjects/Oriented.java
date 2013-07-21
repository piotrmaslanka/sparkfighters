package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.gwobjects.GameWorldObject;
/**
 * An oriented object is an object that has an absolute position
 * in the game world.
 * 
 * Mutable. Private.
 * 
 * @author Henrietta
 */
public interface Oriented extends GameWorldObject {
	/**
	 * Invariant: Modifying returned vector cannot affect this objects velocity.
	 */
	public Vector get_position();
	public Oriented set_position(Vector p);
}
