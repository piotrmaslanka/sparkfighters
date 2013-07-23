package com.sparkfighters.client.monitor;

public class OS 
{
	public  String yourOS = System.getProperty("os.name").toLowerCase();
	
	public  boolean isWindows() 
	{	 	
		return (yourOS.indexOf("win") >= 0);
	}
 
	public  boolean isMac() 
	{
		return (yourOS.indexOf("mac") >= 0);
	}
 
	public  boolean isUnix() 
	{
		return (yourOS.indexOf("nix") >= 0 || yourOS.indexOf("nux") >= 0 || yourOS.indexOf("aix") > 0 );
	}
	
	public  boolean isSupportedOS()
	{
		if(isWindows()==true) return true;
		if(isUnix()==true) return true;
		if(isMac()==true) return true;
	
		return false;
	}
}
