package lintfordpickle.harvest.renderers.hud;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.GameStateController;
import lintfordpickle.harvest.controllers.PlatformController;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.data.platforms.PlatformType;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetManager;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.core.maths.MathHelper;
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
	private PlatformController mPlatformsController;

	private SpriteSheetDefinition mCoreSpritesheet;
	private Texture mMinimapTexture;

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

		mPlatformsController = (PlatformController) lControllerManager.getControllerByNameRequired(PlatformController.CONTROLLER_NAME, entityGroupID());
		mShipController = (ShipController) lControllerManager.getControllerByNameRequired(ShipController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mCoreSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet(SpriteSheetManager.CORE_SPRITESHEET_NAME, LintfordCore.CORE_ENTITY_GROUP_ID);
		mMinimapTexture = resourceManager.textureManager().getTexture("TEXTURE_MINIMAP", ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();
	}

	@Override
	public boolean handleInput(LintfordCore core) {

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_M)) {
			// TODO: Toggle minimap
		}

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

		final var lSize = 196;

		final var lMinimapPositionX = lHudBoundingBox.right() - 10.f - lSize;
		final var lMinimapPositionY = lHudBoundingBox.top() + 48.0f;

		lFontUnit.begin(core.HUD());
		final var lMinimapHudColor = ColorConstants.getWhiteWithAlpha(0.75f);
		lSpriteBatch.draw(mMinimapTexture, 0, 0, 196, 196, lMinimapPositionX, lMinimapPositionY, lSize, lSize, -0.01f, lMinimapHudColor);

		final var lPlatformManager = mPlatformsController.platformManager();
		final var lPlatforms = lPlatformManager.platforms();
		final var lNumPlatforms = lPlatforms.size();
		for (int i = 0; i < lNumPlatforms; i++) {
			final var lPlatform = lPlatforms.get(i);
			final var lWorldPositionX = lPlatform.x();
			final var lWorldPositionY = lPlatform.y();

			final var lScaledPositionX = MathHelper.scaleToRange(lWorldPositionX, -1024, 1024, 0, lSize);
			final var lScaledPositionY = MathHelper.scaleToRange(lWorldPositionY, -1024, 1024, 0, lSize);

			var lShipColor = ColorConstants.GREEN;
			if (lPlatform.platformType == PlatformType.Warehouse) {
				lShipColor = ColorConstants.YELLOW;
			} else if (lPlatform.platformType == PlatformType.Water) {
				lShipColor = ColorConstants.BLUE;
			}

			lSpriteBatch.draw(mCoreSpritesheet, mCoreSpritesheet.getSpriteFrame("TEXTURE_WHITE"), lMinimapPositionX + lScaledPositionX, lMinimapPositionY + lScaledPositionY, 4, 4, -0.01f, lShipColor);
		}

		final var lShipManager = mShipController.shipManager();
		final var lShips = lShipManager.ships();
		final var lNumShips = lShips.size();
		for (int i = 0; i < lNumShips; i++) {
			final var lShip = lShips.get(i);
			final var lWorldPositionX = lShip.body().x * ConstantsPhysics.UnitsToPixels();
			final var lWorldPositionY = lShip.body().y * ConstantsPhysics.UnitsToPixels();

			final var lScaledPositionX = MathHelper.scaleToRange(lWorldPositionX, -1024, 1024, 0, lSize);
			final var lScaledPositionY = MathHelper.scaleToRange(lWorldPositionY, -1024, 1024, 0, lSize);

			var lShipColor = lShip.isPlayerControlled ? ColorConstants.RED : ColorConstants.GREY_DARK;
			lSpriteBatch.draw(mCoreSpritesheet, mCoreSpritesheet.getSpriteFrame("TEXTURE_WHITE"), lMinimapPositionX + lScaledPositionX, lMinimapPositionY + lScaledPositionY, 4, 4, -0.01f, lShipColor);
		}

		lFontUnit.end();
		lSpriteBatch.end();

	}

}
