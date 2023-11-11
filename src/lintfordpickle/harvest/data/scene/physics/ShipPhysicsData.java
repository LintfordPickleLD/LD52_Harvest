package lintfordpickle.harvest.data.scene.physics;

import net.lintfordlib.core.entities.Entity;

public class ShipPhysicsData extends Entity {

	public ShipPhysicsData(int uid) {
		super(uid);

	}

	public int shipEntityUid;

	public int lastCollisionObjectHash;
	public boolean lastCollisionHandled;

	public float lastCollisionMagnitude2;
	public float lastCollisionNormalX;
	public float lastCollisionNormalY;

	public float lastCollisionContactX;
	public float lastCollisionContactY;
}
