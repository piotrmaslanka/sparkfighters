package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
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
		
		DrawEngine.INSTANCE.Init();
		ResourcesManager.INSTANCE.LoadResources();	
		WorldManager.INSTANCE.Init();
	}
	
	/**
	 * Function which hold all process data 
	 */
	public void ProcessData()
	{
		Input.INSTANCE.processInput();
		
		WorldManager.INSTANCE.sharedWorld.worldLogic.advance(Gdx.graphics.getDeltaTime());
		
		WorldManager.INSTANCE.clientWorld.mapFragment.set(WorldManager.INSTANCE.clientWorld.actors.get(WorldManager.INSTANCE.clientWorld.myHeroArrayActors).getX_absolute(), WorldManager.INSTANCE.clientWorld.actors.get(WorldManager.INSTANCE.clientWorld.myHeroArrayActors).getY_absolute());
		
		
		for(int i=0;i<WorldManager.INSTANCE.clientWorld.actors.size();i++)
		{
			int id=WorldManager.INSTANCE.clientWorld.actors.get(i).getId();
			
			int x=(int)WorldManager.INSTANCE.sharedWorld.worldLogic.get_actor(id).physical.get_position().x;
			int y=(int)WorldManager.INSTANCE.sharedWorld.worldLogic.get_actor(id).physical.get_position().y;
			WorldManager.INSTANCE.clientWorld.actors.get(i).setX_absolute(x);
			WorldManager.INSTANCE.clientWorld.actors.get(i).setY_absolute(y);
			
			int x2=(int)WorldManager.INSTANCE.sharedWorld.worldLogic.get_actor(id).controller().get_mouse_position().x;
			int y2=(int)WorldManager.INSTANCE.sharedWorld.worldLogic.get_actor(id).controller().get_mouse_position().y;
			WorldManager.INSTANCE.clientWorld.actors.get(i).setWeaponRotate(x2, y2);
		}
	}
	/**
	 * Function draw proccesed data on screen
	 */
	public void Draw()
	{	
		DrawEngine.INSTANCE.ClearScreen();		
		
		WorldManager.INSTANCE.clientWorld.mapFragment.Draw();
		
		for(int i=0;i<WorldManager.INSTANCE.clientWorld.actors.size();i++)
		{
			WorldManager.INSTANCE.clientWorld.actors.get(i).Draw();
		}
		
		if(debug==Debug.ALLMETHODS || debug==Debug.ONSCREEN) 
			DrawEngine.INSTANCE.DrawDebugInfo();
		
		//run garbage collector FPS drop form 60 to 30
		//when using on each frame framerate: 60 FPS
		//Runtime r = Runtime.getRuntime();
		//r.gc();

	}
}
