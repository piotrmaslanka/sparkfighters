package com.sparkfighters.shared.loader.jsonobjs;

import java.util.ArrayList;

import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.objects.Vector;

public class AnimationData
{
	public int id;
	public Vector synchroPoint=new Vector();
	public float speedOfAnimation;
	public ArrayList<Rectangle> hitboxes=new ArrayList<Rectangle>();
	public ArrayList<Rectangle> frames=new ArrayList<Rectangle>();
	public ArrayList<Vector> headPositions=new ArrayList<Vector>();
}