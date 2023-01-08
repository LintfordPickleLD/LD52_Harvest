package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.game.GameState;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class GameStateController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Game State Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ShipController mShipController;
	private GameState mGameState;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public GameState gameState() {
		return mGameState;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameStateController(ControllerManager controllerManager, GameState gameState, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mGameState = gameState;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mShipController = (ShipController) lControllerManager.getControllerByNameRequired(ShipController.CONTROLLER_NAME, entityGroupUid());

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public boolean hasPlayerLostThroughTime() {
		return false;
	}

	public boolean hasPlayerLostThroughLives() {
		return false;
	}

	public void addPoints(int amt) {
		mGameState.points += amt;
	}

}
