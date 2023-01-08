package lintfordpickle.harvest.data.game;

import lintfordpickle.harvest.ConstantsGame;

public class GameState {

	public int lives;
	public float gameDeathTimerMs; // death on 0
	public int foodDelivered;

	public boolean isGameRunning;

	public void startNewGame() {
		lives = 3;
		foodDelivered = 0;

		gameDeathTimerMs = ConstantsGame.TOTAL_GAME_TIME_AT_START;
		isGameRunning = true;
	}

}
