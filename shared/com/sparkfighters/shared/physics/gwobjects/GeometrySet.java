package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * A collection of integer-indexable geometries
 * @author Henrietta
 *
 */
public class GeometrySet extends Moveable {
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

	@Override
	public Vector get_position() {
		return this.geoms[this.currentlyPicked].get_position();
	}

	@Override
	public Oriented set_position(Vector p) {
		this.geoms[this.currentlyPicked].set_position(p);
		return this;
	}

	@Override
	public Vector get_velocity() {
		return this.geoms[this.currentlyPicked].get_velocity();
	}

	@Override
	public Moveable set_velocity(Vector v) {
		this.geoms[this.currentlyPicked].set_velocity(v);
		return this;
	}
	
	public GeometrySet clone() {
		SmallMovingGeometry[] ngs = new SmallMovingGeometry[this.geoms.length];
		for (int i=0; i<ngs.length; i++)
			ngs[i] = this.geoms[i].clone();
		return new GeometrySet(ngs);
	}
}
