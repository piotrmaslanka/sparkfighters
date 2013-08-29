package com.sparkfighters.client.game.singletons;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.badlogic.gdx.utils.Array;

import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.client.game.HDD.VFS;
import com.sparkfighters.client.game.HDD.VFSFileHandle;
import com.sparkfighters.client.game.resources.HeroDataClient;
import com.sparkfighters.client.game.resources.MapDataClient;
import com.sparkfighters.client.game.resources.WeaponDataClient;
import com.sparkfighters.client.game.ultis.HeroFlashJsonToOurJson;
/**
 * Singleton to hold all data information from HDD.
 * @author Kamil Iwiñski
 *
 */

public enum ResourcesManager
{
	INSTANCE;
		
	public HashMap<Integer, HeroDataClient> heroesData;
	public HashMap<Integer, WeaponDataClient> weaponsData;
	
	public MapDataClient map; 
	
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
					progress=10;
					break;
			
			case 1: 
					progressText="Connecting to server...";
					break;
			
			
			case 2:
					//here init UDP and authorize connection
					try
					{
						Network.INSTANCE.Connect();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					break;
			
			case 3:
					if(Network.INSTANCE.Authorization==false && Network.INSTANCE.GameData==false)
					{
						step--;
					}
					else
					{
						progress=20;
					}
					break;
				
			case 4:
					progressText="Loading fonts...";
					break;
			case 5: 
					LoadFonts();
					progress=30;
					break;
			
			case 6:
					progressText="Loading heroes...";
					break;
				
			case 7:
					LoadHeroes();
					progress=40;
					break;
			
			case 8:
					progressText="Loading weapons...";				
					break;
			case 9:
					LoadWeapons();
					progress=50;
					break;
			
			case 10:
					progressText="Loading map...";
					break;
			
			case 11: 
					LoadMap();
					progress=60;
					break;	
					
			case 12:
					progressText="Generating world...";
					break;
					
			case 13:
					WorldManager.INSTANCE.Init();
					Network.INSTANCE.Send((byte)0, "RDY");
					progress=90;
					break;
			
			case 14:
					progressText="Waiting for others...";
					break;
					
			case 15:
					//here waiting for others
					if(Network.INSTANCE.StartGame==false)
					{
						step--;
					}
					else
					{
						progress=100;
						progressText="";
						step=0;
					}
					break;
					
				
		}
		
		step++;
	}
	
	public void DrawLoadingScreen(String text)
	{
		DrawEngineGUI.INSTANCE.Draw(loadingScreen, 0, 0);
		DrawEngineGUI.INSTANCE.DrawText(100, 100, Color.ORANGE, loadingFont, text);
	}
	
	private void LoadLoadingScreen() 
	{
		loadingScreen = new Texture(HDD.getFileHandle("data/loadingscreen.png"));
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
		heroesData=new HashMap<Integer, HeroDataClient>();
		
		String path="data/heroes/";
		Array<Integer> ids=Network.INSTANCE.GameDataMsg.getIdHeroesToLoad();
		
		//HeroFlashJsonToOurJson.convert("data/hero.json","data/hero.png","data/hero_new.json");
		
		//Directory of hero
		for(int i=0;i<ids.size;i++)
		{
			HeroDataClient HD=new HeroDataClient();
			HD= HDD.loadClass(path+ids.get(i)+"/data.json", HeroDataClient.class);
			HD.loadTextures(path+ids.get(i));
			heroesData.put(new Integer(HD.id), HD);
		}
		
	}

	private void LoadWeapons()
	{		
		weaponsData=new HashMap<Integer, WeaponDataClient>();
		
		String path="data/weapons/";
		Array<Integer> ids=Network.INSTANCE.GameDataMsg.getIdWeaponsToLoad();
		
		
		//Directory of weapon
		for(int i=0;i<ids.size;i++)
		{
			WeaponDataClient WD=new WeaponDataClient();
			WD=HDD.loadClass(path+ids.get(i)+"/data.json", WeaponDataClient.class);
			WD.loadTexture(path+ids.get(i)+"/data.png");
			
			weaponsData.put(new Integer(WD.id), WD);
		}
		
	}

	private void LoadMap()
	{		
		map=new MapDataClient();
		String path="data/maps/"+Network.INSTANCE.GameDataMsg.getIdMap();
		map=HDD.loadClass(path+"/data.json", MapDataClient.class);
		map.loadTexture(path+"/map.png", path+"/base.png");
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


