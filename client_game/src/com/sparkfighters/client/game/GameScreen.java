package com.sparkfighters.client.game;

import com.badlogic.gdx.Screen;
import com.sparkfighters.client.game.singletons.GameEngine;

public class GameScreen implements Screen
{

	private SparkFightersGame game;
	
	public GameScreen(SparkFightersGame game)
	{
		this.game=game;
	}
	
	@Override
	public void render(float delta) 
	{
		GameEngine.INSTANCE.ProcessData();
		GameEngine.INSTANCE.Draw();
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
