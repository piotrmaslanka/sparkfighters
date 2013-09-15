package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.lsd.LSDSerializer;
import com.sparkfighters.shared.lsd.LSDUnserializer;
import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Character has disappeared because underlying player has 
 * disconnected
 * @author Henrietta
 */
public class CharacterUnspawned implements LSDFragment {

	public int actor_id;
	
	public CharacterUnspawned() {}

	public CharacterUnspawned(int actor_id) {
		this.actor_id = actor_id;
	}	
	
	public void toStream(ByteArrayOutputStream bs) throws IOException {
		LSDSerializer ds = new LSDSerializer(bs);
		
		ds.writeShort(this.actor_id);
	
		ds.close();
	}

	public void fromStream(ByteArrayInputStream bs) throws IOException {
		LSDUnserializer ds = new LSDUnserializer(bs);

		this.actor_id = ds.readShort();
		
		ds.close();
	}

	public int getId() {
		return 0;
	}

}
