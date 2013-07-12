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
	private boolean collides_platforms = false;
	
	private boolean h_braked = false;
	private boolean v_braked = false;
	private Rectangle last_obstacle_collided = null; 
	
	public PhysicActor(SmallMovingGeometry[] geometries, int color, boolean collides_platforms) {
		super(geometries);
		this.color = color;
		this.collides_platforms = collides_platforms;
	}
	
	/**
	 * Checks whether this actor intersects with another actor
	 * @param a other actor
	 * @return intersection test result
	 */
	public boolean intersects(PhysicActor a) {
		if (a.color == this.color) return false;
		return this.get().intersects(a.get());
	}
	
	public boolean intersects(HorizSegment hs) {
		if (!this.collides_platforms) return false;
		return this.get().intersects(new Rectangle(hs.x1, hs.y, hs.x2, hs.y));
	}
	
	public PhysicActor clone() {
		GeometrySet gs = ((GeometrySet)this).clone();
		PhysicActor ps = (PhysicActor)gs;
		ps.color = this.color;
		ps.h_braked = this.h_braked;
		ps.v_braked = this.v_braked;	
		if (this.last_obstacle_collided != null)
			ps.last_obstacle_collided = this.last_obstacle_collided.clone();
		else
			ps.last_obstacle_collided = null;
		ps.collides_platforms = this.collides_platforms;
		return ps;
	}

}
