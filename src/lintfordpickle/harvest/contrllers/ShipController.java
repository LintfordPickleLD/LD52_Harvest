package lintfordpickle.harvest.contrllers;

import lintfordpickle.harvest.data.Ship;
import lintfordpickle.harvest.data.ShipManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.geometry.partitioning.GridEntity;
import net.lintford.library.core.geometry.partitioning.SpatialHashGrid;

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

//		lPlayerShip.shipInput.isTurningLeft = core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT);
//		lPlayerShip.shipInput.isTurningRight = core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_RIGHT);
//		lPlayerShip.shipInput.isGas = core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_UP);
//		lPlayerShip.shipInput.isBrake = core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_DOWN);
//		lPlayerShip.shipInput.isHandBrake = core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_SPACE);
//
//		lPlayerShip.shipInput.isShootCannon = core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL);

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

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
		final float lDelta = (float) core.gameTime().elapsedTimeMilli() * 0.001f;

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
