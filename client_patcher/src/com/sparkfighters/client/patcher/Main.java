package com.sparkfighters.client.patcher;

import java.io.IOException;

import com.sparkfighters.client.monitor.Monitor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application implements EventHandler<WindowEvent> 
{

	private Stage primaryStage;
	private AnchorPane anchorPane;
	
	
	private BackgroundWorker backgroundWorker;

	@Override
	public void start(Stage primaryStage)
	{
		  this.primaryStage = primaryStage;	      
	      try 
	      {
	    	  //Init layout
	    	  
	          // Load the root layout from the fxml file
	          FXMLLoader loader = new FXMLLoader();
	          anchorPane = (AnchorPane) loader.load(Main.class.getResource("layout.fxml"));
	          Scene scene = new Scene(anchorPane);
	          this.primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png" ))); 
	          this.primaryStage.setScene(scene);
	          this.primaryStage.setTitle("Spark Fighters Patcher v. 0.8.0");
	          this.primaryStage.centerOnScreen();
	          this.primaryStage.setResizable(false);
	          this.primaryStage.setOnCloseRequest(this);
	          this.primaryStage.show(); 
	          
	    	  //Here set background worker for downloading/updating files
	    	  backgroundWorker=new BackgroundWorker(anchorPane);

	      } 
	      catch (IOException e) 
	      {
	          // Exception gets thrown if the fxml file could not be loaded
	          e.printStackTrace();
	      }
	      

	}

	
	public static void main(String[] args) 
	{       
		String jarName=new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		
		if(Monitor.launch(jarName, Main.class,"Spark Fighters Patcher")==true)
		{
			launch(args);
		}
		else
		{
			Platform.exit();
		}
	}


	public void handle(WindowEvent t) 
	{
		 if (t.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST)
		 {
			 backgroundWorker.stopThread();
			 Platform.exit();
		 }
		
	}
	
}
