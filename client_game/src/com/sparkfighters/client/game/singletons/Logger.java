package com.sparkfighters.client.game.singletons;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum Logger 
{
	INSTANCE;
	String fileName;
	Logger()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		
		fileName="logs/"+dateFormat.format(date)+".txt";
			
		try 
		{		
			File file=new File(fileName).getAbsoluteFile();		
			
			File file2=new File(file.getParent());
			file2.mkdirs();
			
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file,true);
			fw.write("Start logging - "+dateFormat.format(date)+"\r\n");
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public enum LogType
	{
		INFO, WARNING,ERROR, FATALERROR;
	}
	
	public void write(String text, LogType lt)
	{
		try 
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			
			FileWriter fw = new FileWriter(fileName,true);
			fw.write(dateFormat.format(date)+" "+lt+" "+text+"\r\n");
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
