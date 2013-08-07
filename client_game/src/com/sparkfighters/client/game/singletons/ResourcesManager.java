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
 * @author Kamil Iwi�ski
 *
 */

public enum ResourcesManager
{
	INSTANCE;
		
	
	public List<HeroDataClient> heroesData=new ArrayList<HeroDataClient>();
	public List<WeaponDataClient> weaponsData=new ArrayList<WeaponDataClient>();
	public MapDataClient map=new MapDataClient(); 
	
	public BitmapFont debugFont;
	
	
	public Texture loadingScreen;	
	public BitmapFont loadingFont;

	private int progress=0;
	private String progressText="";
	private int step=0;
	
	public void LoadResources()
	{	
		switch (step)
		{
			case 0: 
					progress=0;
					progressText="";		
					Texture.setEnforcePotImages(false);
					LoadLoadingScreen();
					progress+=10;
					break;
			
			case 1:
					progressText="Loading fonts...";
					break;
			case 2: 
					LoadFonts();
					progress+=10;
					break;
			
			case 3:
					progressText="Loading heroes...";
					break;
				
			case 4:
					LoadHeroes();
					progress+=20;
					break;
			
			case 5:
					progressText="Loading weapons...";				
					break;
			case 6:
					LoadWeapons();
					progress+=20;
					break;
			
			case 7:
					progressText="Loading map...";
					break;
			
			case 8: 
					LoadMap();
					progress=100;
					progressText="";
					break;	
		}
		
		step++;
	}
	
	public void DrawLoadingScreen(String text)
	{
		DrawEngine.INSTANCE.ClearScreen();
		DrawEngine.INSTANCE.Draw(loadingScreen, 0, 0);
		DrawEngine.INSTANCE.DrawText(100, 100, Color.ORANGE, loadingFont, text);
	}
	
	private void LoadLoadingScreen() 
	{		
		loadingScreen = new Texture(HDD.getFileHandle("data/loadingScreen.png"));
		loadingScreen.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		loadingScreen.getTextureData().prepare();
		MipMapGenerator.generateMipMap(loadingScreen.getTextureData().consumePixmap(), 
				loadingScreen.getWidth(), loadingScreen.getHeight(), true);	
		
		
		Texture texture = new Texture(HDD.getFileHandle("data/fonts/loadingfont.png"));
		texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
		TextureRegion tr=new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		loadingFont = new BitmapFont(HDD.getFileHandle("data/fonts/loadingfont.fnt"), tr, false);
	}
	
	private void LoadFonts()
	{
		Texture texture = new Texture(HDD.getFileHandle("data/fonts/debugfont.png"));
		texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
		TextureRegion tr=new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		debugFont = new BitmapFont(HDD.getFileHandle("data/fonts/debugfont.fnt"), tr, false);
	}

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

	private void LoadWeapons()
	{
		progressText="Loading weapons...";
		
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

	private void LoadMap()
	{		
		map=new MapDataClient();
		String path="data/maps/0";
		map=HDD.loadClass(path+"/data.json", MapDataClient.class);
		map.loadTexture(path+"/data.png");
	}

	public boolean finished()
	{
		if(progress==100) return true;
		return false;
	}
	
	public int getProgress()
	{
		return progress;
	}
	
	public String getProgressText()
	{
		return progressText;		
	}
	
}


