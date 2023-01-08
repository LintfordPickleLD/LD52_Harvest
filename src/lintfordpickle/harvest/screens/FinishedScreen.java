package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.screens.game.GameScreen;
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

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public FinishedScreen(ScreenManager screenManager, boolean diedThroughLives, int foodDeliverd) {
		super(screenManager, "Game Over");

		this.diedThroughLives = diedThroughLives;
		this.foodAmountDelivered = foodDeliverd;

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

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_RESTART:
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new GameScreen(screenManager(), true)));
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

		final var lGameOverText = "Before you were decommised, you were able to provide the city with addition food";
		final var lTextWidth = lFont.getStringWidth(lGameOverText);

		lFont.begin(core.HUD());
		lFont.drawText(lGameOverText, -lTextWidth / 2, -100, -0.01f, 1.f);
		lFont.end();

		mPaddingTopNormalized = 250.f;

	}
}
