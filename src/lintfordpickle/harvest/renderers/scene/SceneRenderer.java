package lintfordpickle.harvest.renderers.scene;

import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class SceneRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Scene Renderer";

	public static final int WORLD_WIDTH_IN_PX = 1024; // TODO: This has to be removed into a data file.

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	protected SceneController mSceneController;

	private SpriteSheetDefinition mPropsSpritesheetDefintion;

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

		final var lLayersManager = mSceneController.sceneData().layersManager();
		final var lLayers = lLayersManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {

		}

		// TODO: Load the resources used by the scene

	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		// TODO: Unload the resources used by the scene

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		// TODO: Check out the EditorSceneRenderer
//		final var lLayersManager = mSceneController.sceneData().layersManager();
//		final var lLayers = lLayersManager.layers();
//		final var lNumLayers = lLayers.size();
//		for (int i = 0; i < lNumLayers; i++) {
//			final var lSceneLayer = lLayers.get(i);
//			if (lSceneLayer instanceof SceneAnimationLayer) {
//				var lAnimationSceneLayer = (SceneAnimationLayer) lSceneLayer;
//
//				final var lLayerAnimations = lAnimationSceneLayer.spriteAssets();
//				final var lNumAnimations = lLayerAnimations.size();
//				for (int j = 0; j < lNumAnimations; j++) {
//					lLayerAnimations.get(j).update(core);
//				}
//			}
//		}
	}

	@Override
	public void draw(LintfordCore core) {
		final var lLayersManager = mSceneController.sceneData().layersManager();
		final var lLayers = lLayersManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			final var lSceneLayer = lLayers.get(i);

			if (lSceneLayer instanceof SceneTextureLayer) {
				drawTextureLayer(core, (SceneTextureLayer) lSceneLayer);
				continue;
			}

			if (lSceneLayer instanceof SceneAnimationLayer) {
				drawAnimationLayer(core, (SceneAnimationLayer) lSceneLayer);
				continue;
			}

			if (lSceneLayer instanceof SceneNoiseLayer) {
				drawNoiseLayer(core, (SceneNoiseLayer) lSceneLayer);
				continue;
			}
		}
	}

	protected void drawTextureLayer(LintfordCore core, SceneTextureLayer layer) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		lSpriteBatch.begin(core.gameCamera());

		// draw texture scene layer

		lSpriteBatch.end();
	}

	protected void drawAnimationLayer(LintfordCore core, SceneAnimationLayer layer) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		lSpriteBatch.begin(core.gameCamera());

		final var lLayerAnimations = layer.spriteAssets();
		final var lNumAnimations = lLayerAnimations.size();
		for (int i = 0; i < lNumAnimations; i++) {
			final var lSprite = lLayerAnimations.get(i);

			// lSpriteBatch.draw(mPropsSpritesheetDefintion, lSprite, lSprite, 2.f, -0.01f, ColorConstants.WHITE);
		}

		lSpriteBatch.end();
	}

	protected void drawNoiseLayer(LintfordCore core, SceneNoiseLayer layer) {

	}

}
