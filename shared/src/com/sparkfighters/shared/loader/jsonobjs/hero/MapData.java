package com.sparkfighters.shared.loader.jsonobjs.hero;

import java.util.ArrayList;

import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.objects.Rectangle;

public class MapData 
{
	public int id;
	public String name;
	public ArrayList<Vector> spawnPoints;
	public Vector sparkStart;
	public ArrayList<HorizSegment> platforms;
	public ArrayList<Rectangle> obstacles;
}
