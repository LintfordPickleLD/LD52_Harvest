package lintfordpickle.harvest.data.scene.physics;

import lintfordpickle.harvest.data.GridCollisionTypes;
import lintfordpickle.harvest.data.scene.collisions.GridEntityType;
import net.lintfordlib.ConstantsPhysics;
import net.lintfordlib.core.maths.Vector2f;
import net.lintfordlib.core.physics.dynamics.RigidBody;
import net.lintfordlib.core.physics.dynamics.RigidBodyEntity;
import net.lintfordlib.core.physics.shapes.PolygonShape;

public class ScenePhysicsObjectInstance extends RigidBodyEntity {

	// --------------------------------------
	// Variables
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ScenePhysicsObjectInstance(int uid) {
		super(uid, GridCollisionTypes.COLLISION_TYPE_CARGO);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialize(float worldCenterX, float worldCenterY, float rotation) {
		final var lDensity = 5.0f;
		final var lRestitution = .5f;
		final var lStaticFriction = .8f;
		final var lDynamicFriction = .3f;

		final var unitCenterX = ConstantsPhysics.toUnits(worldCenterX);
		final var unitCenterY = ConstantsPhysics.toUnits(worldCenterY);

		body = new RigidBody(true);
		body.addShape(PolygonShape.createEmptyPolygonShape(lDensity, lRestitution, lStaticFriction, lDynamicFriction));
		body.moveTo(unitCenterX, unitCenterY);
		body.angle(rotation);

		body.userData("floor");

		body.categoryBits(GridEntityType.GRID_ENTITY_TYPE_PHYSICS_OBJECTS);
		body.maskBits(GridEntityType.GRID_ENTITY_TYPE_NONE);

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void setPolygonVertices(Vector2f newA, Vector2f newB, Vector2f newC, Vector2f newD) {
		body.shape().setLocalVertices(newA, newB, newC, newD);

	}

}
