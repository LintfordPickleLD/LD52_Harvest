package lintfordpickle.harvest.renderers.editor;

import lintfordpickle.harvest.controllers.editor.EditorSceneController;
import lintfordpickle.harvest.data.assets.SceneAssetInstance;
import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class EditorSceneRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Editor Scene Renderer";

	public static final int WORLD_WIDTH_IN_PX = 1024; // TODO: This has to be removed into a data file.

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private EditorSceneController mSceneController;
	private ResourceManager mResourceManager;

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

	public EditorSceneRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mSceneController = (EditorSceneController) core.controllerManager().getControllerByNameRequired(EditorSceneController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mResourceManager = resourceManager;

	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mResourceManager = null;
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lLayersManager = mSceneController.sceneData().layersManager();
		final var lLayers = lLayersManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			final var lSceneLayer = lLayers.get(i);
			if (lSceneLayer instanceof SceneAnimationLayer) {
				var lAnimationSceneLayer = (SceneAnimationLayer) lSceneLayer;

				final var lLayerAnimations = lAnimationSceneLayer.spriteAssets();
				final var lNumAnimations = lLayerAnimations.size();
				for (int j = 0; j < lNumAnimations; j++) {
					final var lAsset = lLayerAnimations.get(j);

					// TODO: rework this
					if (lAsset.spriteInstance == null) {
						if (lAsset.spriteStatus < 2) {
							final var lAssetDef = lAsset.definition;
							final var lSpritesheetDef = core.resources().spriteSheetManager().getSpriteSheet(lAssetDef.spritesheetDefinitionName, mEntityGroupUid);
							if (lSpritesheetDef != null) {
								lAsset.spriteInstance = lSpritesheetDef.getSpriteInstance(lAssetDef.spriteName);
							} else {
								lAsset.spriteStatus = SceneAssetInstance.TEXTURE_FAILED;
							}
						}

						continue;
					}

					lAsset.spriteInstance.update(core);
				}
			}
		}
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

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	protected void drawTextureLayer(LintfordCore core, SceneTextureLayer layer) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		if (layer.texture != null) {
			final var aabb = core.HUD().boundingRectangle();
			final var aabb_c = core.gameCamera().boundingRectangle();

			final var lCamOffsetX = aabb_c.centerX() * layer.translationSpeedModX;
			final var lCamOffsetY = aabb_c.centerY() * layer.translationSpeedModY;

			lSpriteBatch.begin(core.gameCamera());

			final var lSrcX = 0;
			final var lSrcY = 0;
			final var lSrcW = layer.texture.getTextureWidth();
			final var lSrcH = layer.texture.getTextureHeight();

			final var lDstX = layer.centerX - layer.texture.getTextureWidth() * layer.scaleX * .5f;
			final var lDstY = layer.centerY - layer.texture.getTextureHeight() * layer.scaleY * .5f;
			final var lDstWidth = layer.texture.getTextureWidth() * layer.scaleX;
			final var lDstHeight = layer.texture.getTextureHeight() * layer.scaleY;

			lSpriteBatch.draw(layer.texture, lSrcX, lSrcY, lSrcW, lSrcH, lDstX, lDstY, lDstWidth, lDstHeight, -.01f, ColorConstants.WHITE);

			lSpriteBatch.end();
			return;
		}

		if (layer.textureStatus == SceneTextureLayer.TEXTURE_UNLOADED) {
			layer.texture = mResourceManager.textureManager().loadTexture(layer.textureName(), layer.textureFilepath(), entityGroupID());
			if (mResourceManager.textureManager().textureNotFound().equals(layer.texture)) {
				layer.texture = null;

				return;
			}

			if (layer.texture == null) {
				layer.textureStatus = SceneTextureLayer.TEXTURE_FAILED;
			}
		}

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
