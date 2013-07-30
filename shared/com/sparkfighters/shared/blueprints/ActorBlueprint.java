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
final public class ActorBlueprint {
	
	public GeometrySet gs = null;
	
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
			smgs[i] = ActorBlueprint.animationdata_to_smg(hd.Animations.get(i));
		
		this.gs = new GeometrySet(smgs);
	}
	
	
	static private SmallMovingGeometry animationdata_to_smg(AnimationData ad) {
		Rectangle hitboxes[] = (Rectangle[]) ad.hitboxes.toArray();
		for (int i=0; i<hitboxes.length; i++)
			hitboxes[i] = hitboxes[i].translate(ad.synchroPoint.negative());
		
		return new SmallMovingGeometry(hitboxes);
	}
	
}
