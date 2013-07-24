package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.objects.Rectangle;

/**
 * Actor meta supporting class
 * @author Henrietta
 */
public interface ActorMeta {
	
	/**
	 * Called on collision with Shot
	 * @param s Shot object that collision occurred with
	 */
	public void on_shot(Shot s);

	/**
	 * Called on collision with obstacle
	 * @param r Rectangle that collision occurred with
	 */
	public void on_obstacle(Rectangle r);
	
	/**
	 * Called by engine to determine whether this actor should
	 * be removed from further processing (ie. death)
	 */
	public boolean wants_removal();
	
	/**
	 * Called by engine upon receiving damage
	 */
	public void on_damage(double dmg);
	
}
