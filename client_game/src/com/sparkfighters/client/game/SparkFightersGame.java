package com.sparkfighters.client.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sparkfighters.client.game.screens.LoadingScreen;
import com.sparkfighters.client.game.singletons.GameEngine;
/**
 * ApplicationListener for game loop
 * @author Kamil Iwiñski
 *
 */
public class SparkFightersGame extends Game
{
	
	/**
	 * Function call when game is created 
	 */
	@Override
	public void create() 
	{
		GameEngine.INSTANCE.Init(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		setScreen(new LoadingScreen(this));
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
    
    @Override
    public void dispose() 
    {
    	
    }

}
