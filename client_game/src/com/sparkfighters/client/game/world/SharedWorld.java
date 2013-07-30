package com.sparkfighters.client.game.world;

import java.util.ArrayList;

import com.sparkfighters.shared.blueprints.ActorBlueprint;
import com.sparkfighters.shared.blueprints.MapBlueprint;

public class SharedWorld 
{
	public ArrayList<ActorBlueprint> actorsBluePrint=new ArrayList<ActorBlueprint>();
	public MapBlueprint mapBlueprint;
	
	public com.sparkfighters.shared.world.Actor actorsLogic[];
	
	public com.sparkfighters.shared.physics.world.World worldPhysics;
	
	public com.sparkfighters.shared.world.World worldLogic;
	
}
