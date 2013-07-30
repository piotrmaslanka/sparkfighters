package com.sparkfighters.client.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.sparkfighters.client.game.singletons.GameEngine;
/**
 * ApplicationListener for game loop
 * @author Kamil Iwiñski
 *
 */
public class GameListener implements ApplicationListener 
{
	
	/**
	 * Function call when application is created 
	 */
	public void create() 
	{
		GameEngine.INSTANCE.Init(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	}

	/**
	 * Function call when application is closed
	 */
	public void dispose() 
	{
		
	}

	/**
	 * Main loop
	 */
	public void render() 
	{		
		GameEngine.INSTANCE.ProcessData();
		GameEngine.INSTANCE.Draw();
	}

	/**
	 * Function to resize window
	 */
	public void resize(int width, int height) 
	{
	}

	/**
	 * Function to pause
	 */
	public void pause() 
	{
		
	}

	/**
	 * Function to resume
	 */
	public void resume() 
	{
		
	}
}
