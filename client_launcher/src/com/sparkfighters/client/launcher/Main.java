package com.sparkfighters.client.launcher;

import java.io.IOException;

import com.sparkfighters.client.monitor.Monitor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application
{

	private Stage primaryStage;
	private AnchorPane anchorPane;
	
	@Override
	public void start(Stage primaryStage) 
	{
		  this.primaryStage = primaryStage;	      
	      try 
	      {
	          //FXMLLoader loader = new FXMLLoader();
	          anchorPane = (AnchorPane) FXMLLoader.load(Main.class.getResource("layouts/LoginScreen.fxml"));
	          Scene scene = new Scene(anchorPane);
	          this.primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png" ))); 
	          this.primaryStage.setScene(scene);
	          this.primaryStage.setTitle("Spark Fighters Launcher v. 0.0.1");
	          this.primaryStage.centerOnScreen();
	          
	          this.primaryStage.widthProperty().addListener(new ChangeListener<Number>()
	          {
	        	    public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) 
	        	    {
	        	        System.out.println("Width: " + newSceneWidth);
	        	    }
	          });
				
	          this.primaryStage.show(); 


	      } 
	      catch (IOException e) 
	      {
	          e.printStackTrace();
	      }
	}

	public static void main(String[] args) 
	{
		Monitor m=new Monitor();
		String jarName=new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		if(m.launch(jarName, Main.class,"Spark Fighters Launcher")==true)
		{
			launch(args);
		}
		else
		{
			Platform.exit();
		}
	}


	
}
