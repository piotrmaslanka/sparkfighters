package com.sparkfighters.shared.loader.jsonobjs;

import com.sparkfighters.shared.physics.objects.Rectangle;

public class WeaponData 
{
	public int id;
	public String name;
	public float health;
	public float shield;
	public float runSpeed;
	public float jumpHeight;

	public Rectangle left=new Rectangle();
	public Rectangle right=new Rectangle();

}