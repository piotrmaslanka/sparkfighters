package com.sparkfighters.shared.physics.objects;

import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.objects.HorizSegment;

/**
 * Class representing a rectangle with borders aligned to axes
 * Immutable.
 * @author Henrietta
 *
 */
public class Rectangle implements Cloneable {
	/** 
	 * Do not modify!
	 */
	public double x1;
	/** 
	 * Do not modify!
	 */
	public double y1;
	/** 
	 * Do not modify!
	 */
	public double x2;
	/** 
	 * Do not modify!
	 */
	public double y2;
	
	/**
	 * Constructs a rectangle from coordinates. (x1, y1) is the bottom-left corner,
	 * and (x2, y2) is the top-right corner.
	 */
	public Rectangle(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/**
	 * Constructs a rectangle from points. First point is bottom-left corner,
	 * second point is top-right corner.
	 * @param p1 first point, ie. bottom-left corner
	 * @param p2 second point, ie. top-right corner
	 */
	public Rectangle(Vector p1, Vector p2) {
		this.x1 = p1.x;
		this.y1 = p1.y;
		this.x2 = p2.x;
		this.y2 = p2.y;
	}
	
	/**
	 * Returns height of the rectangle
	 * @return height
	 */
	public double get_height() {
		return this.y2 - this.y1 + 1;
	}

	/**
	 * Returns width of the rectangle
	 * @return width
	 */
	public double get_width() {
		return this.x2 - this.x1 + 1;
	}
	
	/**
	 * Checks whether this rectangle contains the point.
	 * Being located at boundaries also counts.
	 * @param p Point to be tested for inclusion
	 * @return whether p lies inside this rectangle
	 */
	public boolean contains(Vector p) {
		return (this.x1 <= p.x) && (this.y1 <= p.y) &&
			   (this.x2 >= p.x) && (this.y2 >= p.y);
	}
	
	/** 
	 * Checks whether this rectangle contains a horizontal segment.
	 * Being located at boundaries also counts
	 * @param hs Segment to be tested for inclusion
	 * @return whether hs lies inside this rectangle
	 */
	public boolean contains(HorizSegment hs) {
		if ((this.y1 > hs.y) || (this.y2 < hs.y)) return false;
		return (this.x1 >= hs.x1) && (this.x2 <= hs.x2);
	}
	
	/**
	 * Checks whether this rectangle contains another rectangle
	 * @param r rectangle to be checked for being fully included in this rectangle
	 * @return whether r lies exactly inside this rectangle
	 */
	public boolean contains(Rectangle r) {
		return (this.x1 <= r.x1) && (this.y1 <= r.y1) &&
			   (this.x2 >= r.x2) && (this.y2 >= r.y2);		
	}
	
	/**
	 * Checks whether this rectangle intersects with another rectangle
	 * @param r rectangle to check
	 * @return whether rectangles intersect
	 */
	public boolean intersects(Rectangle r) {
		return !((r.x1 > this.x2) || (r.x2 < this.x1) || (r.y1 > this.y2) || (r.y2 < this.y1));
	}
	
	/**
	 * Checks whether this rectangle intersects with another rectangle
	 * given an update in position to this rectangle
	 * @param r the other, stationary rectangle
	 * @param tx X update to this rectangle coordinates
	 * @param ty Y update to this rectangle coordinates
	 */
	public boolean intersects_m(Rectangle r, double tx, double ty) {
		return !((r.x1 > this.x2+tx) || (r.x2 < this.x1+tx) || (r.y1 > this.y2+ty) || (r.y2 < this.y1+ty));
	}

	/**
	 * Checks whether this rectangle intersects with another rectangle
	 * given an update in position to this rectangle.
	 *
	 * @param r the other rectangle
	 * @param tx X update to this rectangle coordinates
	 * @param ty Y update to this rectangle coordinates
	 * @param ox X update to the other rectangle
	 * @param oy Y update to the other rectangle
	 */
	public boolean intersects_m2(Rectangle r, double tx, double ty, double ox, double oy) {
		return !((r.x1+ox > this.x2+tx) || (r.x2+ox < this.x1+tx) || 
				 (r.y1+oy > this.y2+ty) || (r.y2+oy < this.y1+ty));
	}
	
	/**
	 * Checks whether this rectangle intersects with a horizontal segment
	 * @param hs horizontal segment to check
	 * @return whether this rectangle intersects with horizontal segment
	 */
	public boolean intersects(HorizSegment hs) {
		if ((this.y1 > hs.y) || (this.y2 < hs.y)) return false;		
		return (hs.x1 <= this.x2) && (hs.x2 >= this.x1);
	}

	/**
	 * Returns the rectangle reflected across axis Y
	 * @return reflected rectangle
	 */
	public Rectangle reflect_y() {
		return new Rectangle(-this.x2, this.y1, -this.x1, this.y2);
	}
	
	/**
	 * Returns the rectangle reflected across axis X
	 * @return reflected rectangle
	 */
	public Rectangle reflect_x() {
		return new Rectangle(this.x1, -this.y2, this.x2, -this.y1);
	}
	
	
	/**
	 * Because rectangle is immutable, we can return self
	 * @return a clone of this object
	 */
	@Override
	public Rectangle clone() {
		return this;
	}

}
