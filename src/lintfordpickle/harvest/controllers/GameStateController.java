package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.game.GameState;
import lintfordpickle.harvest.data.players.PlayerManager;
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

	private PlayerManager mPlayerManager;
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

	public GameStateController(ControllerManager controllerManager, GameState gameState, PlayerManager playerManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mGameState = gameState;
		mPlayerManager = playerManager;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lPlayerSessions = mPlayerManager.playerSessions();
		final var lNumPlayerSessions = lPlayerSessions.size();
		for (int i = 0; i < lNumPlayerSessions; i++) {
			final var lPlayerSession = lPlayerSessions.get(i);

			mGameState.addPlayerScoreCard(lPlayerSession.playerUid());
		}

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lDelta = (float) core.gameTime().elapsedTimeMilli();

		if (mGameState.gameCountdownTimerUntilDeath > 0.f) {

			mGameState.gameCountdownTimerUntilDeath -= lDelta;
			mGameState.timeAliveInMs += lDelta;
		}

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public boolean hasPlayerLostThroughTime() {
		return mGameState.gameCountdownTimerUntilDeath <= 0.f;
	}

	public boolean isPlayerDead(int playerUid) {
		final var lPlayerScoreCard = mGameState.getScoreCard(playerUid);
		return lPlayerScoreCard.isPlayerDead;
	}

	public void setPlayerDied(int playerUid) {
		final var lPlayerScoreCard = mGameState.getScoreCard(playerUid);
		lPlayerScoreCard.isPlayerDead = true;
	}

}