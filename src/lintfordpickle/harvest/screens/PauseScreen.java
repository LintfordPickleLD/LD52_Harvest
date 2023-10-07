package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.TimeTrialGameScreen;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

public class PauseScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int SCREEN_BUTTON_CONTINUE = 10;
	private static final int SCREEN_BUTTON_RESTART = 11;
	private static final int SCREEN_BUTTON_EXIT = 12;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PlayerManager mPlayerManager;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PauseScreen(ScreenManager screenManager, PlayerManager playerManager) {
		super(screenManager, null);

		mPlayerManager = playerManager;

		final var lLayout = new ListLayout(this);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		lLayout.setDrawBackground(true, ColorConstants.WHITE);
		lLayout.showTitle(true);
		lLayout.title("Paused");

		// ---
		final var lPlayEntry = new MenuEntry(mScreenManager, this, "Continue");
		lPlayEntry.registerClickListener(this, SCREEN_BUTTON_CONTINUE);

		final var lOptionsEntry = new MenuEntry(mScreenManager, this, "Restart");
		lOptionsEntry.registerClickListener(this, SCREEN_BUTTON_RESTART);

		final var lCreditsEntry = new MenuEntry(mScreenManager, this, "Exit");
		lCreditsEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lPlayEntry);
		lLayout.addMenuEntry(lOptionsEntry);
		lLayout.addMenuEntry(lCreditsEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());

		mLayouts.add(lLayout);

		mIsPopup = true;
		mShowBackgroundScreens = true;

		mBlockGamepadInputInBackground = true;
		mBlockKeyboardInputInBackground = true;
		mBlockMouseInputInBackground = true;

		mShowContextualKeyHints = false;
	}

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_CONTINUE:
			exitScreen();
			return;

		case SCREEN_BUTTON_RESTART:
			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new TimeTrialGameScreen(screenManager(), mPlayerManager));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;

		case SCREEN_BUTTON_EXIT:
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new MenuBackgroundScreen(mScreenManager), new MainMenu(screenManager())));
			break;

		}
	}
}
