package com.sparkfighters.client.game.singletons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
	private boolean up,down,right,left;
	private boolean lmb=false,rmb=false;
	
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
		SendInputStatus();
	}
	
	private void SendInputStatus()
	{
		byte[] mouse_x=new byte[2];
		mouse_x[0]=(byte) (x_absolute>>>8);
		mouse_x[1]=(byte) (x_absolute);

		byte[] mouse_y=new byte[2];
		mouse_y[0]=(byte) (y_absolute>>>8);
		mouse_y[1]=(byte) (y_absolute);
		
		byte KBDStatus= (byte) ((up ? 1 : 0) + (right ? 2 : 0) + (down ? 4 : 0)+(left ? 8 : 0)
								+(lmb ? 16 : 0) + (rmb ? 32 : 0));
		
		byte lag=(byte)0;
		if(Network.INSTANCE.ping>=1020)
		{
			lag=(byte) 255;
		}
		else
		{
			lag=(byte) (Network.INSTANCE.ping/4);
		}
		
		byte[] result=new byte[6];
		result[0]=mouse_x[0];
		result[1]=mouse_x[1];
		result[2]=mouse_y[0];
		result[3]=mouse_y[1];
		result[4]=KBDStatus;
		result[5]=lag;
	
			
		Network.INSTANCE.Send((byte)2, result);
	}
	
	/**
	 * Function to process keyboard
	 */	
	private void processKeyboard() 
	{	
		 up=false;down=false;right=false;left=false;
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
		
		lmb=false;rmb=false;
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) 
		{
	       lmb=true;
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) 
		{
			rmb=true;
		}
			
		 int id= WorldManager.INSTANCE.actors.get(WorldManager.INSTANCE.myHeroArrayActors).getId();
		 WorldManager.INSTANCE.worldLogic.actor_by_id.get(id).controller().set_mouse_position(new Vector(x_absolute,y_absolute));
		 WorldManager.INSTANCE.worldLogic.actor_by_id.get(id).controller().set_mouse_status(lmb, rmb);
		
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
		DrawEngineGUI.INSTANCE.DrawText(x,y,color,font,
				"Mouse-relative(x,y)=("+x_relative+","+y_relative+")"+
				" Mouse-absolute(x,y)=("+x_absolute+","+y_absolute+")"
				);
	}
	

}
