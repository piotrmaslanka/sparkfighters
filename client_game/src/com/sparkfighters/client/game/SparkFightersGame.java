package com.sparkfighters.client.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sparkfighters.client.game.singletons.GameEngine;
import com.sparkfighters.client.game.singletons.ResourcesManager;
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
	}
	
	@Override
	public void render() 
	{
		if(ResourcesManager.INSTANCE.finished()==false)
		{
			ResourcesManager.INSTANCE.LoadResources();		
			
			ResourcesManager.INSTANCE.DrawLoadingScreen(
				Integer.toString(ResourcesManager.INSTANCE.getProgress())+" "+
				ResourcesManager.INSTANCE.getProgressText());
		}
		else
		{
			GameEngine.INSTANCE.ProcessData();
			GameEngine.INSTANCE.Draw();
		}
			

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
