package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;

public class Actor 
{
	private int x,y;
	private int idHeroArrayResource,idWeaponArrayResource, pid;
	private int idAnimation;
	private float time;
	
	public Actor(int pid, int idHero, int idWeapon, int x, int y)
	{
		this.pid=pid;
		this.x=x;
		this.y=y;
		
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
	
	public void setAnimation(int id)
	{
		time=0f;
		idAnimation=id;
	}
	

	public void Draw()
	{
		//draw hero
		time += Gdx.graphics.getDeltaTime(); 
		TextureRegion currentFrame=ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).animationsDrawable.get(idAnimation).getKeyFrame(time, true);
		DrawEngine.INSTANCE.Draw(currentFrame, x,y);
		
		//draw weapon
		int h_x=currentFrame.getRegionWidth()/2;
		int h_y=currentFrame.getRegionHeight()/2;
		
		int x1=this.x+h_x;
		int y1=this.y+h_y;
		
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
	
	public void DrawDebugInfo(int x3,int y3, BitmapFont font, Color color)
	{
		//draw hitboxes
		for(int i=0;i<ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.size();i++)
		{
			int x1=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).x1;
			int x2=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).x2;
			int y1=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).y1;
			int y2=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).hitboxes.get(i).y2;
			
			DrawEngine.INSTANCE.DrawRectangle(this.x+x1, this.y+y1, this.x+x2, this.y+y2,2,Color.RED);
		}
		
		//draw synchro point
		int x=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.x;
		int y=(int)ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).Animations.get(idAnimation).synchroPoint.y;
		DrawEngine.INSTANCE.DrawPoint(this.x+x, this.y+y,5, Color.YELLOW);
		
		//draw Data about hero
		DrawEngine.INSTANCE.DrawText(x3,y3,color,font,
				"PID="+pid+
				" HeroID="+ResourcesManager.INSTANCE.heroesData.get(idHeroArrayResource).id+
				" WeaponID="+ResourcesManager.INSTANCE.weaponsData.get(idWeaponArrayResource).id+
				" AnimationID="+idAnimation+
				" Relative(x,y)=("+this.x+","+this.y+")"
				);
	}
	

}
