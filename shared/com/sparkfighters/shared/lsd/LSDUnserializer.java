package com.sparkfighters.shared.lsd;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Class that can be used to unserialize particular data
 * types from DataInputStreams
 * @author Henrietta
 *
 */
public class LSDUnserializer extends DataInputStream {
	
	public LSDUnserializer(ByteArrayInputStream bs) {
		super(bs);
	}
	
	public Vector readVector() throws IOException {
		float x = this.readFloat();
		float y = this.readFloat();
		
		return new Vector(x, y);
	}

}
