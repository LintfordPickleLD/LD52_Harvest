package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.scene.backgrounds.SceneLayer;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
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

		final var lSceneManager = mSceneController.sceneManager();
		final var lLayers = lSceneManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			final var lLayer = lLayers.get(i);
			lLayer.texture = resourceManager.textureManager().getTexture(lLayer.textureName, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		}
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

		final var lTexture = sceneLayer.texture;
		if (lTexture == null)
			return;

		final var lTextureBatch = mRendererManager.uiTextureBatch();
		lTextureBatch.begin(core.gameCamera());

		final int srcX = 0;
		final int srcY = 0;
		final int srcW = 1024;
		final int srcH = 1024;

		final float lDstW = 1024 * 2.f;
		final float lDstH = 1024 * 2.f;

		final float lDstX = -lDstW / 2.f;
		final float lDstY = -lDstH / 2.f;

		lTextureBatch.draw(lTexture, srcX, srcY, srcW, srcH, lDstX, lDstY, lDstW, lDstH, -0.01f, ColorConstants.WHITE);

		lTextureBatch.end();
	}
}
