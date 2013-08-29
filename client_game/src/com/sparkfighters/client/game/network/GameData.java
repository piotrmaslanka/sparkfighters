package com.sparkfighters.client.game.network;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.sparkfighters.client.game.resources.HeroDataClient;
import com.sparkfighters.client.game.singletons.Network;

public class GameData 
{
	private int IdMap=-1;
	private HashMap<Integer, Actor> actors=new HashMap<Integer, Actor>();
	
	public class Actor
	{
		public int IdHero=-1;
		public int IdWeapon=-1;
		public int IdActor=-1;
		public int IdTeam=-1;
		public String login="";
		
		public Actor(int IdHero, int IdWeapon, int IdActor, int IdTeam, String login)
		{
			this.IdHero=IdHero;
			this.IdWeapon=IdWeapon;
			this.IdActor=IdActor;
			this.IdTeam=IdTeam;
			this.login=login;
		}
		
	}
	
	public GameData(String msg)
	{
		String[] s=msg.split("\00");
		
		this.IdMap=Integer.valueOf(s[0]);
		
		for(int i=0;i<(s.length-1)/5;i++)
		{
			Actor a=new Actor(Integer.valueOf(s[i+1]),Integer.valueOf(s[i+2]),Integer.valueOf(s[i+3]),Integer.valueOf(s[i+4]),s[i+5]);
			actors.put(new Integer(a.IdActor), a);
		}
	}
	
	public int getIdMap()
	{
		return IdMap;
	}
	
	public Array<Integer> getIdHeroesToLoad()
	{
		Array<Integer> ids=new Array<Integer>();
		
		for(int i=0;i<actors.size();i++)
		{
			boolean found=false;
			for(int j=0;j<ids.size;j++)
			{
				if(actors.get(i).IdHero==ids.get(j)) found=true;
			}
			
			if(found==false)
			{
				ids.add(actors.get(i).IdHero);
			}
		}
		
		return ids;		
	}
	
	public Array<Integer> getIdWeaponsToLoad()
	{
		Array<Integer> ids=new Array<Integer>();
		
		for(int i=0;i<actors.size();i++)
		{
			boolean found=false;
			for(int j=0;j<ids.size;j++)
			{
				if(actors.get(i).IdWeapon==ids.get(j)) found=true;
			}
			
			if(found==false)
			{
				ids.add(actors.get(i).IdWeapon);
			}
		}
		
		return ids;		
	}

	public Array<Integer> getTeams()
	{
		Array<Integer> ids=new Array<Integer>();
		
		for(int i=0;i<actors.size();i++)
		{
			boolean found=false;
			for(int j=0;j<ids.size;j++)
			{
				if(actors.get(i).IdTeam==ids.get(j)) found=true;
			}
			
			if(found==false)
			{
				ids.add(actors.get(i).IdTeam);
			}
		}
		
		return ids;
	}
	
	public HashMap<Integer, Actor> getActorsByTeamId(int idTeam)
	{
		HashMap<Integer, Actor> a=new HashMap<Integer, Actor>();
		for(int i=0;i<actors.size();i++)
		{
			if(actors.get(i).IdTeam==idTeam)
			{
				a.put(new Integer(actors.get(i).IdActor), actors.get(i));
			}
		}
		
		return a;
	}
}

