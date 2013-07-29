package com.sparkfighters.shared.physics.world;

import java.util.Vector;
import com.sparkfighters.shared.physics.objects.*;
import com.sparkfighters.shared.physics.gwobjects.*;

/**
 * World, as seen by physics only.
 * 
 * Cloning this object is responsibility of the owner. This object
 * does not know how to clone itself
 * 
 * @author Henrietta
 */
public class World {
	public LargeStaticGeometry lsg = new LargeStaticGeometry(new Rectangle[0]);
	public Vector<PhysicActor> actors = new Vector<>();
	public Vector<HorizSegment> platforms = new Vector<>();
	public Rectangle world_area = null;
	
	private WorldCommand processor = null;
	
	static final double GRAVITY = -0.01;	// per 1 time unit, in delta y
	
	public World(Rectangle world_area) {
		this.world_area = world_area.clone();
	}
	
	public World set_processor(WorldCommand p) { this.processor = p; return this; }
	public WorldCommand get_processor() { return this.processor; }
	
	public World set_lsg(LargeStaticGeometry lsg) { this.lsg = lsg; return this; }
	public LargeStaticGeometry get_lsg() { return this.lsg; }
	
	public void add_actor(PhysicActor ac) {
		this.actors.add(ac);
	}
	
	public void remove_actor(PhysicActor ac) {
		this.actors.remove(ac);
	}
	
	public void add_platform(HorizSegment hs) {
		this.platforms.add(hs);
	}
	
	public void remove_platform(HorizSegment hs) {
		this.platforms.remove(hs);
	}
	
	/**
	 * Detects collisions that were to happen if current (dx, dy) was maintained.
	 * Higher layer needs to resolve collisions. Rebound class is on standby if
	 * help with that is needed.
	 * 
	 * @param dt time delta
	 */
	public void advance_collisions(double dt) {
		for (PhysicActor actor : this.actors) {
			
			if (!actor.is_contained_by(this.world_area, dt))
				this.processor.on_actor_hits_world_area(actor, this.world_area, dt);
			
			// check collisions against LSG
			if (this.lsg.intersects(actor, dt))
				 this.processor.on_actor_hits_obstacle(actor, this.lsg, dt);
			
			// check collisions against platforms
			for (HorizSegment segm : this.platforms)
				if (actor.intersects(segm, dt))
					this.processor.on_actor_hits_platform(actor, segm, dt);			
			
			for (PhysicActor other : this.actors)
				if (actor != other)
					if (actor.intersects(other, dt))
						this.processor.on_actor_hits_actor(actor, other, dt);
		}
	}	
	
	/**
	 * Advances actor movement by particular amount of time
	 * @param dt time delta
	 */
	public void advance_movement(double dt) {
		for (PhysicActor actor : this.actors) actor.move_by(dt);
	}
	
	/**
	 * Advances gravity by particular amount of time
	 * @param dt time delta
	 */
	public void advance_gravity(double dt) {
		for (PhysicActor actor : this.actors)
			actor.set_velocity(actor.get_velocity().add_y(
					World.GRAVITY*dt*actor.get_gravity_factor()
					));
	}
	
}
