package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.PlatformController;
import lintfordpickle.harvest.data.platforms.Platform;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class PlatformsRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Platforms Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PlatformController mPlatformsController;
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
		mPlatformsController = (PlatformController) core.controllerManager().getControllerByNameRequired(PlatformController.CONTROLLER_NAME, entityGroupID());
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

		if (lSpriteFrame == null)
			return;

		lSpriteFrame.update(core);

		final var lSpriteBatch = mRendererManager.uiSpriteBatch();
		lSpriteBatch.begin(core.gameCamera());

		final var lWhiteWithAlpha = ColorConstants.getColor(1.f, 1.f, 1.f, 1.f);
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

	private SpriteInstance getPlatformSpriteFrame(Platform platform) {
		final String FARM_FULL_NAME = "textureWheatFull";
		final String FARM_HALF_NAME = "TEXTUREWHEATHALF";
		final String FARM_EMPTY_NAME = "TEXTUREWHEATEMPTY";

		final String WATER_FULL_NAME = "TEXTUREWATERFULL";
		final String WATER_EMPTY_NAME = "TEXTUREWATEREMPTY";

		final String WAREHOUSE_NAME = "TEXTUREWAREHOUSE";

		switch (platform.platformType) {
		case Farm:
			if (platform.isStockFull) {
				if (platform.sprite != null) {
					if (platform.spriteName != null && platform.spriteName.equals(FARM_FULL_NAME))
						return platform.sprite;
				}

				platform.spriteName = FARM_FULL_NAME;
				platform.sprite = mPlatformsSpritesheet.getSpriteInstance(FARM_FULL_NAME);
				return platform.sprite;
			}

			if (platform.isRefillingStock) {
				if (platform.sprite != null) {
					if (platform.spriteName != null && platform.spriteName.equals(FARM_HALF_NAME))
						return platform.sprite;
				}

				platform.spriteName = FARM_HALF_NAME;
				platform.sprite = mPlatformsSpritesheet.getSpriteInstance(FARM_HALF_NAME);
				return platform.sprite;
			}

			if (platform.sprite != null) {
				if (platform.spriteName != null && platform.spriteName.equals(FARM_EMPTY_NAME))
					return platform.sprite;
			}

			platform.spriteName = FARM_EMPTY_NAME;
			platform.sprite = mPlatformsSpritesheet.getSpriteInstance(FARM_EMPTY_NAME);
			return platform.sprite;

		case Water:
			if (platform.isStockFull) {
				if (platform.sprite != null) {
					if (platform.spriteName != null && platform.spriteName.equals(WATER_FULL_NAME))
						return platform.sprite;

					platform.spriteName = WATER_FULL_NAME;
					platform.sprite = mPlatformsSpritesheet.getSpriteInstance(WATER_FULL_NAME);
					return platform.sprite;
				}
			}

			if (platform.spriteName != null && platform.spriteName.equals(WATER_EMPTY_NAME))
				return platform.sprite;

			platform.spriteName = WATER_EMPTY_NAME;
			platform.sprite = mPlatformsSpritesheet.getSpriteInstance(WATER_EMPTY_NAME);
			return platform.sprite;

		default: // warehouse
			if (platform.spriteName != null && platform.spriteName.equals(WAREHOUSE_NAME))
				return platform.sprite;

			platform.spriteName = WAREHOUSE_NAME;
			platform.sprite = mPlatformsSpritesheet.getSpriteInstance(WAREHOUSE_NAME);
			return platform.sprite;
		}

	}
}
