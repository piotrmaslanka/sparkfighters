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
	
	public CharacterZeroFragment(int player_id, Vector position, Vector velocity) {
		this.player_id = player_id;
		this.position = position;
		this.velocity = velocity;
	}

	public void toStream(ByteArrayOutputStream bs) throws IOException {
		LSDSerializer ds = new LSDSerializer(bs);
		
		ds.writeShort(this.player_id);
		ds.writeVector(this.position);
		ds.writeVector(this.velocity);

		ds.close();
	}

	public void fromStream(ByteArrayInputStream bs) throws IOException {
		LSDUnserializer ds = new LSDUnserializer(bs);
		
		this.player_id = ds.readShort();
		this.position = ds.readVector();
		this.velocity = ds.readVector();
		
		ds.close();
	}

}
