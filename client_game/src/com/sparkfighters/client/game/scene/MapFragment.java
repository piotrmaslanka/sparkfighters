package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.GameEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;
import com.sparkfighters.shared.physics.objects.Vector;
/**
 * Class to manage a fragment of map to draw.
 * @author Kamil Iwiñski
 *
 */
public class MapFragment 
{
	private Vector mapPosition=new Vector();
	private Vector actorPosition=new Vector();
	/**
	 * Function return X.
	 * Left bottom corner!!
	 * @return x
	 */
	public int getX()
	{
		return (int)mapPosition.x;
	}
	/**
	 * Function return Y.
	 * Left bottom corner!!
	 * @return y
	 */
	public int getY()
	{
		return (int)mapPosition.y;
	}
	/**
	 * Set map coordinates to show Actor
	 * @param x_actor
	 * @param y_actor
	 */	
	public void set(int x_actor,int y_actor)
	{
		this.actorPosition.x=x_actor;
		this.actorPosition.y=y_actor;
		
		int width=GameEngine.INSTANCE.orginal_width;
		int height=GameEngine.INSTANCE.orginal_height;
		
		this.mapPosition.x=this.actorPosition.x-(width/2);
		if(this.mapPosition.x<0) this.mapPosition.x=0;
		if(this.mapPosition.x>ResourcesManager.INSTANCE.map.mapSize.x2-width) this.mapPosition.x=(int)(ResourcesManager.INSTANCE.map.mapSize.x2-width);
		
		this.mapPosition.y=this.actorPosition.y-(height/2);
		if(this.mapPosition.y<0) this.mapPosition.y=0;
		if(this.mapPosition.y>ResourcesManager.INSTANCE.map.mapSize.y2-height) this.mapPosition.y=(int) (ResourcesManager.INSTANCE.map.mapSize.y2-height);
		
	}
	
	/**
	 * Function draw fragment of map
	 */
	public void Draw()
	{
		int width=GameEngine.INSTANCE.orginal_width;
		int height=GameEngine.INSTANCE.orginal_height;
		int height_map=(int)ResourcesManager.INSTANCE.map.mapSize.y2;
		
		TextureRegion tr=new TextureRegion(ResourcesManager.INSTANCE.map.texture);
		tr.setRegion((int)this.mapPosition.x, height_map-(int)this.mapPosition.y-height, width, height);
		DrawEngine.INSTANCE.Draw(tr, 0, 0, 0);
	}
	/**
	 * Function draw debug info on screen
	 * @param x
	 * @param y
	 * @param font
	 * @param color
	 */
	public void DrawDebugInfo(int x, int y, BitmapFont font, Color color) 
	{
		DrawEngine.INSTANCE.DrawText(x,y,color,font,"Map-fragment(x,y)=("+getX()+","+getY()+")");
		
		//debug platforms
		for(int i=0;i<ResourcesManager.INSTANCE.map.platforms.size();i++)
		{
			int x1=(int) (ResourcesManager.INSTANCE.map.platforms.get(i).x1-this.mapPosition.x);
			int x2=(int) (ResourcesManager.INSTANCE.map.platforms.get(i).x2-this.mapPosition.x);
			int y1=(int) (ResourcesManager.INSTANCE.map.platforms.get(i).y-this.mapPosition.y);
			int y2=(int) (ResourcesManager.INSTANCE.map.platforms.get(i).y-this.mapPosition.y);
			
			DrawEngine.INSTANCE.DrawLine(x1, y1, x2, y2, 3, Color.ORANGE);
		}
	}
}
