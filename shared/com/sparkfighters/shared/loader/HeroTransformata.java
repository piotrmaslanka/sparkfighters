package com.sparkfighters.shared.loader;

import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.objects.Vector;
import com.sparkfighters.shared.physics.gwobjects.*;
import com.sparkfighters.shared.loader.jsonobjs.hero.*;

/**
 * Collection of functions that transform hero raw data
 * into processed .shared data
 * @author Henrietta
 *
 */
public class HeroTransformata {
	/**
	 * Transforms an AnimationData into a SmallMovingGeometry
	 * @param ad
	 * @return
	 */
	private static SmallMovingGeometry transform(AnimationData ad) {
		// construct an array and transcribe ArrayList into it
		Rectangle[] rects = new Rectangle[ad.hitboxes.size()];
		for (int i=0; i<ad.hitboxes.size(); i++) rects[i] = ad.hitboxes.get(i);
		
		// perform moving
		for (int i=0; i<rects.length; i++)
			rects[i] = new Rectangle(rects[i].x1 - ad.synchroPoint.x,
									 rects[i].y1 - ad.synchroPoint.y,
									 rects[i].x2 - ad.synchroPoint.x,
									 rects[i].y2 - ad.synchroPoint.y);

		return new SmallMovingGeometry(rects);
	}
	
	
	/**
	 * Transforms a HeroData instance into a GeometrySet instance
	 */
	public static GeometrySet transform(HeroData hd) {
		SmallMovingGeometry[] gs = new SmallMovingGeometry[hd.Animations.size()];
		for (int i=0; i<hd.Animations.size(); i++) 
			gs[i] = HeroTransformata.transform(hd.Animations.get(i));
		
		return new GeometrySet(gs);
	}
}
