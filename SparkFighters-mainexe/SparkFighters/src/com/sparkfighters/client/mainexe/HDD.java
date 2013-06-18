package com.sparkfighters.client.mainexe;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class HDD 
{
	public static FileHandle Load(String path)
	{
		return Gdx.files.internal(path);	
	}
	
	public static FileHandle[] getDirectories(String path)
	{
		FileHandle dirHandle;
		//if (Gdx.app.getType() == ApplicationType.Android) 
		//{
			dirHandle=HDD.Load(path);
		//} 
		//else 
		//{
			  //ApplicationType.Desktop ..
			//dirHandle=HDD.Load("./bin/"+path);
		//}
		return dirHandle.list();

	}
}
