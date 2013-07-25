package com.sparkfighters.client.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.sparkfighters.client.game.singletons.GameEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;

public class GameListener implements ApplicationListener 
{
	@Override
	public void create() 
	{
		ResourcesManager.INSTANCE.LoadResources();
		GameEngine.INSTANCE.InitEngine(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() 
	{
		
	}

	@Override
	public void render() 
	{		
		GameEngine.INSTANCE.ProcessData();
		GameEngine.INSTANCE.Draw();
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
