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
	
	public List<HeroData> HeroesData;

	public void LoadResources()
	{
		LoadHeroes();
	}
	
	private void LoadHeroes()
	{
		HeroesData=new ArrayList<HeroData>();
		
		
		FileHandle[] list=HDD.getDirectories("data/heroes");
		
		//Directory of hero
		for(int i=0;i<list.length;i++)
		{
			HeroData HG=new HeroData();
			HG.HeroName=list[i].name();
			
			//parse json and png
			//Texture Sheet=new Texture(HDD.getFileHandle(list[i]+"/data.png"));
			//Sheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			FileHandle fh=HDD.getFileHandle(list[i]+"/data.json");
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
						HG.Animations.add(Animation);
						Animation=new AnimationData();
					}
					Animation.speedOfAnimation=j3.get("speed").getAsFloat();
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
			
			HG.Animations.add(Animation);
			HeroesData.add(HG);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json2 = gson.toJson(HG);
			
			json2=json2.replace("\n","\r\n");
			
			FileHandle fh2=HDD.getFileHandle("data/test.json");
			fh2.writeString(json2, false);
			
		}
		

	}
	
}


