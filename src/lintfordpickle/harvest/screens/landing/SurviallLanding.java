package lintfordpickle.harvest.screens.landing;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.SurvivalGameScreen;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class SurviallLanding extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "";

	private static final int SCREEN_BUTTON_PLAY_SURVIVAL = 10;
	private static final int SCREEN_BUTTON_EXIT = 15;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ReplayController mReplayController;
	private ListLayout mMainMenuListBox;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public SurviallLanding(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		mMainMenuListBox = new ListLayout(this);

		// As we know the game canvas size
		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlaySurvivaEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Start Survival");
		lPlaySurvivaEntry.desiredWidth(lDesiredEntryWidth);
		lPlaySurvivaEntry.desiredHeight(lDesiredEntryHeight);
		lPlaySurvivaEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_SURVIVAL);
		lPlaySurvivaEntry.setToolTip("Starts a new survival game. You need to deliver as much food to the HQ before the time runs out.");

		final var lExitEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Back");
		lExitEntry.desiredWidth(lDesiredEntryWidth);
		lExitEntry.desiredHeight(lDesiredEntryHeight);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		mMainMenuListBox.addMenuEntry(lPlaySurvivaEntry);
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
		case SCREEN_BUTTON_PLAY_SURVIVAL: {
			final var lPlayerManager = new PlayerManager();
			lPlayerManager.getPlayer(0).setPlayerControlled(true);

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new SurvivalGameScreen(screenManager(), lPlayerManager));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;
		}

		case SCREEN_BUTTON_EXIT:
			exitScreen();

			break;
		}
	}
}
