package lintfordpickle.harvest.controllers;

import net.lintford.library.core.physics.collisions.ContactManifold;
import net.lintford.library.core.physics.interfaces.ICollisionCallback;

public class PhysicsCollisionCallback implements ICollisionCallback {

	boolean firstRecorded = false;

	public PhysicsCollisionCallback() {

	}

	@Override
	public void preContact(ContactManifold manifold) {
		if (!firstRecorded) {
			System.out.println("-------------------");
			System.out.println("First contact record:");
			System.out.println(manifold.depth);
			firstRecorded = true;
		}

	}

	@Override
	public void postContact(ContactManifold manifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(ContactManifold manifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(ContactManifold manifold) {
		// TODO Auto-generated method stub

	}

}
