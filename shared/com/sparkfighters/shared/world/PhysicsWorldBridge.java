package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.gwobjects.LargeStaticGeometry;
import com.sparkfighters.shared.physics.gwobjects.PhysicActor;
import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.world.WorldCommand;

/**
 * A bridge from physics world to shared world.
 * 
 * This class has essentially no state.
 * 
 * @author Henrietta
 *
 */
public class PhysicsWorldBridge implements WorldCommand {

	World world;
	
	
	/**
	 * PRIVATE. To be called by shared.world.World
	 * @param world
	 */
	public PhysicsWorldBridge(World world) {
		this.world = world;
	}
	
	public void on_actor_hits_world_area(PhysicActor ac, double dt) {
		
	}
	public void on_actor_hits_obstacle(PhysicActor ac, LargeStaticGeometry lsg, double dt) {
		
	}
	public void on_actor_hits_platform(PhysicActor ac, HorizSegment segm, double dt) {
		
	}
	public void on_actor_hits_actor(PhysicActor a1, PhysicActor a2, double dt) {
		
	}

	public void on_actor_hits_world_area(PhysicActor ac, Rectangle wa, double dt) {
		
	}
	

}
