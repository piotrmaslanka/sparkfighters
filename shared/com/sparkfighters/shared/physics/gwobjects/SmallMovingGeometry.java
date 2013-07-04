package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.objects.Rectangle;

/**
 * A moveable set of rectangles. Rectangles cannot be changed
 * after this has been initialized
 * 
 * Mutable
 * 
 * @author Henrietta
 *
 */
public class SmallMovingGeometry extends Moveable {
	private Vector position;
	private Vector velocity;
	
	private Rectangle[] rectangles;
	private Rectangle mbr;
	
	/**
	 * @param Immutable
	 */
	public SmallMovingGeometry(Rectangle[] rectangles) {
		this.rectangles = rectangles;
		double mbrx1 = Double.POSITIVE_INFINITY;
		double mbry1 = Double.POSITIVE_INFINITY;
		double mbrx2 = Double.NEGATIVE_INFINITY;
		double mbry2 = Double.NEGATIVE_INFINITY;
		for (Rectangle r : rectangles) {
			if (r.x1 < mbrx1) mbrx1 = r.x1;
			if (r.y1 < mbry1) mbry1 = r.y1;
			if (r.x2 > mbrx2) mbrx2 = r.x2;
			if (r.y2 > mbry2) mbry2 = r.y2;
		}
		this.mbr = new Rectangle(mbrx1, mbry1, mbrx2, mbry2);
	}

	
	public SmallMovingGeometry set_position(Vector p) {	this.position = p; return this;	}
	public SmallMovingGeometry set_velocity(Vector v) {	this.velocity = v; return this;	}
	public Vector get_position() { return this.position.clone(); }
	public Vector get_velocity() { return this.velocity.clone(); }
	
	/**
	 * Checks whether this geometry intersects with a stationary rectangle
	 * @param rect other, stationary rectangle
	 * @return intersection test result
	 */
	public boolean intersects(Rectangle rect) {
		if (!this.mbr.intersects_m(rect, this.position.x, this.position.y))
			return false;
		for (Rectangle r: this.rectangles)
			if (r.intersects_m(rect, this.position.x, this.position.y))
				return true;
		return false;
	}
	
	/**
	 * Checks whether this geometry intersects with an oriented rectangle
	 * @param rect other, oriented rectangle
	 * @param ox the other rectangle's X position
	 * @param oy the other rectangle's Y position
	 * @return intersection test result
	 */
	public boolean intersects_m(Rectangle rect, double ox, double oy) {
		if (!this.mbr.intersects_m2(rect, this.position.x, this.position.y, ox, oy))
			return false;
		for (Rectangle r: this.rectangles)
			if (r.intersects_m2(rect, this.position.x, this.position.y, ox, oy))
				return true;
		return false;
	}	
	
	public SmallMovingGeometry clone() {
		return (new SmallMovingGeometry(this.rectangles)).set_position(this.position)
														 .set_velocity(this.velocity);
	}
	

}
