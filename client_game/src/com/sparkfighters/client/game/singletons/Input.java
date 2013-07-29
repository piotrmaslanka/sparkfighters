package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public enum Input 
{
	INSTANCE;
	
	private int x;
	private int y;
	
	public int getX() {return x;}

	public int getY() {return y;}

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
		float zoom=(float)GameEngine.INSTANCE.orginal_width/(float)GameEngine.INSTANCE.window_width;
		x=(int)(Gdx.input.getX()*zoom);
		y=GameEngine.INSTANCE.orginal_height-(int)(Gdx.input.getY()*zoom);
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) 
		{
	       
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) 
		{
	       
		}
			
	}
	
	public void DrawDebugInfo(int x, int y, BitmapFont font, Color color)
	{
		DrawEngine.INSTANCE.DrawText(x,y,color,font,"Mouse-relative(x,y)=("+getX()+","+getY()+")");
	}
	

}
