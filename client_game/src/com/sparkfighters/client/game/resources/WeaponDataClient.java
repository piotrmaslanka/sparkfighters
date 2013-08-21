package com.sparkfighters.client.game.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.shared.loader.jsonobjs.WeaponData;
/**
 * Class extend WeaponData.
 * @author Kamil Iwiñski
 *
 */
public class WeaponDataClient extends WeaponData
{

	public Texture texture;
	public TextureRegion[] animations;
	
	/**
	 * Load texture and prepare right_region, left_region
	 * @param filePath String to texture
	 */
	public void loadTexture(String filePath)
	{
		texture=new Texture(HDD.getFileHandle(filePath));
		texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
		
		animations=new TextureRegion[2];
		
		animations[0]=new TextureRegion(texture);
		animations[1]=new TextureRegion(texture);
		animations[1].flip(true, false);
	}
	
}
