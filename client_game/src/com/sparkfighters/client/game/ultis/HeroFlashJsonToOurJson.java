package com.sparkfighters.client.game.ultis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sparkfighters.client.game.HDD.HDD;
import com.sparkfighters.shared.loader.jsonobjs.AnimationData;
import com.sparkfighters.shared.loader.jsonobjs.HeroData;
import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.objects.Vector;
/**
 * Class to convert data about heroes/bodies from Flash Json to Our Json type 
 * @author Kamil Iwiñski
 *
 */
public class HeroFlashJsonToOurJson 
{
	public static void convert(String loadPathJson, String loadPathImage, String savePath)
	{
		HeroData HD=new HeroData();
		HD.name=loadPathJson;
		
		FileHandle fh=HDD.getFileHandle(loadPathJson);
		String json=fh.readString();
		
		JsonParser parser = new JsonParser(); 
		JsonObject j1 = (JsonObject) parser.parse(json); 
		JsonObject j2=j1.getAsJsonObject("frames");
		
		AnimationData Animation=new AnimationData();
		
		String nameAnimation="";
		
		for (Entry<String, JsonElement> je : j2.entrySet()) 
		{				
			String key=je.getKey().substring(0, je.getKey().length()-4);
			JsonObject j3=j2.getAsJsonObject(je.getKey());
			JsonObject j4=j3.getAsJsonObject("frame");
			
			if(key.equals(nameAnimation)==false)
			{
				nameAnimation=key;
				if(Animation.frames.size()>0)					
				{
					HD.Animations.add(Animation);
					HD.Animations.add(leftAnimation(Animation));
					Animation=new AnimationData();
				}
				
				Animation.speedOfAnimation=0.1f;
				/*if(nameAnimation.equals("death_left")) Animation.id=13;
				if(nameAnimation.equals("death_right")) Animation.id=12;
				if(nameAnimation.equals("idle_left")) Animation.id=1;
				if(nameAnimation.equals("idle_left_45")) Animation.id=3;
				if(nameAnimation.equals("idle_right")) Animation.id=0;
				if(nameAnimation.equals("idle_right_45")) Animation.id=2;
				if(nameAnimation.equals("jump_left")) Animation.id=9;			
				if(nameAnimation.equals("jump_left_45")) Animation.id=11;		
				if(nameAnimation.equals("jump_right")) Animation.id=8;	
				if(nameAnimation.equals("jump_right_45")) Animation.id=10;	
				if(nameAnimation.equals("run_left")) Animation.id=5;
				if(nameAnimation.equals("run_left _45")) Animation.id=7;
				if(nameAnimation.equals("run_right")) Animation.id=4;
				if(nameAnimation.equals("run_right_45")) Animation.id=6;*/
				

				
				if(nameAnimation.equals("idle_right")) Animation.id=0;
				if(nameAnimation.equals("run_right")) Animation.id=2;	
				if(nameAnimation.equals("jump_right")) Animation.id=4;	
				if(nameAnimation.equals("death_right")) Animation.id=6;
				
				int w=j4.get("w").getAsInt();
				int h=j4.get("h").getAsInt();
				Animation.hitboxes.add(new Rectangle(0,0,w,h+20));
				Animation.synchroPoint=new Vector(w/2,h/2); 
			}
			
			if(key.equals(nameAnimation)==true)
			{				
				int x=j4.get("x").getAsInt();
				int y=j4.get("y").getAsInt();
				int w=j4.get("w").getAsInt();
				int h=j4.get("h").getAsInt();
				
				Rectangle frame=new Rectangle(x, y, x+w, y+h);
				Animation.frames.add(frame);	
			}
		}	
		
		HD.Animations.add(Animation);
		HD.Animations.add(leftAnimation(Animation));
		
		//sort animations
		for(int i=0;i<HD.Animations.size();i++)
		{
			for(int j=0;j<HD.Animations.size();j++)
			{
				if(HD.Animations.get(i).id<HD.Animations.get(j).id)
				{
					AnimationData a=new AnimationData();
					a=HD.Animations.get(i);
					
					HD.Animations.set(i, HD.Animations.get(j));
					HD.Animations.set(j, a);
				}
			}
		}
		
		//pink dot system for head
		for(int i=0;i<HD.Animations.size();i++)
		{
			for(int j=0;j<HD.Animations.get(i).frames.size();j++)
			{
				Vector headpoint=new Vector();
				headpoint=findHead(HD.Animations.get(i).frames.get(j), loadPathImage);
				HD.Animations.get(i).headPositions.add(headpoint);
			}
		}
		
		HDD.saveClass(savePath, HD);
		
	}
	
	private static Vector findHead(Rectangle r,String loadPathImage)
	{
		Vector headpoint=new Vector();
		
		ImageIcon icon = new ImageIcon(loadPathImage);
        Image image = icon.getImage();
        BufferedImage buffImage =
                new BufferedImage(
                        image.getWidth(null),
                        image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
              
        for (int i =(int)r.y1; i <= (int)r.y2; i++) 
        {
          	
            for (int j = (int)r.x1; j <= (int)r.x2; j++) 
            {
            	Color c = new Color(buffImage.getRGB(j, i));   
            	if(c.getAlpha()!=0)
            	{            	
	            	//System.out.println(c.getRed()+ " "+ c.getGreen()+ " "+c.getBlue());
	            	if(c.getRed()==255)
	            	{
	            		if(c.getGreen()==0)
	            		{
	            			if(c.getBlue()==240)
	            			{
	            				headpoint.x=(r.x2-r.x1)-(j-r.x1);
	            				headpoint.y=(r.y2-r.y1)-(i-r.y1);
	            				return headpoint;
	            			}
	            		}
	            	}
            	}
               
            }
        }
        
		
		return headpoint;
	}
	
	private static AnimationData leftAnimation(AnimationData rightAnimation)
	{
		AnimationData left=new AnimationData();
		
		left.id=rightAnimation.id+1;
		left.speedOfAnimation=rightAnimation.speedOfAnimation;
		left.synchroPoint=rightAnimation.synchroPoint.clone();
		
		for(int i=0;i<rightAnimation.hitboxes.size();i++)
		{	
			left.hitboxes.add(rightAnimation.hitboxes.get(i).clone());
		}
		
		for(int i=0;i<rightAnimation.frames.size();i++)
		{	
			left.frames.add(rightAnimation.frames.get(i).clone());
		}
		
		return left;
	}
}
