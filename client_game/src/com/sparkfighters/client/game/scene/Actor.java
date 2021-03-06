package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngineGUI;
import com.sparkfighters.client.game.singletons.DrawEngineScene;
import com.sparkfighters.client.game.singletons.ResourcesManager;
import com.sparkfighters.client.game.singletons.WorldManager;
import com.sparkfighters.shared.physics.objects.Vector;
/**
 * Class to manage Actor on scene
 * @author Kamil Iwi�ski
 *
 */
public class Actor 
{
	private Vector actorPositionAbsolute=new Vector();
	private Vector actorPositionRelative=new Vector();
	private Vector mousePositionAbsolute=new Vector();
	private Vector mousePositionRelative=new Vector();
	
	private int idHero,idWeapon, id, idHeroType;
	private int idAnimation;
	private float time;
	private float degree;
	
	public void setDegree(float degree)
	{
		this.degree=degree;
	}
	
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
	public Actor(int id, int idHero,int idHeroType, int idWeapon)
	{
		this.id=id;
		this.idHero=idHero;
		this.idHeroType=idHeroType;
		this.idWeapon=idWeapon;
			
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
		
		this.mousePositionRelative.x=this.mousePositionAbsolute.x-WorldManager.INSTANCE.mapFragment.getX();
		this.mousePositionRelative.y=this.mousePositionAbsolute.y-WorldManager.INSTANCE.mapFragment.getY();
	}
	/**
	 * Function draw Actor: body, weapon
	 */
	public void Draw()
	{
		//calculate body
		int x_body=(int)(this.actorPositionAbsolute.x-ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).synchroPoint.x);
		int y_body=(int)(this.actorPositionAbsolute.y-ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).synchroPoint.y);
		int direction=idAnimation%2;
		
		time += Gdx.graphics.getDeltaTime(); 
		TextureRegion currentFrameBody=ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableBody.get(idHeroType).get(idAnimation).getKeyFrame(time, true);
		int idFrame=ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableBody.get(idHeroType).get(idAnimation).getKeyFrameIndex(time);

		//calculate head
		int x_head=(int)(x_body+ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).headPositions.get(idFrame).x-ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction].getRegionWidth()/2);
		int y_head=(int)(y_body+ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).headPositions.get(idFrame).y-ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction].getRegionHeight()/2);

		//calculate weapon
		int h_x=0;
		int diff=0;
		if(currentFrameBody.getRegionWidth()>ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction].getRegionWidth())
		{
			h_x=currentFrameBody.getRegionWidth()/2;
		}
		else
		{
			diff=(currentFrameBody.getRegionWidth()-ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction].getRegionWidth())/2;
			h_x=ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction].getRegionWidth()/2;	
		}
		int h_y=(currentFrameBody.getRegionHeight()+ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction].getRegionHeight())/3;

		int x_weapon=x_body+h_x-ResourcesManager.INSTANCE.weaponsData.get(idWeapon).animations[direction].getRegionWidth()/2+diff;
		int y_weapon=y_body+h_y-ResourcesManager.INSTANCE.weaponsData.get(idWeapon).animations[direction].getRegionHeight()/2;
		
		float degrees=0;

		if(direction==0) degrees=-degree;
		if(direction==1) degrees=(-degree)+180;
		
		//draw body, head, weapon
		DrawEngineScene.INSTANCE.Draw(currentFrameBody, x_body,y_body,0);				
		DrawEngineScene.INSTANCE.Draw(ResourcesManager.INSTANCE.heroesData.get(idHero).animationsDrawableHead.get(idHeroType)[direction], x_head, y_head,degrees);
		DrawEngineScene.INSTANCE.Draw(ResourcesManager.INSTANCE.weaponsData.get(idWeapon).animations[direction], x_weapon,y_weapon,degrees);
		
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
		int x_absolute=(int)(this.actorPositionAbsolute.x-ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).synchroPoint.x);
		int y_absolute=(int)(this.actorPositionAbsolute.y-ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).synchroPoint.y);
		
		//draw hitboxes
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).hitboxes.size();i++)
		{
			int x1=(int)ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).hitboxes.get(i).x1;
			int x2=(int)ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).hitboxes.get(i).x2;
			int y1=(int)ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).hitboxes.get(i).y1;
			int y2=(int)ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).hitboxes.get(i).y2;
			
			DrawEngineScene.INSTANCE.DrawRectangle(x_absolute+x1, y_absolute+y1, x_absolute+x2, y_absolute+y2,2,Color.RED);
		}
		
		//draw synchro point
		int x3=(int)ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).synchroPoint.x;
		int y3=(int)ResourcesManager.INSTANCE.heroesData.get(idHero).Animations.get(idAnimation).synchroPoint.y;
		DrawEngineScene.INSTANCE.DrawPoint(x_absolute+x3, y_absolute+y3,5, Color.YELLOW);
		
		//draw Data about hero
		DrawEngineGUI.INSTANCE.DrawText(x,y,color,font,
				"PID="+id+
				" HeroID="+ResourcesManager.INSTANCE.heroesData.get(idHero).id+
				" WeaponID="+ResourcesManager.INSTANCE.weaponsData.get(idWeapon).id+
				" AnimationID="+idAnimation+
				" Relative(x,y)=("+this.actorPositionRelative.x+","+this.actorPositionRelative.y+")"+
				" Absolute(x,y)=("+this.actorPositionAbsolute.x+","+this.actorPositionAbsolute.y+")"+
				" Angle="+this.degree
				);
	}
	

}
