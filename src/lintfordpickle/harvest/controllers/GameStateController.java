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
	public void update(LintfordCore core) {
		super.update(core);

		if (mGameState.gameDeathTimerMs > 0.f) {
			mGameState.gameDeathTimerMs -= core.gameTime().elapsedTimeMilli();
		}

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public boolean hasPlayerLostThroughTime() {
		return mGameState.gameDeathTimerMs <= 0.f;
	}

	public boolean hasPlayerLostThroughLives() {
		return mGameState.lives <= 0;
	}

	public boolean isPlayerDead() {
		return mGameState.isPlayerDead;
	}

	public void setPlayerDied() {
		mGameState.isPlayerDead = true;
	}

	public void addFoodDelivered(int amt) {
		mGameState.foodDelivered += amt;
		mGameState.gameDeathTimerMs += 30000; // 30 seconds
	}

}