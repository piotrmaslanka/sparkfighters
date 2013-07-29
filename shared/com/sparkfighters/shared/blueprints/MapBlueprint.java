package com.sparkfighters.shared.blueprints;

import com.sparkfighters.shared.physics.world.World;
import com.sparkfighters.shared.physics.gwobjects.LargeStaticGeometry;
import com.sparkfighters.shared.physics.objects.HorizSegment;

/**
 * This object alone contains enough information to 
 * build a map.
 * It's purpose is to be a transfer object containing 
 * information about building a map. It is a source 
 * object that can be readed by a World and provide
 * information on map construction.
 * 
 * Idea is simple - loader outputs a MapBlueprint object,
 * then you use it to construct a world.
 * 
 * @author Henrietta
 */
public class MapBlueprint {

	private LargeStaticGeometry lsg;
	private HorizSegment[] platforms;
	
	public MapBlueprint(LargeStaticGeometry lsg, HorizSegment[] platforms) {
		this.lsg = lsg.clone();
		this.platforms = platforms.clone();
	}
	
	/**
	 * Call this upon an empty shared.physics.world.World to have it
	 * accept map represented by this object
	 * @return 
	 */
	public void feed_to_physics_world(World world) {
		for (HorizSegment hs : this.platforms)
			world.add_platform(hs);
		world.set_lsg(this.lsg);
	}

}
