package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.gwobjects.PhysicActor;

/**
 * This represents a shot. 
 * @author Henrietta
 *
 */
public class Shot implements Cloneable {
	/**
	 * The shot identifier
	 */
	protected PhysicActor physical;
	public int id;
	
	public Shot(int id, PhysicActor pa) {
		this.id = id;
		this.physical = pa;
	}
	
	public Shot clone() {
		Shot ac = new Shot(this.id, this.physical.clone());
		return ac;
	}
	
}
