package lintfordpickle.harvest.renderers.hud;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.GameStateController;
import lintfordpickle.harvest.controllers.PlatformsController;
import lintfordpickle.harvest.controllers.ShipController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;
import net.lintford.library.renderers.windows.components.UiBar;

public class MinimapRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Minimap Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private ShipController mShipController;
	private PlatformsController mPlatformsController;

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

	public MinimapRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);

		mHealthBar = new UiBar(0.f, 100.f);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		final var lControllerManager = core.controllerManager();

		mPlatformsController = (PlatformsController) lControllerManager.getControllerByNameRequired(PlatformsController.CONTROLLER_NAME, entityGroupID());
		mShipController = (ShipController) lControllerManager.getControllerByNameRequired(ShipController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());
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

		final var lTimeRemaining = mGameStateController.gameState().gameCountdownTimerUntilDeath / 1000.f;
		final var lTimeFormatted = String.format(java.util.Locale.US, "%.1f", lTimeRemaining);

		lFontUnit.begin(core.HUD());
		lSpriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_CLOCK"), lHudBoundingBox.left() + 5.f, lHudBoundingBox.top() + 5.0f, 32, 32, -0.01f, ColorConstants.WHITE);
		lFontUnit.drawText(": " + lTimeFormatted, lHudBoundingBox.left() + 38.f, lHudBoundingBox.top() + 5.0f, -0.01f, 1.f);

		lSpriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_WHEAT"), lHudBoundingBox.left() + 5.f, lHudBoundingBox.top() + 38.0f, 32, 32, -0.01f, ColorConstants.WHITE);
		lFontUnit.drawText(": " + mGameStateController.gameState().foodDelivered, lHudBoundingBox.left() + 38.f, lHudBoundingBox.top() + 40.0f, -0.01f, 1.f);

		lSpriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_SPANNER"), lHudBoundingBox.right() - 5.f - 32f, lHudBoundingBox.top() + 5.0f, 32, 32, -0.01f, ColorConstants.WHITE);
		final var lShip = mShipController.shipManager().playerShip();

		mHealthBar.innerBorderPadding(2);
		mHealthBar.setInnerColor(0.92f, 0.07f, 0.04f, 1.f);
		mHealthBar.setDestRectangle(lHudBoundingBox.right() - 5 - 205 - 32, lHudBoundingBox.top() + 12, 200, 20);
		mHealthBar.setCurrentValue(lShip.health);
		mHealthBar.setMinMax(0, 100);
		mHealthBar.draw(core, lSpriteBatch, lFontUnit, -0.01f);

		lFontUnit.end();
		lSpriteBatch.end();

	}

}
