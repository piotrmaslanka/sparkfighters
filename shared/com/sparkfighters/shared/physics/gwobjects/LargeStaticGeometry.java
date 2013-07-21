package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Rectangle;

/**
 * A static set of rectangles, good for representing static elements of the map.
 * Additionally, this geometry is large - which means that Minimum Bounding Rectangle
 * would be meaningless, because it would always collide with everything, and other
 * check-speedups should be considered.
 * 
 * Set of rectangles is static upon initialization.
 * 
 * Immutable.
 * 
 * @author Henrietta
 */
public class LargeStaticGeometry implements GameWorldObject {
	private Rectangle[] rectangles;
	
	/**
	 * 
	 * @param rectangles Rectangles to construct with. Immutable.
	 */
	public LargeStaticGeometry(Rectangle[] rectangles) {
		this.rectangles = rectangles; 
	}

	/**
	 * Checks if this collides with a SmallMovingGeometry
	 * @param smg a SmallMovingGeometry to check
	 * @return bool whether collision occurs
	 */
	public boolean intersects(SmallMovingGeometry smg, double dt) {
		for (Rectangle rect : rectangles)
			if (smg.intersects(rect, dt)) return true;
		return false;
	}
	
	/**
	 * Checks if this collides with a GeometrySet
	 * @param gs a GeometrySet to check
	 * @return bool whether collision occurs
	 */
	public boolean intersects(GeometrySet gs, double dt) {
		return this.intersects(gs.get(), dt);
	}

	/**
	 * Checks if this collides with a PhysicActor
	 * @param gs a GeometrySet to check
	 * @return bool whether collision occurs
	 */
	public boolean intersects(PhysicActor gs, double dt) {
		return this.intersects(gs.get(), dt);
	}	
	
	/**
	 * Because LSG is immutable, we can return self
	 */
	public LargeStaticGeometry clone() {
		return this;
	}
	
}
