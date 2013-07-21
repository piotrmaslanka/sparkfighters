package com.sparkfighters.shared.physics.world;

import java.util.Vector;
import com.sparkfighters.shared.physics.objects.*;
import com.sparkfighters.shared.physics.gwobjects.*;

/**
 * World, as seen by physics only.
 * 
 * This does not, at all, apply gravity!!!
 * 
 * @author Henrietta
 */
abstract public class World implements Cloneable {
	private LargeStaticGeometry lsg = null;
	private Vector<PhysicActor> actors = new Vector<>();
	private Vector<HorizSegment> platforms = new Vector<>();
	private Rectangle world_area = null;
	
	protected World(LargeStaticGeometry ls, Rectangle world_area) {
		this.lsg = ls.clone();
		this.world_area = world_area.clone();
	}
	
	protected void add_actor(PhysicActor ac) {
		this.actors.add(ac);
	}
	
	protected void remove_actor(PhysicActor ac) {
		this.actors.remove(ac);
	}
	
	protected void add_platform(HorizSegment hs) {
		this.platforms.add(hs);
	}
	
	protected void remove_platform(HorizSegment hs) {
		this.platforms.remove(hs);
	}
	
	/**
	 * Advances the simulation by t steps
	 * 
	 * 1. Resolves collisions that were to happen if current (dx, dy) was maintained
	 * 2. Moves actors
	 * 
	 * @param dt
	 */
	protected void advance(double dt) {
		for (PhysicActor actor : this.actors) {
			
			if (!actor.is_contained_by(this.world_area, dt))
				this.on_actor_hits_world_area(actor, dt);
			
			// check collisions against LSG
			if (this.lsg.intersects(actor, dt))
				 this.on_actor_hits_obstacle(actor, this.lsg, dt);
			
			// check collisions against platforms
			for (HorizSegment segm : this.platforms)
				if (actor.intersects(segm, dt))
					this.on_actor_hits_platform(actor, segm, dt);
			
			
			for (PhysicActor other : this.actors)
				if (actor != other)
					if (actor.intersects(other, dt))
						this.on_actor_hits_actor(actor, other, dt);
		}
		
		// Advance people
		for (PhysicActor actor : this.actors) actor.move_by(dt);
	}
	
	
	// --------------- callbacks
	abstract void on_actor_hits_world_area(PhysicActor ac, double dt);
	abstract void on_actor_hits_obstacle(PhysicActor ac, LargeStaticGeometry lsg, double dt);
	abstract void on_actor_hits_platform(PhysicActor ac, HorizSegment segm, double dt);
	abstract void on_actor_hits_actor(PhysicActor a1, PhysicActor a2, double dt);
}
