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
	
	/**
	 * For some reason character info was split into two JSON
	 * classes. They need to specified right here for proper
	 * assembly.
	 * @param wd weapon data part
	 * @param hd hero data part
	 */
	public ActorBlueprint(WeaponData wd, HeroData hd) {
		
	}
	
}
