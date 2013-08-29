package com.sparkfighters.client.game.resources;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.shared.loader.jsonobjs.HeroData;
/**
 * Class extend HeroData.
 * @author Kamil Iwiñski
 *
 */
public class HeroDataClient extends HeroData
{

	public Texture textureMyHeroBody;
	public Texture textureMyHeroHead;	
	public ArrayList<Animation> animationsDrawableMyHeroBody;
	public TextureRegion[] animationsDrawableMyHeroHead;
	
	public Texture textureFriendHeroBody;
	public Texture textureFriendHeroHead;	
	public ArrayList<Animation> animationsDrawableFriendHeroBody;
	public TextureRegion[] animationsDrawableFriendHeroHead;
	
	public Texture textureEnemyHeroBody;
	public Texture textureEnemyHeroHead;	
	public ArrayList<Animation> animationsDrawableEnemyHeroBody;
	public TextureRegion[] animationsDrawableEnemyHeroHead;
	
	public void loadTextures(String Path)
	{
		 CreateAnimations(Path+"/my_hero_body.png", Path+"/my_hero_head.png",
				 textureMyHeroBody, textureMyHeroHead, 
				 animationsDrawableMyHeroBody, animationsDrawableMyHeroHead);
		 
		 CreateAnimations(Path+"/friend_hero_body.png", Path+"/friend_hero_head.png",
				 textureFriendHeroBody, textureFriendHeroHead, 
				 animationsDrawableFriendHeroBody, animationsDrawableFriendHeroHead);
		 
		 CreateAnimations(Path+"/enemy_hero_body.png", Path+"/enemy_hero_head.png",
				 textureEnemyHeroBody, textureEnemyHeroHead, 
				 animationsDrawableEnemyHeroBody, animationsDrawableEnemyHeroHead);
	}
	
	private void CreateAnimations(String filePathBody, String filePathHead,
			Texture textureBody, Texture textureHead, 
			ArrayList<Animation> animationsDrawableBody,TextureRegion[] animationsDrawableHead)
	{
		textureBody=new Texture(HDD.getFileHandle(filePathBody));
		textureBody.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);
		
		textureBody.getTextureData().prepare();
		MipMapGenerator.generateMipMap(textureBody.getTextureData().consumePixmap(), 
				textureBody.getWidth(), textureBody.getHeight(), true);
		
		animationsDrawableBody=new ArrayList<Animation>();
		
		for(int i=0;i<Animations.size();i++)
		{
			TextureRegion[] frames=new TextureRegion[Animations.get(i).frames.size()];
			
			for(int j=0;j<Animations.get(i).frames.size();j++)
			{
				int x=(int)Animations.get(i).frames.get(j).x1;
			    int y=(int)Animations.get(i).frames.get(j).y1;
			    int w=(int)Animations.get(i).frames.get(j).x2-x;
			    int h=(int)Animations.get(i).frames.get(j).y2-y;
				TextureRegion tr=new TextureRegion(textureBody);
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
			animationsDrawableBody.add(a);
		}
		
		
		textureHead=new Texture(HDD.getFileHandle(filePathHead));
		textureHead.setFilter(TextureFilter.MipMap, TextureFilter.MipMap);	
		textureHead.getTextureData().prepare();
		MipMapGenerator.generateMipMap(textureHead.getTextureData().consumePixmap(), 
				textureHead.getWidth(), textureHead.getHeight(), true);
		
		animationsDrawableHead=new TextureRegion[2];
		animationsDrawableHead[0]=new TextureRegion(textureHead);
		animationsDrawableHead[1]=new TextureRegion(textureHead);
		animationsDrawableHead[1].flip(true, false);
	}
	
}
