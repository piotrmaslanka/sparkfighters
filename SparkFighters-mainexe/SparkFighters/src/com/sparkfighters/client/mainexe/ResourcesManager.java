package com.sparkfighters.client.mainexe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


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
			//parse json and png
			Texture Sheet=new Texture(HDD.Load(list[i]+"/data.png"));
			Sheet.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			FileHandle fh=HDD.Load(list[i]+"/data.json");
			String json=fh.readString();
			
			JsonParser parser = new JsonParser(); 
			JsonObject j1 = (JsonObject) parser.parse(json); 
			JsonObject j2=j1.getAsJsonObject("frames");
			
			ArrayList<TextureRegion> listFrames=new ArrayList<TextureRegion>();
			ArrayList<Animation> Animations=new ArrayList<Animation>();
			String nameAnimation="";
			float AnimationSpeed=0f;
			
			for (Entry<String, JsonElement> je : j2.entrySet()) 
			{
				String key=je.getKey().substring(0, je.getKey().length()-4);
				JsonObject j3=j2.getAsJsonObject(je.getKey());
				JsonObject j4=j3.getAsJsonObject("frame");
				
				if(key.equals(nameAnimation)==false)
				{
					nameAnimation=key;
					//AnimationSpeed=j3.get("speed").getAsFloat();
					if(listFrames.size()>0)
					{
						TextureRegion[] tmp=new TextureRegion[listFrames.size()];
						listFrames.toArray(tmp);
						Animation anim=new Animation(AnimationSpeed,tmp);
						Animations.add(anim);
						listFrames.clear();
					}
					AnimationSpeed=j3.get("speed").getAsFloat();
				}
				
				if(key.equals(nameAnimation)==true)
				{				
					int x=j4.get("x").getAsInt();
					int y=j4.get("y").getAsInt();
					int w=j4.get("w").getAsInt();
					int h=j4.get("h").getAsInt();
					
					TextureRegion frame=new TextureRegion(Sheet);
					frame.setRegion(x, y, w, h);
					listFrames.add(frame);
				}
			}
			
			TextureRegion[] tmp=new TextureRegion[listFrames.size()];
			listFrames.toArray(tmp);
			Animation anim=new Animation(AnimationSpeed,tmp);
			Animations.add(anim);
			
			HeroData HG=new HeroData();
			HG.HeroName=list[i].name();
			HG.Animations=Animations;
			HeroesData.add(HG);
			
		}


	}
	
}


