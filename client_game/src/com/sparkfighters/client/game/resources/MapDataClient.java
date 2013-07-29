package com.sparkfighters.client.game.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.sparkfighters.shared.loader.jsonobjs.MapData;

public class MapDataClient extends MapData
{
	public Texture texture;
	
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
