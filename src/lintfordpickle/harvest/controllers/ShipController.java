package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.actionevents.GameActionEventController;
import lintfordpickle.harvest.controllers.camera.CameraShipChaseController;
import lintfordpickle.harvest.data.physics.ShipPhysicsData;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.data.ships.ShipManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.PhysicsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.geometry.partitioning.GridEntity;
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

	private GameStateController mGameStateController;
	private GameActionEventController mActionEventController;
	private PhysicsController mPhysicsController;

	private PlayerManager mPlayerManager;
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

	public ShipController(ControllerManager controllerManager, ShipManager shipManager, PlayerManager playerManager, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mPlayerManager = playerManager;
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
		mPhysicsController = (PhysicsController) lControllerManager.getControllerByNameRequired(PhysicsController.CONTROLLER_NAME, entityGroupUid());

		final var lPhysicsWorld = mPhysicsController.world();

		// loop over the players in the playermanager (player and ghosts), and create an action session for them
		final int lNumPlayers = mPlayerManager.numActivePlayers();
		for (int i = 0; i < lNumPlayers; i++) {
			final var lPlayerSession = mPlayerManager.getPlayer(i);

			// Add a ship for this entry to the world
			final var lShip = new Ship(GridEntity.getNewEntityUid());
			lShip.owningPlayerSessionUid = lPlayerSession.playerUid();
			lShip.isPlayerControlled = lPlayerSession.isPlayerControlled();
			lShip.isGhostShip = lPlayerSession.isGhostMode();

			final float lShipPositionX = -1.2f;
			final float lShipPositionY = 13.1f;

			lShip.body().moveTo(lShipPositionX, lShipPositionY);

			mShipManager.ships().add(lShip);

			if (lShip.isGhostShip) {
				lShip.body().categoryBits(ConstantsGame.PHYSICS_WORLD_MASK_GHOST);
				lShip.body().maskBits(ConstantsGame.PHYSICS_WORLD_MASK_WALL);
			} else {
				lShip.body().categoryBits(ConstantsGame.PHYSICS_WORLD_MASK_SHIP);
				lShip.body().maskBits(ConstantsGame.PHYSICS_WORLD_MASK_WALL);
			}

			lPhysicsWorld.addBody(lShip.body());

		}

		final var lCameraChaseController = (CameraShipChaseController) lControllerManager.getControllerByNameRequired(CameraShipChaseController.CONTROLLER_NAME, entityGroupUid());
		if (lCameraChaseController != null) {
			final var lDefaultShip = mShipManager.ships().get(0);
			lCameraChaseController.setTrackedEntity(core.gameCamera(), lDefaultShip);
		}
	}

	@Override
	public void unloadController() {

	}

	@Override
	public boolean handleInput(LintfordCore core) {

		final var lShips = mShipManager.ships();
		final var lNumShips = lShips.size();
		for (int i = 0; i < lNumShips; i++) {
			final var lShip = lShips.get(i);
			final var lPlayerSessions = mPlayerManager.getPlayer(lShip.owningPlayerSessionUid);
			final var lInputFrame = mActionEventController.actionEventPlayer(lPlayerSessions.actionEventUid());

			lShip.inputs.isLeftThrottle = lInputFrame.currentActionEvents.isThrottleLeftDown;
			lShip.inputs.isRightThrottle = lInputFrame.currentActionEvents.isThrottleRightDown;
			lShip.inputs.isUpThrottle = lInputFrame.currentActionEvents.isThrottleDown;
		}

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lShips = mShipManager.ships();
		final var lNumShips = lShips.size();
		for (int i = 0; i < lNumShips; i++) {
			final var lShip = lShips.get(i);

			updateShip(core, lShip);

			if (lShip.isDead() && lShip.isPlayerControlled) {
				mGameStateController.setPlayerDied(lShip.owningPlayerSessionUid);
			}
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
