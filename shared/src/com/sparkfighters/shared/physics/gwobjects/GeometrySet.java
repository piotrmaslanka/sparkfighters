package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * A collection of integer-indexable geometries.
 * 
 * Mutable. Private.
 * 
 * @author Henrietta
 *
 */
public class GeometrySet extends Moveable {
	
	final static int IDLE_RIGHT = 0;
	final static int IDLE_LEFT = 1;
	final static int RUN_RIGHT = 4;
	final static int RUN_LEFT = 5;
	final static int JUMP_RIGHT = 8;
	final static int JUMP_LEFT = 9;
	
	private SmallMovingGeometry[] geoms;
	private int currentlyPicked = 0;
	
	public GeometrySet(SmallMovingGeometry[] geometries) {
		this.geoms = geometries.clone();
		assert(geometries.length > 0);
	}
	
	/**
	 * Copying constructor
	 * @param gs
	 */
	public GeometrySet(GeometrySet gs) {
		this.geoms = gs.geoms.clone();
	}
	
	
	/**
	 * Set this geometry to idle or jumping - anyway, actor
	 * has braked horizontally.
	 * 
	 * Only changes geometry.
	 */
	public void on_hbrake() {
		int prev_direction = this.currentlyPicked & 1;
		
		if (Math.abs(this.get().get_velocity().y) < Rebound.VERT_EPSILON) {
			this.set(GeometrySet.JUMP_RIGHT + prev_direction);
		} else {
			this.set(GeometrySet.IDLE_RIGHT + prev_direction);
		}		
	}
	
	/**
	 * Set this geometry to running or idle, maintaining
	 * direction stemming from current dx. Actor has braked
	 * vertically.
	 * 
	 * Only changes geometry.
	 */
	public void on_vbrake() {
		Vector velocity = this.get().get_velocity();
		
		if (Math.abs(velocity.x) < Rebound.VERT_EPSILON) {
			// not moving at all
			int current_dir = this.currentlyPicked & 1;
			this.set(current_dir);
		} else {
			// moving
			int direction = (velocity.x < 0) ? 1 : 0;
			this.set(GeometrySet.RUN_RIGHT + direction);
		}		
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
