package lintfordpickle.harvest.data.scene.physics;

import lintfordpickle.harvest.data.GridCollisionTypes;
import net.lintfordlib.ConstantsPhysics;
import net.lintfordlib.core.maths.Vector2f;
import net.lintfordlib.core.physics.dynamics.RigidBody;
import net.lintfordlib.core.physics.dynamics.RigidBodyEntity;
import net.lintfordlib.core.physics.shapes.PolygonShape;

public class ScenePhysicsobjectInstance extends RigidBodyEntity {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final Vector2f a = new Vector2f();
	public final Vector2f b = new Vector2f();
	public final Vector2f c = new Vector2f();
	public final Vector2f d = new Vector2f();

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ScenePhysicsobjectInstance(int uid) {
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

//		body.categoryBits(GridEntityType.GRID_ENTITY_TYPE_FLOOR);
//		body.maskBits(GridEntityType.GRID_ENTITY_TYPE_NONE);

	}

	// --------------------------------------
	// Methods
	// --------------------------------------
}
