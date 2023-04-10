package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.SurvivalGameScreen;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

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
			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new SurvivalGameScreen(screenManager(), mPlayerManager));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;

		case SCREEN_BUTTON_EXIT:
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new MenuBackgroundScreen(mScreenManager), new MainMenu(screenManager())));
			break;

		}
	}
}
