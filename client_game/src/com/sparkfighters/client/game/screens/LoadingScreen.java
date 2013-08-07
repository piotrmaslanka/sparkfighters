package com.sparkfighters.client.game.screens;

import com.badlogic.gdx.Screen;
import com.sparkfighters.client.game.SparkFightersGame;
import com.sparkfighters.client.game.singletons.ResourcesManager;
import com.sparkfighters.client.game.singletons.WorldManager;

public class LoadingScreen implements Screen
{
	private SparkFightersGame game;
	
	public LoadingScreen(SparkFightersGame game)
	{
		this.game=game;
	}
	
	@Override
	public void render(float delta) 
	{
		if(ResourcesManager.INSTANCE.finished()==false)
		{
			ResourcesManager.INSTANCE.LoadResources();
		}
		else
		{
			WorldManager.INSTANCE.Init();	
			game.setScreen(new GameScreen(game));
		}
			
		ResourcesManager.INSTANCE.DrawLoadingScreen(
				Integer.toString(ResourcesManager.INSTANCE.getProgress())+" "+
				ResourcesManager.INSTANCE.getProgressText());
	}

	@Override
	public void show() 
	{
		
	}
	
	@Override
	public void resize(int width, int height)
	{
		
	}

	@Override
	public void hide()
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
	
	@Override
	public void dispose() 
	{
		
	}
}
