package com.sparkfighters.client.game.loader;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.sparkfighters.shared.loader.jsonobjs.hero.HeroData;



public class HeroDataClient extends HeroData
{

	public Texture texture;
	
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
}
