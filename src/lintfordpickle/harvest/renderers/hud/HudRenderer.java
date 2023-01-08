package lintfordpickle.harvest.renderers.hud;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.GameStateController;
import lintfordpickle.harvest.controllers.PlatformsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class HudRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private PlatformsController mPlatformsController;

	private SpriteSheetDefinition mHudSpritesheet;

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

	public HudRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		final var lControllerManager = core.controllerManager();

		mPlatformsController = (PlatformsController) lControllerManager.getControllerByNameRequired(PlatformsController.CONTROLLER_NAME, entityGroupID());
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

		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		lSpriteBatch.begin(core.HUD());
//		lSpriteBatch.draw(mHudSpritesheet, mHudSpritesheet.getSpriteFrame("TEXTURE_HIDE_LAYER"), 0, 0, 32, 32, -0.01f, ColorConstants.WHITE);
		lSpriteBatch.end();

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

}
