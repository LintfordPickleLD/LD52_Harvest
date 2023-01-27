package lintfordpickle.harvest.data.game;

import lintfordpickle.harvest.ConstantsGame;

public class GameState {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	public boolean isPlayerDead;
	public int lives;
	public float gameCountdownTimerUntilDeath; // death at 0
	public int timeAliveInMs;
	public short foodDelivered;

	public boolean isGameRunning;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void startNewGame() {
		lives = 3;
		foodDelivered = 0;
		timeAliveInMs = 0;

		gameCountdownTimerUntilDeath = ConstantsGame.TOTAL_GAME_TIME_AT_START;
		isGameRunning = true;
		isPlayerDead = false;
	}

}
