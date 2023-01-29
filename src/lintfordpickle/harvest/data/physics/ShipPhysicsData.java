package lintfordpickle.harvest.data.physics;

import net.lintford.library.core.entity.BaseInstanceData;

public class ShipPhysicsData extends BaseInstanceData {

	private static final long serialVersionUID = 6525771337467313372L;

	public int shipEntityUid;

	public int lastCollisionObjectHash;
	public boolean lastCollisionHandled;

	public float lastCollisionMagnitude2;
	public float lastCollisionNormalX;
	public float lastCollisionNormalY;
}
