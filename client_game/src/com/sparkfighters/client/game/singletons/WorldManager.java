package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;

import com.sparkfighters.client.game.scene.MapFragment;
import com.sparkfighters.shared.blueprints.ActorBlueprint;
import com.sparkfighters.shared.blueprints.MapBlueprint;
import com.sparkfighters.shared.physics.objects.Vector;


public enum WorldManager 
{
	INSTANCE;
	
	public ArrayList<com.sparkfighters.client.game.scene.Actor> actors=new ArrayList<com.sparkfighters.client.game.scene.Actor>();
	public int myHeroArrayActors=-1;
	public MapFragment mapFragment=new MapFragment();
	
	public com.sparkfighters.shared.world.World worldLogic;
	
	public void Init()
	{
		int id=0;
		int idHero=0;
		int idWeapon=4;
		
	
		actors.add(new com.sparkfighters.client.game.scene.Actor(id,idHero,idWeapon));
		myHeroArrayActors=0;

		//Create Blueprints
		MapBlueprint mapBlueprint=new MapBlueprint(ResourcesManager.INSTANCE.map);
		ActorBlueprint actorsBlueprint=new ActorBlueprint(ResourcesManager.INSTANCE.weaponsData.get(idWeapon), ResourcesManager.INSTANCE.heroesData.get(idHero));
	
		//create logic actors
		com.sparkfighters.shared.world.Actor actorsLogic[]=new com.sparkfighters.shared.world.Actor[1];
		actorsLogic[0]=new com.sparkfighters.shared.world.Actor(id, actorsBlueprint);
		actorsLogic[0].physical=actorsLogic[0].actor_blueprint.create_physicactor(id);
		actorsLogic[0].physical.set_position(new Vector(1900,2600));
		
		//create physic world
		com.sparkfighters.shared.physics.world.World worldPhysics=new com.sparkfighters.shared.physics.world.World(ResourcesManager.INSTANCE.map.mapSize);
		worldPhysics.add_actor(actorsLogic[0].physical);
		
		//feed physic world
		mapBlueprint.feed_to_physics_world(worldPhysics);

		//Create logic world
		worldLogic=new com.sparkfighters.shared.world.World(worldPhysics,actorsLogic);
	}
	
}
