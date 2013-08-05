package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;
import com.sparkfighters.client.game.singletons.WorldManager;
import com.sparkfighters.shared.physics.objects.Vector;
/**
 * Class to manage Actor on scene
 * @author Kamil Iwiñski
 *
 */
public class Actor 
{
	private Vector actorPositionAbsolute=new Vector();
	private Vector actorPositionRelative=new Vector();
	private Vector mousePositionAbsolute=new Vector();
	
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
		this.actorPositionAbsolute.x=x;
		this.actorPositionRelative.x=this.actorPositionAbsolute.x-WorldManager.INSTANCE.mapFragment.getX();
	}
	/**
	 * Function set Y absolute on map
	 * Calculate Y relative
	 * @param y
	 */
	public void setY_absolute(int y)
	{
		this.actorPositionAbsolute.y=y;
		this.actorPositionRelative.y=this.actorPositionAbsolute.y-WorldManager.INSTANCE.mapFragment.getY();
	}
	/**
	 * 
	 * @return X absolute
	 */
	public int getX_absolute()
	{
		return (int)this.actorPositionAbsolute.x;
	}
	/**
	 * 
	 * @return Y absolute
	 */
	public int getY_absolute()
	{
		return (int)this.actorPositionAbsolute.y;
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
	public void setMouseTarget(int x_mouse_aboslute, int y_mouse_absolute)
	{
		this.mousePositionAbsolute.x=x_mouse_aboslute;
		this.mousePositionAbsolute.y=y_mouse_absolute;
	}
	/**
	 * Function draw Actor: body, weapon
	 */
	public void Draw()
	{
		//draw hero
		int x_relative=(int)(this.actorPositionRelative.x-ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x);
		int y_relative=(int)(this.actorPositionRelative.y-ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y);
		
		time += Gdx.graphics.getDeltaTime(); 
		TextureRegion currentFrame=ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).animationsDrawable.get(idAnimation).getKeyFrame(time, true);
		//DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative,0);
		
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
			
			double C=Math.sqrt((mousePositionAbsolute.x-actorPositionAbsolute.x)*(mousePositionAbsolute.x-actorPositionAbsolute.x)+(mousePositionAbsolute.y-actorPositionAbsolute.y)*(mousePositionAbsolute.y-actorPositionAbsolute.y));
			double A=Math.sqrt((mousePositionAbsolute.x-mousePositionAbsolute.x)*(mousePositionAbsolute.x-mousePositionAbsolute.x)+(actorPositionAbsolute.y-mousePositionAbsolute.y)*(actorPositionAbsolute.y-mousePositionAbsolute.y));
			
			double radians=Math.tan(A/C);
			degrees=(float)(radians*180/Math.PI);
			if(actorPositionAbsolute.y-mousePositionAbsolute.y>0) degrees=-degrees;
			
	
			if(degrees>45.0f)
			{
				idAnimation=idAnimation+2;
				currentFrame=ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).animationsDrawable.get(idAnimation).getKeyFrame(time, true);
				DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative,0);
			}
			else
			{
				DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative,0);
			}
			DrawEngine.INSTANCE.Draw(ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).right_region, x1,y1,degrees);	
		}

		if(idAnimation%2==1)
		{
			int w_x=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region.getRegionWidth()/2;
			int w_y=ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).left_region.getRegionHeight()/2;
			
			x1=x1-w_x;
			y1=y1-w_y;
			
			double C=Math.sqrt((mousePositionAbsolute.x-actorPositionAbsolute.x)*(mousePositionAbsolute.x-actorPositionAbsolute.x)+(mousePositionAbsolute.y-actorPositionAbsolute.y)*(mousePositionAbsolute.y-actorPositionAbsolute.y));
			double A=Math.sqrt((mousePositionAbsolute.x-mousePositionAbsolute.x)*(mousePositionAbsolute.x-mousePositionAbsolute.x)+(actorPositionAbsolute.y-mousePositionAbsolute.y)*(actorPositionAbsolute.y-mousePositionAbsolute.y));
			
			double radians=Math.tan(A/C);
			degrees=(float)(radians*180/Math.PI);
			if(degrees>45.0f)
			{
				idAnimation=idAnimation+2;
				currentFrame=ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).animationsDrawable.get(idAnimation).getKeyFrame(time, true);
				DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative,0);
			}
			else
			{
				DrawEngine.INSTANCE.Draw(currentFrame, x_relative,y_relative,0);
			}
			if(actorPositionAbsolute.y-mousePositionAbsolute.y<0) degrees=-degrees;
			
			
			
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
		int x_relative=(int)(this.actorPositionRelative.x-ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x);
		int y_relative=(int)(this.actorPositionRelative.y-ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y);
		
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
				" Relative(x,y)=("+this.actorPositionRelative.x+","+this.actorPositionRelative.y+")"+
				" Absolute(x,y)=("+this.actorPositionAbsolute.x+","+this.actorPositionAbsolute.y+")"
				);
	}
	

}
