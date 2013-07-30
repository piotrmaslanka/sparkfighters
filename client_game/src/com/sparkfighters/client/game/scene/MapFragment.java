package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.GameEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;
/**
 * Class to manage a fragment of map to draw.
 * @author Kamil Iwiñski
 *
 */
public class MapFragment 
{
	private int x,y;
	private int x_center,y_center;
	/**
	 * Function return X.
	 * Left bottom corner!!
	 * @return x
	 */
	public int getX()
	{
		return x;
	}
	/**
	 * Function return Y.
	 * Left bottom corner!!
	 * @return y
	 */
	public int getY()
	{
		return y;
	}
	/**
	 * Set map coordinates to show Actor
	 * @param x_actor
	 * @param y_actor
	 */	
	public void set(int x_actor,int y_actor)
	{
		this.x_center=x_actor;
		this.y_center=y_actor;
		
		int width=GameEngine.INSTANCE.orginal_width;
		int height=GameEngine.INSTANCE.orginal_height;
		
		this.x=this.x_center-(width/2);
		if(this.x<0) this.x=0;
		if(this.x>ResourcesManager.INSTANCE.map.mapSize.x2-width) this.x=(int) (ResourcesManager.INSTANCE.map.mapSize.x2-width);
		
		this.y=this.y_center-(height/2);
		if(this.y<0) this.y=0;
		if(this.y>ResourcesManager.INSTANCE.map.mapSize.y2-height) this.y=(int) (ResourcesManager.INSTANCE.map.mapSize.y2-height);
		
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
		tr.setRegion(x, height_map-y-height, width, height);
		DrawEngine.INSTANCE.Draw(tr, 0, 0,0);
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
			int x1=(int) ResourcesManager.INSTANCE.map.platforms.get(i).x1-this.x;
			int x2=(int) ResourcesManager.INSTANCE.map.platforms.get(i).x2-this.x;
			int y1=(int) ResourcesManager.INSTANCE.map.platforms.get(i).y-this.y;
			int y2=(int) ResourcesManager.INSTANCE.map.platforms.get(i).y-this.y;
			
			DrawEngine.INSTANCE.DrawLine(x1, y1, x2, y2, 1, Color.ORANGE);
		}
	}
}
