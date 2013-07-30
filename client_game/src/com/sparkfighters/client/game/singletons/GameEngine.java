package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;

import com.sparkfighters.client.game.enums.Debug;
import com.sparkfighters.client.game.scene.Actor;
import com.sparkfighters.client.game.scene.MapFragment;
import com.sparkfighters.client.game.ultis.MapImageToJson;
/**
 * Singleton to hold all needed information about game and scene.
 * @author Kamil Iwi�ski
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
	
	public ArrayList<Actor> actors;
	public int myHeroArrayActors;
	
	public MapFragment mapFragment;
	
	public long iteration;
	
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
		
		iteration=0;
		
		actors=new ArrayList<Actor>();
		
		mapFragment=new MapFragment();
		
		Actor a=new Actor(0,0, 0, 100, 0);
		actors.add(a);
		myHeroArrayActors=0;
		
		a=new Actor(1,1, 1, 100, 150);
		a.setAnimation(1);
		actors.add(a);
		
		a=new Actor(2,2, 2, 100, 300);
		actors.add(a);
		a=new Actor(3,2, 3, 100, 450);
		actors.add(a);
		
		a=new Actor(4,2, 4, 100, 600);
		actors.add(a);	
		a=new Actor(5,2, 0, 100, 750);
		actors.add(a);	
		
		//MapImageToJson c=new MapImageToJson();
		//c.convert("p.png", "out.json");
	}
	/**
	 * Function which hold all process data 
	 */
	public void ProcessData()
	{
		Input.INSTANCE.processInput();
		mapFragment.set(GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).getX_absolute(), GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).getY_absolute());
		iteration++;
	}
	/**
	 * Function draw proccesed data on screen
	 */
	public void Draw()
	{	
		DrawEngine.INSTANCE.ClearScreen();		
		
		mapFragment.Draw();
		
		for(int i=0;i<actors.size();i++)
		{
			actors.get(i).setX_absolute(actors.get(i).getX_absolute());
			actors.get(i).setY_absolute(actors.get(i).getY_absolute());
			
			actors.get(i).Draw();
		}
		
		if(debug==Debug.ALLMETHODS || debug==Debug.ONSCREEN) 
			DrawEngine.INSTANCE.DrawDebugInfo();
		
		//run garbage collector FPS drop form 60 to 30
		//when using on each frame framerate: 60 FPS
		//Runtime r = Runtime.getRuntime();
		//r.gc();

	}
}