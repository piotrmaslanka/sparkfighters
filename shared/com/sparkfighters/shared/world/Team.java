package com.sparkfighters.shared.world;

/**
 * Class symbolizing a single team.
 * 
 * This class has only a limited capability to clone itself. It does not clone it's 
 * actors field - only empty array of the same size will be initialized
 * 
 * @author Henrietta
 *
 */
public class Team implements Cloneable {

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

	public Team clone() {
		return new Team(this.id, new Actor[this.actors.length]);
	}
}
