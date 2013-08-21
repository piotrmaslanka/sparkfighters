package com.sparkfighters.client.game.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.shared.loader.jsonobjs.MapData;
/**
 * Class extend MapData.
 * @author Kamil Iwiñski
 *
 */
public class MapDataClient extends MapData
{
	public Texture mapTexture;
	public Texture baseTexture;
	
	public TextureRegion[] base;
	/**
	 * Load texture
	 * @param filePathMap String to texture
	 */
	public void loadTexture(String filePathMap, String filePathBase)
	{
		mapTexture=new Texture(HDD.getFileHandle(filePathMap));
		mapTexture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		
		mapTexture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(mapTexture.getTextureData().consumePixmap(), 
				mapTexture.getWidth(), mapTexture.getHeight(), true);
		
		
		baseTexture=new Texture(HDD.getFileHandle(filePathBase));
		baseTexture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		
		baseTexture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(baseTexture.getTextureData().consumePixmap(), 
				 baseTexture.getWidth(), baseTexture.getHeight(), true);
		
		base=new TextureRegion[2];
		base[0]=new TextureRegion(baseTexture);
		base[0].setRegion(0, 0, 512, 512);
		base[1]=new TextureRegion(baseTexture);
		base[1].setRegion(0, 512, 512, 1024);		
	}
}
