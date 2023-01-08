package lintfordpickle.harvest.data.game;

public class GameState {

	public int lives;
	public float gameDeathTimerSeconds; // death on 0
	public int points;

	public void startNewGame() {
		lives = 3;
		points = 0;

		gameDeathTimerSeconds = 60 * 4;
	}

}
