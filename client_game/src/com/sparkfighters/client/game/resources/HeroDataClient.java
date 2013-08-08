package com.sparkfighters.client.game.resources;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.sparkfighters.shared.loader.jsonobjs.HeroData;
/**
 * Class extend HeroData.
 * @author Kamil Iwiñski
 *
 */
public class HeroDataClient extends HeroData
{

	public Texture texture;
	public ArrayList<Animation> animationsDrawable;
	
	/**
	 * Load texture and prepare animationsDrawable
	 * @param filePath String to texture
	 */
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		
		texture.getTextureData().prepare();
		MipMapGenerator.generateMipMap(texture.getTextureData().consumePixmap(), 
				texture.getWidth(), texture.getHeight(), true);
		
		animationsDrawable=new ArrayList<Animation>();
		
		for(int i=0;i<Animations.size();i++)
		{
			TextureRegion[] frames=new TextureRegion[Animations.get(i).frames.size()];
			
			for(int j=0;j<Animations.get(i).frames.size();j++)
			{
				int x=(int)Animations.get(i).frames.get(j).x1;
			    int y=(int)Animations.get(i).frames.get(j).y1;
			    int w=(int)Animations.get(i).frames.get(j).x2-x;
			    int h=(int)Animations.get(i).frames.get(j).y2-y;
				TextureRegion tr=new TextureRegion(texture);
				tr.setRegion(x,y, w, h);
				if(i%2==0)
				{
					//right animation
				}
				else
				{
					//left animation
					tr.flip(true, false);
				}
				frames[j]=tr;
			}
			Animation a=new Animation(Animations.get(i).speedOfAnimation,frames);
			animationsDrawable.add(a);
		}
	}
	
}
