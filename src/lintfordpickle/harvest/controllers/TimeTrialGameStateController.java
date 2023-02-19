package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.game.GameState;
import lintfordpickle.harvest.data.players.PlayerManager;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class TimeTrialGameStateController extends GameStateController {

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public TimeTrialGameStateController(ControllerManager controllerManager, GameState gameState, PlayerManager playerManager, int entityGroupUid) {
		super(controllerManager, gameState, playerManager, entityGroupUid);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lDelta = (float) core.gameTime().elapsedTimeMilli();
		mGameState.gameTimer += lDelta;
		mGameState.timeAliveInMs += lDelta;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	public boolean hasPlayerLostThroughTime() {
		return false;
	}

}