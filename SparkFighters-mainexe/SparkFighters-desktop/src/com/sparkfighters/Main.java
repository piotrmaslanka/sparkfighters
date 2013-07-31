package com.sparkfighters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {
	
	public static String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); //for ex foo.txt
	   try {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e) {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	public static void main(String[] args) 
	{

		String s=readFile("data/cfg.txt");
		String[] s2=s.split(" ");
		
		int window_width=Integer.parseInt(s2[0]);
		int window_height=Integer.parseInt(s2[1]);
		Boolean fullscreen= Boolean.valueOf(s2[2]);
		int fps=40;
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Spark Fighters";
		cfg.useGL20 = false;
		
		cfg.fullscreen=fullscreen;
		cfg.width = window_width;
		cfg.height = window_height;
		
		cfg.resizable=false;
		cfg.backgroundFPS=fps;
		cfg.foregroundFPS=fps;
		
		new LwjglApplication(new SparkFightersGame(), cfg);
	}
}