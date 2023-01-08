package lintfordpickle.harvest.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.data.ships.ShipManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class ShipController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Ship Controller";

	protected static final int MAX_SHIELDS_COMPONENTS = 40;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

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

	}

	@Override
	public void unloadController() {

	}

	@Override
	public boolean handleInput(LintfordCore core) {
		final var lPlayerShip = mShipManager.playerShip();
		final var lKeyboard = core.input().keyboard();

		lPlayerShip.inputs.isLeftThrottle = lKeyboard.isKeyDown(GLFW.GLFW_KEY_LEFT) || lKeyboard.isKeyDown(GLFW.GLFW_KEY_A);
		lPlayerShip.inputs.isRightThrottle = lKeyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT) || lKeyboard.isKeyDown(GLFW.GLFW_KEY_D);
		lPlayerShip.inputs.isUpThrottle = lKeyboard.isKeyDown(GLFW.GLFW_KEY_UP) || lKeyboard.isKeyDown(GLFW.GLFW_KEY_W);

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lPlayerShip = mShipManager.playerShip();
		updateShip(core, lPlayerShip);

		// ----

		final var lShipList = mShipManager.ships();
		final var lNumShips = lShipList.size();

		for (int i = 0; i < lNumShips; i++) {
			final var lShip = lShipList.get(i);

			updateShip(core, lShip);
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void updateShip(LintfordCore core, Ship ship) {
		ship.update(core);

		final var body = ship.body();
		final var lShipInput = ship.inputs;

		final float lThrustUpForce = 100.f;
		final float lAngularTorque = 3.f;

		if (lShipInput.isUpThrottle) {
			final float lAngle = body.angle;
			final float upX = (float) Math.cos(lAngle);
			final float upY = (float) Math.sin(lAngle);

			ship.body().accX += -upY * -lThrustUpForce * body.invMass();
			ship.body().accY += upX * -lThrustUpForce * body.invMass();
		}

		{
			if (lShipInput.isLeftThrottle) {
				body.torque -= lAngularTorque * body.invInertia();
			}

			if (lShipInput.isRightThrottle) {
				body.torque += lAngularTorque * body.invInertia();
			}

			if (!lShipInput.isLeftThrottle && !lShipInput.isRightThrottle)
				body.angularVelocity *= 0.8f;

		}
	}

	// DAMAGE ---------------------------------------------

	public void dealDamageToShip(int shipEntityUid, int damageAmount, float hitAngle) {
		final var lShipToDamage = getShipByEntityUid(shipEntityUid);
		if (lShipToDamage == null)
			return;

		// TODO:
	}
}
