package com.sparkfighters.shared.physics.gwobjects;

/**
 * A collection of integer-indexable geometries
 * @author Henrietta
 *
 */
public class GeometrySet {
	
	private SmallMovingGeometry[] geoms;
	private int currentlyPicked = 0;
	
	public GeometrySet(SmallMovingGeometry[] geometries) {
		this.geoms = geometries.clone();
		assert(geometries.length > 0);
	}
	
	/**
	 * Returns currently picked geometry
	 * @return currently picked geometry
	 */
	public SmallMovingGeometry get() {
		return this.geoms[this.currentlyPicked];
	}
	
	/**
	 * Sets a new picked geometry. Does this
	 * directly by modifying pointer and inheriting
	 * other geometry's speed and position.
	 * @param newgeom New geometry's ID
	 */
	public void set(int newgeom) {
		SmallMovingGeometry oldg = this.geoms[this.currentlyPicked];
		SmallMovingGeometry newg = this.geoms[newgeom];
		
		newg.set_position(oldg.get_position()).set_velocity(oldg.get_velocity());
		
		this.currentlyPicked = newgeom;
	}
}
