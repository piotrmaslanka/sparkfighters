package com.sparkfighters.shared.physics.objects;

import java.lang.Math;

/**
 * Class representing a vector, or a vector.
 * It is immutable once created. It's x and y properties are public, but
 * don't modify them.
 * @author Henrietta
 */
public class Vector implements Cloneable {
	public double x;
	public double y;
	
	/**
	 * Creates the vector from raw coordinates
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor-from-array.
	 * @param in At-least two-element array. First element is X coord, second element is Y coord
	 */
	public Vector(double[] in) {
		this.x = in[0];
		this.y = in[1];
	}
	
	/**
	 * Norm of this vector minus vector p
	 * @param p the another vector
	 * @return euclidean distance
	 */
	public double distance(Vector p) {
		return Math.hypot(this.x - p.x, this.y - p.y);
	}
	
	/**
	 * Returns distance of the vector from the origin, ie. (0, 0)
	 * @return euclidean distance from (0, 0)
	 */
	public double norm() {
		return Math.hypot(this.x, this.y);
	}
	
	/**
	 * Adds this vector to another vector, returning a new vector
	 * @param p the another vector
	 * @return result vector
	 */
	public Vector add(Vector p) {
		return new Vector(this.x + p.x, this.y + p.y);
	}
	
	/**
	 * Returns an negative version of this vector
	 * By negative it means that coordinates are negative, so if you added this
	 * vector to it's inverted version, you would get (0, 0).
	 * @return new vector
	 */
	public Vector negative() {
		return new Vector(-this.x, -this.y);
	}
	
	/**
	 * Returns a new vector which is composed of this vector with added values
	 * to coordinates
	 * @param dx Value to add to X
	 * @param dy Value to add to Y
	 * @return new vector
	 */
	public Vector delta(double dx, double dy) {
		return new Vector(this.x + dx, this.y + dy);
	}
	
	/**
	 * Returns new vector which is composed of this vector by coordinates
	 * multiplied by k.
	 * new vector is (x*k, y*k)
	 * @param k Factor by which both coordinates should be multiplied.
	 * @return
	 */
	public Vector multiply(double k) {
		return new Vector(this.x*k, this.y*k);
	}
	
	/**
	 * Returns this vector as two-element array of double
	 * @return double[2]. First element is X coordinate, second element is Y coordinate.
	 */
	public double[] as_double2() {
		double[] rv = {this.x, this.y};
		return rv;
	}
	
	/**
	 * Returns a new vector - a clone
	 */
	@Override
	public Vector clone() {
		return new Vector(this.x, this.y);
	}
	
	/**
	 * Retuns dot product of two vectors
	 * @param p the other vector
	 * @result dot product ot two vectors
	 */
	public double dot(Vector p) {
		return p.x*this.x + p.y*this.y;
	}
}
