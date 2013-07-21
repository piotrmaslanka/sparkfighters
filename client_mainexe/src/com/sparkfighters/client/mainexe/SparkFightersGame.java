package com.sparkfighters.client.mainexe;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.sparkfighters.client.ResourcesManager.ResourcesManager;

public class SparkFightersGame implements ApplicationListener 
{
	@Override
	public void create() 
	{
		ResourcesManager.INSTANCE.LoadResources();
		Engine.INSTANCE.InitEngine(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
