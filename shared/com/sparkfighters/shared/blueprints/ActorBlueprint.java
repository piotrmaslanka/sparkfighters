package com.sparkfighters.shared.blueprints;

import com.sparkfighters.shared.loader.jsonobjs.*;
import com.sparkfighters.shared.physics.gwobjects.*;
import com.sparkfighters.shared.physics.objects.*;

/**
 * This object alone contains enough information to 
 * build an actor.
 * It's purpose is to be a transfer object containing 
 * information about an actor.
 * 
 * This object is able to load a JSON data object and 
 * parse it to actor information.
 * 
 * Immutable.
 * 
 * @author Henrietta
 */
final public class ActorBlueprint implements Cloneable {
	
	public GeometrySet gs = null;
	
	public double runSpeed;
	public double jumpSpeed;
	
	/**
	 * For some reason character info was split into two JSON
	 * classes. They need to specified right here for proper
	 * assembly.
	 * @param wd weapon data part
	 * @param hd hero data part
	 */
	public ActorBlueprint(WeaponData wd, HeroData hd) {
		SmallMovingGeometry[] smgs = new SmallMovingGeometry[hd.Animations.size()];
		for (int i=0; i<smgs.length; i++)
			smgs[i] = ActorBlueprint.animationdata_to_smg(hd.Animations.get(i), false);
		
		assert(smgs.length >= 5);
		
		this.gs = new GeometrySet(smgs);
		
		this.runSpeed = (double) wd.runSpeed;
		this.jumpSpeed = (double) wd.jumpHeight;
	}
	
	/**
	 * Creates a new PhysicActor from the blueprint
	 * @return new Physicactor
	 */
	public PhysicActor create_physicactor(int color) {
		PhysicActor pa = new PhysicActor(this.gs.clone(), color, true);
		pa.set_gravity_factor(1);
		return pa;
		
	}
	
	/**
	 * @param reflect Whether rectangles should be reflected along Y axis
	 */
	static private SmallMovingGeometry animationdata_to_smg(AnimationData ad, boolean reflect) {
		Rectangle hitboxes[] = ad.hitboxes.toArray(new Rectangle[0]);
		if (reflect)
			for (int i=0; i<hitboxes.length; i++)
				hitboxes[i] = hitboxes[i].reflect_y();
	
		for (int i=0; i<hitboxes.length; i++)
			hitboxes[i] = hitboxes[i].translate(ad.synchroPoint.negative());
		
		return new SmallMovingGeometry(hitboxes);
	}
	
	/**
	 * PRIVATE
	 */
	public ActorBlueprint clone() { return this; }
}
