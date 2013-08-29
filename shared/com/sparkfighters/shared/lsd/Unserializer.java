package com.sparkfighters.shared.lsd;

import java.io.DataInputStream;
import java.io.IOException;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Class that can be used to unserialize particular data
 * types from DataInputStreams
 * @author Henrietta
 *
 */
public class Unserializer {

	private DataInputStream dis;
	
	public Unserializer(DataInputStream dis) {
		this.dis = dis;
	}
	
	public Vector vector() throws IOException {
		float x = this.dis.readFloat();
		float y = this.dis.readFloat();
		
		return new Vector(x, y);
	}

}
