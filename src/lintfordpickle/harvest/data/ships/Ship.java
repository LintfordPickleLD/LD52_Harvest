package lintfordpickle.harvest.data.ships;

import lintfordpickle.harvest.data.CollisionTypes;
import lintfordpickle.harvest.renderers.trails.TrailRendererComponent;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.collisions.RigidBody;
import net.lintford.library.core.collisions.RigidBodyEntity;
import net.lintford.library.core.maths.Vector2f;

public class Ship extends RigidBodyEntity {

	public class Cargo {

		// ---------------------------------------------
		// Constants
		// ---------------------------------------------

		public static final int TOTAL_CARGO_SPACE = 3;

		// ---------------------------------------------
		// Variables
		// ---------------------------------------------

		public int freeSpace;

		public int waterAmt;
		public int wheatAmt;

		// ---------------------------------------------
		// Constructor
		// ---------------------------------------------

		public Cargo() {
			freeSpace = TOTAL_CARGO_SPACE;
			waterAmt = 0;
			wheatAmt = 0;
		}
	}

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

	public float velocityMagnitude;

	public final ShipInput inputs = new ShipInput();
	public final Vector2f rearEngine = new Vector2f();
	public final Vector2f frontEngine = new Vector2f();

	public float engineColorR = 0.11f;
	public float engineColorG = 0.2f;
	public float engineColorB = 0.74f;

	public final TrailRendererComponent mRearTrailRendererComponent = new TrailRendererComponent();
	public final TrailRendererComponent mFrontTrailRendererComponent = new TrailRendererComponent();

	public final Cargo cargo = new Cargo();

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Ship(int entityUid) {
		super(entityUid, CollisionTypes.COLLISION_TYPE_SHIP);

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final float lDensity = 2.f;

		// these are in meters
		body = RigidBody.createPolygonBody(64.f * lPixelsToUnits, 32.f * lPixelsToUnits, lDensity, .1f, .8f, .5f, false);

	}

	public void update(LintfordCore core) {

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final float s = (float) Math.sin(body.angle);
		final float c = (float) Math.cos(body.angle);

		final float axleLengthHalf = 24f * lPixelsToUnits;
		final float axleHeightHalf = 16f * lPixelsToUnits;

		final float lRearX = -axleLengthHalf * c - axleHeightHalf * s;
		final float lRearY = -axleLengthHalf * s + axleHeightHalf * c;

		final float lFrontX = axleLengthHalf * c - axleHeightHalf * s;
		final float lFrontY = axleLengthHalf * s + axleHeightHalf * c;

		rearEngine.set(body.x + lRearX, body.y + lRearY);
		frontEngine.set(body.x + lFrontX, body.y + lFrontY);

		updateEngineTrails(core);

		velocityMagnitude = body.vx * body.vx + body.vy * body.vy;
	}

	private void updateEngineTrails(LintfordCore core) {

		final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();
		final float rearWorldX = rearEngine.x * lUnitsToPixels;
		final float rearWorldY = rearEngine.y * lUnitsToPixels;
		final float frontWorldX = frontEngine.x * lUnitsToPixels;
		final float frontWorldY = frontEngine.y * lUnitsToPixels;

		mFrontTrailRendererComponent.color(engineColorR, engineColorG, engineColorB, .5f);
		mFrontTrailRendererComponent.updateTrail(core, rearWorldX, rearWorldY, body().angle);

		mRearTrailRendererComponent.color(engineColorR, engineColorG, engineColorB, .5f);
		mRearTrailRendererComponent.updateTrail(core, frontWorldX, frontWorldY, body().angle);
	}

}
