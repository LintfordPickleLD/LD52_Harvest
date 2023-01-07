package lintfordpickle.harvest.data;

import net.lintford.library.core.collisions.RigidBody;
import net.lintford.library.core.collisions.RigidBodyEntity;

public class Ship extends RigidBodyEntity {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final long serialVersionUID = 1971639550782581754L;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	public int tiltLevel;
	public float tiltAmount;
	public boolean isPlayerControlled;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Ship(int entityUid) {
		super(entityUid, CollisionTypes.COLLISION_TYPE_SHIP);
		
		body = RigidBody.createCircleBody(15.f, 5.f, .5f, .6f, .4f, false);
	}

}
