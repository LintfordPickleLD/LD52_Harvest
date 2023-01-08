package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.PlatformsController;
import lintfordpickle.harvest.data.platforms.Platform;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.SpriteFrame;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class PlatformsRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Platforms Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PlatformsController mPlatformsController;
	private SpriteSheetDefinition mPlatformsSpritesheet;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mPlatformsController != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlatformsRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mPlatformsController = (PlatformsController) core.controllerManager().getControllerByNameRequired(PlatformsController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mPlatformsSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_PROPS", ConstantsGame.GAME_RESOURCE_GROUP_ID);

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
		final var lPlatformManager = mPlatformsController.platformManager();
		final var lPlatformsList = lPlatformManager.platforms();
		final int lNumPlatform = lPlatformsList.size();
		for (int i = 0; i < lNumPlatform; i++) {
			final var lPlatform = lPlatformsList.get(i);

			drawPlatform(core, lPlatform);
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void drawPlatform(LintfordCore core, Platform platform) {
		if (platform == null)
			return;

		final var lSpriteFrame = getPlatformSpriteFrame(platform);

		final var lSpriteBatch = mRendererManager.uiSpriteBatch();
		lSpriteBatch.begin(core.gameCamera());

		final var lWhiteWithAlpha = ColorConstants.getColor(0.95f, 0.12f, 0.03f, 0.6f);
		lSpriteBatch.draw(mPlatformsSpritesheet, lSpriteFrame, platform, -0.01f, lWhiteWithAlpha);

		if (platform.isStockFull) {
			lSpriteBatch.draw(mPlatformsSpritesheet, mPlatformsSpritesheet.getSpriteFrame("TEXTURELIGHTGLOW"), platform.x(), platform.y(), 8, 8, -0.01f, ColorConstants.GREEN);
		} else {
			if (platform.isRefillingStock)
				lSpriteBatch.draw(mPlatformsSpritesheet, mPlatformsSpritesheet.getSpriteFrame("TEXTURELIGHTGLOW"), platform.x(), platform.y(), 8, 8, -0.01f, ColorConstants.YELLOW);
			else
				lSpriteBatch.draw(mPlatformsSpritesheet, mPlatformsSpritesheet.getSpriteFrame("TEXTURELIGHTGLOW"), platform.x(), platform.y(), 8, 8, -0.01f, ColorConstants.RED);
		}

		lSpriteBatch.end();
	}

	private SpriteFrame getPlatformSpriteFrame(Platform platform) {
		// TODO: Pick the correct spriteframe based on platform type and state
		return mPlatformsSpritesheet.getSpriteFrame("WHITE");
	}
}
