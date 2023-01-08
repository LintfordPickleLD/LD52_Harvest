package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.platforms.Platform;
import lintfordpickle.harvest.data.platforms.PlatformManager;
import lintfordpickle.harvest.data.ships.Ship;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class PlatformsController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Platforms Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private ShipController mShipController;
	private PlatformManager mPlatformManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public PlatformManager platformManager() {
		return mPlatformManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlatformsController(ControllerManager controllerManager, PlatformManager platformManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mPlatformManager = platformManager;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mShipController = (ShipController) lControllerManager.getControllerByNameRequired(ShipController.CONTROLLER_NAME, entityGroupUid());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupUid());
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		updatePlatformsState(core);
	}

	private void updatePlatformsState(LintfordCore core) {

		final var lPlatformList = mPlatformManager.platforms();
		final var lNumPlatforms = lPlatformList.size();
		for (int i = 0; i < lNumPlatforms; i++) {
			final var lPlatform = lPlatformList.get(i);

			switch (lPlatform.platformType) {
			case Water:
				updateWaterPlatform(core, lPlatform);

				break;
			case Farm:
				updateFarmPlatform(core, lPlatform);
				break;
			default: // warehouse
				updateWarehousePlatform(core, lPlatform);
				break;
			}

		}

	}

	private Ship getPlayerShip() {
		return mShipController.shipManager().playerShip();
	}

	private boolean isPlayerShipAtPlatform(LintfordCore core, Platform platform, Ship ship) {
		// TODO: Cache this in the ship class
		final var lUnitsToPx = ConstantsPhysics.UnitsToPixels();
		final var lAABB = ship.body().aabb();
		final var lShipX = lAABB.left() * lUnitsToPx;
		final var lShipY = lAABB.top() * lUnitsToPx;
		final var lShipW = lAABB.width() * lUnitsToPx;
		final var lShipH = lAABB.height() * lUnitsToPx;

		if (platform.intersectsAA(lShipX, lShipY, lShipW, lShipH)) {
			// only register if ships comes to halt
			if (Math.abs(ship.velocityMagnitude) < 0.01) {
				System.out.println("Platform Activated bozo!");
				return true;
			}
		}

		return false;
	}

	// ---------------------------------------------

	private void updateWaterPlatform(LintfordCore core, Platform platform) {
		final var lShip = getPlayerShip();
		final var lIsPlayerAtPlatform = isPlayerShipAtPlatform(core, platform, lShip);

		// stock full
		if (platform.isStockFull) {
			if (lIsPlayerAtPlatform && lShip.cargo.freeSpace > 0) {
				lShip.cargo.waterAmt++;
				lShip.cargo.freeSpace--;

				platform.isStockFull = false;
				platform.stockValueF = 0.f;
			}

		}

		// refill
		if (!platform.isRefillingStock && !platform.isStockFull && lIsPlayerAtPlatform) {
			platform.timeUntilRefillStarts = 200;
		} else {
			if (!platform.isRefillingStock) {
				platform.timeUntilRefillStarts -= core.gameTime().elapsedTimeMilli();
				if (platform.timeUntilRefillStarts <= 0) {
					platform.isRefillingStock = true;
				}
			}
		}

		// normal
		if (platform.isRefillingStock) {
			platform.stockValueF += 0.1f * core.gameTime().elapsedTimeMilli() * 0.001f;
			if (platform.stockValueF >= 1.f) {
				platform.stockValueF = 1.f;

				platform.isRefillingStock = false;
				platform.isStockFull = true;
			}
		}

	}

	private void updateFarmPlatform(LintfordCore core, Platform platform) {
		final var lShip = getPlayerShip();
		final var lIsPlayerAtPlatform = isPlayerShipAtPlatform(core, platform, lShip);

		if (lIsPlayerAtPlatform) {
			System.out.println("");
		}

		// stock full
		if (platform.isStockFull) {
			if (lIsPlayerAtPlatform && lShip.cargo.freeSpace > 0) {
				lShip.cargo.wheatAmt++;
				lShip.cargo.freeSpace--;

				platform.isStockFull = false;
				platform.stockValueI = 0;
			}
		}

		// refill
		if (!platform.isRefillingStock && !platform.isStockFull && lIsPlayerAtPlatform) {
			if (!platform.refilPrerequisteFulfilled) {
				if (lIsPlayerAtPlatform && lShip.cargo.waterAmt > 0) {
					lShip.cargo.waterAmt--;
					lShip.cargo.freeSpace++;

					platform.refilPrerequisteFulfilled = true;
				}
			}
		}

		if (!platform.isRefillingStock && platform.refilPrerequisteFulfilled) {
			platform.timeUntilRefillStarts -= core.gameTime().elapsedTimeMilli();
			if (platform.timeUntilRefillStarts <= 0) {
				platform.isRefillingStock = true;
			}
		}

		// normal
		if (platform.isRefillingStock) {
			platform.stockValueF += 0.1f * core.gameTime().elapsedTimeMilli() * 0.001f;
			if (platform.stockValueF >= 1.f) {
				platform.stockValueF = 0.f;
				platform.stockValueI = 1;

				platform.isRefillingStock = false;
				platform.refilPrerequisteFulfilled = false;
				platform.isStockFull = true;
			}
		}

	}

	private void updateWarehousePlatform(LintfordCore core, Platform platform) {
		final var lShip = getPlayerShip();
		final var lIsPlayerAtPlatform = isPlayerShipAtPlatform(core, platform, lShip);

		// check unload of wheat
		if (lIsPlayerAtPlatform && lShip.cargo.wheatAmt > 0) {
			final int lNumWheat = lShip.cargo.wheatAmt;
			for (int i = 0; i < lNumWheat; i++) {
				mGameStateController.addPoints(50);

				lShip.cargo.freeSpace++;
				lShip.cargo.wheatAmt--;
			}
		}
	}
}
