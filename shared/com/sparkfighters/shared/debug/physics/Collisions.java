package com.sparkfighters.shared.debug.physics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sparkfighters.shared.physics.objects.*;
import com.sparkfighters.shared.physics.gwobjects.*;
import com.sparkfighters.shared.physics.world.*;

public class Collisions {
	World world;
	
	private class CollisionResponder implements WorldCommand {
		public void on_actor_hits_world_area(PhysicActor ac, Rectangle wa, double dt) {
			Rebound.rebound(ac, wa, dt);
		}
		public void on_actor_hits_obstacle(PhysicActor ac, LargeStaticGeometry lsg, double dt) {
			Rebound.rebound(ac, lsg, dt);
		}
		public void on_actor_hits_platform(PhysicActor ac, HorizSegment segm, double dt) {
			Rebound.rebound(ac, segm, dt);
		}
		public void on_actor_hits_actor(PhysicActor a1, PhysicActor a2,	double dt) {}
	}

	@Before
	public void setUp() throws Exception {
		this.world = new World(new Rectangle(0, 0, 1000, 1000));
		this.world.set_processor(new CollisionResponder());
		this.world.set_lsg(new LargeStaticGeometry(new Rectangle[0]));
	}

	
	static private PhysicActor make_dummy_actor() {
		// He's called Blocky Balboa
		Rectangle[] ar = {new Rectangle(-5, -5, 5, 5)};
		SmallMovingGeometry smg = new SmallMovingGeometry(ar);
		SmallMovingGeometry[] smga = {smg, smg, null, null, smg, smg, null, null, smg, smg};
		GeometrySet gs = new GeometrySet(smga);
		PhysicActor ps = new PhysicActor(gs, 0, true);
		ps.set_gravity_factor(1);
		return ps;
	}
	
	@Test
	public void testWorldBoxCollision() {
		PhysicActor ps = Collisions.make_dummy_actor();
		ps.set_position(new Vector(20, 40));
		
		this.world.add_actor(ps);
		
		for (int i=0; i<1000; i++) {
			this.world.advance_gravity(1);
			this.world.advance_collisions(1);
			this.world.advance_movement(1);
		}
		
		assertTrue(ps.get_position().y - 5 < 0.14);
	}
	
	@Test
	public void testPlatformCollision() {
		PhysicActor ps = Collisions.make_dummy_actor();
		ps.set_position(new Vector(20, 40));
		
		HorizSegment hs = new HorizSegment(0, 1000, 30);
		this.world.add_platform(hs);
		this.world.add_actor(ps);
		
		for (int i=0; i<1000; i++) {
			this.world.advance_gravity(1);
			this.world.advance_collisions(1);
			this.world.advance_movement(1);
		}
		
		assertTrue(ps.get_position().y - 35 < 0.14);		
	}
	
}
