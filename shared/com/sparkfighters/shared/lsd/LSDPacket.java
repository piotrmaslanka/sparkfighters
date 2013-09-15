package com.sparkfighters.shared.lsd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;

import com.sparkfighters.shared.lsd.fragments.*;

/**
 * This class represents a single LSD packet that is sent and received
 * from the network. It has methods to serialize and unserialize the packet
 * from data received from network.
 * @author Henrietta
 *
 */
public class LSDPacket {
	/**
	 * Iteration this LSD packet refers to beginning
	 */
	public int iteration;

	public Vector<LSDFragment> fragments = new Vector<>();

	public LSDPacket(int iteration) {
		this.iteration = iteration;
	}

	/**
	 * Outputs the packet to a buffer
	 * @param bs Buffer to output to
	 * @throws IOException
	 */
	public void toStream(ByteArrayOutputStream bs) throws IOException {
		DataOutputStream ds = new DataOutputStream(bs);
		ds.writeInt(this.iteration);
		for (LSDFragment frag : this.fragments) {
			ds.writeByte(frag.getId());
			frag.toStream(bs);
		}		
	}
	
	public void fromStream(ByteArrayInputStream bs) throws IOException {
		DataInputStream ds = new DataInputStream(bs);
		this.iteration = ds.readInt();
		
		while (true) {
			LSDFragment frag = null;
			
			int x;
			try {
				x = ds.readByte();
			} catch (EOFException e) {
				break;
			}
			
			switch (x) {		// procure proper datatype
				case 0:
					frag = new CharacterUnspawned();
					break;
				case 1:
					frag = new CharacterSpawned();
					break;
				case 2:
					frag = new CharacterInputUpdate();
					break;
				default:
					break;
			}
			
			try {							// read from the stream
				frag.fromStream(bs);
			} catch (EOFException e) {
				break;
			}
			
			this.fragments.add(frag);		// add to vector
		}		
	}

}
