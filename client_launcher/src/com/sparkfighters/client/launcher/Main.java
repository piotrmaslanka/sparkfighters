package com.sparkfighters.client.launcher;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
	          FXMLLoader loader = new FXMLLoader();
	          anchorPane = (AnchorPane) loader.load(Main.class.getResource("layouts/LoginScreen.fxml"));
	          Scene scene = new Scene(anchorPane);
	          this.primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("logo.png" ))); 
	          this.primaryStage.setScene(scene);
	          this.primaryStage.setTitle("Spark Fighters Launcher v. 0.0.1");
	          this.primaryStage.centerOnScreen();
	          this.primaryStage.show(); 


	      } 
	      catch (IOException e) 
	      {
	          e.printStackTrace();
	      }
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
