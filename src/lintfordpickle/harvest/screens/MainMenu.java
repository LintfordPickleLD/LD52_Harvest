package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.screens.landing.SurvivalLandingScreen;
import lintfordpickle.harvest.screens.landing.TimeTrailLandingScreen;
import net.lintford.library.core.graphics.Color;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintford.library.screenmanager.layouts.ListLayout;

public class MainMenu extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = null;

	private static final int SCREEN_BUTTON_PLAY_SURVIVAL = 10;
	private static final int SCREEN_BUTTON_PLAY_TIME_TRIAL = 11;
	private static final int SCREEN_BUTTON_PLAY_TEST = 20;
	private static final int SCREEN_BUTTON_HELP = 12;
	private static final int SCREEN_BUTTON_OPTIONS = 13;
	private static final int SCREEN_BUTTON_EXIT = 15;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ReplayController mReplayController;
	private ListLayout mMainMenuListBox;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public MainMenu(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		mMainMenuListBox = new ListLayout(this);
		mMainMenuListBox.setDrawBackground(true, new Color(.4f, .4f, .4f, .8f));
		mMainMenuListBox.setDrawBackground(true, new Color(0.02f, 0.12f, 0.15f, 0.8f));
		mMainMenuListBox.layoutWidth(LAYOUT_WIDTH.HALF);
		mMainMenuListBox.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlaySurvivaEntry = new MenuEntry(mScreenManager, this, "Survival");
		lPlaySurvivaEntry.desiredHeight(lDesiredEntryHeight);
		lPlaySurvivaEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lPlaySurvivaEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_SURVIVAL);
		lPlaySurvivaEntry.setToolTip("Starts a new survival game. You need to deliver as much food to the HQ before the time runs out.");

		final var lPlayTimeEntry = new MenuEntry(mScreenManager, this, "Time-Trial");
		lPlayTimeEntry.desiredHeight(lDesiredEntryHeight);
		lPlayTimeEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lPlayTimeEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_TIME_TRIAL);
		lPlayTimeEntry.setToolTip("You need to harvest and deliver food from each of the farms. Fastest time wins.");

		final var lTestEntry = new MenuEntry(mScreenManager, this, "Test");
		lTestEntry.desiredHeight(lDesiredEntryHeight);
		lTestEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lTestEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_TEST);

		final var lHelpButton = new MenuEntry(mScreenManager, this, "Instructions");
		lHelpButton.desiredHeight(lDesiredEntryHeight);
		lHelpButton.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lHelpButton.registerClickListener(this, SCREEN_BUTTON_HELP);

		final var lOptionsEntry = new MenuEntry(mScreenManager, this, "Options");
		lOptionsEntry.desiredHeight(lDesiredEntryHeight);
		lOptionsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lOptionsEntry.registerClickListener(this, SCREEN_BUTTON_OPTIONS);

		final var lExitEntry = new MenuEntry(mScreenManager, this, "Exit");
		lExitEntry.desiredHeight(lDesiredEntryHeight);
		lExitEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		mMainMenuListBox.addMenuEntry(lPlaySurvivaEntry);
		mMainMenuListBox.addMenuEntry(lPlayTimeEntry);
		mMainMenuListBox.addMenuEntry(lTestEntry);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lHelpButton);
		mMainMenuListBox.addMenuEntry(lOptionsEntry);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lExitEntry);

		mLayouts.add(mMainMenuListBox);

		mSelectedLayoutIndex = mLayouts.size() - 1;
		mSelectedEntryIndex = 0;

		mPaddingTopNormalized = 30.f;
		INNER_PADDING_W = 50.f;
		mLayoutAlignment = LAYOUT_ALIGNMENT.LEFT;

		mIsPopup = false;
		mShowBackgroundScreens = true;
		mESCBackEnabled = false;
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
			final var lTopMostScreen = mScreenManager.getTopScreen();
			if (lTopMostScreen instanceof SurvivalLandingScreen) {
				return;
			}

			if (!(lTopMostScreen instanceof MainMenu)) {
				mScreenManager.removeScreen(lTopMostScreen);
			}

			screenManager().addScreen(new SurvivalLandingScreen(mScreenManager));
			break;
		}

		case SCREEN_BUTTON_PLAY_TIME_TRIAL: {
			final var lTopMostScreen = mScreenManager.getTopScreen();

			if (lTopMostScreen instanceof TimeTrailLandingScreen) {
				return;
			}

			if (!(lTopMostScreen instanceof MainMenu)) {
				mScreenManager.removeScreen(lTopMostScreen);
			}

			screenManager().addScreen(new TimeTrailLandingScreen(mScreenManager));
			break;
		}

		case SCREEN_BUTTON_OPTIONS: {
			final var lTopMostScreen = mScreenManager.getTopScreen();
			if (!(lTopMostScreen instanceof MainMenu)) {
				mScreenManager.removeScreen(lTopMostScreen);
			}

			screenManager().addScreen(new OptionsScreen(mScreenManager));
			break;
		}

		case SCREEN_BUTTON_PLAY_TEST: {
			screenManager().addScreen(new TestMenuScreen(mScreenManager));
			break;
		}

		case SCREEN_BUTTON_HELP: {
			final var lTopMostScreen = mScreenManager.getTopScreen();
			if (!(lTopMostScreen instanceof MainMenu)) {
				mScreenManager.removeScreen(lTopMostScreen);
			}

			screenManager().addScreen(new MenuHelpScreen(mScreenManager));
			break;
		}

		case SCREEN_BUTTON_EXIT:
			screenManager().exitGame();
			break;
		}
	}
}
