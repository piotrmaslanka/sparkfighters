package com.sparkfighters.client.game.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.sparkfighters.shared.loader.jsonobjs.MapData;
/**
 * Class extend MapData.
 * @author Kamil Iwi�ski
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
		
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
	}
}
