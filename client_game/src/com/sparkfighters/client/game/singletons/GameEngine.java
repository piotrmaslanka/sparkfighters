package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sparkfighters.client.game.enums.Debug;
/**
 * Singleton to hold information about game.
 * @author Kamil Iwiñski
 *
 */
public enum GameEngine 
{
	INSTANCE;
	
	public final int orginal_width=1920;
	public final int orginal_height=1080;
	public int window_width;
	public int window_height;
	
	public Debug debug=Debug.ALLMETHODS;	
		
	/**
	 * Function to configure GameEngine parameters
	 * @param window_width
	 * @param window_height
	 */
	public void Init(int window_width,int window_height)
	{	
		this.window_width=window_width;
		this.window_height=window_height;	
		
		DrawEngineScene.INSTANCE.Init();
		DrawEngineGUI.INSTANCE.Init();
	}
	
	/**
	 * Function which hold all process data 
	 */
	public void ProcessData()
	{				
		Input.INSTANCE.processInput();
		
		WorldManager.INSTANCE.worldLogic.advance(10.0*Gdx.graphics.getDeltaTime());
		
		WorldManager.INSTANCE.mapFragment.set(WorldManager.INSTANCE.actors.get(WorldManager.INSTANCE.myHeroArrayActors).getX_absolute(), WorldManager.INSTANCE.actors.get(WorldManager.INSTANCE.myHeroArrayActors).getY_absolute());
		
		for(int i=0;i<WorldManager.INSTANCE.actors.size();i++)
		{
			int id=WorldManager.INSTANCE.actors.get(i).getId();
			
			int x=(int)WorldManager.INSTANCE.worldLogic.get_actor(id).physical.get_position().x;
			int y=(int)WorldManager.INSTANCE.worldLogic.get_actor(id).physical.get_position().y;
			WorldManager.INSTANCE.actors.get(i).setX_absolute(x);
			WorldManager.INSTANCE.actors.get(i).setY_absolute(y);
			
			int x2=(int)WorldManager.INSTANCE.worldLogic.get_actor(id).controller().get_mouse_position().x;
			int y2=(int)WorldManager.INSTANCE.worldLogic.get_actor(id).controller().get_mouse_position().y;
			WorldManager.INSTANCE.actors.get(i).setMouseTarget(x2, y2);
			
			WorldManager.INSTANCE.actors.get(i).setAnimation(WorldManager.INSTANCE.worldLogic.get_actor(id).physical.get_geom_id());		
		}
	}
	/**
	 * Function draw proccesed data on screen
	 */
	public void Draw()
	{	
		DrawEngineScene.INSTANCE.ClearScreen();	
		
		
		WorldManager.INSTANCE.mapFragment.Draw();
		
		for(int i=0;i<WorldManager.INSTANCE.actors.size();i++)
		{
			WorldManager.INSTANCE.actors.get(i).Draw();
		}
		
		if(debug==Debug.ALLMETHODS || debug==Debug.ONSCREEN) 
			DrawDebugInfo();
		
		//run garbage collector FPS drop form 60 to 30
		//when using on each frame framerate: 60 FPS
		//Runtime r = Runtime.getRuntime();
		//r.gc();

	}
	
	/**
	 * Function draw debug info on screen from all classes
	 */
	public void DrawDebugInfo()
	{	
		//int w=GameEngine.INSTANCE.orginal_width;
		int h=GameEngine.INSTANCE.orginal_height;
		BitmapFont font=ResourcesManager.INSTANCE.debugFont;
		Color color=Color.GREEN;
		int space_h=40;
		
		//FPS COUNTER
		DrawEngineGUI.INSTANCE.DrawText(0,h,color,font,"FPS: "+Gdx.graphics.getFramesPerSecond());
		h-=space_h;
		//Input
		Input.INSTANCE.DrawDebugInfo(0, h, font, color);
		h-=space_h;
		//debug about map
		WorldManager.INSTANCE.mapFragment.DrawDebugInfo(0,h,font,color);
		h-=space_h;
		//debug about heroes
		for(int i=0;i<WorldManager.INSTANCE.actors.size();i++)
		{
			WorldManager.INSTANCE.actors.get(i).DrawDebugInfo(0,h,font,color);
			h-=space_h;
		}
			

	}
}
