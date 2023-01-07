package lintfordpickle.harvest.data;

import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.collisions.RigidBody;
import net.lintford.library.core.collisions.RigidBodyEntity;
import net.lintford.library.core.maths.Vector2f;

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

	public final ShipInput inputs = new ShipInput();
	public final Vector2f rearEngine = new Vector2f();
	public final Vector2f frontEngine = new Vector2f();

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Ship(int entityUid) {
		super(entityUid, CollisionTypes.COLLISION_TYPE_SHIP);

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		// these are in meters
		body = RigidBody.createPolygonBody(32.f * lPixelsToUnits, 16.f * lPixelsToUnits, 4.f, .1f, .8f, .5f, false);

	}

	public void update(LintfordCore core) {

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final float s = (float) Math.sin(body.angle);
		final float c = (float) Math.cos(body.angle);

		final float axleLengthHalf = 12f * lPixelsToUnits;
		final float axleHeightHalf = 8f * lPixelsToUnits;

		final float lRearX = -axleLengthHalf * c - axleHeightHalf * s;
		final float lRearY = -axleLengthHalf * s + axleHeightHalf * c;

		final float lFrontX = axleLengthHalf * c - axleHeightHalf * s;
		final float lFrontY = axleLengthHalf * s + axleHeightHalf * c;

		rearEngine.set(body.x + lRearX, body.y + lRearY);
		frontEngine.set(body.x + lFrontX, body.y + lFrontY);

	}

}
