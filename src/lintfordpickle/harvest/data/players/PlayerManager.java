package lintfordpickle.harvest.data.players;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int DEFAULT_PLAYER_SESSION_UID = 0;
	public static final int MAX_PLAYERS = 4;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<PlayerSession> mPlayerSessions = new ArrayList<>();

	private int mPlayerUidCounter;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	private int getNewPlayerUid() {
		return mPlayerUidCounter++;
	}

	public int numActivePlayers() {
		return mPlayerSessions.size();
	}

	public List<PlayerSession> playerSessions() {
		return mPlayerSessions;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlayerManager() {
		final var lDefaultPlayer = new PlayerSession(getNewPlayerUid());

		mPlayerSessions.add(lDefaultPlayer);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void resetSessions() {
		// restart all the sessions
	}

	public boolean isPlayerActive(int playerNumber) {
		return playerNumber <= mPlayerSessions.size() - 1;
	}

	public PlayerSession getPlayer(int playerUid) {
		if (playerUid < 0 || playerUid >= mPlayerSessions.size())
			return null;

		return mPlayerSessions.get(playerUid);
	}

	public PlayerSession addNewPlayer() {
		if (numActivePlayers() >= MAX_PLAYERS)
			return null;

		final var lNewPlayer = new PlayerSession(getNewPlayerUid());

		mPlayerSessions.add(lNewPlayer);
		return lNewPlayer;
	}
}
