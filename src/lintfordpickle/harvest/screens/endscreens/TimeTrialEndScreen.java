package lintfordpickle.harvest.screens.endscreens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.MainMenu;
import lintfordpickle.harvest.screens.MenuBackgroundScreen;
import lintfordpickle.harvest.screens.game.TimeTrialGameScreen;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.time.TimeConstants;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
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
		super(screenManager, "");

		mPlayerManager = playerManager;
		mTotalTimeInMs = totalTimeInMs;
		mFastestTimeRecorded = fastestTimeRecorded;

		final var lLayout = new ListLayout(this);

		// ---

		final var lRetryButton = new MenuEntry(mScreenManager, this, "Go Again");
		lRetryButton.registerClickListener(this, SCREEN_BUTTON_RESTART);

		final var lExitToMenuButton = new MenuEntry(mScreenManager, this, "Back to Menu");
		lExitToMenuButton.registerClickListener(this, SCREEN_BUTTON_EXIT);

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

	@Override
	public void draw(LintfordCore core) {

		final var lTitleFont = mRendererManager.uiTitleFont();
		final var lFont = mRendererManager.uiTextFont();

		super.draw(core);

		final var lHeaderText = "Run Complete";
		final var lHeaderTextWidth = lTitleFont.getStringWidth(lHeaderText);

		lTitleFont.begin(core.HUD());
		lTitleFont.drawText(lHeaderText, -lHeaderTextWidth / 2, -200, -0.01f, 1.f);
		lTitleFont.end();

		final var lGameOverText0 = "You made the full delivery!";
		final var lTextWidth0 = lFont.getStringWidth(lGameOverText0);

		lFont.begin(core.HUD());
		lFont.drawText(lGameOverText0, -lTextWidth0 / 2, -100, -0.01f, 1.f);

		var tempTime = mTotalTimeInMs;
		final var lTotalMinutes = (int) tempTime / TimeConstants.MillisPerMinute;
		tempTime -= lTotalMinutes * TimeConstants.MillisPerMinute;
		final var lTotalSeconds = (int) tempTime / TimeConstants.MillisPerSecond;
		tempTime -= lTotalSeconds * TimeConstants.MillisPerSecond;

		final var lGameOverText1 = "Your total time was: " + lTotalMinutes + ":" + lTotalSeconds + ":" + tempTime;
		final var lTextWidth1 = lFont.getStringWidth(lGameOverText1);

		if (mFastestTimeRecorded) {

		}

		lFont.begin(core.HUD());
		lFont.drawText(lGameOverText1, -lTextWidth1 / 2, -80, -0.01f, 1.f);
		lFont.end();

		mScreenPaddingTop = 370.f;

	}
}
