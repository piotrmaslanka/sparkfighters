package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.lsd.LSDSerializer;
import com.sparkfighters.shared.lsd.LSDUnserializer;
import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Zeroth order fragment. Contains information about character.
 * @author Henrietta
 *
 */
public class CharacterZeroFragment implements LSDFragment {

	public int player_id;
	public Vector position;
	public Vector velocity;
	
	public int lookAngle;	// 0 - 360 degrees
	public boolean kbd_up;
	public boolean kbd_left;
	public boolean kbd_right;
	public boolean kbd_down;
	
	public CharacterZeroFragment() {}
	
	public CharacterZeroFragment(int player_id, Vector position, Vector velocity, 
								 int lookAngle, boolean kbd_up, boolean kbd_right, 
								  boolean kbd_down, boolean kbd_left) {
		this.player_id = player_id;
		this.position = position;
		this.velocity = velocity;
		this.lookAngle = lookAngle;
		this.kbd_up = kbd_up;
		this.kbd_left = kbd_left;
		this.kbd_right = kbd_right;
		this.kbd_down = kbd_down;
	}

	public void toStream(ByteArrayOutputStream bs) throws IOException {
		LSDSerializer ds = new LSDSerializer(bs);
		
		ds.writeShort(this.player_id);
		ds.writeVector(this.position);
		ds.writeVector(this.velocity);
		ds.writeShort(this.lookAngle);
		
		int s = (this.kbd_up ? 1 : 0) + (this.kbd_right ? 2 : 0) + (this.kbd_down ? 4 : 0) + (this.kbd_left ? 16 : 0);
		ds.writeByte(s);
				
		ds.close();
	}

	public void fromStream(ByteArrayInputStream bs) throws IOException {
		LSDUnserializer ds = new LSDUnserializer(bs);
		
		this.player_id = ds.readShort();
		this.position = ds.readVector();
		this.velocity = ds.readVector();
		this.lookAngle = ds.readShort();
		
		int keys = ds.readByte();
		
		this.kbd_up = (keys & 1) > 0;
		this.kbd_right = (keys & 2) > 0;
		this.kbd_down = (keys & 4) > 0;
		this.kbd_left = (keys & 8) > 0;
		
		ds.close();
	}

	public int getId() {
		return 0;
	}

}
