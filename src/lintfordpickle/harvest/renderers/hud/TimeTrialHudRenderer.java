package lintfordpickle.harvest.renderers.hud;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.GameStateController;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.data.game.GameState;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.batching.SpriteBatch;
import net.lintford.library.core.graphics.fonts.FontUnit;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.time.TimeConstants;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;
import net.lintford.library.renderers.windows.components.UiBar;

public class TimeTrialHudRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private ShipController mShipController;

	private GameState mGameState;
	private SpriteSheetDefinition mHudSpritesheet;
	private UiBar mHealthBar;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mGameStateController != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public TimeTrialHudRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);

		mHealthBar = new UiBar(0.f, 100.f);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		final var lControllerManager = core.controllerManager();

		mShipController = (ShipController) lControllerManager.getControllerByNameRequired(ShipController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());

		mGameState = mGameStateController.gameState();
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mHudSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_HUD", ConstantsGame.GAME_RESOURCE_GROUP_ID);

	}

	@Override
	public void unloadResources() {
		super.unloadResources();
	}

	@Override
	public boolean handleInput(LintfordCore core) {

		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

	}

	@Override
	public void draw(LintfordCore core) {
		final var lHudBoundingBox = core.HUD().boundingRectangle();

		final var lFontUnit = mRendererManager.uiTitleFont();
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		lSpriteBatch.begin(core.HUD());

		final var lTimeRemaining = mGameStateController.gameState().gameTimer;

		var tempTime = lTimeRemaining;
		final var lTotalMinutes = (int) tempTime / TimeConstants.MillisPerMinute;
		tempTime -= lTotalMinutes * TimeConstants.MillisPerMinute;
		final var lTotalSeconds = (int) tempTime / TimeConstants.MillisPerSecond;
		tempTime -= lTotalSeconds * TimeConstants.MillisPerSecond;

		final var lTimeFormatted = String.format(java.util.Locale.US, "%02d", lTotalMinutes) + ":" + String.format(java.util.Locale.US, "%02d", lTotalSeconds) + ":"
				+ String.format(java.util.Locale.US, "%1.0f", tempTime);

		lFontUnit.begin(core.HUD());
		lSpriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_CLOCK"), lHudBoundingBox.left() + 5.f, lHudBoundingBox.top() + 5.0f, 32, 32, -0.01f, ColorConstants.WHITE);
		lFontUnit.drawText(": " + lTimeFormatted, lHudBoundingBox.left() + 38.f, lHudBoundingBox.top() + 5.0f, -0.01f, 1.f);

		float lGridPositionY = lHudBoundingBox.top() + 16.0f;

		// Player Stats
		drawPlatformStatus(core, lFontUnit, lSpriteBatch, lHudBoundingBox.left() + 10.f, lGridPositionY += 32.f, 1);
		drawPlatformStatus(core, lFontUnit, lSpriteBatch, lHudBoundingBox.left() + 10.f, lGridPositionY += 32.f, 2);
		drawPlatformStatus(core, lFontUnit, lSpriteBatch, lHudBoundingBox.left() + 10.f, lGridPositionY += 32.f, 3);
		drawPlatformStatus(core, lFontUnit, lSpriteBatch, lHudBoundingBox.left() + 10.f, lGridPositionY += 32.f, 4);

		lSpriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_SPANNER"), lHudBoundingBox.right() - 5.f - 32f, lHudBoundingBox.top() + 5.0f, 32, 32, -0.01f, ColorConstants.WHITE);
		final var lShip = mShipController.shipManager().playerShip();

		final var lHealthBarWidth = 196.f - 32.f;
		mHealthBar.innerBorderPadding(2);
		mHealthBar.setInnerColor(0.92f, 0.07f, 0.04f, 1.f);
		mHealthBar.setDestRectangle(lHudBoundingBox.right() - 10 - lHealthBarWidth - 32, lHudBoundingBox.top() + 12, lHealthBarWidth, 20);
		mHealthBar.setCurrentValue(lShip.health);
		mHealthBar.setMinMax(0, 100);
		mHealthBar.draw(core, lSpriteBatch, lFontUnit, -0.01f);

		lFontUnit.end();
		lSpriteBatch.end();

	}

	private void drawPlatformStatus(LintfordCore core, FontUnit font, SpriteBatch spriteBatch, float x, float y, int platformNr) {
		font.drawText(String.valueOf(platformNr), x, y, -0.01f, 1.f);

		// TODO: Show both players
		final var lPlayerScorecard = mGameState.getScoreCard(0);

		final var lWaterColor = lPlayerScorecard.isPlatformWatered(platformNr) ? ColorConstants.WHITE : ColorConstants.GREY_DARK;
		spriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_WATER"), x + 24.f, y, 32, 32, -0.01f, lWaterColor);

		final var lWheatColor = lPlayerScorecard.isPlatformHarvested(platformNr) ? ColorConstants.WHITE : ColorConstants.GREY_DARK;
		spriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_WHEAT"), x + 48.f, y, 32, 32, -0.01f, lWheatColor);
	}

}