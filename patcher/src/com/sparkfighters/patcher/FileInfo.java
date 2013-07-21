package com.sparkfighters.patcher;

public class FileInfo 
{
	String path;
	long size;	
	String lastModify;
	
	public FileInfo(String path, long size, String lastModify)
	{
		this.path=path;
		this.size=size;
		this.lastModify=lastModify;
	}
}
