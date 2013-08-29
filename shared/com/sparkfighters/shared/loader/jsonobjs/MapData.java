package com.sparkfighters.shared.loader.jsonobjs;

import java.util.ArrayList;

import com.sparkfighters.shared.physics.objects.HorizSegment;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.objects.Rectangle;

public class MapData 
{
	public class SpawnPoint {
		public int team_id;
		public Vector position;
	}
	
	public int id;
	public String name;
	public Rectangle mapSize=new Rectangle();
	public ArrayList<SpawnPoint> spawnPoints=new ArrayList<>();
	public Vector sparkStart=new Vector();
	public ArrayList<HorizSegment> platforms=new ArrayList<HorizSegment>();
	public ArrayList<Rectangle> obstacles=new ArrayList<Rectangle>();
}