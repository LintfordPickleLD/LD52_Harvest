package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.GameScreen;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class MainMenu extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "Main Menu";

	private static final int SCREEN_BUTTON_PLAY = 10;
	private static final int SCREEN_BUTTON_EXIT = 15;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public MainMenu(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		final var lLayout = new ListLayout(this);

		// As we know the game canvas size
		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlayEntry = new MenuEntry(mScreenManager, lLayout, "Start");
		lPlayEntry.desiredWidth(lDesiredEntryWidth);
		lPlayEntry.desiredHeight(lDesiredEntryHeight);
		lPlayEntry.registerClickListener(this, SCREEN_BUTTON_PLAY);

		final var lExitEntry = new MenuEntry(mScreenManager, lLayout, "Exit");
		lExitEntry.desiredWidth(lDesiredEntryWidth);
		lExitEntry.desiredHeight(lDesiredEntryHeight);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lPlayEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lExitEntry);

		mLayouts.add(lLayout);

		mIsPopup = false;
		mShowBackgroundScreens = true;
		mESCBackEnabled = false;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		mPaddingTopNormalized = 300f;
	}

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_PLAY:

			final var lPlayerManager = new PlayerManager();
			final var lGhostPlayer = lPlayerManager.addNewPlayer();
			lGhostPlayer.setPlayback("ghost.lmp");

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new GameScreen(screenManager(), lPlayerManager, true));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;

		case SCREEN_BUTTON_EXIT:
			screenManager().exitGame();
			break;
		}
	}
}
