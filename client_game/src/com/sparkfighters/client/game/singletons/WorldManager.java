package com.sparkfighters.client.game.singletons;

import com.sparkfighters.shared.blueprints.ActorBlueprint;
import com.sparkfighters.shared.blueprints.MapBlueprint;
import com.sparkfighters.shared.physics.objects.Vector;


public enum WorldManager 
{
	INSTANCE;
	
	public com.sparkfighters.client.game.world.ClientWorld clientWorld;
	public com.sparkfighters.client.game.world.SharedWorld sharedWorld;
	
	public void Init()
	{
		clientWorld=new com.sparkfighters.client.game.world.ClientWorld();
		sharedWorld=new com.sparkfighters.client.game.world.SharedWorld();

		
		clientWorld.actors.add(new com.sparkfighters.client.game.clientWorld.Actor(0,0,0));
		clientWorld.myHeroArrayActors=0;

		//Create Blueprints
		sharedWorld.mapBlueprint=new MapBlueprint(ResourcesManager.INSTANCE.map);
		sharedWorld.actorsBluePrint.add(new ActorBlueprint(ResourcesManager.INSTANCE.weaponsData.get(0), ResourcesManager.INSTANCE.heroesData.get(0)));
	
		//create logic actors
		sharedWorld.actorsLogic=new com.sparkfighters.shared.world.Actor[1];
		sharedWorld.actorsLogic[0]=new com.sparkfighters.shared.world.Actor(0, sharedWorld.actorsBluePrint.get(0));
		sharedWorld.actorsLogic[0].physical=sharedWorld.actorsLogic[0].actor_blueprint.create_physicactor(0);
		sharedWorld.actorsLogic[0].physical.set_position(new Vector(1900,2600));
		
		//create physic world
		sharedWorld.worldPhysics=new com.sparkfighters.shared.physics.world.World(ResourcesManager.INSTANCE.map.mapSize);
		sharedWorld.worldPhysics.add_actor(sharedWorld.actorsLogic[0].physical);
		
		//feed physic world
		sharedWorld.mapBlueprint.feed_to_physics_world(sharedWorld.worldPhysics);

		//Create logic world
		sharedWorld.worldLogic=new com.sparkfighters.shared.world.World(sharedWorld.worldPhysics,sharedWorld.actorsLogic);
	}
	
}
