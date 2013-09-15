package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.lsd.LSDSerializer;
import com.sparkfighters.shared.lsd.LSDUnserializer;

/**
 * An information that input of given character has been changed
 * @author Henrietta
 */
public class CharacterInputUpdate implements LSDFragment {

	public int player_id;
	public boolean kbd_up;
	public boolean kbd_left;
	public boolean kbd_down;
	public boolean kbd_right;
	/**
	 * Angle is counted counterclockwise in degrees. X axis points to zero degrees.
	 */
	public int angle;	// 0 to 360	
	
	public CharacterInputUpdate(int player_id, 
								boolean kbd_up, boolean kbd_left, boolean kbd_down, boolean kbd_right, int angle) {
		this.player_id = player_id;
		this.kbd_up = kbd_up;
		this.kbd_left = kbd_left;
		this.kbd_down = kbd_down;
		this.kbd_right = kbd_right;
		this.angle = angle;
	}
	
	public CharacterInputUpdate() {}

	public void toStream(ByteArrayOutputStream bs) throws IOException {
		LSDSerializer ls = new LSDSerializer(bs);
		
		int kbd = (kbd_up ? 1 : 0) + (kbd_left ? 2 : 0) + (kbd_down ? 4 : 0) + (kbd_right ? 8 : 0);
		ls.writeShort(this.player_id);
		ls.writeByte(kbd);		
		ls.writeShort(this.angle);
		
		ls.close();
	}

	public void fromStream(ByteArrayInputStream bs) throws IOException {
		LSDUnserializer ls = new LSDUnserializer(bs);
		
		this.player_id = ls.readShort();
		int kbd = ls.readByte();
		this.kbd_up = (kbd & 1) > 0;
		this.kbd_left = (kbd & 2) > 0;
		this.kbd_down = (kbd & 4) > 0;
		this.kbd_right = (kbd & 8) > 0;
		this.angle = ls.readShort();
		
		ls.close();
	}

	public int getId() { return 2; }

}
