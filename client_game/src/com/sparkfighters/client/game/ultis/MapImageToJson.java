package com.sparkfighters.client.game.ultis;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sparkfighters.client.game.HDD;
import com.sparkfighters.shared.loader.jsonobjs.hero.MapData;
import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.objects.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MapImageToJson 
{
	private MapData MD=new MapData();
	
	private void platformsToJson(String in, String out)
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
        
        try 
        {
            //Write back modified file to file system
            File file = new File(out);
            ImageIO.write(buffImage, "png", file);
        } 
        catch (Exception e) 
        {
        	
        }
	}
	
	public void convert(String platformImagePath, String out)
	{
		MD= new MapData();
		MD.id=0;
		MD.name="first map";
		MD.sparkStart=new Vector(400,400);
		MD.spawnPoints.add(new Vector(600,600));
		MD.spawnPoints.add(new Vector(800,800));
		MD.spawnPoints.add(new Vector(1000,1000));
		
		
		platformsToJson(platformImagePath,out);
		
		HDD.saveClass(out+".json", MD);
    }
	
}
