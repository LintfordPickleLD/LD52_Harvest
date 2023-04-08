package lintfordpickle.harvest.screens.landing;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.SurvivalGameScreen;
import net.lintford.library.core.graphics.Color;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintford.library.screenmanager.entries.MenuInputEntry;
import net.lintford.library.screenmanager.entries.MenuLabelEntry;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class SurvivalLandingScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "";

	private static final int SCREEN_BUTTON_PLAY_SURVIVAL = 10;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ListLayout mMainMenuListBox;

	private MenuInputEntry mHighScore;
	private MenuLabelEntry mNoHighScore;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public SurvivalLandingScreen(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		mMainMenuListBox = new ListLayout(this);
		mMainMenuListBox.layoutWidth(LAYOUT_WIDTH.HALF);
		mMainMenuListBox.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		mMainMenuListBox.setDrawBackground(true, new Color(0.02f, 0.12f, 0.15f, .8f));
		mMainMenuListBox.title("Survival Mode");
		mMainMenuListBox.showTitle(true);

		final var lPlaySurvivaEntry = new MenuEntry(mScreenManager, this, "Start");
		lPlaySurvivaEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_SURVIVAL);

		mHighScore = new MenuInputEntry(pScreenManager, this);
		mHighScore.label("HighScore");
		mHighScore.horizontalFillType(FILLTYPE.THIRD_PARENT);
		mHighScore.readOnly(true);

		mNoHighScore = new MenuLabelEntry(pScreenManager, this);
		mNoHighScore.label("No fastest time");
		mNoHighScore.enabled(false);

		mMainMenuListBox.addMenuEntry(mHighScore);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(mNoHighScore);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lPlaySurvivaEntry);

		mLayouts.add(mMainMenuListBox);

		mSelectedLayoutIndex = 0;
		mSelectedEntryIndex = 5;

		mScreenPaddingTop = 30.f;
		mLayoutAlignment = LAYOUT_ALIGNMENT.RIGHT;
		mLayoutPaddingHorizontal = 50.f;

		mIsPopup = false;
		mShowBackgroundScreens = true;

		mBlockMouseInputInBackground = false;

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

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
		}
	}
}
