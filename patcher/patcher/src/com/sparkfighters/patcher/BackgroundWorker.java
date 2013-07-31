package com.sparkfighters.patcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class BackgroundWorker  
{
	@FXML private Button buttonStart;
	@FXML private Text textString;
	@FXML private Text textMB;
	@FXML private ProgressBar progressBar;
	
	private Thread thread;
	
	private ArrayList<FileInfo> clientFiles;
	private ArrayList<FileInfo> serverFiles;
	
	private ArrayList<FileInfo> downloadFiles;
	private ArrayList<FileInfo> removeFiles;
	
	private ArrayList<String> ignoreFiles;
	private ArrayList<String> ignoreFolders;
	
	private long totalSize;
	private long totalDownloadedSize;
	
	
	Task task = new Task<Void>() 
			{
			    public Void call() throws InterruptedException 
			    {
			    	while(true)
			    	{
				    	generateHDDFileList();
				    	downloadHDDServerFileList();
				    	if(compareFiles()==true)
				    	{
				    		progressBar.setProgress(1.0f);
				    		textString.setText("");
				    		textMB.setText("");
				    		buttonStart.setDisable(false);
				    		stopThread();
				    	}
				    	downloadMissingFiles();
				    	removeFiles();
			    	}
				   //return null;
			    }
			};
	
	public void stopThread()
	{
		thread.stop();
	}
	private void generateHDDFileList()
	{
		textString.setText("Generating list files");
		progressBar.setProgress(0.0f);
		GetFilesFromDirectory getHDD=new GetFilesFromDirectory();
		clientFiles=new ArrayList<FileInfo>();
		File file = new File("data");
		file.mkdir();
		clientFiles=getHDD.getInfo(System.getProperty("user.dir")+"\\data");
		progressBar.setProgress(0.1f);
	}
	
	private void downloadHDDServerFileList()
	{
		textString.setText("Download files list from server");
		
		ignoreFiles=new ArrayList<String>();
		ignoreFolders=new ArrayList<String>();
		serverFiles=new ArrayList<FileInfo>();
		
		String serverfilestxt="";
		String serverfilesignoretxt="";
		try
		{
			serverfilestxt=getTextFromWebsiteFile("http://sparkfighters.com/patcher/server_data.dat");
			serverfilesignoretxt=getTextFromWebsiteFile("http://sparkfighters.com/patcher/server_data_ignore.dat");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		//server files
		String lines[]=serverfilestxt.split("\r\n");
		for(int i=0; i<lines.length;i++)
		{
			String[] line=lines[i].split(" ");
			String path=line[0];
			long size=Long.valueOf(line[1]).longValue();
			String date=line[2]+" "+line[3];
			serverFiles.add(new FileInfo(path,size,date));
		}
		
		//ignore list
		String lines2[]=serverfilesignoretxt.split("\r\n");
		for(int i=0;i<lines2.length;i++)
		{
			if(lines2[i].endsWith("\\")==true)
			{
				ignoreFolders.add(lines2[i]);
			}
			else
			{
				ignoreFiles.add(lines2[i]);
			}
		}
		
		progressBar.setProgress(0.2f);
	}
	
	private boolean compareFiles()
	{
		downloadFiles=new ArrayList<FileInfo>();
		removeFiles=new ArrayList<FileInfo>();
		textString.setText("Compare files");
		
		//download files list
		for(int i=0;i<serverFiles.size();i++)
		{
			boolean found=false;
			boolean ignore=false;
			for(int j=0;j<clientFiles.size();j++)
			{
				if(serverFiles.get(i).path.equals(clientFiles.get(j).path))
				{
					for(int k=0;k<ignoreFiles.size();k++)
					{
						if(clientFiles.get(j).path.equals(ignoreFiles.get(k))==true)
						{
							ignore=true;
						}
					}
					if(serverFiles.get(i).size==clientFiles.get(j).size)
					{
						if(serverFiles.get(i).lastModify.equals(clientFiles.get(j).lastModify))
						{
							found=true;
							break;
						}
					}
				}
			}
			
			if(found==false)
			{
				if(ignore==false)
				{
					downloadFiles.add(serverFiles.get(i));
				}

			}
		}
		
		
		//remove file list
		
		for(int i=0;i<clientFiles.size();i++)
		{
			boolean quit=false;
			//checking ignore list
			for(int j=0;j<ignoreFiles.size();j++)
			{
				if(clientFiles.get(i).path.equals(ignoreFiles.get(j)))
				{
					quit=true;
				}
			}
			
			for(int j=0;j<ignoreFolders.size();j++)
			{
				if(clientFiles.get(i).path.startsWith(ignoreFolders.get(j))==true)
				{
					quit=true;
				}
			}
			if(quit==true) continue;
			
			boolean foundOnServer=false;
			for(int j=0;j<serverFiles.size();j++)
			{
				if(clientFiles.get(i).path.equals(serverFiles.get(j).path))
				{
					if(clientFiles.get(i).size==serverFiles.get(j).size)
					{
						if(clientFiles.get(i).lastModify.equals(serverFiles.get(j).lastModify))
						{
							foundOnServer=true;
						}
					}
				}
			}
			
			if(foundOnServer==false)
			{
				removeFiles.add(clientFiles.get(i));
			}
		}
		
		
		
		
		if(downloadFiles.size()==0)
		{
			if(removeFiles.size()==0)
			{
				return true;
			}
		}
		
		progressBar.setProgress(0.3f);
		return false;
	}
	
	private void downloadMissingFiles()
	{
		textString.setText("Downloading files");
		String url="http://sparkfighters.com/patcher/";
		totalSize=0;
		totalDownloadedSize=0;
		
		for(int i=0;i<downloadFiles.size();i++)
		{
			totalSize+=downloadFiles.get(i).size;
		}
		
		for(int i=0;i<downloadFiles.size();i++)
		{	
			saveFileFromURL(url+downloadFiles.get(i).path,downloadFiles.get(i).path,downloadFiles.get(i).lastModify);		
		}
	}
	
	private void removeFiles()
	{
		textString.setText("Deleting files");
		for(int i=0;i<removeFiles.size();i++)
		{
			File file = new File(removeFiles.get(i).path);
			file.delete();
		}
		progressBar.setProgress(0.9f);
	}
	
	public void saveFileFromURL(String URL, String savePath, String lastModify)
	{
		URL=URL.replace("\\","/");			
		try 
		{
			File file = new File(savePath);		
			File file2 = new File(file.getParent());
			file2.mkdirs();
			
			BufferedInputStream in = new java.io.BufferedInputStream(new java.net.URL(URL).openStream());
			FileOutputStream fos;
			fos = new java.io.FileOutputStream(savePath);
			BufferedOutputStream bout = new BufferedOutputStream(fos);
			byte data[] = new byte[1024];
			int read;
			while((read = in.read(data,0,1024))>=0)
			{
				bout.write(data, 0, read);
				
				totalDownloadedSize+=read;
				
				float progress=(float)totalDownloadedSize*0.5f/(float)totalSize;
				progressBar.setProgress(0.3+progress);
				
				DecimalFormat df = new DecimalFormat("0.00");
				
				float totalDownloadedSizeMB=(float)totalDownloadedSize/1000000;				
				float totalSizeMB=(float)totalSize/1000000;
				
				textMB.setText(df.format(totalDownloadedSizeMB)+"/"+df.format(totalSizeMB)+" MB");
			}
			bout.close();
			in.close();
			
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastModify);
			File file3=new File(savePath);
			file3.setLastModified(date.getTime());
		} 
		catch (IOException | ParseException e)
		{	
			e.printStackTrace();
		}
        	
	}
	
	public static String getTextFromWebsiteFile(String url) throws Exception 
	{
	        URL website = new URL(url);
	        URLConnection connection = website.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                    connection.getInputStream()));

	        StringBuilder response = new StringBuilder();
	        String inputLine;

	        while ((inputLine = in.readLine()) != null)
	        {
	            response.append(inputLine);
	            response.append("\r\n");
	        }

	        in.close();

	        return response.toString();
	}

	
	public BackgroundWorker(AnchorPane anchorPane)
	{
		//bind controls from layout
        buttonStart= (Button) anchorPane.lookup("#buttonStart");
    	textString=(Text)anchorPane.lookup("#textString");
  	  	textMB=(Text)anchorPane.lookup("#textMB");
  	  	progressBar=(ProgressBar)anchorPane.lookup("#progressBar");
  
  	  	textString.setText("");
  	  	textMB.setText("");
  	  		 	  	
  	  	thread=new Thread(task);
  	  	thread.start();
	}
	
	


}
