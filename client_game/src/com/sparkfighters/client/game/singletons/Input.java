package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;

public enum Input 
{
	INSTANCE;
	
	public void processInput()
	{
		processMouse();
		processKeyboard();
	}
	
	private void processKeyboard() 
	{
		
		
	}

	private void processMouse()
	{
		int x=Gdx.input.getX();
		int y=Gdx.input.getY();
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) 
		{
	       
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) 
		{
	       
		}
		
		
	}
}
