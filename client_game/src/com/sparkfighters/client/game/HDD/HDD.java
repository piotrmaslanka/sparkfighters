package com.sparkfighters.client.game.HDD;
import java.io.File;

import net.java.truevfs.access.TFile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Static class to input, output
 * @author Kamil Iwiñski
 * 
 */
public class HDD
{
	/**
	 * type=0 normal catalog
	 * type=1 archive zip catalog
	 */
	private static int type=0;
	/**
	 * @param path to file as String
	 * @return FileHandle
	 */
	public static FileHandle getFileHandle(String path)
	{
		if(type==0)
		{	
			File file = new TFile(path);
			FileHandle fh=new FileHandle(file);
			return fh;
		}
		
		if(type==1)
		{
			path="assets.dat/"+path;
			TFile file = new TFile(path);
			VFSFileHandle fh=new VFSFileHandle(file);
			return fh;
		}
		
		return null;
	}
	
	/**
	 * Function checking what is in folder
	 * @param path to dir as String
	 * @return array of FileHandle
	 */
	public static FileHandle[] getDirContent(String path)
	{
		FileHandle dirHandle;
		dirHandle=HDD.getFileHandle(path);
		return dirHandle.list();
	}
	
	
	/**
	 * Function save class to json file
	 * @param path where save class on HDD
	 * @param cl class which we want save
	 */
	public static <T> void saveClass(String path,T cl)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(cl);
		json=json.replace("\n","\r\n");
		
		FileHandle fh=HDD.getFileHandle(path);
		fh.writeString(json, false);
	}
	
	/**
	 * Function load class from json file and return it
	 * @param path from where load class
	 * @param cl which type is class
	 * @return <T> loaded class 
	 */
	public static <T> T loadClass(String path, Class<T> cl)
	{
		FileHandle fh=HDD.getFileHandle(path);
		String json=fh.readString();
		Gson gson = new Gson();
		
		return (T) gson.fromJson(json, cl);
	}
}
