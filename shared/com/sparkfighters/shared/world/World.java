package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.gwobjects.LargeStaticGeometry;
import com.sparkfighters.shared.physics.gwobjects.PhysicActor;
import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Rectangle;

public class World extends com.sparkfighters.shared.physics.world.World {

	
	
	public World(Rectangle world_area) {
		super(world_area);
	}
	
	
	protected void on_actor_hits_world_area(PhysicActor ac, double dt) {
		
	}
	protected void on_actor_hits_obstacle(PhysicActor ac, LargeStaticGeometry lsg, double dt) {
		
	}
	protected void on_actor_hits_platform(PhysicActor ac, HorizSegment segm, double dt) {
		
	}
	protected void on_actor_hits_actor(PhysicActor a1, PhysicActor a2, double dt) {
		
	}

}
