package com.sparkfighters.shared.lsd;

import java.io.DataOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Class that can be used to serialize particular data
 * types to DataOutputStreams
 * @author Henrietta
 *
 */
public class Serializer {

	private DataOutputStream dos;
	
	public Serializer(DataOutputStream dos) {
		this.dos = dos;
	}
	
	public Serializer serialize(Vector v) throws IOException {
		this.dos.writeFloat((float)v.x);
		this.dos.writeFloat((float)v.y);
		return this;
	}

}
