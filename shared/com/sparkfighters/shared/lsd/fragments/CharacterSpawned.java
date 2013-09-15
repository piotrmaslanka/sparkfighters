package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.lsd.LSDSerializer;
import com.sparkfighters.shared.lsd.LSDUnserializer;
import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Character has been spawned because:
 * 1. Game has started
 * 2. User got connected
 * 3. Team got respawned
 * @author Henrietta
 *
 */
public class CharacterSpawned implements LSDFragment {

	public int actor_id;
	public Vector position;
	
	public CharacterSpawned() {}

	public CharacterSpawned(int actor_id, Vector position) {
		this.actor_id = actor_id;
		this.position = position;
	}	
	
	public void toStream(ByteArrayOutputStream bs) throws IOException {
		LSDSerializer ds = new LSDSerializer(bs);
		
		ds.writeShort(this.actor_id);
		ds.writeInt((int)this.position.x);
		ds.writeInt((int)this.position.y);
		
		ds.close();
	}

	public void fromStream(ByteArrayInputStream bs) throws IOException {
		LSDUnserializer ds = new LSDUnserializer(bs);

		this.actor_id = ds.readShort();
		int x = ds.readInt();
		int y = ds.readInt();
		this.position = new Vector(x, y);
		
		ds.close();
	}

	public int getId() {
		return 1;
	}

}
