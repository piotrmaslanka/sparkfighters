package com.sparkfighters.client.patcher;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
 
public class Controller
{

	@FXML public Button buttonStart;
    
	@FXML protected void buttonStartClick(ActionEvent event) 
     {
		 //Here launch game
		/*
		try 
		{
			String[] params = new String [3];
			params[0] = "notepad";
		    params[1] = "arg1";
		    params[2] = "arg2";

			Runtime runTime = Runtime.getRuntime();
			Process process = runTime.exec(params);
			
			Platform.exit();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		*/
     }
	 

}