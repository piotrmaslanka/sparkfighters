package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;
import com.sparkfighters.client.game.singletons.WorldManager;
/**
 * Class to manage Actor on scene
 * @author Kamil Iwiñski
 *
 */
public class Actor 
{
	private int x_absolute,y_absolute;
	private int x_relative,y_relative;
	private int x_mouse_absolute,y_mouse_absolute;
	private int x_mouse_relative,y_mouse_relative;
	
	private int idHeroArrayResource,idWeaponArrayResource, id;
	private int idAnimation;
	private float time;
	
	public int getId(){return id;}
	/**
	 * Function set X absolute on map
	 * Calculate X relative
	 * @param x
	 */
	public void setX_absolute(int x)
	{
		this.x_absolute=x;
		this.x_relative=this.x_absolute-WorldManager.INSTANCE.mapFragment.getX();
	}
	
	/**
	 * Function set Y absolute on map
	 * Calculate Y relative
	 * @param y
	 */
	public void setY_absolute(int y)
	{
		this.y_absolute=y;
		this.y_relative=this.y_absolute-WorldManager.INSTANCE.mapFragment.getY();
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
	 * @param id to identify Actor across all map
	 * @param idHero Actor need body to exist
	 * @param idWeapon Actor need a weapon to exist
	 */
	public Actor(int id, int idHero, int idWeapon)
	{
		this.id=id;
		
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
		if(idAnimation!=id)
		{
			time=0f;
			idAnimation=id;
		}
	}
	/**
	 * Rotate weapon to target mouse x,y
	 * @param x_mouse_aboslute
	 * @param y_mouse_absolute
	 */
	public void setWeaponRotate(int x_mouse_aboslute, int y_mouse_absolute)
	{
		this.x_mouse_absolute=x_mouse_aboslute;
		this.y_mouse_absolute=y_mouse_absolute;
		
		this.x_mouse_relative=this.x_mouse_absolute-WorldManager.INSTANCE.mapFragment.getX();
		this.y_mouse_relative=this.y_mouse_absolute-WorldManager.INSTANCE.mapFragment.getY();
	}
	/**
	 * Function draw Actor: body, weapon
	 */
	public void Draw()
	{
		//draw hero
		int x_relative=this.x_relative-(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x;
		int y_relative=this.y_relative-(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y;
		
		time += Gdx.graphics.getDeltaTime(); 
		TextureRegion currentFrame=ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).animationsDrawable.get(idAnimation).getKeyFrame(time, true);
		DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative,0);
		
		//draw weapon
		int h_x=currentFrame.getRegionWidth()/2;
		int h_y=currentFrame.getRegionHeight()/2;
		
		int x1=x_relative+h_x;
		int y1=y_relative+h_y;
		
		float degrees=0.0f;
		if(idAnimation%2==0)
		{
			int w_x=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region.getRegionWidth()/2;
			int w_y=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region.getRegionHeight()/2;
			
			x1=x1-w_x;
			y1=y1-w_y;
			
			double C=Math.sqrt((x_mouse_absolute-x_absolute)*(x_mouse_absolute-x_absolute)+(y_mouse_absolute-y_absolute)*(y_mouse_absolute-y_absolute));
			double A=Math.sqrt((x_mouse_absolute-x_mouse_absolute)*(x_mouse_absolute-x_mouse_absolute)+(y_absolute-y_mouse_absolute)*(y_absolute-y_mouse_absolute));
			
			double radians=Math.tan(A/C);
			degrees=(float)(radians*180/Math.PI);
			if(y_absolute-y_mouse_absolute>0) degrees=-degrees;
			
	
			DrawEngine.INSTANCE.Draw(ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region, x1,y1,degrees);	
		}

		if(idAnimation%2==1)
		{
			int w_x=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region.getRegionWidth()/2;
			int w_y=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region.getRegionHeight()/2;
			
			x1=x1-w_x;
			y1=y1-w_y;
			
			double C=Math.sqrt((x_mouse_absolute-x_absolute)*(x_mouse_absolute-x_absolute)+(y_mouse_absolute-y_absolute)*(y_mouse_absolute-y_absolute));
			double A=Math.sqrt((x_mouse_absolute-x_mouse_absolute)*(x_mouse_absolute-x_mouse_absolute)+(y_absolute-y_mouse_absolute)*(y_absolute-y_mouse_absolute));
			
			double radians=Math.tan(A/C);
			degrees=(float)(radians*180/Math.PI);
			if(y_absolute-y_mouse_absolute<0) degrees=-degrees;
			
			DrawEngine.INSTANCE.Draw(ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region, x1,y1,degrees);	
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
		int x_relative=this.x_relative-(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x;
		int y_relative=this.y_relative-(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y;
		
		//draw hitboxes
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.size();i++)
		{
			int x1=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).x1;
			int x2=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).x2;
			int y1=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).y1;
			int y2=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).y2;
			
			DrawEngine.INSTANCE.DrawRectangle(x_relative+x1, y_relative+y1, x_relative+x2, y_relative+y2,2,Color.RED);
		}
		
		//draw synchro point
		int x3=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x;
		int y3=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y;
		DrawEngine.INSTANCE.DrawPoint(x_relative+x3, y_relative+y3,5, Color.YELLOW);
		
		//draw Data about hero
		DrawEngine.INSTANCE.DrawText(x,y,color,font,
				"PID="+id+
				" HeroID="+ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).id+
				" WeaponID="+ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).id+
				" AnimationID="+idAnimation+
				" Relative(x,y)=("+this.x_relative+","+this.y_relative+")"+
				" Absolute(x,y)=("+this.x_absolute+","+this.y_absolute+")"
				);
	}
	

}
