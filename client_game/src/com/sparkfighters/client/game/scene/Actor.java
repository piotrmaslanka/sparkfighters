package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.GameEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;
/**
 * Class to manage Actor on scene
 * @author Kamil Iwiñski
 *
 */
public class Actor 
{
	private int x_absolute,y_absolute;
	private int x_relative,y_relative;
	
	private int idHeroArrayResource,idWeaponArrayResource, pid;
	private int idAnimation;
	private float time;
	/**
	 * Function set X absolute on map
	 * Calculate X relative
	 * @param x
	 */
	public void setX_absolute(int x)
	{
		this.x_absolute=x;
		this.x_relative=this.x_absolute-GameEngine.INSTANCE.mapFragment.getX();
	}
	
	/**
	 * Function set Y absolute on map
	 * Calculate Y relative
	 * @param y
	 */
	public void setY_absolute(int y)
	{
		this.y_absolute=y;
		this.y_relative=this.y_absolute-GameEngine.INSTANCE.mapFragment.getY();
	}
	/**
	 * 
	 * @return X absolute
	 */
	public int getX_absolute()
	{
		return this.x_absolute;
	}
	/**
	 * 
	 * @return Y absolute
	 */
	public int getY_absolute()
	{
		return this.y_absolute;
	}
	/**
	 * Constructor to create Actor
	 * @param pid to identify Actor across all map
	 * @param idHero Actor need body to exist
	 * @param idWeapon Actor need a weapon to exist
	 * @param x absolute of actor on map
	 * @param y absolute of actor on map
	 */
	public Actor(int pid, int idHero, int idWeapon, int x, int y)
	{
		this.pid=pid;
		setX_absolute(x);
		setY_absolute(y);
		
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.size();i++)
		{
			if(idHero==ResourcesManager.INSTANCE.heroesData.get(i).id)
			{
				this.idHeroArrayResource=i;
				break;
			}
		}
		
		for(int i=0;i<ResourcesManager.INSTANCE.weaponsData.size();i++)
		{
			if(idWeapon==ResourcesManager.INSTANCE.weaponsData.get(i).id)
			{
				this.idWeaponArrayResource=i;
				break;
			}
		}
		
		setAnimation(0);
	}
	
	/**
	 * Set animation by animation id
	 * @param id
	 */
	public void setAnimation(int id)
	{
		time=0f;
		idAnimation=id;
	}
	/**
	 * Function draw Actor: body, weapon
	 */
	public void Draw()
	{
		//draw hero
		time += Gdx.graphics.getDeltaTime(); 
		TextureRegion currentFrame=ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).animationsDrawable.get(idAnimation).getKeyFrame(time, true);
		DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative);
		
		//draw weapon
		int h_x=currentFrame.getRegionWidth()/2;
		int h_y=currentFrame.getRegionHeight()/2;
		
		int x1=this.x_relative+h_x;
		int y1=this.y_relative+h_y;
		
		if(idAnimation%2==0)
		{
			int w_x=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region.getRegionWidth()/2;
			int w_y=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region.getRegionHeight()/2;
			
			x1=x1-w_x;
			y1=y1-w_y;
			
			DrawEngine.INSTANCE.Draw(ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region, x1,y1);	
		}

		if(idAnimation%2==1)
		{
			int w_x=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region.getRegionWidth()/2;
			int w_y=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region.getRegionHeight()/2;
			
			x1=x1-w_x;
			y1=y1-w_y;
			
			DrawEngine.INSTANCE.Draw(ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region, x1,y1);	
		}
	}
	
	/**
	 * Function draw debug information on screen
	 * @param x int
	 * @param y int
	 * @param font BitmapFont
	 * @param color Color
	 */
	public void DrawDebugInfo(int x,int y, BitmapFont font, Color color)
	{
		//draw hitboxes
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.size();i++)
		{
			int x1=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).x1;
			int x2=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).x2;
			int y1=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).y1;
			int y2=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).y2;
			
			DrawEngine.INSTANCE.DrawRectangle(this.x_relative+x1, this.y_relative+y1, this.x_relative+x2, this.y_relative+y2,2,Color.RED);
		}
		
		//draw synchro point
		int x3=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x;
		int y3=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y;
		DrawEngine.INSTANCE.DrawPoint(this.x_relative+x3, this.y_relative+y3,5, Color.YELLOW);
		
		//draw Data about hero
		DrawEngine.INSTANCE.DrawText(x,y,color,font,
				"PID="+pid+
				" HeroID="+ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).id+
				" WeaponID="+ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).id+
				" AnimationID="+idAnimation+
				" Relative(x,y)=("+this.x_relative+","+this.y_relative+")"+
				" Absolute(x,y)=("+this.x_absolute+","+this.y_absolute+")"
				);
	}
	

}
