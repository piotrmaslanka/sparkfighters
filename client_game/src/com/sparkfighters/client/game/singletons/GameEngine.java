package com.sparkfighters.client.game.singletons;

import java.util.ArrayList;

import com.sparkfighters.client.game.enums.Debug;
import com.sparkfighters.client.game.scene.Actor;

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
	
	public long iteration;
	
	public void Init(int window_width,int window_height)
	{
		this.window_width=window_width;
		this.window_height=window_height;	
		
		DrawEngine.INSTANCE.Init();
		ResourcesManager.INSTANCE.LoadResources();	
		
		iteration=0;
		
		actors=new ArrayList<Actor>();
		
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
	}
	
	public void ProcessData()
	{
		Input.INSTANCE.processInput();
		iteration++;
	}
	
	public void Draw()
	{	
		DrawEngine.INSTANCE.ClearScreen();		
		
		for(int i=0;i<actors.size();i++)
		{
			actors.get(i).Draw();
		}
		
		if(debug==Debug.ALLMETHODS || debug==Debug.ONSCREEN) 
			DrawEngine.INSTANCE.DrawDebugInfo();
	}
}
