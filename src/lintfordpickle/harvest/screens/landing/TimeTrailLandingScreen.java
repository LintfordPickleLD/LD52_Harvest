package lintfordpickle.harvest.screens.landing;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.players.ReplayManager;
import lintfordpickle.harvest.screens.game.TimeTrialGameScreen;
import net.lintford.library.core.graphics.Color;
import net.lintford.library.core.time.TimeConstants;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintford.library.screenmanager.entries.MenuInputEntry;
import net.lintford.library.screenmanager.entries.MenuLabelEntry;
import net.lintford.library.screenmanager.entries.MenuToggleEntry;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class TimeTrailLandingScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "";

	private static final int SCREEN_BUTTON_PLAY_TIME_TRIAL = 11;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ReplayController mReplayController;
	private ListLayout mMainMenuListBox;

	private MenuToggleEntry mGhostEnabled;
	private MenuInputEntry mGhostFatestTime;
	private MenuLabelEntry mNoFastestTime;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public TimeTrailLandingScreen(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		mMainMenuListBox = new ListLayout(this);
		mMainMenuListBox.layoutWidth(LAYOUT_WIDTH.HALF);
		mMainMenuListBox.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		mMainMenuListBox.setDrawBackground(true, new Color(0.02f, 0.12f, 0.15f, 0.8f));
		mMainMenuListBox.title("Time-Trial");
		mMainMenuListBox.showTitle(true);

		// As we know the game canvas size
		final float lDesiredEntryWidth = 300.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlayTimeEntry = new MenuEntry(mScreenManager, this, "Start");
		lPlayTimeEntry.desiredWidth(lDesiredEntryWidth);
		lPlayTimeEntry.desiredHeight(lDesiredEntryHeight);
		lPlayTimeEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_TIME_TRIAL);
		lPlayTimeEntry.setToolTip("You need ot harvest and deliver food from each of the farms. Fastest time wins.");

		mGhostFatestTime = new MenuInputEntry(pScreenManager, this);
		mGhostFatestTime.label("Time");
		mGhostFatestTime.horizontalFillType(FILLTYPE.THIRD_PARENT);
		mGhostFatestTime.enabled(false);

		mGhostEnabled = new MenuToggleEntry(pScreenManager, this);
		mGhostEnabled.label("Ghost");
		mGhostEnabled.horizontalFillType(FILLTYPE.TAKE_DESIRED_SIZE);
		mGhostEnabled.showInfoButton(true);
		mGhostEnabled.setToolTip("The ghost ship replays the actions of the fastest time, but doesn't interfere with the gameplay");
		mGhostEnabled.desiredWidth(lDesiredEntryWidth);
		mGhostEnabled.desiredHeight(lDesiredEntryHeight);

		mNoFastestTime = new MenuLabelEntry(pScreenManager, this);
		mNoFastestTime.label("No fastest time");

		mMainMenuListBox.addMenuEntry(mNoFastestTime);
		mMainMenuListBox.addMenuEntry(mGhostFatestTime);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(mGhostEnabled);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lPlayTimeEntry);

		mLayouts.add(mMainMenuListBox);

		mSelectedLayoutIndex = 0;
		mSelectedEntryIndex = 6;

		mPaddingTopNormalized = 30.f;
		mLayoutAlignment = LAYOUT_ALIGNMENT.RIGHT;

		mIsPopup = false;
		mShowBackgroundScreens = true;

		mBlockMouseInputInBackground = false;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		final var lControllerManager = mScreenManager.core().controllerManager();
		mReplayController = (ReplayController) lControllerManager.getControllerByNameRequired(ReplayController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);

		final var lReplayManager = mReplayController.replayManager();
		lReplayManager.loadRecordedGame();

		if (lReplayManager.isRecordedGameAvailable()) {

			var tempTime = lReplayManager.header().runtimeInSeconds();
			final var lTotalMinutes = (int) tempTime / TimeConstants.MillisPerMinute;
			tempTime -= lTotalMinutes * TimeConstants.MillisPerMinute;
			final var lTotalSeconds = (int) tempTime / TimeConstants.MillisPerSecond;
			tempTime -= lTotalSeconds * TimeConstants.MillisPerSecond;

			mNoFastestTime.enabled(false);

			mGhostEnabled.isChecked(true);

			mGhostFatestTime.inputString(lTotalMinutes + ":" + lTotalSeconds + " s");
			mGhostFatestTime.canHaveFocus(false);

		} else {
			mNoFastestTime.enabled(true);
			mNoFastestTime.active(true);

			mGhostEnabled.enabled(false);
			mGhostEnabled.isChecked(false);

			mGhostFatestTime.inputString(null);
		}
	}

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_PLAY_TIME_TRIAL: {

			// TODO: Default player is created automatically - and will be controlled by the player.
			final var lPlayerManager = new PlayerManager();
			lPlayerManager.getPlayer(PlayerManager.DEFAULT_PLAYER_SESSION_UID).setRecorder("player.lmp");
			lPlayerManager.getPlayer(PlayerManager.DEFAULT_PLAYER_SESSION_UID).setPlayerControlled(true);

			if (mGhostEnabled.isChecked()) {
				final var lReplayManager = mReplayController.replayManager();
				if (lReplayManager.isRecordedGameAvailable()) {
					final var lGhostPlayer = lPlayerManager.addNewPlayer();
					lGhostPlayer.setPlayback(ReplayManager.RecordedGameFilename);
					lGhostPlayer.setPlayerControlled(false);
					lGhostPlayer.isGhostMode(true);
				}
			}

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new TimeTrialGameScreen(screenManager(), lPlayerManager));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;
		}
		}
	}
}
