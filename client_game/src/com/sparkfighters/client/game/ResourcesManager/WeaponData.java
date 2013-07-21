package com.sparkfighters.client.game.ResourcesManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.sparkfighters.shared.physics.objects.Rectangle;

public class WeaponData 
{
	public String name;
	public float health;
	public float shield;
	public float runSpeed;
	public float jumpHeight;

	public Rectangle left;
	public Rectangle right;
	public Texture texture;
	
	public void loadTexture(String filePath)
	{
		texture=new Texture(filePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
}
