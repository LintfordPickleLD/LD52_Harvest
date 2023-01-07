package lintfordpickle.harvest.contrllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.data.Ship;
import lintfordpickle.harvest.data.ShipManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.geometry.partitioning.GridEntity;
import net.lintford.library.core.geometry.partitioning.SpatialHashGrid;
import net.lintford.library.core.maths.Vector2f;

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
	public void unload() {

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

		final float lThrustUpForce = 2000.f;
		final float lAngularTorque = 500.f;

		if (lShipInput.isUpThrottle) {
			final float lAngle = body.angle;
			final float upX = (float) Math.cos(lAngle);
			final float upY = (float) Math.sin(lAngle);

			ship.body().accX += -upY * -lThrustUpForce * body.invMass();
			ship.body().accY += upX * -lThrustUpForce * body.invMass();
		}

		{ // apply a self-righting force
			final float upX = 0.f;
			final float upY = -1.f;
			final float lAngle = body.angle - (float) Math.toRadians(90.f);
			final float shipUpx = (float) Math.cos(lAngle);
			final float shipUpY = (float) Math.sin(lAngle);

			final float dot = Vector2f.dot(upX, upY, shipUpx, shipUpY);

			final float controlAmt = (lAngularTorque * dot);
			final float correctAmt = (lAngularTorque * (1.f - dot));

			if (lShipInput.isLeftThrottle) {
				ship.body().torque -= controlAmt * body.invInertia();
			}

			if (lShipInput.isRightThrottle) {
				ship.body().torque += controlAmt * body.invInertia();
			}

			if (!lShipInput.isLeftThrottle && !lShipInput.isRightThrottle && dot > 0.0f) {
				if (body.angle != 0) {
					if (body.angle < .0f) {
						ship.body().torque += correctAmt * body.invInertia();
					} else {
						ship.body().torque -= correctAmt * body.invInertia();
					}
				}
			}
		}
	}

	private void updateShipOnGrid(LintfordCore core, SpatialHashGrid<GridEntity> grid, Ship ship) {
		grid.updateEntity(ship);
	}

	private void updateEngineTrails(LintfordCore core, Ship ship) {
		final float c = (float) Math.cos(ship.body().angle);
		final float s = (float) Math.sin(ship.body().angle);

		float engineOffsetX = -15;
		float engineOffsetY = 0;

		float xx = engineOffsetX;
		float yy = engineOffsetY;

		final float engineX = xx * c - yy * s;
		final float engineY = yy * c + xx * s;

//		ship.mTrailRendererComponent.color(ship.mEngineColorR, ship.mEngineColorG, ship.mEngineColorB, .5f);
//		ship.mTrailRendererComponent.updateTrail(core, ship.body().x + engineX, ship.body().y + engineY, ship.body().angle);
	}

	// DAMAGE ---------------------------------------------

	public void dealDamageToShip(int shipEntityUid, int damageAmount, float hitAngle) {
		final var lShipToDamage = getShipByEntityUid(shipEntityUid);
		if (lShipToDamage == null)
			return;

		// TODO:
	}
}
