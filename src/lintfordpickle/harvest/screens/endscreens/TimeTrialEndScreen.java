package lintfordpickle.harvest.screens.endscreens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.MainMenu;
import lintfordpickle.harvest.screens.MenuBackgroundScreen;
import lintfordpickle.harvest.screens.game.TimeTrialGameScreen;
import net.lintford.library.core.time.TimeConstants;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.entries.MenuPanelEntry;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class TimeTrialEndScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int SCREEN_BUTTON_RESTART = 11;
	private static final int SCREEN_BUTTON_EXIT = 12;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PlayerManager mPlayerManager;
	private float mTotalTimeInMs;
	private boolean mFastestTimeRecorded;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public TimeTrialEndScreen(ScreenManager screenManager, PlayerManager playerManager, float totalTimeInMs, boolean fastestTimeRecorded) {
		super(screenManager, "run complete");

		mPlayerManager = playerManager;
		mTotalTimeInMs = totalTimeInMs;
		mFastestTimeRecorded = fastestTimeRecorded;

		final var lLayout = new ListLayout(this);

		// ---

		var tempTime = mTotalTimeInMs;
		final var lTotalMinutes = (int) tempTime / TimeConstants.MillisPerMinute;
		tempTime -= lTotalMinutes * TimeConstants.MillisPerMinute;
		final var lTotalSeconds = (int) tempTime / TimeConstants.MillisPerSecond;
		tempTime -= lTotalSeconds * TimeConstants.MillisPerSecond;

		var lGameOverText1 = "You made the full delivery!\nYour total time was: " + lTotalMinutes + ":" + lTotalSeconds + ":" + tempTime;

		final var lPanelEntry = new MenuPanelEntry(screenManager, this);
		lPanelEntry.text(lGameOverText1);
		lPanelEntry.desiredHeight(64.f + 10.f);
		lPanelEntry.readOnly(true);
		lPanelEntry.canHaveFocus(false);

		final var lRetryButton = new MenuEntry(mScreenManager, this, "Go Again");
		lRetryButton.registerClickListener(this, SCREEN_BUTTON_RESTART);

		final var lExitToMenuButton = new MenuEntry(mScreenManager, this, "Back to Menu");
		lExitToMenuButton.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lPanelEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lRetryButton);
		lLayout.addMenuEntry(lExitToMenuButton);

		mLayouts.add(lLayout);

		mIsPopup = true;
		mShowBackgroundScreens = true;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_RESTART:
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new TimeTrialGameScreen(screenManager(), mPlayerManager)));
			break;

		case SCREEN_BUTTON_EXIT:
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new MenuBackgroundScreen(mScreenManager), new MainMenu(screenManager())));
			break;
		}
	}

}
