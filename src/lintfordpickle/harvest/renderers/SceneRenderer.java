package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.backgrounds.SceneLayer;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class SceneRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Scene Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private SceneController mSceneController;

	private SpriteSheetDefinition mSceneSpritesheet;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mSceneController != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SceneRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mSceneController = (SceneController) core.controllerManager().getControllerByNameRequired(SceneController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mSceneSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_SCENE", ConstantsGame.GAME_RESOURCE_GROUP_ID);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

	}

	@Override
	public void draw(LintfordCore core) {
		final var lSceneManager = mSceneController.sceneManager();
		final var lLayers = lSceneManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			drawScene(core, lLayers.get(i));
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void drawScene(LintfordCore core, SceneLayer sceneLayer) {
		if (sceneLayer == null)
			return;

		final var lSpriteFrame = mSceneSpritesheet.getSpriteFrame(sceneLayer.spriteName);
		if (lSpriteFrame == null)
			return;

		final var lTexture = mSceneSpritesheet.texture();

		final var lTextureBatch = mRendererManager.uiTextureBatch();
		lTextureBatch.begin(core.gameCamera());

		final float lDstW = 960 * 2;
		final float lDstH = 540 * 2;
		
		final float lDstX = -lDstW / 2.f;
		final float lDstY = -lDstH / 2.f;
		

		lTextureBatch.draw(lTexture, lSpriteFrame, lDstX, lDstY, lDstW, lDstH, -0.01f, ColorConstants.WHITE);

		lTextureBatch.end();
	}
}
