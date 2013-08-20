package com.sparkfighters.client.game;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileOutputStream;
import net.java.truevfs.access.TFileReader;
import net.java.truevfs.access.TFileWriter;

import pl.com.henrietta.lnx2.exceptions.NothingToRead;
import pl.com.henrietta.lnx2.exceptions.NothingToSend;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sparkfighters.client.game.singletons.Logger;
import com.sparkfighters.client.game.singletons.Network;
import com.sparkfighters.client.game.ultis.VFS;
import com.sparkfighters.client.monitor.Monitor;

/**
 * Main for Spark Fighters Game
 * @author Kamil Iwiñski
 *
 */
public class Main {
	/**
	 * 
	 * @param filename path to file
	 * @return String with context of file
	 */
	public static String readFile(String filename)
	{
	   String content = null;
	   File file = new File(filename); 
	   try 
	   {
	       FileReader reader = new FileReader(file);
	       char[] chars = new char[(int) file.length()];
	       reader.read(chars);
	       content = new String(chars);
	       reader.close();
	   } catch (IOException e)
	   {
	       e.printStackTrace();
	   }
	   return content;
	}
	
	public static void main(String[] args)
	{
		Logger.INSTANCE.write("SparkFightersGame.jar", Logger.LogType.INFO);
		
		Monitor m=new Monitor();
		String jarName=new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		if(m.launch(jarName, Main.class,"Spark Fighters Game")==true)
		{		
			String s=readFile("data/cfg.cfg");
			String[] s2=s.split(" ");
			
			int window_width=Integer.parseInt(s2[0]);
			int window_height=Integer.parseInt(s2[1]);
			Boolean fullscreen= Boolean.valueOf(s2[2]);
			int fps=60;
			
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = "Spark Fighters Game";
			cfg.useGL20 = true;
			
			cfg.fullscreen=fullscreen;
			cfg.width = window_width;
			cfg.height = window_height;
			
			cfg.resizable=false;
			cfg.backgroundFPS=fps;
			cfg.foregroundFPS=fps;
			
			//cfg.addIcon("logo.png", FileType.Internal);
			try 
			{
				Network.INSTANCE.Init(args[0], args[1], args[2], args[3]);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			new LwjglApplication(new SparkFightersGame(), cfg);
		}
		else
		{
			System.exit(0);
		}
		
		
	}
}
