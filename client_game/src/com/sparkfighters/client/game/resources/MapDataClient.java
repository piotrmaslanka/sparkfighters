package com.sparkfighters.client.game.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.sparkfighters.shared.loader.jsonobjs.MapData;
/**
 * Class extend MapData.
 * @author Kamil Iwiñski
 *
 */
public class MapDataClient extends MapData
{
	public Texture texture;
	/**
	 * Load texture
	 * @param filePath String to texture
	 */
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
}
