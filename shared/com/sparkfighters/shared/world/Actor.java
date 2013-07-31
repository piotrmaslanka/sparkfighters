package com.sparkfighters.shared.world;

import com.sparkfighters.shared.physics.gwobjects.PhysicActor;
import com.sparkfighters.shared.blueprints.ActorBlueprint;
import com.sparkfighters.shared.physics.objects.*;

/**
 * This represents an actor. Logical actor, his HP, affiliations and so on.
 * 
 * This cannot clone itself exhaustively and must be helped by outside.
 * 
 * This does not represent a physical actor in the game world. This is only
 * a coordinator class for a single actor - such as players, or the Spark
 * 
 * @author Henrietta
 *
 */
public class Actor implements Cloneable {
	// ----------------------------       core options
	/**
	 * Handle to a physical actor allocated by this actor. This is functionally a 'weak'
	 * reference, the primary responsibility path is .shared.physics.world.World.
	 */
	public PhysicActor physical;
	public ActorBlueprint actor_blueprint;
	public int id;
	
	/**
	 * Return current physic actor representing this character.
	 * 
	 * This may be subject to change, for example if there is a "clone" spell
	 * then a single Actor may allocate more than one physic actor.
	 * 
	 * @return Actor or null if not present (ie. dead)
	 */
	public PhysicActor get_physical_actor() { return this.physical;	}
	
	
	// ----------------------------      controller options
	// Everything here is PRIVATE. Use Controller if you wish to read this data
	private Controller _cached_controller;
	public Vector _mouse_position;
	public boolean _kbd_up = false;
	public boolean _kbd_left = false;
	public boolean _kbd_right = false;
	public boolean _kbd_down = false;
	
	/**
	 * Returns a controller for this actor.
	 * 
	 * This function is cheap to call.
	 * 
	 * @return Controller instance.
	 */
	public Controller controller() {
		if (this._cached_controller == null)
			this._cached_controller = new Controller(this);
		return this._cached_controller;
	}
	
	/**
	 * Called by World, to process keyboard input. PhysicActor
	 * has info about colliding with environment available by now.
	 */
	public void process_controller_input() {
		if (this.physical == null) return;
		
		if (this._kbd_up)
			if (this.physical.get_v_braked())
				this.physical.set_velocity(this.physical.get_velocity().force_y(
						this.actor_blueprint.jumpSpeed));
		
		if (this._kbd_left)
			this.physical.set_velocity(this.physical.get_velocity().force_x(
					-this.actor_blueprint.runSpeed));

		if (this._kbd_right)
			this.physical.set_velocity(this.physical.get_velocity().force_x(
					this.actor_blueprint.runSpeed));
		
		this.physical.set_collides_platforms(!this._kbd_down);
	}
	
	public Actor(int id, ActorBlueprint abp) {
		this.id = id;
		this.actor_blueprint = abp;
	}

	/**
	 * This will not clone .physical
	 */
	public Actor clone() {
		Actor nac = new Actor(this.id, this.actor_blueprint.clone());
		nac._cached_controller = null;
		return nac;
	}
	
}
