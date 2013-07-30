package pl.com.henrietta.lnx2;

import pl.com.henrietta.lnx2.exceptions.PacketMalformedError;
import java.lang.System;
import java.util.Arrays;


/**
 * A LNX2 packet.
 * @author Henrietta
 */
public class Packet {
	public byte channel_id;
	public byte window_id;
	public byte[] data;
	public boolean is_ack = false;
	
	/**
	 * @param data Data in packet. Reference borrowed by this class.
	 * It won't be mutated unless you do that from outside.
	 * @param channel_id Channel ID
	 * @param window_id Window ID
	 */
	public Packet(byte[] data, byte channel_id, byte window_id) {
		this.data = data;
		this.channel_id = channel_id;
		this.window_id = window_id;		
	}
	
	/**
	 * Creates a packet that represents an ACKnowledge
	 * @param channel_id Channel ID
	 * @param window_id Window ID
	 */
	public static Packet create_ack(byte channel_id, byte window_id) {
		Packet pck = new Packet(new byte[0], channel_id, window_id);
		pck.is_ack = true;
		return pck;
	}
	
	/**
	 * Converts this packet to bytes, ready to send over network
	 * @return packet in binary format, ready to send
	 */
	public byte[] to_bytes() {
		byte[] buf = new byte[this.data.length + 2];
		buf[0] = this.window_id;
		if (this.is_ack) buf[0] += 128;
		buf[1] = this.channel_id;
		System.arraycopy(this.data, 0, buf, 2, this.data.length);
		return buf;
	}
	
	/**
	 * Compares packet with another packet
	 */
	public boolean equals(Packet p) {
		return (p.window_id == this.window_id) &&
			   (p.channel_id == this.channel_id) &&
			   (Arrays.equals(p.data, this.data) &&
		       (p.is_ack == this.is_ack));
	}
	
	/**
	 * Creates a packet from byte buffer.
	 * 
	 * @param data Buffer with data. Not borrowed.
	 */
	public static Packet from_bytes(byte[] data) throws PacketMalformedError {
		if (data.length < 2) throw new PacketMalformedError();
		
		byte window_id = (byte)(data[0] & 63);
		byte channel_id = data[1];
		
		if ((data[0] & 128) > 0) {	// this is an ACKnowledge packet
			if (data.length != 2) throw new PacketMalformedError();
			return Packet.create_ack(channel_id, window_id);
		}
		if ((data[0] & 64) > 0) throw new PacketMalformedError();
		byte[] buf = Arrays.copyOfRange(data, 2, data.length);
		return new Packet(buf, channel_id, window_id);
	}
}
