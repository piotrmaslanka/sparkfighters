package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public enum Input 
{
	INSTANCE;
	
	private int x_relative,y_relative;
	private int x_absolute,y_absolute;
	
	public int getX() {return x_absolute;}

	public int getY() {return y_absolute;}

	public void processInput()
	{
		processMouse();
		processKeyboard();
	}
	
	private void processKeyboard() 
	{
		 if(Gdx.input.isKeyPressed(Keys.W))
		 {
			 GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).setY_absolute( GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).getY_absolute()+10);
		 }
		
		 if(Gdx.input.isKeyPressed(Keys.S))
		 {
			 GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).setY_absolute( GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).getY_absolute()-10);
		 }
		 
		 if(Gdx.input.isKeyPressed(Keys.A)) 
		 {
		    GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).setX_absolute( GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).getX_absolute()-10);
		 }
		 
		 if(Gdx.input.isKeyPressed(Keys.D)) 
		 {
		    GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).setX_absolute( GameEngine.INSTANCE.actors.get(GameEngine.INSTANCE.myHeroArrayActors).getX_absolute()+10);
		 }
		
	}

	private void processMouse()
	{
		float zoom=(float)GameEngine.INSTANCE.orginal_width/(float)GameEngine.INSTANCE.window_width;
		x_relative=(int)(Gdx.input.getX()*zoom);
		y_relative=GameEngine.INSTANCE.orginal_height-(int)(Gdx.input.getY()*zoom);
		
		x_absolute=x_relative+GameEngine.INSTANCE.mapFragment.getX();
		y_absolute=y_relative+GameEngine.INSTANCE.mapFragment.getY();
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) 
		{
	       
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) 
		{
	       
		}
			
	}
	
	public void DrawDebugInfo(int x, int y, BitmapFont font, Color color)
	{
		DrawEngine.INSTANCE.DrawText(x,y,color,font,
				"Mouse-relative(x,y)=("+x_relative+","+y_relative+")"+
				" Mouse-absolute(x,y)=("+x_absolute+","+y_absolute+")"		
				);
	}
	

}
