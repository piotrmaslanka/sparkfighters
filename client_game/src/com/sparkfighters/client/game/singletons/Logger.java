package com.sparkfighters.client.game.singletons;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Singleton to handle logging information to file
 * @author Kamil Iwiñski
 *
 */
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
	/**
	 * Types of LogType
	 * @author Kamil Iwiñski
	 *
	 */
	public enum LogType
	{
		INFO, DEBUG, WARNING, ERROR, FATALERROR;
	}
	/**
	 * Function writes to file text with LogType
	 * @param text
	 * @param logtype
	 */
	public void write(String text, LogType logtype)
	{
		try 
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			
			FileWriter fw = new FileWriter(fileName,true);
			fw.write(dateFormat.format(date)+" "+logtype+" "+text+"\r\n");
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
