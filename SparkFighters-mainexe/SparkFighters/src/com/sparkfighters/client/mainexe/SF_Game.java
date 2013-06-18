package com.sparkfighters.client.mainexe;

import com.badlogic.gdx.ApplicationListener;

public class SF_Game implements ApplicationListener 
{

	
	@Override
	public void create() 
	{
		Engine.INSTANCE.InitEngine();
		ResourcesManager.INSTANCE.LoadResources();
	}

	@Override
	public void dispose() 
	{
		
	}

	@Override
	public void render() 
	{	
		Engine.INSTANCE.ProcessData();
		Engine.INSTANCE.Draw();

	}

	@Override
	public void resize(int width, int height)
	{
		
	}

	@Override
	public void pause() 
	{
		
	}

	@Override
	public void resume() 
	{
		
	}
}
