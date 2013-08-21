package com.sparkfighters.client.game.ultis;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.compress.utils.IOUtils;

import com.sparkfighters.client.game.HDD.VFS;

import scala.collection.Set;
import scala.collection.immutable.HashSet;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileOutputStream;
import net.java.truevfs.access.TFileReader;
import net.java.truevfs.access.TFileWriter;
import net.java.truevfs.access.TVFS;

public class DataCipherVFS 
{
	static String src="data/";
	static String dst="assets.dat/";
	
	
	public static void main(String[] args)
	{
		VFS.setDetector();
		
		listFile(src,0); 		
	
		
		File f=new TFile(dst);
		f.delete();
		f.mkdirs();
		
		try
		{
			for(int i=0;i<list0.size();i++)
			{			
				TFile src2 = new TFile(src); // e.g. "file"
				TFile dst2 = new TFile(dst); // e.g. "archive.zip"
				if (dst2.isArchive() || dst2.isDirectory())
				    dst2 = new TFile(dst2, src2.getName());
				System.out.println("Copy file: "+list0.get(i));
				src2.cp_rp(dst2);
			}
			
			TVFS.umount();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		listFile(dst,1); 
		
		check();
		System.out.println("END");
	}
	
	
	private static void check()
	{
		for(int i=0;i<list0.size();i++)
		{
			boolean found=false;
			for(int j=0;j<list1.size();j++)
			{
				String s=list1.get(j).substring(dst.length(), list1.get(j).length());
				//System.out.println(s);
				if(list0.get(i).equals(s)==true) {found=true;break;}
			}
			
			if(found==false)
			{
				System.out.println("Missing file: "+list0.get(i));
			}
		}
	}
	
	private static ArrayList<String> list0=new ArrayList<String> ();
	private static ArrayList<String> list1=new ArrayList<String> ();
	
	private static void listFile(String pathname,int type) 
	{
		List<File> allFiles = new ArrayList<File>();
		Queue<File> dirs = new LinkedList<File>();
		dirs.add(new TFile(pathname));
		while (!dirs.isEmpty()) 
		{
		  for (File f : dirs.poll().listFiles()) 
		  {
		    if (f.isDirectory()) 
		    {
		      dirs.add(f);
		    } else if (f.isFile()) 
		    {
		      allFiles.add(f);
			  if(type==0) list0.add(f.getPath());
			  if(type==1) list1.add(f.getPath());
		    }
		  }
		}
	          

	}
	
}
