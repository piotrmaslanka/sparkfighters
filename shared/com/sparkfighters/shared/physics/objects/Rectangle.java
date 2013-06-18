package com.sparkfighters.shared.physics.objects;

import com.sparkfighters.shared.physics.objects.Point;

/**
 * Class representing a rectangle.
 * Immutable.
 * @author Henrietta
 *
 */
public class Rectangle implements Cloneable {
	public double x1;
	public double y1;
	public double x2;
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
	public Rectangle(Point p1, Point p2) {
		this.x1 = p1.x;
		this.y1 = p1.y;
		this.x2 = p2.x;
		this.y2 = p2.y;
	}
	
	/**
	 * Returns height of the rectangle
	 * @return height
	 */
	public double getHeight() {
		return this.y2 - this.y1;
	}

	/**
	 * Returns width of the rectangle
	 * @return width
	 */
	public double getWidth() {
		return this.x2 - this.x1;
	}
	
	/**
	 * Checks whether this rectangle contains the point.
	 * Being located at boundaries also counts.
	 * @param p Point to be tested for inclusion
	 * @return whether p lies inside this rectangle
	 */
	public boolean contains(Point p) {
		return (this.x1 <= p.x) && (this.y1 <= p.y) &&
			   (this.x2 >= p.x) && (this.y2 >= p.y);
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
	 * @param whether rectangles intersect
	 */
	public boolean intersects(Rectangle r) {
		return !((r.x1 > this.x2) || (r.x2 < this.x1) || (r.y1 > this.y2) || (r.y2 < this.y1));
	}
	
	/**
	 * Returns this rectangle as two points. First element is bottom-left corner,
	 * second element is top-right corner
	 * @return two-element array containing extreme points
	 */
	public Point[] as_point2() {
		Point[] out = {new Point(this.x1, this.y2), new Point(this.x2, this.y2)};
		return out;
	}
	
	/**
	 * Clones this object
	 * @return a clone of this object
	 */
	@Override
	public Rectangle clone() {
		return new Rectangle(this.x1, this.y1, this.x2, this.y2);
	}

}
