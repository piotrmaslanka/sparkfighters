package com.sparkfighters.shard.network;

import java.util.Random;
import java.util.Vector;

import pl.com.henrietta.lnx2.Channel;
import pl.com.henrietta.lnx2.RetransmissionMode;

/**
 * Connection with it's own metadata
 * @author Henrietta
 *
 */
public class Connection extends pl.com.henrietta.lnx2.Connection {

	/**
	 * Player ID is fundamentally the same as logical actor ID in shared.
	 * ID is assigned by login - from BPF file
	 */
	public int player_id = -1;
	public String username = null;	// associated username
	public boolean is_logged_in = false;	// whether login went successfully
	
	private byte[] challenge = new byte[20];	// challenge/response nonce
	
	/**
	 * Returns a vector of channels to put into Connection constructor. Because, 
	 * well, stupid Java and 'parent constructor as first statement'.
	 */
	private static Vector<Channel> _get_channels_vector() {
		Channel chan_0 = new Channel((byte)0, RetransmissionMode.RTM_AUTO_ORDERED,
				(float)10, 60);

		Vector<Channel> channels = new Vector<>();
		channels.add(chan_0);
		return channels;
	}
	
	
	public Connection() {
		// Init parent		
		super(Connection._get_channels_vector(), (float)15);
		
		// Generate nonce
		Random rand = new Random(System.currentTimeMillis());
		rand.nextBytes(this.challenge);
		
	}

}
