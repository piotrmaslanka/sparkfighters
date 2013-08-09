package com.sparkfighters.shard.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.sparkfighters.shared.world.*;
import com.sparkfighters.shared.blueprints.*;

import com.sparkfighters.shared.loader.jsonobjs.*;

/**
 * This class's job is to load a World from DTO objects
 * @author Henrietta
 *
 */
public class WorldConstructor {

	private String contentfs_root;
	
	public WorldConstructor(String contentfs_root) {
		this.contentfs_root = contentfs_root;
	}

	public World load_from_dto(JSONBattleDTO dto) throws Exception {
		// load physical world
		MapBlueprint map_bp = this.load_map_bp(dto.map);
		com.sparkfighters.shared.physics.world.World pworld = 
				new com.sparkfighters.shared.physics.world.World(map_bp.world_box);
		map_bp.feed_to_physics_world(pworld);
		
		
		// load teams
		Team[] teams = new Team[4];	// always at least 4 teams
		for (int i=0; i<4; i++) teams[i] = new Team(i, new Actor[0]);
		
		// go thru actors. Create them and append
		for (JSONUserDTO user : dto.users) {
			ActorBlueprint abp = this.load_actor_bp(user.weapon_id, user.hero_id);
			Actor actor = new Actor(user.id, abp);
			
			Actor[] target_actors = new Actor[teams[user.team_id].actors.length+1];
			target_actors[teams[user.team_id].actors.length] = actor;
			teams[user.team_id].actors = target_actors;
		}
		
		// create world
		return new World(pworld, teams);
	}

	
	
	public ActorBlueprint load_actor_bp(int weapon_id, int hero_id) throws Exception {
		return new ActorBlueprint(this.load_weapon(weapon_id),
								  this.load_hero(hero_id));
	}
	
	public MapBlueprint load_map_bp(int map_id) throws Exception {
		return new MapBlueprint(this.load_map(map_id));
	}
	
	public MapData load_map(int map_id) throws Exception {
		Path p = Paths.get(this.contentfs_root);
		p = p.resolve("maps").resolve((new Integer(map_id).toString()));

		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(p.toString()))));
		return gson.fromJson(reader, MapData.class);		
	}

	public HeroData load_hero(int hero_id) throws Exception {
		Path p = Paths.get(this.contentfs_root);
		p = p.resolve("heroes").resolve((new Integer(hero_id).toString()));
		
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(p.toString()))));
		return gson.fromJson(reader, HeroData.class);		
	}

	public WeaponData load_weapon(int weapon_id) throws Exception {
		Path p = Paths.get(this.contentfs_root);
		p = p.resolve("weapons").resolve((new Integer(weapon_id).toString()));

		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(p.toString()))));
		return gson.fromJson(reader, WeaponData.class);
	}
}
