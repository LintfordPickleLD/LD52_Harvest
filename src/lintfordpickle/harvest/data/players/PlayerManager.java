package lintfordpickle.harvest.data.players;

public class PlayerManager {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int MAX_PLAYERS = 4;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final PlayerSession[] mPlayerSessions = new PlayerSession[MAX_PLAYERS];

	private int mNumberActivePlayers = 1;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public int numActivePlayers() {
		return mNumberActivePlayers;
	}

	public PlayerSession[] playerSessions() {
		return mPlayerSessions;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlayerManager() {
		mPlayerSessions[0] = new PlayerSession(1, false);
		mPlayerSessions[1] = new PlayerSession(2, true);
		mPlayerSessions[2] = new PlayerSession(3, true);
		mPlayerSessions[3] = new PlayerSession(4, true);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public boolean isPlayerActive(int playerNumber) {
		return playerNumber <= mNumberActivePlayers;
	}

	public PlayerSession getPlayer(int i) {
		if (i < 1 || i > 4)
			return null;

		return mPlayerSessions[i - 1];
	}

	public void addPlayer() {
		if (mNumberActivePlayers >= MAX_PLAYERS)
			return;

		mNumberActivePlayers++;

		switch (mNumberActivePlayers) {
		case 4:
			mPlayerSessions[3].enablePlayer(true);
			break;
		case 3:
			mPlayerSessions[2].enablePlayer(true);
			break;
		case 2:
			mPlayerSessions[1].enablePlayer(true);
			break;
		}
	}

	public void disablePlayer() {
		if (mNumberActivePlayers <= 1)
			return;

		switch (mNumberActivePlayers) {
		case 4:
			mPlayerSessions[3].enablePlayer(false);
			break;
		case 3:
			mPlayerSessions[2].enablePlayer(false);
			break;
		case 2:
			mPlayerSessions[1].enablePlayer(false);
			break;
		}

		mNumberActivePlayers--;
	}
}
