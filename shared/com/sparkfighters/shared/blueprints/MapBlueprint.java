package com.sparkfighters.shared.blueprints;

import com.sparkfighters.shared.loader.jsonobjs.MapData;
import com.sparkfighters.shared.physics.gwobjects.LargeStaticGeometry;
import com.sparkfighters.shared.physics.objects.*;

/**
 * This object alone contains enough information to 
 * build a map.
 * It's purpose is to be a transfer object containing 
 * information about building a map. It is a source 
 * object that can be readed by a World and provide
 * information on map construction.
 * 
 * This object is able to load a JSON data object and 
 * parse it to a map.
 * 
 * Immutable
 * 
 * @author Henrietta
 */
final public class MapBlueprint {

	private final LargeStaticGeometry lsg;
	private final HorizSegment[] platforms;
	public final Rectangle world_box;
	
	public final Vector spawnpoints[];
	public final int spawnpoints_team[];
	
	@SuppressWarnings("unused")
	private final Vector spark_start;
	
	public MapBlueprint(MapData md) {
		this.lsg = new LargeStaticGeometry(md.obstacles.toArray(new Rectangle[0]));
		
		this.platforms = md.platforms.toArray(new HorizSegment[0]);
		
		this.spawnpoints = new Vector[md.spawnPoints.size()];
		this.spawnpoints_team = new int[md.spawnPoints.size()];
		
		int i=0;
		for (MapData.SpawnPoint sp : md.spawnPoints) {
			this.spawnpoints[i] = sp.position;
			this.spawnpoints_team[i] = sp.team_id;
			i++;
		}
		this.spark_start = md.sparkStart;
		
		this.world_box = md.mapSize;
	}
	
	/**
	 * Call this upon an empty shared.physics.world.World to have it
	 * accept map represented by this object
	 * @return 
	 */
	public void feed_to_physics_world(com.sparkfighters.shared.physics.world.World world) {
		for (HorizSegment hs : this.platforms)
			world.add_platform(hs);
		world.set_lsg(this.lsg);
		world.set_world_area(this.world_box);
	}

}
