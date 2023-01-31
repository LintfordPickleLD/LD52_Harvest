package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.screens.landing.SurvialLandingScreen;
import lintfordpickle.harvest.screens.landing.TimeTrailLandingScreen;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.layouts.ListLayout;

public class MainMenu extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "Main Menu";

	private static final int SCREEN_BUTTON_PLAY_SURVIVAL = 10;
	private static final int SCREEN_BUTTON_PLAY_TIME_TRIAL = 11;
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

		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlaySurvivaEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Start Survival");
		lPlaySurvivaEntry.desiredWidth(lDesiredEntryWidth);
		lPlaySurvivaEntry.desiredHeight(lDesiredEntryHeight);
		lPlaySurvivaEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_SURVIVAL);
		lPlaySurvivaEntry.setToolTip("Starts a new survival game. You need to deliver as much food to the HQ before the time runs out.");

		final var lPlayTimeEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Time Trial");
		lPlayTimeEntry.desiredWidth(lDesiredEntryWidth);
		lPlayTimeEntry.desiredHeight(lDesiredEntryHeight);
		lPlayTimeEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_TIME_TRIAL);
		lPlayTimeEntry.setToolTip("You need ot harvest and deliver food from each of the farms. Fastest time wins.");

		final var lHelpButton = new MenuEntry(mScreenManager, mMainMenuListBox, "Instructions");
		lHelpButton.desiredWidth(lDesiredEntryWidth);
		lHelpButton.desiredHeight(lDesiredEntryHeight);
		lHelpButton.registerClickListener(this, SCREEN_BUTTON_HELP);

		final var lOptionsEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Options");
		lOptionsEntry.desiredWidth(lDesiredEntryWidth);
		lOptionsEntry.desiredHeight(lDesiredEntryHeight);
		lOptionsEntry.registerClickListener(this, SCREEN_BUTTON_OPTIONS);

		final var lExitEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Exit");
		lExitEntry.desiredWidth(lDesiredEntryWidth);
		lExitEntry.desiredHeight(lDesiredEntryHeight);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		mMainMenuListBox.addMenuEntry(lPlaySurvivaEntry);
		mMainMenuListBox.addMenuEntry(lPlayTimeEntry);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lHelpButton);
		mMainMenuListBox.addMenuEntry(lOptionsEntry);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lExitEntry);

		mLayouts.add(mMainMenuListBox);

		mMenuScreenWidthScaleFactor = 0.5f;
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
			if (lTopMostScreen instanceof SurvialLandingScreen) {
				return;
			}

			if (!(lTopMostScreen instanceof MainMenu)) {
				mScreenManager.removeScreen(lTopMostScreen);
			}

			screenManager().addScreen(new SurvialLandingScreen(mScreenManager));
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
