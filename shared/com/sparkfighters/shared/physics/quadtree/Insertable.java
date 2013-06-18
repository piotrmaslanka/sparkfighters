package com.sparkfighters.shared.physics.quadtree;

import com.sparkfighters.shared.physics.objects.Rectangle;
import com.sparkfighters.shared.physics.quadtree.Node;

/**
 * Implementing this interface means that an object can be inserted into
 * the quadtree, and will be able to tell the tree where to put it.
 * 
 * It is considered good practice not to have your own objects implement this,
 * but provide wrapper objects implementing Insertable that will link your objects
 * to the quadtree.
 * 
 * @author Henrietta
 */
public interface Insertable {
	/**
	 * Returns a rectangle that will be considered object's
	 * Minimum Bounding Rectangle for the purposes of the quadtree.
	 * It should account for both object's position and size.
	 * @return Object's MBR
	 */
	public Rectangle quadtree_get_mbr();
	
	/**
	 * Returns the node the object is currently placed in
	 */
	public Node quadtree_get_node();
}
