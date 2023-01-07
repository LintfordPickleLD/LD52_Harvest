package lintfordpickle.harvest.data.players;

public class PlayerSession {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final PlayerGameContainer mGameContainer = new PlayerGameContainer();

	private int mPlayerNumber;

	private boolean mIsPlayerEnabled;
	private boolean mCanBeDeactivated;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean canBeDeactivated() {
		return mCanBeDeactivated;
	}

	public PlayerGameContainer gameContainer() {
		return mGameContainer;
	}

	public int playerNumber() {
		return mPlayerNumber;
	}

	public boolean isPlayerEnabled() {
		return mIsPlayerEnabled;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlayerSession(int playerNumber, boolean canBeDeactivated) {
		mCanBeDeactivated = canBeDeactivated;
		mIsPlayerEnabled = !canBeDeactivated;
		mPlayerNumber = playerNumber;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void enablePlayer(boolean enable) {
		if (enable) {
			mIsPlayerEnabled = true;

			return;
		}

		mIsPlayerEnabled = false;

	}

	public void init(float x, float y) {
		mGameContainer.position.x = x;
		mGameContainer.position.y = y;

		mIsPlayerEnabled = true;
	}

	public void reset() {

	}

}
