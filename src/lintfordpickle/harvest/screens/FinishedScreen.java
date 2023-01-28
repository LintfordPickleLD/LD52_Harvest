package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.game.SurvivalGameScreen;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class FinishedScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int SCREEN_BUTTON_RESTART = 11;
	private static final int SCREEN_BUTTON_EXIT = 12;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private boolean diedThroughLives;
	private int foodAmountDelivered;

	private PlayerManager mPlayerManager;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public FinishedScreen(ScreenManager screenManager, PlayerManager playerManager, boolean diedThroughLives, int foodDeliverd) {
		super(screenManager, "Game Over");

		this.diedThroughLives = diedThroughLives;
		this.foodAmountDelivered = foodDeliverd;

		mPlayerManager = playerManager;

		final var lLayout = new ListLayout(this);

		// ---

		final var lOptionsEntry = new MenuEntry(mScreenManager, lLayout, "Restart");
		lOptionsEntry.registerClickListener(this, SCREEN_BUTTON_RESTART);

		final var lCreditsEntry = new MenuEntry(mScreenManager, lLayout, "Exit");
		lCreditsEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lOptionsEntry);
		lLayout.addMenuEntry(lCreditsEntry);

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
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new SurvivalGameScreen(screenManager(), mPlayerManager)));
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

		final var lHeaderText = "Food Delivered " + foodAmountDelivered;
		final var lHeaderTextWidth = lTitleFont.getStringWidth(lHeaderText);

		lTitleFont.begin(core.HUD());
		lTitleFont.drawText(lHeaderText, -lHeaderTextWidth / 2, -200, -0.01f, 1.f);
		lTitleFont.end();

		final var lGameOverText0 = "You totaled your ship!";
		final var lTextWidth0 = lFont.getStringWidth(lGameOverText0);

		lFont.begin(core.HUD());
		lFont.drawText(lGameOverText0, -lTextWidth0 / 2, -100, -0.01f, 1.f);

		final var lGameOverText1 = "You were able to provide the city with " + foodAmountDelivered + "tn addition food";
		final var lTextWidth1 = lFont.getStringWidth(lGameOverText1);

		lFont.begin(core.HUD());
		lFont.drawText(lGameOverText1, -lTextWidth1 / 2, -80, -0.01f, 1.f);
		lFont.end();

		mPaddingTopNormalized = 250.f;

	}
}
