package lintfordpickle.harvest.screens.endscreens;

import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.MainMenu;
import lintfordpickle.harvest.screens.game.TimeTrialGameScreen;
import lintfordpickle.harvest.screens.menu.MenuBackgroundScreen;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.graphics.textures.Texture;
import net.lintfordlib.core.time.TimeConstants;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.layouts.ListLayout;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

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
	private boolean mSurvived;

	private Texture mMenuTextureWrecked;
	private Texture mMenuTextureCompleted;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public TimeTrialEndScreen(ScreenManager screenManager, PlayerManager playerManager, boolean survived, float totalTimeInMs, boolean fastestTimeRecorded) {
		super(screenManager, "");

		mSurvived = survived;

		mPlayerManager = playerManager;
		mTotalTimeInMs = totalTimeInMs;

		final var lLayout = new ListLayout(this);

		// ---

		var tempTime = mTotalTimeInMs;
		final var lTotalMinutes = (int) tempTime / TimeConstants.MillisPerMinute;
		tempTime -= lTotalMinutes * TimeConstants.MillisPerMinute;
		final var lTotalSeconds = (int) tempTime / TimeConstants.MillisPerSecond;
		tempTime -= lTotalSeconds * TimeConstants.MillisPerSecond;

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
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mMenuTextureWrecked = resourceManager.textureManager().loadTexture("TEXTURE_MENU_WRECKED", "res/textures/textureTextWrecked.png", entityGroupUid());
		mMenuTextureCompleted = resourceManager.textureManager().loadTexture("TEXTURE_MENU_COMPLETED", "res/textures/textureTextComplete.png", entityGroupUid());
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mMenuTextureWrecked = null;
		mMenuTextureCompleted = null;
	}

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

		final var lTextureBatch = rendererManager().uiSpriteBatch();
		final var lUiStructureController = mScreenManager.UiStructureController();
		final var lHeaderRect = lUiStructureController.menuTitleRectangle();

		lFont.begin(core.HUD());
		mSurvived = true;
		if (mSurvived) {
			if (mMenuTextureWrecked != null) {
				lTextureBatch.begin(core.HUD());

				final float logoWidth = mMenuTextureCompleted.getTextureWidth();
				final float logoHeight = mMenuTextureCompleted.getTextureHeight();

				lTextureBatch.draw(mMenuTextureCompleted, 0, 0, logoWidth, logoHeight, -logoWidth * .5f, lHeaderRect.top(), logoWidth, logoHeight, -0.01f, screenColor);
				lTextureBatch.end();
			}

			var tempTime = mTotalTimeInMs;
			final var lTotalMinutes = (int) tempTime / TimeConstants.MillisPerMinute;
			tempTime -= lTotalMinutes * TimeConstants.MillisPerMinute;
			final var lTotalSeconds = (int) tempTime / TimeConstants.MillisPerSecond;
			tempTime -= lTotalSeconds * TimeConstants.MillisPerSecond;

			final var lHeaderText = "TIME: " + lTotalMinutes + ":" + lTotalSeconds + ":" + (int) tempTime;
			final var lHeaderTextWidth = lTitleFont.getStringWidth(lHeaderText);
			final var lScreenHeight = core.config().display().windowHeight();

			final var lTextTitleHeight = -lScreenHeight / 4.f;
			mMenuHeaderPadding = lScreenHeight / 20.f;

			lTitleFont.begin(core.HUD());
			lTitleFont.drawText(lHeaderText, -lHeaderTextWidth / 2, lTextTitleHeight, -0.01f, 1.f);
			lTitleFont.end();

			final var lGameOverText0 = "Well Done!";
			final var lTextWidth0 = lFont.getStringWidth(lGameOverText0);
			lFont.drawText(lGameOverText0, -lTextWidth0 / 2, lTextTitleHeight + 50, -0.01f, 1.f);

			final var isNewTopTime = true;
			if (isNewTopTime) {
				final var lGameOverText1 = "You have set a new record time";
				final var lTextWidth1 = lFont.getStringWidth(lGameOverText1);
				lFont.drawText(lGameOverText1, -lTextWidth1 / 2, lTextTitleHeight + 75, -0.01f, 1.f);
			}

		} else {
			if (mMenuTextureWrecked != null) {
				lTextureBatch.begin(core.HUD());

				final float logoWidth = mMenuTextureWrecked.getTextureWidth();
				final float logoHeight = mMenuTextureWrecked.getTextureHeight();

				lTextureBatch.draw(mMenuTextureWrecked, 0, 0, logoWidth, logoHeight, -logoWidth * .5f, lHeaderRect.top(), logoWidth, logoHeight, -0.01f, screenColor);
				lTextureBatch.end();
			}

			final var lHeaderText = "Failed to deliver food";
			final var lHeaderTextWidth = lTitleFont.getStringWidth(lHeaderText);
			final var lScreenHeight = core.config().display().windowHeight();

			final var lTextTitleHeight = -lScreenHeight / 4.f;
			mMenuHeaderPadding = lScreenHeight / 20.f;

			lTitleFont.begin(core.HUD());
			lTitleFont.drawText(lHeaderText, -lHeaderTextWidth / 2, lTextTitleHeight, -0.01f, 1.f);
			lTitleFont.end();

			final var lGameOverText0 = "You totaled your ship!";
			final var lTextWidth0 = lFont.getStringWidth(lGameOverText0);
			lFont.drawText(lGameOverText0, -lTextWidth0 / 2, lTextTitleHeight + 50, -0.01f, 1.f);

		}

		lFont.end();

		mScreenPaddingTop = 250.f;
	}

}
