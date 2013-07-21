package com.sparkfighters.client.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HDD 
{
	public static FileHandle getFileHandle(String path)
	{
		return Gdx.files.local(path);	
		//return Gdx.files.internal(path);
	}
	
	public static FileHandle[] getDirContent(String path)
	{
		FileHandle dirHandle;
		//if (Gdx.app.getType() == ApplicationType.Android) 
		//{
			dirHandle=HDD.getFileHandle(path);
		//} 
		//else 
		//{
			  //ApplicationType.Desktop ..
			//dirHandle=HDD.Load("./bin/"+path);
		//}
		return dirHandle.list();
	}
	
	public static <T> void saveClass(String path,T cl)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(cl);
		json=json.replace("\n","\r\n");
		
		FileHandle fh=HDD.getFileHandle(path);
		fh.writeString(json, false);
	}
	
	public static <T> T loadClass(String path, Class<T> cl)
	{
		FileHandle fh=HDD.getFileHandle(path);
		String json=fh.readString();
		Gson gson = new Gson();
		
		return (T) gson.fromJson(json, cl);
	}
}
