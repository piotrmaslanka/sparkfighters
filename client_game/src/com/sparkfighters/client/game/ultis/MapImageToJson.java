package com.sparkfighters.client.game.ultis;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.shared.loader.jsonobjs.MapData;
import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
/**
 * Class to create map json from platforms image
 * @author Kamil Iwiñski
 *
 */
public class MapImageToJson 
{
	private MapData MD=new MapData();
	
	private void platformsToJson(String in)
	{
        ImageIcon icon = new ImageIcon(in);
        Image image = icon.getImage();
        BufferedImage buffImage =
                new BufferedImage(
                        image.getWidth(null),
                        image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        MD.mapSize.x1=0;
        MD.mapSize.y1=0;
        MD.mapSize.x2=buffImage.getWidth();
        MD.mapSize.y2=buffImage.getHeight();
        
        for (int i = 0; i < buffImage.getHeight(); i++) 
        {
        	boolean platform=false;
        	int x1=-1,x2=-1;
        	int y=buffImage.getHeight()-i;
        	
            for (int j = 0; j < buffImage.getWidth(); j++) 
            {

                int alpha = (buffImage.getRGB(j, i) >> 24) & 0xff;
                if (alpha == 0) 
                {
                	if(platform==true)
                	{
                		platform=false;  
               
                		HorizSegment HS=new HorizSegment(x1,x2,y);
                		MD.platforms.add(HS);
                	}
                } 
                else 
                {
                	if(platform==false) {x1=j;platform=true;}
                	x2=j;
                	buffImage.setRGB(j, i, Color.red.getRGB());
                }
            }
        }
        

        	
	        for(int i=MD.platforms.size()-1;i>0;i--)
	        {       
	        	 for(int j=MD.platforms.size()-1;j>0;j--)
	        	 {
	        		 if(MD.platforms.get(i).y==MD.platforms.get(j).y-1)
	        		 {
	        			 MD.platforms.remove(i);
	        			 break;
	        		 }
	        	 
	        	 }
	        }

        
        try 
        {
            //Write back modified file to file system
        	String name=in.substring(0,in.length()-4);
        	String type=in.substring(in.length()-4,in.length());
            File file = new File(name+"_out"+type);
            ImageIO.write(buffImage, "png", file);
        } 
        catch (Exception e) 
        {
        	
        }
	}
	
	public void convert(String platformImagePath, String out_json)
	{
		MD= new MapData();
		MD.id=0;
		MD.name="first map";
		MD.sparkStart=new Vector(400,400);
		MD.spawnPoints.add(new Vector(600,600));
		MD.spawnPoints.add(new Vector(800,800));
		MD.spawnPoints.add(new Vector(1000,1000));
		
		
		platformsToJson(platformImagePath);
		
		HDD.saveClass(out_json, MD);
    }
	
}
