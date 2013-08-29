package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.objects.*;

/**
 * A virtual keyboard/mouse for given player
 * @author Henrietta
 *
 */
public class Controller {
	private Actor actor = null;
	
	/**
	 * PRIVATE. To be invoked by Actor
	 */
	public Controller(Actor a) {
		this.actor = a;
	}
	
	/**
	 * Returns current mouse position.
	 * @return current mouse position. Lends reference.
	 */
	public Vector get_mouse_position() { return this.actor._mouse_position; }

	/**
	 * Sets mouse position
	 * @param v New mouse position. Borrows reference
	 */
	public Controller set_mouse_position(Vector v) {
		this.actor._mouse_position = v;
		return this;
	}
	
	/**
	 * Updates keyboard status
	 * @param up Status of UP arrow pressing
	 * @param right Status of RIGHT arrow pressing
	 * @param down Status of DOWN arrow pressing
	 * @param left Status of LEFT arrow pressing
	 */
	public Controller set_keyboard_status(boolean up, boolean right, 
										  boolean down, boolean left) {
		this.actor._kbd_up = up;
		this.actor._kbd_right = right;
		this.actor._kbd_down = down;
		this.actor._kbd_left = left;
		return this;
	}

	/**
	 * Updates mouse status
	 * @param lmb: Left Mouse Button status
	 * @param rmb: Right Mouse Button status
	 */
	public Controller set_mouse_status(boolean lmb, boolean rmb) {
		return this;
	}
	
}
