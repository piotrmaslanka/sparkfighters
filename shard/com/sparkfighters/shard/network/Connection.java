package com.sparkfighters.shard.network;

import java.net.SocketAddress;
import java.util.Random;
import java.util.Vector;

import com.sparkfighters.shard.loader.JSONUserDTO;

import pl.com.henrietta.lnx2.Channel;
import pl.com.henrietta.lnx2.RetransmissionMode;

/**
 * Connection with it's own metadata. This is what Network layer
 * knows about a connected user.
 * @author Henrietta
 *
 */
public class Connection extends pl.com.henrietta.lnx2.Connection {

	/**
	 * Player ID is fundamentally the same as logical actor ID in shared.
	 * ID is assigned by login - from BPF file
	 */
	public SocketAddress address = null;
	public int player_id = -1;
	public String username = null;	// associated username
	/**
	 * 0 - not logged in
	 * 1 - authorized, sent information about map
	 * 2 - acknowledged readiness
	 */
	public int login_phase = 0;
	
	public byte[] challenge_nonce = new byte[20];	// challenge/response nonce
	public JSONUserDTO associated_dto = null;
	
	public int lag_state;	// current ping in miliseconds
	
	/**
	 * Returns a vector of channels to put into Connection constructor. Because, 
	 * well, stupid Java and 'parent constructor as first statement'.
	 */
	private static Vector<Channel> _get_channels_vector() {
		Vector<Channel> channels = new Vector<>();
		channels.add(new Channel((byte)0, RetransmissionMode.RTM_AUTO_ORDERED, (float)10, 60));
		channels.add(new Channel((byte)1, RetransmissionMode.RTM_MANUAL, (float)5, 1));
		channels.add(new Channel((byte)2, RetransmissionMode.RTM_MANUAL, (float)5, 1));
		channels.add(new Channel((byte)3, RetransmissionMode.RTM_AUTO_ORDERED, (float)10, 60));
		return channels;
	}
	
	
	public Connection() {
		// Init parent		
		super(Connection._get_channels_vector(), (float)15);
		
		// Generate nonce
		Random rand = new Random(System.currentTimeMillis());
		rand.nextBytes(this.challenge_nonce);
		
	}

}
