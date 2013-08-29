package com.sparkfighters.shard.network.bridge.net;

import com.sparkfighters.shard.network.bridge.NetworkToExecutor;

/**
 * Means that client has sent a new keyboard/mouse input status
 * @author Henrietta
 */
public class InputStatusChanged extends NetworkToExecutor {

	public long mouse_x;
	public long mouse_y;
	public boolean kbd_up;
	public boolean kbd_left;
	public boolean kbd_down;
	public boolean kbd_right;
	public boolean mouse_lmb;
	public boolean mouse_rmb;
	
	public InputStatusChanged(int player_id, long mouse_x, long mouse_y, 
											 boolean kbd_up, boolean kbd_right,
											 boolean kbd_down, boolean kbd_left,
											 boolean mouse_lmb, boolean mouse_rmb)
	{
		super(player_id);

		this.mouse_x = mouse_x;
		this.mouse_y = mouse_y;
		
		this.kbd_up = kbd_up;
		this.kbd_down = kbd_down;
		this.kbd_left = kbd_left;
		this.kbd_right = kbd_right;
		
		this.mouse_lmb = mouse_lmb;
		this.mouse_rmb = mouse_rmb;
	}

}
