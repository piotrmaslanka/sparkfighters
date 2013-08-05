package com.sparkfighters.client.game.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.sparkfighters.shared.loader.jsonobjs.WeaponData;
/**
 * Class extend WeaponData.
 * @author Kamil Iwi�ski
 *
 */
public class WeaponDataClient extends WeaponData
{
	public Texture texture;
	public TextureRegion right_region;
	public TextureRegion left_region;
	
	/**
	 * Load texture and prepare right_region, left_region
	 * @param filePath String to texture
	 */
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
		
		right_region=new TextureRegion(texture);
		right_region.setRegion((int)right.x1, (int)right.y1,(int)(right.x2-right.x1),(int) (right.y2-right.y1));
		
		left_region=new TextureRegion(texture);
		left_region.setRegion((int)left.x1, (int)left.y1,(int)(left.x2-left.x1),(int)(left.y2-left.y1));
	}
	
}
