package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sparkfighters.shared.physics.objects.Vector;
/**
 * Singleton to hold input data from mouse and keyboard
 * @author Kamil Iwiñski
 *
 */
public enum Input 
{
	INSTANCE;
	
	private int x_relative,y_relative;
	private int x_absolute,y_absolute;
	/**
	 * 
	 * @return x_absolute
	 */
	public int getX_absolute() {return x_absolute;}
	/**
	 * 
	 * @return y_absolute
	 */
	public int getY_absolute() {return y_absolute;}
	/**
	 * Function to process data from mouse and keyboard
	 */
	public void processInput()
	{
		processMouse();
		processKeyboard();
	}
	/**
	 * Function to process keyboard
	 */
	private void processKeyboard() 
	{
		
		 boolean up=false,down=false,right=false,left=false;
		 if(Gdx.input.isKeyPressed(Keys.W))
		 {
			up=true;
		 }
		
		 if(Gdx.input.isKeyPressed(Keys.S))
		 {
			down=true;
		 }
		 
		 if(Gdx.input.isKeyPressed(Keys.A)) 
		 {
		    left=true;
		 }
		 
		 if(Gdx.input.isKeyPressed(Keys.D)) 
		 {
		    right=true;
		 }
		 int id= WorldManager.INSTANCE.actors.get( WorldManager.INSTANCE.myHeroArrayActors).getId();
		 WorldManager.INSTANCE.worldLogic.actor_by_id.get(id).controller().set_keyboard_status(up, right, down, left);
		
	}
	/**
	 * Function to process mouse
	 */
	private void processMouse()
	{
		float zoom=(float)GameEngine.INSTANCE.orginal_width/(float)GameEngine.INSTANCE.window_width;
		x_relative=(int)(Gdx.input.getX()*zoom);
		y_relative=GameEngine.INSTANCE.orginal_height-(int)(Gdx.input.getY()*zoom);
		
		x_absolute=x_relative+WorldManager.INSTANCE.mapFragment.getX();
		y_absolute=y_relative+WorldManager.INSTANCE.mapFragment.getY();
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) 
		{
	       
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) 
		{
	       
		}
			
		 int id= WorldManager.INSTANCE.actors.get( WorldManager.INSTANCE.myHeroArrayActors).getId();
		 WorldManager.INSTANCE.worldLogic.actor_by_id.get(id).controller().set_mouse_position(new Vector(x_absolute,y_absolute));
		
	}
	/**
	 * Draw debug information on screen
	 * @param x
	 * @param y
	 * @param font
	 * @param color
	 */
	public void DrawDebugInfo(int x, int y, BitmapFont font, Color color)
	{
		DrawEngine.INSTANCE.DrawText(x,y,color,font,
				"Mouse-relative(x,y)=("+x_relative+","+y_relative+")"+
				" Mouse-absolute(x,y)=("+x_absolute+","+y_absolute+")"		
				);
	}
	

}
