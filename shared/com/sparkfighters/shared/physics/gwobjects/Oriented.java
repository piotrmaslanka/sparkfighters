package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.gwobjects.GameWorldObject;
/**
 * An oriented object is an object that has an absolute position
 * in the game world. Additionally, it is mutable.
 * @author Henrietta
 */
public interface Oriented extends GameWorldObject {
	public Vector get_position();
	public void set_position(Vector p);
}
