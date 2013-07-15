package com.sparkfighters.client.ResourcesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.files.FileHandle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sparkfighters.client.mainexe.HDD;
import com.sparkfighters.shared.physics.objects.Rectangle;


public enum ResourcesManager 
{
	INSTANCE;
	
	public List<HeroData> heroesData=new ArrayList<HeroData>();
	public List<WeaponData> weaponsData=new ArrayList<WeaponData>();

	public void LoadResources()
	{
		LoadHeroes();
		LoadWeapons();
	}
	
	private void LoadHeroes()
	{
		heroesData=new ArrayList<HeroData>();
		
		FileHandle[] list=HDD.getDirContent("data/heroes");
		
		//Directory of hero
		for(int i=0;i<list.length;i++)
		{
			//ConvertFlashJsonToOurJson(list[i]+"/data.json","data/"+list[i].name()+"_new.json");
			HeroData HD=new HeroData();
			HD=HDD.loadClass(list[i]+"/data.json", HeroData.class);
			HD.loadTexture(list[i]+"/data.png");
			heroesData.add(HD);
		}
		

	}
	
	private void LoadWeapons()
	{
		weaponsData=new ArrayList<WeaponData>();
		
		FileHandle[] list=HDD.getDirContent("data/weapons");
		
		//Directory of hero
		for(int i=0;i<list.length;i++)
		{
			WeaponData WD=new WeaponData();
			HDD.saveClass(list[i]+"/data.json", WD);
			//WD=HDD.loadClass(list[i]+"/data.json", WeaponData.class);
			//WD.loadTexture(list[i]+"/data.png");
			//weaponsData.add(WD);
		}
		
	}
	
	private void ConvertFlashJsonToOurJson(String loadPath, String savePath)
	{
		HeroData HD=new HeroData();
		HD.HeroName=loadPath;
		
		FileHandle fh=HDD.getFileHandle(loadPath);
		String json=fh.readString();
		
		JsonParser parser = new JsonParser(); 
		JsonObject j1 = (JsonObject) parser.parse(json); 
		JsonObject j2=j1.getAsJsonObject("frames");
		
		AnimationData Animation=new AnimationData();
		
		String nameAnimation="";
		
		for (Entry<String, JsonElement> je : j2.entrySet()) 
		{				
			String key=je.getKey().substring(0, je.getKey().length()-4);
			JsonObject j3=j2.getAsJsonObject(je.getKey());
			JsonObject j4=j3.getAsJsonObject("frame");
			
			if(key.equals(nameAnimation)==false)
			{
				nameAnimation=key;
				if(Animation.frames.size()>0)					
				{
					HD.Animations.add(Animation);
					Animation=new AnimationData();
				}
				
				Animation.speedOfAnimation=0.1f;
				if(nameAnimation.equals("death_left")) Animation.id=13;
				if(nameAnimation.equals("death_right")) Animation.id=12;
				if(nameAnimation.equals("idle_left")) Animation.id=1;
				if(nameAnimation.equals("idle_left_45")) Animation.id=3;
				if(nameAnimation.equals("idle_right")) Animation.id=0;
				if(nameAnimation.equals("idle_right_45")) Animation.id=2;
				if(nameAnimation.equals("jump_left")) Animation.id=9;			
				if(nameAnimation.equals("jump_left_45")) Animation.id=11;		
				if(nameAnimation.equals("jump_right")) Animation.id=8;	
				if(nameAnimation.equals("jump_right_45")) Animation.id=10;	
				if(nameAnimation.equals("run_left")) Animation.id=5;
				if(nameAnimation.equals("run_left _45")) Animation.id=7;
				if(nameAnimation.equals("run_right")) Animation.id=4;
				if(nameAnimation.equals("run_right_45")) Animation.id=6;
			}
			
			if(key.equals(nameAnimation)==true)
			{				
				int x=j4.get("x").getAsInt();
				int y=j4.get("y").getAsInt();
				int w=j4.get("w").getAsInt();
				int h=j4.get("h").getAsInt();
				
				Rectangle frame=new Rectangle(x, y, x+w, y+h);
				Animation.frames.add(frame);
				
			}
		}	
		
		HD.Animations.add(Animation);
		
		for(int i=0;i<HD.Animations.size();i++)
		{
			for(int j=0;j<HD.Animations.size();j++)
			{
				if(HD.Animations.get(i).id<HD.Animations.get(j).id)
				{
					AnimationData a=new AnimationData();
					a=HD.Animations.get(i);
					
					HD.Animations.set(i, HD.Animations.get(j));
					HD.Animations.set(j, a);
				}
			}
		}
		
		HDD.saveClass(savePath, HD);
		
	}
}


