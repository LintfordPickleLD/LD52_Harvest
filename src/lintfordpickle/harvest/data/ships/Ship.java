package lintfordpickle.harvest.data.ships;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.GridCollisionTypes;
import lintfordpickle.harvest.data.cargo.Cargo;
import lintfordpickle.harvest.data.cargo.CargoType;
import lintfordpickle.harvest.data.input.ShipInput;
import lintfordpickle.harvest.data.physics.ShipPhysicsData;
import lintfordpickle.harvest.renderers.trails.TrailRendererComponent;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.graphics.sprites.SpriteInstance;
import net.lintford.library.core.maths.Vector2f;
import net.lintford.library.core.physics.dynamics.RigidBody;
import net.lintford.library.core.physics.dynamics.RigidBodyEntity;

public class Ship extends RigidBodyEntity {

	public class Inventory {

		// ---------------------------------------------
		// Constants
		// ---------------------------------------------

		public static final int TOTAL_CARGO_SPACE = 3;

		// ---------------------------------------------
		// Variables
		// ---------------------------------------------

		private final List<Cargo> mCargo = new ArrayList<>();

		// ---------------------------------------------
		// Properties
		// ---------------------------------------------

		public Cargo getCargoByIndex(int index) {
			return mCargo.get(index);
		}

		public boolean hasFreeSpace() {
			return mCargo.size() < TOTAL_CARGO_SPACE;
		}

		public int freeSpace() {
			return TOTAL_CARGO_SPACE - mCargo.size();
		}

		public Cargo removeCargoOfType(CargoType cargoType) {
			final var lNumCargo = mCargo.size();
			for (int i = 0; i < lNumCargo; i++) {
				if (mCargo.get(i).cargoType == cargoType)
					return mCargo.remove(i);
			}
			return null;
		}

		// ---------------------------------------------
		// Constructor
		// ---------------------------------------------

		public Inventory() {
		}

		// ---------------------------------------------
		// Methods
		// ---------------------------------------------

		public void addCargo(Cargo newCargo) {
			if (hasFreeSpace() == false)
				return;

			if (mCargo.contains(newCargo))
				return;

			mCargo.add(newCargo);
		}

		public Cargo removeCargo(CargoType type) {
			final var lNumCargo = mCargo.size();
			for (int i = 0; i < lNumCargo; i++) {
				if (mCargo.get(i).cargoType == type) {
					return mCargo.remove(i);
				}
			}

			return null;
		}
	}

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

	public int owningPlayerSessionUid;

	// ghost ships are used in time-trial mode to show the relative state of a previous playthrough
	public boolean isGhostShip;

	public float engineColorR = 0.11f;
	public float engineColorG = 0.2f;
	public float engineColorB = 0.74f;

	public final TrailRendererComponent mRearTrailRendererComponent = new TrailRendererComponent();
	public final TrailRendererComponent mFrontTrailRendererComponent = new TrailRendererComponent();

	public final Inventory cargo = new Inventory();
	public final int maxHealth = 100;
	public int health;

	public SpriteInstance leftEngineSprite;
	public SpriteInstance rightEngineSprite;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean isDead() {
		return health <= 0;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Ship(int entityUid) {
		super(entityUid, GridCollisionTypes.COLLISION_TYPE_SHIP);

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final float lDensity = 2.f;

		// these are in meters
		body = RigidBody.createPolygonBody(64.f * lPixelsToUnits, 32.f * lPixelsToUnits, lDensity, .1f, .8f, .5f, false);
		body.userData(new ShipPhysicsData(entityUid));

		health = maxHealth;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void update(LintfordCore core) {

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final float s = (float) Math.sin(body.angle);
		final float c = (float) Math.cos(body.angle);

		final float axleLengthHalf = 24f * lPixelsToUnits;
		final float leftThrottleAmt = (inputs.isRightThrottle || inputs.isUpThrottle) ? 1.f : 0.f;
		final float rightThrottleAmt = (inputs.isLeftThrottle || inputs.isUpThrottle) ? 1.f : 0.f;
		final float axleHeightHalfL = (16f * leftThrottleAmt) * lPixelsToUnits;
		final float axleHeightHalfR = (16f * rightThrottleAmt) * lPixelsToUnits;

		final float lRearX = -axleLengthHalf * c - axleHeightHalfL * s;
		final float lRearY = -axleLengthHalf * s + axleHeightHalfL * c;

		final float lFrontX = axleLengthHalf * c - axleHeightHalfR * s;
		final float lFrontY = axleLengthHalf * s + axleHeightHalfR * c;

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

	public void applyDamage(int amt) {
		health -= amt;
	}

}
