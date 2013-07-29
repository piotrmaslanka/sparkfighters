package com.sparkfighters.client.game.ultis;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.sparkfighters.shared.loader.jsonobjs.hero.MapData;
import com.sparkfighters.shared.physics.objects.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PlatformsImageToJson 
{

	public void convert(String in, String out)
	{
		MapData MD= new MapData();
		MD.id=0;
		MD.name="first map";
		MD.sparkStart=new Vector(400,400);
		MD.spawnPoints.add(new Vector(600,600));
		MD.spawnPoints.add(new Vector(800,800));
		MD.spawnPoints.add(new Vector(1000,1000));
		
		// Get Image
        ImageIcon icon = new ImageIcon(in);
        Image image = icon.getImage();
        // Create empty BufferedImage, sized to Image
        BufferedImage buffImage =
                new BufferedImage(
                        image.getWidth(null),
                        image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        //Dispose the Graphics
        g.dispose();
        //Here 2 for loops used for iterate through each and every pixel in image
        for (int i = 0; i < buffImage.getWidth(); i++) 
        {
            for (int j = 0; j < buffImage.getHeight(); j++) 
            {
                //signed bit shift right
                /*
                How to extract different color components
                blue = pix & 0xFF;
                green = (pix>>8) & 0xFF;
                red = (pix>>16) & 0xFF;
                alpha = (pix>>24) & 0xFF;
                 */
                int alpha = (buffImage.getRGB(i, j) >> 24) & 0xff;
                if (alpha == 0) 
                {
                   //Now we will have pixel with Alpha 0 (Transparent pixel)
                   //As example If you need to fill transparent pixels with white color
                   //use this code 
                   //buffImage.setRGB(i, j, Color.white.getRGB());
                   //buffImage.setRGB(i, j, 0);
                } 
                else 
                {
                	buffImage.setRGB(i, j, Color.red.getRGB());
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
	
}
