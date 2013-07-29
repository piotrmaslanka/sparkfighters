package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sparkfighters.client.game.HDD;
import com.sparkfighters.client.game.resources.HeroDataClient;
import com.sparkfighters.client.game.resources.WeaponDataClient;
import com.sparkfighters.shared.loader.jsonobjs.hero.AnimationData;
import com.sparkfighters.shared.loader.jsonobjs.hero.HeroData;
import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.objects.Vector;


public enum ResourcesManager 
{
	INSTANCE;
	
	public List<HeroDataClient> heroesData=new ArrayList<HeroDataClient>();
	public List<WeaponDataClient> weaponsData=new ArrayList<WeaponDataClient>();
	
	public BitmapFont font;

	public void LoadResources()
	{
		//Texture.setEnforcePotImages(false);
		LoadFonts();
		LoadHeroes();
		LoadWeapons();
		
	}
	
	private void LoadFonts()
	{
		Texture texture = new Texture(HDD.getFileHandle("data/font.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		TextureRegion tr=new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		font = new BitmapFont(HDD.getFileHandle("data/font.fnt"), tr, false);
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
			HD= HDD.loadClass(list[i]+"/data.json", HeroDataClient.class);
			HD.loadTexture(list[i]+"/data.png");
			heroesData.add(HD);
		}
		

	}
	
	private void LoadWeapons()
	{
		weaponsData=new ArrayList<WeaponDataClient>();
		
		FileHandle[] list=HDD.getDirContent("data/weapons");
		
		//Directory of hero
		for(int i=0;i<list.length;i++)
		{
			WeaponDataClient WD=new WeaponDataClient();
			//277 x 63
			/*WD.right=new Rectangle(0.0f,0.0f,277.0f,63.0f);
			WD.left=new Rectangle(227.0f,63.0f,554.0f,126.0f);
			WD.name="Weapon "+i;
			HDD.saveClass(list[i]+"/data.json", WD);*/
			WD=HDD.loadClass(list[i]+"/data.json", WeaponDataClient.class);
			WD.loadTexture(list[i]+"/data.png");
			weaponsData.add(WD);
		}
		
	}
	
	
}


