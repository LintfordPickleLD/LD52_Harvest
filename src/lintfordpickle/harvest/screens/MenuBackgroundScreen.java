package lintfordpickle.harvest.screens;

import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.fonts.FontUnit;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.screenmanager.Screen;
import net.lintford.library.screenmanager.ScreenManager;

public class MenuBackgroundScreen extends Screen {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private Texture mBackgroundTexture;

	private FontUnit mGameTitleFont;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public MenuBackgroundScreen(ScreenManager screenManager) {
		super(screenManager);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mBackgroundTexture = resourceManager.textureManager().loadTexture("TEXTURE_MENU_BACKGROUND", "res/textures/textureMenuBackground.png", entityGroupUid());

		mGameTitleFont = resourceManager.fontManager().getFontUnit("FONT_NULSCHOCK_22");
	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		GL11.glClearColor(0.06f, 0.18f, 0.11f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		final var lHudBoundingBox = core.HUD().boundingRectangle();
		final var lTextureBatch = rendererManager().uiTextureBatch();

		lTextureBatch.begin(core.HUD());
		lTextureBatch.draw(mBackgroundTexture, 0, 0, 960, 540, lHudBoundingBox.left(), lHudBoundingBox.top(), lHudBoundingBox.width(), lHudBoundingBox.height(), -0.01f, ColorConstants.WHITE);
		lTextureBatch.end();

		mGameTitleFont.begin(core.HUD());
		mGameTitleFont.drawText(ConstantsGame.FOOTER_TEXT, lHudBoundingBox.left() + 5.f, lHudBoundingBox.bottom() - 5.f - mGameTitleFont.fontHeight(), -0.01f, 1.f);
		mGameTitleFont.end();
	}
}
