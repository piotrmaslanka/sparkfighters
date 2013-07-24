package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.gwobjects.PhysicActor;

/**
 * This represents an actor. 
 * @author Henrietta
 *
 */
public class Actor implements Cloneable {
	/**
	 * The actor identifier
	 */
	protected PhysicActor physical;
	public int id;
	
	public Actor(int id, PhysicActor pa) {
		this.id = id;
		this.physical = pa;
	}
	
	public Actor clone() {
		Actor ac = new Actor(this.id, this.physical.clone());
		return ac;
	}
	
}
