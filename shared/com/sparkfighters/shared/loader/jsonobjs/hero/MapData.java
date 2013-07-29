package com.sparkfighters.shared.loader.jsonobjs.hero;

import java.util.ArrayList;

import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.objects.Rectangle;

public class MapData 
{
	public int id;
	public String name;
	public Rectangle mapSize;
	public ArrayList<Vector> spawnPoints=new ArrayList<Vector>();
	public Vector sparkStart;
	public ArrayList<HorizSegment> platforms=new ArrayList<HorizSegment>();
	public ArrayList<Rectangle> obstacles=new  ArrayList<Rectangle>();
}
