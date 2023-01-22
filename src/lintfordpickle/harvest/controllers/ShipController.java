package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.controllers.actionevents.GameActionEventController;
import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.data.ships.ShipManager;
import lintfordpickle.harvest.data.ships.ShipPhysicsData;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.maths.Vector2f;

public class ShipController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Ship Controller";

	public static final int DAMAGE_TOP_THREASHOLD = 2;
	public static final int DAMAGE_BOTTOM_THREASHOLD = 10;

	protected static final int MAX_SHIELDS_COMPONENTS = 40;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameActionEventController mActionEventController;
	private GameStateController mGameStateController;
	private ShipManager mShipManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ShipManager shipManager() {
		return mShipManager;
	}

	public Ship getShipByEntityUid(int entityUid) {
		final var lShips = mShipManager.ships();
		final int lNumShips = lShips.size();
		for (int i = 0; i < lNumShips; i++) {
			if (lShips.get(i).entityUid == entityUid)
				return lShips.get(i);
		}

		return null;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public ShipController(ControllerManager controllerManager, ShipManager shipManager, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mShipManager = shipManager;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupUid());
		mActionEventController = (GameActionEventController) lControllerManager.getControllerByNameRequired(GameActionEventController.CONTROLLER_NAME, entityGroupUid());

	}

	@Override
	public void unloadController() {

	}

	@Override
	public boolean handleInput(LintfordCore core) {
		final var lPlayerShip = mShipManager.playerShip();
		final var lInputFrame = mActionEventController.actionEventPlayer(0);

		lPlayerShip.inputs.isLeftThrottle = lInputFrame.currentActionEvents.isThrottleLeftDown;
		lPlayerShip.inputs.isRightThrottle = lInputFrame.currentActionEvents.isThrottleRightDown;
		lPlayerShip.inputs.isUpThrottle = lInputFrame.currentActionEvents.isThrottleDown;

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lPlayerShip = mShipManager.playerShip();
		updateShip(core, lPlayerShip);

		if (lPlayerShip.isDead()) {
			mGameStateController.setPlayerDied();
		}

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void updateShip(LintfordCore core, Ship ship) {
		ship.update(core);

		if (ship.isDead()) {
			ship.body().angularVelocity *= 0.99f;
			return;
		}

		final var body = ship.body();
		final var lShipInput = ship.inputs;

		final float lThrustUpForce = 100.f;
		final float lAngularTorque = .003f;

		if (lShipInput.isUpThrottle) {
			final float lAngle = body.angle;
			final float upX = (float) Math.cos(lAngle);
			final float upY = (float) Math.sin(lAngle);

			ship.body().accX += -upY * -lThrustUpForce * body.invMass();
			ship.body().accY += upX * -lThrustUpForce * body.invMass();
			body.angularVelocity *= 0.99f;
		}

		{
			if (lShipInput.isLeftThrottle) {
				body.angle -= lAngularTorque * core.gameTime().elapsedTimeMilli();
				if (body.angularVelocity > 0.f)
					body.angularVelocity *= 0.9f;
			}

			if (lShipInput.isRightThrottle) {
				body.angle += lAngularTorque * core.gameTime().elapsedTimeMilli();
				if (body.angularVelocity < 0.f)
					body.angularVelocity *= 0.9f;
			}
		}

		final var lShipUserData = (ShipPhysicsData) ship.body().userData();
		if (lShipUserData.lastCollisionHandled == false) {

			final float lAdjustedAngle = ship.body().angle + (float) Math.toRadians(-90.f);
			final float upX = (float) Math.cos(lAdjustedAngle);
			final float upY = (float) Math.sin(lAdjustedAngle);

			final float dot = Vector2f.dot(upX, upY, lShipUserData.lastCollisionNormalX, lShipUserData.lastCollisionNormalY);

			if (dot <= 0) { // top end of ship
				final int mag = (int) Math.sqrt(lShipUserData.lastCollisionMagnitude2);
				if (mag > DAMAGE_TOP_THREASHOLD) {
					ship.applyDamage(mag);
				}
			} else {
				final int mag = (int) Math.sqrt(lShipUserData.lastCollisionMagnitude2);
				if (mag > DAMAGE_BOTTOM_THREASHOLD) {
					ship.applyDamage(mag - DAMAGE_BOTTOM_THREASHOLD);
				}
			}

			lShipUserData.lastCollisionHandled = true;
			lShipUserData.lastCollisionMagnitude2 = 0.f;
		}
	}

	// DAMAGE ---------------------------------------------

	public void dealDamageToShip(int shipEntityUid, int damageAmount, float hitAngle) {
		final var lShipToDamage = getShipByEntityUid(shipEntityUid);
		if (lShipToDamage == null)
			return;
	}
}
