package com.sparkfighters.patcher.WebBrowser;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebBrowser extends Region
{    
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
     
    public WebBrowser()
    {
        final String url="http://sparkfighters.com/patcher/news.html";
        
        webEngine.load(url);
         
        getChildren().add(webView);
        
        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, final String oldLoc, final String loc) {
              if (!loc.contains(url)) {
                Platform.runLater(new Runnable() {
                   public void run() 
                   {
                	  webEngine.load(oldLoc);
                	  Desktop d = Desktop.getDesktop();
                	  URI uri=URI.create(loc);
                	  try {
						d.browse(uri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                  }
                });
              }
            }
          });
     
    }
    
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(webView,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 750;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}