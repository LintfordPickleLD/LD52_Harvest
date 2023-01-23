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

	private PlaybackMode mPlaybackMode;

	private boolean nIsPlayerControlled;
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

	public boolean isPlayerEnabled() {
		return nIsPlayerControlled;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlayerSession(int uid) {
		nIsPlayerControlled = true;
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

	public void enablePlayer(boolean enable) {
		if (enable) {
			nIsPlayerControlled = true;

			return;
		}

		nIsPlayerControlled = false;

	}

	public void init(float x, float y) {
		mGameContainer.position.x = x;
		mGameContainer.position.y = y;

		nIsPlayerControlled = true;
	}

	public void reset() {

	}

}
