package com.sparkfighters.client.ResourcesManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.sparkfighters.shared.physics.objects.Rectangle;

public class WeaponData 
{
	public float health;
	public float shield;

	public Rectangle left;
	public Rectangle right;
	public Texture texture;
	
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
}
