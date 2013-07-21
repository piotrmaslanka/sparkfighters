package com.sparkfighters.shared.physics.gwobjects;

import com.sparkfighters.shared.physics.objects.Rectangle; 
import com.sparkfighters.shared.physics.objects.HorizSegment; 

/**
 * A physical object with actor-like properties. This is the
 * smallest 'moving geometry' unit that can be subjected to 
 * collision detection.
 * 
 * @author Henrietta
 */
public class PhysicActor extends GeometrySet {
	
	private int color = 0;
	
	/**
	 * Anchor controller
	 */
	private boolean collides_platforms = false;
	
	/**
	 * Whether actor was forced to dy=0 on this turn
	 */
	private boolean h_braked = false;
	/**
	 * Whether actor was forced to dx=0 on this turn
	 */
	private boolean v_braked = false;
	/**
	 * Whether actor innately wants to move (ie. keys depressed)
	 */
	private boolean h_moving = false;
	
	private Rectangle last_obstacle_collided = null; 
	
	public boolean get_h_braked() { return this.h_braked; }
	public boolean get_v_braked() { return this.v_braked; }
	public boolean get_h_moving() { return this.h_moving; }
	public Rectangle get_last_obstacle_collided() {
		return this.last_obstacle_collided;
	}
	
	public PhysicActor set_h_braked(boolean x) { this.h_braked = x; return this; }
	public PhysicActor set_v_braked(boolean x) { this.v_braked = x; return this; }
	public PhysicActor set_h_moving(boolean x) { this.h_moving = x; return this; }
	
	public PhysicActor set_last_obstacle_collided(Rectangle r) {
		this.last_obstacle_collided = r;
		return this;
	}
	
	public PhysicActor(SmallMovingGeometry[] geometries, int color, boolean collides_platforms) {
		super(geometries);
		this.color = color;
		this.collides_platforms = collides_platforms;
	}
	
	/**
	 * Signals the actor that we are advancing to next round.
	 * Call upon transition to new round
	 */
	public void advance_round() {
		this.h_braked = false;
		this.v_braked = false;
	}
	
	/**
	 * Checks whether this actor intersects with another actor
	 * @param a other actor
	 * @param dt time delta
	 * @return intersection test result
	 */
	public boolean intersects(PhysicActor a, double dt) {
		if (a.color == this.color) return false;
		return this.get().intersects(a.get(), dt);
	}
	
	public boolean intersects(Rectangle r, double dt) {
		return this.get().intersects(r, dt);
	}

	/**
	 * Check whether the other rectangle contains this actor
	 */
	public boolean is_contained_by(Rectangle rect, double dt) {
		return this.get().is_contained_by(rect, dt);
	}
	
	/**
	 * Checks not only whether actor collides with a platform, but whether
	 * he should be interacted about that platform
	 * @param hs platform
	 * @param dt time delta
	 * @return whether corrective measures about actor position/velocity should be taken
	 */
	public boolean intersects(HorizSegment hs, double dt) {
		if (!this.collides_platforms) return false;
		if (!this.get().intersects(new Rectangle(hs.x1, hs.y, hs.x2, hs.y), dt)) return false;
		// so, actor collides but we must track whether he was falling down with enabled
		// anchor
		SmallMovingGeometry actor = this.get();	
		double a = actor.get_mbr().y1+actor.get_position().y-actor.get_velocity().multiply(dt).y+Rebound.GRAVITY_EPSILON; 
		return a >= hs.y;
	}
	
	public PhysicActor clone() {
		GeometrySet gs = ((GeometrySet)this).clone();
		PhysicActor ps = (PhysicActor)gs;
		ps.color = this.color;
		ps.h_braked = this.h_braked;
		ps.v_braked = this.v_braked;	
		if (this.last_obstacle_collided != null)
			ps.last_obstacle_collided = this.last_obstacle_collided;
		else
			ps.last_obstacle_collided = null;
		ps.collides_platforms = this.collides_platforms;
		return ps;
	}

}
