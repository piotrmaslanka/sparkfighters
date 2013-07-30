package com.sparkfighters.shared.physics.world;

import java.util.Vector;
import java.util.HashMap;
import com.sparkfighters.shared.physics.objects.*;
import com.sparkfighters.shared.physics.gwobjects.*;

/**
 * World, as seen by physics only.
 * 
 * Cloning this object is responsibility of the owner. This object
 * does not know how to clone itself - it needs assistance. 
 * It will copy all the fields save for actors and
 * processor - which also needs to be helped.
 * 
 * @author Henrietta
 */
public class World implements Cloneable {
	public LargeStaticGeometry lsg = new LargeStaticGeometry(new Rectangle[0]);
	public Vector<PhysicActor> actors = new Vector<>();
	public Vector<HorizSegment> platforms = new Vector<>();
	public Rectangle world_area = null;
	
	private WorldCommand processor = null;
	
	static final double GRAVITY = -0.01;	// per 1 time unit, in delta y
	
	public World clone() {
		World wrld = new World(this.world_area);
		wrld.set_lsg(this.lsg.clone());
		for(HorizSegment platf : this.platforms) wrld.add_platform(platf);
		return wrld;
	}
	
	public World(Rectangle world_area) {
		this.world_area = world_area.clone();
	}
	
	public World set_processor(WorldCommand p) { this.processor = p; return this; }
	public WorldCommand get_processor() { return this.processor; }
	
	public World set_lsg(LargeStaticGeometry lsg) { this.lsg = lsg; return this; }
	public World set_world_area(Rectangle area) { this.world_area = area; return this; }
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
	 * Retuns a hashmap that maps a PhysicActor to it's cloned
	 * replacement.
	 * PAILUT = PhysicActor Instance LookUp Table
	 * Used only by .shared.world.World during cloning.
	 * 
	 * The reason this exists is that cloning needs to be smart. If an Actor
	 * has it's instance of PhysicActor AND PhysicWorld has the same, it would be 
	 * stupid if during cloning those were 'separated'. After cloning they should be the
	 * same object, and that's what this method is for.
	 */
	public HashMap<PhysicActor, PhysicActor> clone_return_PAILUT() {
		HashMap<PhysicActor, PhysicActor> ilut = new HashMap<>();
		for (PhysicActor pa : this.actors)
			ilut.put(pa, pa.clone());
		return ilut;
	}
	
	/**
	 * Call before advancing.
	 * @return
	 */
	public void pre_advance(double dt) {
		for (PhysicActor actor : this.actors) actor.advance_round();
	}

	/**
	 * Call after advancing.
	 * @return
	 */
	public void post_advance(double dt) {
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
