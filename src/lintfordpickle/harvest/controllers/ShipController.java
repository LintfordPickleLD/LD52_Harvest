package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.actionevents.GameActionEventController;
import lintfordpickle.harvest.controllers.camera.CameraShipChaseController;
import lintfordpickle.harvest.data.physics.ShipPhysicsData;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.data.ships.ShipManager;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
import net.lintford.library.controllers.physics.PhysicsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.geometry.partitioning.GridEntity;
import net.lintford.library.core.maths.RandomNumbers;
import net.lintford.library.core.maths.Vector2f;
import net.lintford.library.core.particles.particlesystems.ParticleSystemInstance;

public class ShipController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Ship Controller";

	public static final int DAMAGE_TOP_THREASHOLD = 2;
	public static final int DAMAGE_BOTTOM_THREASHOLD = 10;

	protected static final int MAX_SHIELDS_COMPONENTS = 40;

	private static final boolean DEBUG_DISABLE_PARTICLES = false;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private GameActionEventController mActionEventController;
	private PhysicsController mPhysicsController;

	private PlayerManager mPlayerManager;
	private ShipManager mShipManager;

	private ParticleSystemInstance mJetParticleSystem;
	private ParticleSystemInstance mJetIntenseParticleSystem;
	
	private ParticleSystemInstance mSparkParticleSystem;
	private ParticleSystemInstance mEngineSparkParticleSystem;

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
			if (lShips.get(i).uid == entityUid)
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

		createShipsFromPlayerManager();

		setupPlayerCamera(core, lControllerManager);

		setupParticleSystems(core, lControllerManager);
	}

	private void setupParticleSystems(LintfordCore core, final ControllerManager lControllerManager) {
		final var lParticleController = (ParticleFrameworkController) lControllerManager.getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupUid());
		mJetParticleSystem = lParticleController.particleFrameworkData().particleSystemManager().createNewParticleSystemFromDefinitionName("PARTICLESYSTEM_JET");
		mJetIntenseParticleSystem = lParticleController.particleFrameworkData().particleSystemManager().createNewParticleSystemFromDefinitionName("PARTICLESYSTEM_JET_INTENSE");
		
		mSparkParticleSystem = lParticleController.particleFrameworkData().particleSystemManager().createNewParticleSystemFromDefinitionName("PARTICLESYSTEM_SPARK");
		mEngineSparkParticleSystem = lParticleController.particleFrameworkData().particleSystemManager().createNewParticleSystemFromDefinitionName("PARTICLESYSTEM_ENGINESPARK");
	}

	private void setupPlayerCamera(LintfordCore core, final ControllerManager lControllerManager) {
		final var lCameraChaseController = (CameraShipChaseController) lControllerManager.getControllerByNameRequired(CameraShipChaseController.CONTROLLER_NAME, entityGroupUid());
		if (lCameraChaseController != null) {
			final var lDefaultShip = mShipManager.ships().get(0);
			lCameraChaseController.setTrackedEntity(core.gameCamera(), lDefaultShip);
		}
	}

	private void createShipsFromPlayerManager() {
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

		final float lJetForce = 50.f;
		final float lSparkForce = 20.f;

		final float lUnitsToPixels = ConstantsPhysics.UnitsToPixels();

		final float lAngle = body.angle;
		final float lUpAngleX = (float) Math.cos(lAngle);
		final float lUpAngleY = (float) Math.sin(lAngle);

		final float lAdjustedAngle = ship.body().angle + (float) Math.toRadians(90.f);
		final float lAdjustedAngleX = (float) Math.cos(lAdjustedAngle);
		final float lAdjustedAngleY = (float) Math.sin(lAdjustedAngle);

		final float lAdjustedVx = body.vx * lUnitsToPixels * .5f;
		final float lAdjustedVy = body.vy * lUnitsToPixels * .5f;

		final float lSparkChange = 32.8f;

		if (lShipInput.isUpThrottle) {
			ship.body().accX += -lUpAngleY * -lThrustUpForce * body.invMass();
			ship.body().accY += lUpAngleX * -lThrustUpForce * body.invMass();
			body.angularVelocity *= 0.99f;

			if (DEBUG_DISABLE_PARTICLES == false) {
				final var lFrontEnginePositionX = ship.frontEngine.x * lUnitsToPixels;
				final var lFrontEnginePositionY = ship.frontEngine.y * lUnitsToPixels;
				mJetParticleSystem.spawnParticle(lFrontEnginePositionX, lFrontEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);
				mJetIntenseParticleSystem.spawnParticle(lFrontEnginePositionX, lFrontEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);

				{
					if (RandomNumbers.getRandomChance(lSparkChange)) {
						final float lMaxAngle = 10.f;
						final float lAngleOffset = RandomNumbers.random(-lMaxAngle, lMaxAngle);
						
						final float lSparkAngleX = (float) Math.cos(lAngle + lAngleOffset);
						final float lSparkAngleY = (float) Math.sin(lAngle + lAngleOffset);
						
						mEngineSparkParticleSystem.spawnParticle(lFrontEnginePositionX, lFrontEnginePositionY, 
								( lSparkAngleX) * lSparkForce,
								( lSparkAngleY) * lSparkForce);
					}

				}

				final var lRearEnginePositionX = ship.rearEngine.x * lUnitsToPixels;
				final var lRearEnginePositionY = ship.rearEngine.y * lUnitsToPixels;
				mJetParticleSystem.spawnParticle(lRearEnginePositionX, lRearEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);
				mJetIntenseParticleSystem.spawnParticle(lRearEnginePositionX, lRearEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);

				{
					if (RandomNumbers.getRandomChance(lSparkChange)) {
						final float lMaxAngle = 10.f;
						final float lAngleOffset = RandomNumbers.random(-lMaxAngle, lMaxAngle);
						
						final float lSparkAngleX = (float) Math.cos(lAngle + lAngleOffset);
						final float lSparkAngleY = (float) Math.sin(lAngle + lAngleOffset);
						
						mEngineSparkParticleSystem.spawnParticle(lRearEnginePositionX, lRearEnginePositionY, 
								( lSparkAngleX) * lSparkForce,
								( lSparkAngleY) * lSparkForce);
					}
				}
			}
		}

		{
			if (lShipInput.isLeftThrottle) {
				body.angle -= lAngularTorque * core.gameTime().elapsedTimeMilli();
				if (body.angularVelocity > 0.f)
					body.angularVelocity *= 0.9f;

				if (DEBUG_DISABLE_PARTICLES == false) {
					final var lFrontEnginePositionX = ship.frontEngine.x * lUnitsToPixels;
					final var lFrontEnginePositionY = ship.frontEngine.y * lUnitsToPixels;
					mJetParticleSystem.spawnParticle(lFrontEnginePositionX, lFrontEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);
					mJetIntenseParticleSystem.spawnParticle(lFrontEnginePositionX, lFrontEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);

					if (RandomNumbers.getRandomChance(lSparkChange)) {
						final float lMaxAngle = 10.f;
						final float lAngleOffset = RandomNumbers.random(-lMaxAngle, lMaxAngle);
						
						final float lSparkAngleX = (float) Math.cos(lAngle + lAngleOffset);
						final float lSparkAngleY = (float) Math.sin(lAngle + lAngleOffset);
						
						mEngineSparkParticleSystem.spawnParticle(lFrontEnginePositionX, lFrontEnginePositionY, 
								( lSparkAngleX) * lSparkForce,
								( lSparkAngleY) * lSparkForce);
					}
				}
			}

			if (lShipInput.isRightThrottle) {

				body.angle += lAngularTorque * core.gameTime().elapsedTimeMilli();
				if (body.angularVelocity < 0.f)
					body.angularVelocity *= 0.9f;

				if (DEBUG_DISABLE_PARTICLES == false) {
					final var lRearEnginePositionX = ship.rearEngine.x * lUnitsToPixels;
					final var lRearEnginePositionY = ship.rearEngine.y * lUnitsToPixels;
					mJetParticleSystem.spawnParticle(lRearEnginePositionX, lRearEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);
					mJetIntenseParticleSystem.spawnParticle(lRearEnginePositionX, lRearEnginePositionY, lAdjustedVx + lAdjustedAngleX * lJetForce, lAdjustedVy + lAdjustedAngleY * lJetForce);

					if (RandomNumbers.getRandomChance(lSparkChange)) {
						final float lMaxAngle = 10.f;
						final float lAngleOffset = RandomNumbers.random(-lMaxAngle, lMaxAngle);
						
						final float lSparkAngleX = (float) Math.cos(lAngle + lAngleOffset);
						final float lSparkAngleY = (float) Math.sin(lAngle + lAngleOffset);
						
						mEngineSparkParticleSystem.spawnParticle(lRearEnginePositionX, lRearEnginePositionY, 
								( lSparkAngleX) * lSparkForce,
								( lSparkAngleY) * lSparkForce);
					}
				}
			}
		}

		// this assumes we can only collide with the level
		final var lShipUserData = (ShipPhysicsData) ship.body().userData();
		if (lShipUserData.lastCollisionHandled == false) {

			final float dot = Vector2f.dot(lAdjustedAngleX, lAdjustedAngleY, lShipUserData.lastCollisionNormalX, lShipUserData.lastCollisionNormalY);

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

			final float lWorldX = lShipUserData.lastCollisionContactX * lUnitsToPixels;
			final float lWorldY = lShipUserData.lastCollisionContactY * lUnitsToPixels;

			final var lLen = 20.f + (float) Math.sqrt(body.vx * body.vx + body.vy * body.vy);
			final int lNumSparks = RandomNumbers.random(2, 7);
			for (int i = 0; i < lNumSparks; i++) {
				final float t = 1.f;
				final float lOffsetX = RandomNumbers.random(-t, t);
				final float lOffsetY = RandomNumbers.random(-t, t);

				mSparkParticleSystem.spawnParticle(lWorldX, lWorldY, (lShipUserData.lastCollisionNormalX + lOffsetX) * lLen, (lShipUserData.lastCollisionNormalY + lOffsetY) * lLen);
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
