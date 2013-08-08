package com.sparkfighters.shared.world;

/**
 * Class symbolizing a single team
 * @author Henrietta
 *
 */
public class Team {

	/**
	 * Team ID [TID]
	 */
	public int id;
	public Actor[] actors;
	
	/**
	 * @param id Team ID [TID]
	 * @param actors Actors in this team. Borrows reference.
	 */
	public Team(int id, Actor[] actors) {
		this.id = id;
		this.actors = actors;
	}

}
