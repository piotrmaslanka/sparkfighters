package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.sparkfighters.client.game.network.GameData.Actor;
import com.sparkfighters.client.game.scene.MapFragment;
import com.sparkfighters.shared.blueprints.ActorBlueprint;
import com.sparkfighters.shared.blueprints.MapBlueprint;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.world.Team;


public enum WorldManager 
{
	INSTANCE;
	
	public ArrayList<com.sparkfighters.client.game.scene.Actor> actors=new ArrayList<com.sparkfighters.client.game.scene.Actor>();
	public int myHeroArrayActors=-1;
	public MapFragment mapFragment=new MapFragment();
	
	public com.sparkfighters.shared.world.World worldLogic;
	
	public void Init()
	{
		Array<Integer> ids_teams=Network.INSTANCE.GameDataMsg.getTeams();
		
		//Create Blueprint map
		MapBlueprint mapBlueprint=new MapBlueprint(ResourcesManager.INSTANCE.map);
		
		//create teams
		Team[] teams=new Team[ids_teams.size];
		
		//create physic world
		com.sparkfighters.shared.physics.world.World worldPhysics=new com.sparkfighters.shared.physics.world.World(ResourcesManager.INSTANCE.map.mapSize);
		
		for(int i=0;i<ids_teams.size;i++)
		{
			HashMap<Integer, Actor> actor_n=Network.INSTANCE.GameDataMsg.getActorsByTeamId(ids_teams.get(i));
			
			//create logic actors
			com.sparkfighters.shared.world.Actor actorsLogic[]=new com.sparkfighters.shared.world.Actor[actor_n.size()];
			
			for(int j=0;j<actor_n.size();j++)
			{
				int idActor=actor_n.get(j).IdActor;
				int idHero=actor_n.get(j).IdHero;
				int idWeapon=actor_n.get(j).IdWeapon;
				int idTeam=actor_n.get(j).IdTeam;
				String login=actor_n.get(j).login;
				
				actors.add(new com.sparkfighters.client.game.scene.Actor(idActor,idHero,idWeapon));
				if(login.equals(Network.INSTANCE.login)) 
				{
					myHeroArrayActors=actors.size()-1;
				}
				
				//Create Blueprint Actor
				ActorBlueprint actorsBlueprint=new ActorBlueprint(ResourcesManager.INSTANCE.weaponsData.get(idWeapon), ResourcesManager.INSTANCE.heroesData.get(idHero));
				
				//create logic actors
				actorsLogic[j]=new com.sparkfighters.shared.world.Actor(idActor, actorsBlueprint);
				actorsLogic[j].physical=actorsLogic[0].actor_blueprint.create_physicactor(idActor);
				actorsLogic[j].physical.set_position(ResourcesManager.INSTANCE.map.spawnPoints.get(i)); //!!!!!
				worldPhysics.add_actor(actorsLogic[j].physical);
			}
			
			teams[i]=new Team(ids_teams.get(i),actorsLogic);
		}
		
		
		
		//feed physic world
		mapBlueprint.feed_to_physics_world(worldPhysics);
		
		//Create logic world
		worldLogic=new com.sparkfighters.shared.world.World(worldPhysics,teams);
	}
	
}
