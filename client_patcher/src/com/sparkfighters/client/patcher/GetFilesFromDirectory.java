package com.sparkfighters.client.patcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetFilesFromDirectory 
{
	ArrayList<FileInfo> filesInfo=new ArrayList<FileInfo>();
	String rootfolder="";

	public ArrayList<FileInfo> getInfo(String folder)
	{
		if(rootfolder.equals("")) 
		{
			File f=new File(folder);
			rootfolder=f.getParent();
		}

		File file = new File(folder);


		File[] fileArray = file.listFiles();

		for (int i = 0; i < fileArray.length; i++) 
		{

			if (fileArray[i].isDirectory())
			{

				getInfo(fileArray[i].getAbsolutePath());
			}
			else 
			{
				String path=fileArray[i].getAbsolutePath();
				path=path.replace(rootfolder, "");
				path=path.substring(1, path.length());

				long size=fileArray[i].length();
				
				String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(fileArray[i].lastModified()));		
				
				filesInfo.add(new FileInfo(path,size,date));

				//System.out.println(path+" "+size+" "+date);
			}
		}

		return filesInfo;
	}

}