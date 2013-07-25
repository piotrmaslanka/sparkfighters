package com.sparkfighters.client.game.singletons;

import com.sparkfighters.client.game.enums.Debug;
import com.sparkfighters.client.game.tests.DrawTest;

public enum GameEngine 
{
	INSTANCE;
	
	public final int orginal_width=1920;
	public final int orginal_height=1080;
	public int window_width;
	public int window_height;
	public Debug debug=Debug.ALLMETHODS;	
	
	DrawTest dt=new DrawTest();
	
	public void Init(int window_width,int window_height)
	{
		this.window_width=window_width;
		this.window_height=window_height;	
		
		DrawEngine.INSTANCE.Init();
		ResourcesManager.INSTANCE.LoadResources();		
	}
	
	public void ProcessData()
	{
		
	}
	
	public void Draw()
	{	
		DrawEngine.INSTANCE.ClearScreen();		
		dt.runTest();	
	}
}
