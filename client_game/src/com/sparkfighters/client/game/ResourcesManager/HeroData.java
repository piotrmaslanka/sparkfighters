package com.sparkfighters.client.game.ResourcesManager;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;



public class HeroData 
{
	public int id;
	public String name;
	public ArrayList<AnimationData> Animations=new ArrayList<AnimationData>();
	public Texture texture;
	
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
}