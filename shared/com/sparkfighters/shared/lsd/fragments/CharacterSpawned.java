package com.sparkfighters.shared.lsd.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.lsd.LSDSerializer;
import com.sparkfighters.shared.lsd.LSDUnserializer;
import com.sparkfighters.shared.physics.objects.Vector;

public class CharacterSpawned implements LSDFragment {

	int player_id;
	Vector position;
	
	public CharacterSpawned() {}

	public CharacterSpawned(int player_id, Vector position) {
		this.player_id = player_id;
		this.position = position;
	}	
	
	public void toStream(ByteArrayOutputStream bs) throws IOException {
		LSDSerializer ds = new LSDSerializer(bs);
		
		ds.writeShort(this.player_id);
		ds.writeInt((int)this.position.x);
		ds.writeInt((int)this.position.y);
		
		ds.close();
	}

	public void fromStream(ByteArrayInputStream bs) throws IOException {
		LSDUnserializer ds = new LSDUnserializer(bs);

		this.player_id = ds.readShort();
		int x = ds.readInt();
		int y = ds.readInt();
		this.position = new Vector(x, y);
		
		ds.close();
	}

	public int getId() {
		return 1;
	}

}
