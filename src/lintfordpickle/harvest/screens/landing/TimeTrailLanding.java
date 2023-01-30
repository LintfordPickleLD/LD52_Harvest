package lintfordpickle.harvest.screens.landing;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.players.ReplayManager;
import lintfordpickle.harvest.screens.game.TrialGameScreen;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class TimeTrailLanding extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "";

	private static final int SCREEN_BUTTON_PLAY_TIME_TRIAL = 11;
	private static final int SCREEN_BUTTON_EXIT = 15;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ReplayController mReplayController;
	private ListLayout mMainMenuListBox;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public TimeTrailLanding(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		mMainMenuListBox = new ListLayout(this);

		// As we know the game canvas size
		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlayTimeEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Time Trial");
		lPlayTimeEntry.desiredWidth(lDesiredEntryWidth);
		lPlayTimeEntry.desiredHeight(lDesiredEntryHeight);
		lPlayTimeEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_TIME_TRIAL);
		lPlayTimeEntry.setToolTip("You need ot harvest and deliver food from each of the farms. Fastest time wins.");

		final var lExitEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Exit");
		lExitEntry.desiredWidth(lDesiredEntryWidth);
		lExitEntry.desiredHeight(lDesiredEntryHeight);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		mMainMenuListBox.addMenuEntry(lPlayTimeEntry);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lExitEntry);

		mLayouts.add(mMainMenuListBox);

		mLayoutAlignment = LAYOUT_ALIGNMENT.RIGHT;

		mIsPopup = false;
		mShowBackgroundScreens = true;
		mBlockInputInBackground = false;
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
	}

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_PLAY_TIME_TRIAL: {

			// TODO: Default player is created automatically - and will be controlled by the player.
			final var lPlayerManager = new PlayerManager();
			lPlayerManager.getPlayer(PlayerManager.DEFAULT_PLAYER_SESSION_UID).setRecorder("player.lmp");
			lPlayerManager.getPlayer(PlayerManager.DEFAULT_PLAYER_SESSION_UID).setPlayerControlled(true);

			final var lReplayManager = mReplayController.replayManager();
			if (lReplayManager.isRecordedGameAvailable()) {
				final var lGhostPlayer = lPlayerManager.addNewPlayer();
				lGhostPlayer.setPlayback(ReplayManager.RecordedGameFilename);
				lGhostPlayer.setPlayerControlled(false);
				lGhostPlayer.isGhostMode(true);
			}

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new TrialGameScreen(screenManager(), lPlayerManager));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;
		}

		case SCREEN_BUTTON_EXIT:
			exitScreen();

			break;
		}
	}
}
