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
import net.java.truevfs.access.TVFS;

import pl.com.henrietta.lnx2.exceptions.NothingToRead;
import pl.com.henrietta.lnx2.exceptions.NothingToSend;
import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.client.game.HDD.VFS;
import com.sparkfighters.client.game.singletons.Logger;
import com.sparkfighters.client.game.singletons.Network;
import com.sparkfighters.client.monitor.Monitor;

/**
 * Main for Spark Fighters Game
 * @author Kamil Iwiñski
 *
 */
public class Main {
	
	public static void main(String[] args)
	{
		/* 
		 * WORKING:
		 * args[0] -login
		 * args[1] -password
		 * args[2] -IP
		 * args[3] -port UDP
		 * 
		 * args[4] -width of window
		 * args[5] -height of window
		 * args[6] -fullscreen
		 * 
		 * NOT WORKING NOW:
		 * args[7] -debug type
		 * args[8] -normal catalog(0), archive zip catalog(1)
		 */
		Logger.INSTANCE.write("SparkFightersGame.jar", Logger.LogType.INFO);
		
		Monitor m=new Monitor();
		String jarName=new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		if(m.launch(jarName, Main.class,"Spark Fighters Game")==true)
		{	
			VFS.setDetector();
					
			int window_width=Integer.parseInt(args[4]);
			int window_height=Integer.parseInt(args[5]);
			Boolean fullscreen= Boolean.valueOf(args[6]);
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
	
			Network.INSTANCE.Init(args[0], args[1], args[2], args[3]);
			
			new LwjglApplication(new SparkFightersGame(), cfg);
		}
		else
		{
			System.exit(0);
		}
		
		
	}
}
