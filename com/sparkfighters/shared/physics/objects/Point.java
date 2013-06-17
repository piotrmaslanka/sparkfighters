package com.sparkfighters.shared.physics.objects;

import java.lang.Math;

/**
 * Class representing a point, or a vector.
 * It is immutable once created. It's x and y properties are public, but
 * don't modify them.
 * @author Henrietta
 */
public class Point implements Cloneable {
	public double x;
	public double y;
	
	/**
	 * Creates the point from raw coordinates
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor-from-array.
	 * @param in At-least two-element array. First element is X coord, second element is Y coord
	 */
	public Point(double[] in) {
		this.x = in[0];
		this.y = in[1];
	}
	
	/**
	 * Euclidean distance from this to another point
	 * @param p the another point
	 * @return euclidean distance
	 */
	public double distance(Point p) {
		return Math.hypot(this.x - p.x, this.y - p.y);
	}
	
	/**
	 * Adds this point to another point, returning a new point
	 * @param p the another point
	 * @return result point
	 */
	public Point add(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}
	
	/**
	 * Returns an inverted version of this point
	 * By inverted it means that coordinates are negative, so if you added this
	 * point to it's inverted version, you would get (0, 0).
	 * @return new inverted point
	 */
	public Point invert() {
		return new Point(-this.x, -this.y);
	}
	
	/**
	 * Returns a new point which is composed of this point with added values
	 * to coordinates
	 * @param dx Value to add to X
	 * @param dy Value to add to Y
	 * @return new point
	 */
	public Point delta(double dx, double dy) {
		return new Point(this.x + dx, this.y + dy);
	}
	
	/**
	 * Returns new point which is composed of this point by coordinates
	 * multiplied by k.
	 * new point is (x*k, y*k)
	 * @param k Factor by which both coordinates should be multiplied.
	 * @return
	 */
	public Point multiply(double k) {
		return new Point(this.x*k, this.y*k);
	}
	
	/**
	 * Returns this point as two-element array of double
	 * @return double[2]. First element is X coordinate, second element is Y coordinate.
	 */
	public double[] as_vec2() {
		double[] rv = {this.x, this.y};
		return rv;
	}
	
	/**
	 * Returns a new point - a clone
	 */
	@Override
	public Point clone() {
		return new Point(this.x, this.y);
	}
}
