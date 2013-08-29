package com.sparkfighters.shared.lsd;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sparkfighters.shared.physics.objects.Vector;

/**
 * Class that can be used to serialize particular data
 * types to DataOutputStreams
 * @author Henrietta
 *
 */
public class LSDSerializer extends DataOutputStream {

	public LSDSerializer(ByteArrayOutputStream os) {
		super(os);
	}
	
	public void writeVector(Vector v) throws IOException {
		this.writeFloat((float)v.x);
		this.writeFloat((float)v.y);
	}

}
