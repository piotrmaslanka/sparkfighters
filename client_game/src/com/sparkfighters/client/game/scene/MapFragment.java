package com.sparkfighters.client.game.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sparkfighters.client.game.singletons.DrawEngine;
import com.sparkfighters.client.game.singletons.GameEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;

public class MapFragment 
{
	private int x,y;
	private int x_center,y_center;
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public MapFragment()
	{
		x=0;
		y=0;
	}
	
	public void set(int x_hero,int y_hero)
	{
		this.x_center=x_hero;
		this.y_center=y_hero;
		
		int width=GameEngine.INSTANCE.orginal_width;
		int height=GameEngine.INSTANCE.orginal_height;
		
		this.x=this.x_center-(width/2);
		if(this.x<0) this.x=0;
		if(this.x>ResourcesManager.INSTANCE.map.mapSize.x2-width) this.x=(int) (ResourcesManager.INSTANCE.map.mapSize.x2-width);
		
		this.y=this.y_center-(height/2);
		if(this.y<0) this.y=0;
		if(this.y>ResourcesManager.INSTANCE.map.mapSize.y2-height) this.y=(int) (ResourcesManager.INSTANCE.map.mapSize.y2-height);
		
	}
	
	public void Draw()
	{
		int width=GameEngine.INSTANCE.orginal_width;
		int height=GameEngine.INSTANCE.orginal_height;
		int height_map=(int)ResourcesManager.INSTANCE.map.mapSize.y2;
		
		TextureRegion tr=new TextureRegion(ResourcesManager.INSTANCE.map.texture);
		tr.setRegion(x, height_map-y-height, width, height);
		DrawEngine.INSTANCE.Draw(tr, 0, 0);
	}

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
