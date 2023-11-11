package lintfordpickle.harvest.data;

import lintfordpickle.harvest.data.scene.physics.ShipPhysicsData;
import net.lintfordlib.core.physics.collisions.ContactManifold;
import net.lintfordlib.core.physics.interfaces.ICollisionCallback;

public class CollisionHandler implements ICollisionCallback {

	@Override
	public void preContact(ContactManifold manifold) {
		if (manifold.bodyA.userData() instanceof ShipPhysicsData) {
			final var lUserData = (ShipPhysicsData) manifold.bodyA.userData();

			if (lUserData.lastCollisionHandled && lUserData.lastCollisionObjectHash != manifold.bodyB.hashCode()) {
				lUserData.lastCollisionMagnitude2 = manifold.bodyA.vx * manifold.bodyA.vx + manifold.bodyA.vy * manifold.bodyA.vy;
			}
		}

		if (manifold.bodyB.userData() instanceof ShipPhysicsData) {
			final var lUserData = (ShipPhysicsData) manifold.bodyB.userData();

			if (lUserData.lastCollisionHandled && lUserData.lastCollisionObjectHash != manifold.bodyA.hashCode()) {
				lUserData.lastCollisionMagnitude2 = manifold.bodyB.vx * manifold.bodyB.vx + manifold.bodyB.vy * manifold.bodyB.vy;
			}
		}
	}

	@Override
	public void postContact(ContactManifold manifold) {

	}

	@Override
	public void preSolve(ContactManifold manifold) {
		if (manifold.bodyA.userData() instanceof ShipPhysicsData) {
			final var lUserData = (ShipPhysicsData) manifold.bodyA.userData();

			if (lUserData.lastCollisionHandled && lUserData.lastCollisionObjectHash != manifold.bodyB.hashCode()) {
				lUserData.lastCollisionHandled = false;

				lUserData.lastCollisionObjectHash = manifold.bodyB.hashCode();

				lUserData.lastCollisionNormalX = manifold.normal.x;
				lUserData.lastCollisionNormalY = manifold.normal.y;

				lUserData.lastCollisionContactX = manifold.contact1.x;
				lUserData.lastCollisionContactY = manifold.contact1.y;
			}
		}

		if (manifold.bodyB.userData() instanceof ShipPhysicsData) {
			final var lUserData = (ShipPhysicsData) manifold.bodyB.userData();

			if (lUserData.lastCollisionHandled && lUserData.lastCollisionObjectHash != manifold.bodyA.hashCode()) {
				lUserData.lastCollisionHandled = false;

				lUserData.lastCollisionObjectHash = manifold.bodyA.hashCode();

				lUserData.lastCollisionNormalX = manifold.normal.x;
				lUserData.lastCollisionNormalY = manifold.normal.y;

				lUserData.lastCollisionContactX = manifold.contact1.x;
				lUserData.lastCollisionContactY = manifold.contact1.y;
			}
		}
	}

	@Override
	public void postSolve(ContactManifold manifold) {

		// notify controllers of new state information, if they are registered ??
		// example: ship collided with dynamic object, and we are saving a time-ghost.

	}

}
