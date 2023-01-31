package lintfordpickle.harvest.screens.landing;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.SurvivalGameScreen;
import net.lintford.library.core.graphics.Color;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
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
		mMainMenuListBox.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);
		mMainMenuListBox.setDrawBackground(true, new Color(0.02f, 0.12f, 0.15f, 0.13f));

		// As we know the game canvas size
		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlaySurvivaEntry = new MenuEntry(mScreenManager, mMainMenuListBox, "Start");
		lPlaySurvivaEntry.desiredWidth(lDesiredEntryWidth);
		lPlaySurvivaEntry.desiredHeight(lDesiredEntryHeight);
		lPlaySurvivaEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_SURVIVAL);

		mHighScore = new MenuInputEntry(pScreenManager, mMainMenuListBox);
		mHighScore.label("HighScore");
		mHighScore.horizontalFillType(FILLTYPE.TAKE_DESIRED_SIZE);
		mHighScore.canHoverOver(false);

		mNoHighScore = new MenuLabelEntry(pScreenManager, mMainMenuListBox);
		mNoHighScore.label("No fastest time");

		mMainMenuListBox.addMenuEntry(mHighScore);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(mNoHighScore);
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(MenuEntry.menuSeparator());
		mMainMenuListBox.addMenuEntry(lPlaySurvivaEntry);

		mLayouts.add(mMainMenuListBox);

		mMenuScreenWidthScaleFactor = 0.5f;
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

		// TODO: Implement high-scores in survival mode

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
		}
	}
}
