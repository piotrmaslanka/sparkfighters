package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;

import com.sparkfighters.client.game.HDD;
import com.sparkfighters.client.game.resources.HeroDataClient;
import com.sparkfighters.client.game.resources.MapDataClient;
import com.sparkfighters.client.game.resources.WeaponDataClient;

/**
 * Singleton to hold all data information from HDD.
 * @author Kamil Iwiñski
 *
 */

public enum ResourcesManager 
{
	INSTANCE;
	
	public List<HeroDataClient> heroesData=new ArrayList<HeroDataClient>();
	public List<WeaponDataClient> weaponsData=new ArrayList<WeaponDataClient>();
	public MapDataClient map=new MapDataClient(); 
	
	public BitmapFont font;
	public Texture loadingScreen;
	
	/**
	 * Function to load resources from hdd
	 */
	public void LoadResources()
	{
		Texture.setEnforcePotImages(false);
		LoadFonts();
		LoadLoadingScreen();
		
		LoadHeroes();
		LoadWeapons();
		LoadMap();
	}
	
	public void DrawLoadingScreen(String text)
	{
		DrawEngine.INSTANCE.ClearScreen();
		DrawEngine.INSTANCE.Draw(loadingScreen, 0, 0);
		DrawEngine.INSTANCE.DrawText(100, 100, Color.ORANGE, font, text);
	}
	
	private void LoadLoadingScreen() 
	{
		loadingScreen = new Texture(HDD.getFileHandle("data/loadingScreen.png"));
		loadingScreen.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		loadingScreen.getTextureData().prepare();
		MipMapGenerator.generateMipMap(loadingScreen.getTextureData().consumePixmap(), 
				loadingScreen.getWidth(), loadingScreen.getHeight(), true);		
	}
	/**
	 * Function load fonts from HDD
	 */
	private void LoadFonts()
	{
		Texture texture = new Texture(HDD.getFileHandle("data/font.png"));
		texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
		TextureRegion tr=new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		font = new BitmapFont(HDD.getFileHandle("data/font.fnt"), tr, false);
	}
	/**
	 * Function load heroes/bodies from HDD
	 */
	private void LoadHeroes()
	{
		DrawLoadingScreen("Loading heroes...");
		
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
		DrawLoadingScreen("Loading weapons...");
		
		weaponsData=new ArrayList<WeaponDataClient>();
		
		FileHandle[] list=HDD.getDirContent("data/weapons");
		
		//Directory of weapon
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
		DrawLoadingScreen("Loading map...");
		
		map=new MapDataClient();
		String path="data/maps/0";
		map=HDD.loadClass(path+"/data.json", MapDataClient.class);
		map.loadTexture(path+"/data.png");
	}
	
}


