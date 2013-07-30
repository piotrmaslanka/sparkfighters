package com.sparkfighters.shared.misc;

import java.util.BitSet;

/**
 * A class that can issue an ID.
 * This could be much faster, but I'll improve it
 * if it warrants an improvement.
 * PRIVATE
 * @author Henrietta
 *
 */
public class IDIssuer {
	private BitSet bits = new BitSet(65536);
	
	public IDIssuer() {}
	
	public int get() {
		for (int i=0; i<65536; i++)
			if (!this.bits.get(i)) {
				this.bits.set(i, true);
				return i;
			}
		
		throw new RuntimeException("Out of bits");
	}
	
	public void release(int i) {
		this.bits.set(i, false);
	}
}
