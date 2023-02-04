package lintfordpickle.harvest.data.players;

import net.lintford.library.core.actionevents.ActionEventManager.PlaybackMode;

public class PlayerSession {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final PlayerGameContainer mGameContainer = new PlayerGameContainer();

	private int mUid;

	private int mActionEventUid;
	private String mActionInputFilename;

	// ghost ships are used in time-trial mode to show the relative state of a previous playthrough
	private boolean mIsGhostMode;
	private PlaybackMode mPlaybackMode;

	private boolean mIsPlayerControlled;
	private boolean mCanBeDeactivated;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public PlaybackMode mode() {
		return mPlaybackMode;
	}

	public int actionEventUid() {
		return mActionEventUid;
	}

	public void actionEventUid(int actionEventUid) {
		mActionEventUid = actionEventUid;
	}

	public String actionFilename() {
		return mActionInputFilename;
	}

	public boolean canBeDeactivated() {
		return mCanBeDeactivated;
	}

	public PlayerGameContainer gameContainer() {
		return mGameContainer;
	}

	public int playerUid() {
		return mUid;
	}

	public boolean isPlayerControlled() {
		return mIsPlayerControlled;
	}

	public boolean isGhostMode() {
		return mIsGhostMode;
	}

	public void isGhostMode(boolean isGhostMode) {
		mIsGhostMode = isGhostMode;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlayerSession(int uid) {
		mUid = uid;
		mIsPlayerControlled = true;
		mPlaybackMode = PlaybackMode.Normal;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void setPlayback(String filename) {
		mPlaybackMode = PlaybackMode.Playback;
		mActionInputFilename = filename;
	}

	public void setRecorder(String filename) {
		mPlaybackMode = PlaybackMode.Record;
		mActionInputFilename = filename;
	}

	/** Marks this player as being player controllered or not. A ghost-ship in time-trials or a recorded playback is not player-controlled. */
	public void setPlayerControlled(boolean enable) {
		if (enable) {
			mIsPlayerControlled = true;

			return;
		}

		mIsPlayerControlled = false;

	}

	public void init(float x, float y) {
		mGameContainer.position.x = x;
		mGameContainer.position.y = y;

		mIsPlayerControlled = true;
	}

	public void reset() {

	}

}
