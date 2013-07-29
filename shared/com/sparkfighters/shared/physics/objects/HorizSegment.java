package com.sparkfighters.shared.physics.objects;

/**
 * Class representing a horizontal segment.
 * Immutable.
 * @author Henrietta
 */
public class HorizSegment implements Cloneable {
	public double x1;
	public double x2;
	public double y;
	
	public HorizSegment() {}
	
	/**
	 * Creates a horizontal segment
	 * @param x1 Starting X. Less than x2
	 * @param x2 Stop X. More than x1
	 * @param y Segment Y position
	 */
	public HorizSegment(double x1, double x2, double y) {
		this.x1 = x1;
		this.x2 = x2;
		this.y = y;
	}
	
	/**
	 * Because hsegment is immutable, we can return self
	 */
	@Override
	public HorizSegment clone() {
		return this;
	}

}
