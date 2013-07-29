package com.sparkfighters.shared.physics.world;

import com.sparkfighters.shared.physics.gwobjects.LargeStaticGeometry;
import com.sparkfighters.shared.physics.gwobjects.PhysicActor;
import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Rectangle;
/**
 * An command-like pattern inteface to 
 * execute and process information coming from World
 * @author Henrietta
 *
 */
public interface WorldCommand {
	public void on_actor_hits_world_area(PhysicActor ac, Rectangle wa, double dt);
	public void on_actor_hits_obstacle(PhysicActor ac, LargeStaticGeometry lsg, double dt);
	public void on_actor_hits_platform(PhysicActor ac, HorizSegment segm, double dt);
	public void on_actor_hits_actor(PhysicActor a1, PhysicActor a2, double dt);
}
