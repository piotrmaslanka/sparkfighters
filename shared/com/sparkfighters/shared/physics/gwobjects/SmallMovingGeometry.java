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
	private Vector position = new Vector(0, 0);
	private Vector velocity = new Vector(0, 0);
	
	private Rectangle[] rectangles;
	private Rectangle mbr;

	/**
	 * @param rectangles Rectangles for use. Borrows this array,
	 * but seeing as it's an array of immutable items, then it's also
	 * immutable.
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
	public boolean intersects(Rectangle rect, double dt) {
		if (!this.mbr.intersects_m(rect, this.velocity.multiply(dt).add(this.position)))
			return false;
		for (Rectangle r: this.rectangles)
			if (r.intersects_m(rect, this.velocity.multiply(dt).add(this.position)))
				return true;
		return false;
	}
	
	/**
	 * Check whether the other rectangle contains this geometry
	 */
	public boolean is_contained_by(Rectangle rect, double dt) {
		return rect.contains_m(this.mbr, this.velocity.multiply(dt).add(this.position));
	}
	
	/**
	 * Checks whether this geometry intersects with an oriented rectangle
	 * @param rect other, oriented rectangle
	 * @param o the other rectangle's position
	 * @return intersection test result
	 */
	public boolean intersects_m(Rectangle rect, Vector o, double dt) {
		if (!this.mbr.intersects_m2(rect, this.velocity.multiply(dt).add(this.position), o))
			return false;
		for (Rectangle r: this.rectangles)
			if (r.intersects_m2(rect, this.velocity.multiply(dt).add(this.position), o))
				return true;
		return false;
	}	
	
	/**
	 * Checks whether this geometry intersects with another geometry
	 * @param smg other, geometry
	 * @return intersection test result
	 */
	public boolean intersects(SmallMovingGeometry smg, double dt) {
		if (!this.mbr.intersects_m2(smg.mbr, this.velocity.multiply(dt).add(this.position),
											 smg.velocity.multiply(dt).add(smg.position)))
			return false;
	
		for (Rectangle thisr : this.rectangles)
			for (Rectangle otherr : smg.rectangles)
				if (thisr.intersects_m2(otherr, this.velocity.multiply(dt).add(this.position),
												smg.velocity.multiply(dt).add(smg.position)))
					return true;
		return false;
	}
	
	public SmallMovingGeometry clone() {
		return (new SmallMovingGeometry(this.rectangles))
								.set_position(this.position)
								.set_velocity(this.velocity);
	}
	

}
