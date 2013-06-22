package com.sparkfighters;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sparkfighters.client.mainexe.Engine;

public class Main {
	public static void main(String[] args) 
	{
		int window_width=1280;
		int window_height=720;
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Spark Fighters";
		cfg.useGL20 = false;
		cfg.width = window_width;
		cfg.height = window_height;
		cfg.resizable=false;
		//cfg.fullscreen=true;
		cfg.backgroundFPS=40;
		cfg.foregroundFPS=40;
		
		Engine.INSTANCE.Create(window_width,window_height);
		
		new LwjglApplication(new SparkFightersGame(), cfg);
	}
}
