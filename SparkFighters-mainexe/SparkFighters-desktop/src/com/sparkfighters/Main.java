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
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Spark Fighters";
		cfg.useGL20 = false;
		cfg.width = window_width;
		cfg.height = window_height;
		cfg.resizable=false;
		//cfg.fullscreen=true;
		cfg.backgroundFPS=40;
		cfg.foregroundFPS=40;
		
		new LwjglApplication(new SparkFightersGame(), cfg);
	}
}
