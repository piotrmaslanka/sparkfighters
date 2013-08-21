package com.sparkfighters.client.game.HDD;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class VFSFileHandle extends FileHandle 
{ 
	TFile file2;
	
	public VFSFileHandle(TFile file) 
	{
		this.file2=file;
	}
	
	public String toString () 
	{
		return file2.getPath().replace('\\', '/');
	}
	public String name () 
	{
		return file2.getName();
	}
	
	public String extension () 
	{
		String name = name();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1) return "";
		return name.substring(dotIndex + 1);
	}

	public String readString(String charset)
	{
		int fileLength = (int)length();
		if (fileLength == 0) fileLength = 512;
		StringBuilder output = new StringBuilder(fileLength);
		InputStreamReader reader = null;
		try 
		{
			if (charset == null)
				reader = new InputStreamReader(read());
			else
				reader = new InputStreamReader(read(), charset);
			char[] buffer = new char[256];
			while (true) 
			{
				int length = reader.read(buffer);
				if (length == -1) break;
				output.append(buffer, 0, length);
			}
		} 
		catch (IOException ex) 
		{
			throw new GdxRuntimeException("Error reading layout file: " + this, ex);
		} 
		finally
		{
			try 
			{
				if (reader != null) reader.close();
			} 
			catch (IOException ignored) 
			{
				
			}
		}
		return output.toString();
	}
	
	public long length () 
	{
		return file2.length();
	}
	
	public TFileInputStream read () 
	 {
		 try 
		 {
			 TFileInputStream is=new TFileInputStream(file2);			 
			 return is;
		 } 
		 catch (IOException e) 
		 {
			 throw new GdxRuntimeException("File not found: " + file2.getPath());
		 }
	 }
	
	public String readString () 
	{
		return readString(null);
	}
	
	public VFSFileHandle parent () 
	{
		TFile parent = file2.getParentFile();
		if (parent == null) 
		{		
			parent = new TFile("/");
		}
		return new VFSFileHandle(parent, type);
	}
	
	protected VFSFileHandle (TFile file, FileType type) 
	{
		this.file2 = file;
		this.type = type;
	}
	
	public FileHandle child (String name) 
	{
		if (file2.getPath().length() == 0) return new VFSFileHandle(new TFile(name), type);
		return new VFSFileHandle(new TFile(file2, name), type);
	}
	
	public String path () 
	{
		return file2.getPath().replace('\\', '/');
	}
	
	public FileHandle[] list () 
	{
		String[] relativePaths = file2.list();
		if (relativePaths == null) return new VFSFileHandle[0];
		FileHandle[] handles = new VFSFileHandle[relativePaths.length];
		
		String s=path();
		s=s.substring(s.indexOf("/"), s.length());
		
		this.file2=new TFile(s);
				
		for (int i = 0, n = relativePaths.length; i < n; i++)
		{
			handles[i] = child(relativePaths[i]);
		}
			
		return handles;
	}
}

