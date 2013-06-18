package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.gwobjects.GameWorldObject;
import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.objects.Vector;

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
	 * @param rectangles Rectangles to construct with. This array
	 * will be borrowed by this instance.
	 */
	public LargeStaticGeometry(Rectangle[] rectangles) {
		this.rectangles = rectangles; 
	}
	
	
	
	
	/**
	 * Because LSG is immutable, we can return self
	 */
	public LargeStaticGeometry clone() {
		return this;
	}
	
}
