package com.sparkfighters.shard.executor.synchronizer;

import com.sparkfighters.shared.lsd.fragments.CharacterInputUpdate;

public class InputStatusChanged implements SyncUnit {

	public int iteration;
	public int player_id;

	public boolean kbd_up;
	public boolean kbd_left;
	public boolean kbd_down;
	public boolean kbd_right;
	public short angle;

	/**
	 * Direct construction for parameters 
	 */
	public InputStatusChanged(int player_id, int iteration,  
							  boolean kbd_up, boolean kbd_left, boolean kbd_down, boolean kbd_right,
							  short angle) {
		this.iteration = iteration;
		this.player_id = player_id;
		this.kbd_up = kbd_up;
		this.kbd_left = kbd_left;
		this.kbd_down = kbd_down;
		this.kbd_right = kbd_right;
		this.angle = angle;
	}

	public int getIteration() { return this.iteration; }
	public int getChannelAffiliation() { return 5; }

	// Helper functions, written for my convenience
	
	@Override
	public int hashCode() {
		return this.iteration ^ this.player_id ^ this.angle;
	}
	
	/**
	 * Checks whether contents (ie. raw information about controls) is the same as
	 * another objects
	 * @param isc Another InputStatusChanged to check
	 * @return whether control information is the same
	 */
	public boolean controlEquals(InputStatusChanged isc) {
		return (isc.kbd_up == this.kbd_up) && 
			   (isc.kbd_left == this.kbd_left) && 
			   (isc.kbd_right == this.kbd_right) &&
			   (isc.kbd_down == this.kbd_down) &&
			   (isc.angle == this.angle);
	}
	
	/**
	 * Converts this message to a LSD fragment
	 */
	public CharacterInputUpdate asLSD() {
		CharacterInputUpdate ciu = new CharacterInputUpdate(this.player_id, this.kbd_up, this.kbd_left, 
															this.kbd_down, this.kbd_right, this.angle);
		return ciu;
	}
}
