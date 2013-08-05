package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.sparkfighters.client.game.HDD;
import com.sparkfighters.client.game.resources.HeroDataClient;
import com.sparkfighters.client.game.resources.MapDataClient;
import com.sparkfighters.client.game.resources.WeaponDataClient;

/**
 * Singleton to hold all data information from HDD.
 * @author Kamil Iwi�ski
 *
 */

public enum ResourcesManager 
{
	INSTANCE;
	
	public List<HeroDataClient> heroesData=new ArrayList<HeroDataClient>();
	public List<WeaponDataClient> weaponsData=new ArrayList<WeaponDataClient>();
	public MapDataClient map=new MapDataClient(); 
	
	public BitmapFont font;
	
	/**
	 * Function to load resources from hdd
	 */
	public void LoadResources()
	{
		Texture.setEnforcePotImages(false);
		LoadFonts();
		LoadHeroes();
		LoadWeapons();
		LoadMap();
	}
	/**
	 * Function load fonts from HDD
	 */
	private void LoadFonts()
	{
		Texture texture = new Texture(HDD.getFileHandle("data/font.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		TextureRegion tr=new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		font = new BitmapFont(HDD.getFileHandle("data/font.fnt"), tr, false);
	}
	/**
	 * Function load heroes/bodies from HDD
	 */
	private void LoadHeroes()
	{
		heroesData=new ArrayList<HeroDataClient>();
		
		FileHandle[] list=HDD.getDirContent("data/heroes");
		
		//Directory of hero
		for(int i=0;i<list.length;i++)
		{
			//ConvertFlashJsonToOurJson(list[i]+"/data.json","data/"+list[i].name()+"_new.json");
			HeroDataClient HD=new HeroDataClient();
			HD= HDD.loadClass("data/heroes/"+i+"/data.json", HeroDataClient.class);
			HD.loadTexture(list[i]+"/data.png");
			heroesData.add(HD);
		}
		

	}
	/**
	 * Function load weapons form HDD
	 */
	private void LoadWeapons()
	{
		weaponsData=new ArrayList<WeaponDataClient>();
		
		FileHandle[] list=HDD.getDirContent("data/weapons");
		
		//Directory of hero
		for(int i=0;i<list.length;i++)
		{
			WeaponDataClient WD=new WeaponDataClient();
			WD=HDD.loadClass("data/weapons/"+i+"/data.json", WeaponDataClient.class);
			WD.loadTexture("data/weapons/"+i+"/data.png");
			weaponsData.add(WD);
		}
		
	}
	/**
	 * Function to load map from hdd
	 */
	private void LoadMap()
	{
		map=new MapDataClient();
		String path="data/maps/0";
		map=HDD.loadClass(path+"/data.json", MapDataClient.class);
		map.loadTexture(path+"/data.png");
	}
	
	public void setTextureFilter(TextureFilter tf)
	{
		for(int i=0;i<heroesData.size();i++)
		{
			heroesData.get(i).texture.setFilter(tf, tf);
		}
		
		for(int i=0;i<weaponsData.size();i++)
		{
			weaponsData.get(i).texture.setFilter(tf, tf);
		}
		
		map.texture.setFilter(tf, tf);
	}
	
}


