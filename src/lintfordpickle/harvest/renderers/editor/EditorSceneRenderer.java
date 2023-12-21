package lintfordpickle.harvest.renderers.editor;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.controllers.editor.EditorLayerController;
import lintfordpickle.harvest.controllers.editor.EditorSceneController;
import lintfordpickle.harvest.data.assets.SceneAssetInstance;
import lintfordpickle.harvest.data.editor.EditorLayersData;
import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import lintfordpickle.harvest.renderers.scene.NoiseLayerShader;
import net.lintfordLib.editor.controllers.EditorBrushController;
import net.lintfordLib.editor.data.EditorLayerBrush;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.geometry.FullScreenTexturedQuad;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class EditorSceneRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Editor Scene Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private EditorSceneController mSceneController;
	private ResourceManager mResourceManager;

	private EditorLayerController mEditorLayerController;
	private EditorBrushController mEditorBrushController;

	private FullScreenTexturedQuad mTexturedQuad;
	private NoiseLayerShader mNoiseLayerShader;

	private float mMouseX;
	private float mMouseY;

	private boolean mMouseDownLastFrame;
	private boolean mIsNewLeftClick;
	private float mMouseDownX;
	private float mMouseDownY;

	private boolean mRenderLayers;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mSceneController != null;

	}

	public boolean renderLayers() {
		return mRenderLayers;
	}

	public void renderLayers(boolean renderingEnabled) {
		mRenderLayers = renderingEnabled;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public EditorSceneRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);

		mTexturedQuad = new FullScreenTexturedQuad();
		mNoiseLayerShader = new NoiseLayerShader();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		final var lControllerManager = core.controllerManager();

		mSceneController = (EditorSceneController) lControllerManager.getControllerByNameRequired(EditorSceneController.CONTROLLER_NAME, entityGroupID());
		mEditorBrushController = (EditorBrushController) lControllerManager.getControllerByNameRequired(EditorBrushController.CONTROLLER_NAME, mEntityGroupUid);
		mEditorLayerController = (EditorLayerController) lControllerManager.getControllerByNameRequired(EditorLayerController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mResourceManager = resourceManager;
		mNoiseLayerShader.loadResources(resourceManager);
		mTexturedQuad.loadResources(resourceManager);

	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mResourceManager = null;
		mNoiseLayerShader.unbind();
		mTexturedQuad.unloadResources();
	}

	@Override
	public boolean handleInput(LintfordCore core) {
		var lInputHandled = super.handleInput(core);

		if (mEditorBrushController.isLayerActive(EditorLayersData.Layers) == false)
			return lInputHandled;

		final var leftMouseDown = core.input().mouse().tryAcquireMouseLeftClick(hashCode());
		mMouseX = core.gameCamera().getMouseWorldSpaceX();
		mMouseY = core.gameCamera().getMouseWorldSpaceY();

		// --- ACTIONS
		if (mEditorBrushController.brush().isActionSet() == false) {
			final var lSelectedLayer = mEditorLayerController.selectedLayer();
			final var lIsLayerSelected = lSelectedLayer != null;

			if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_G, this)) {
				mIsNewLeftClick = false;

				if (lIsLayerSelected) {
					mEditorBrushController.setAction(EditorLayerController.ACTION_OBJECT_TRANSLATE_SELECTED_LAYER, "Translate Layer", hashCode());

					mMouseDownX = mMouseX;
					mMouseDownY = mMouseY;
				}
			} else if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_W, this)) {
				mIsNewLeftClick = false;

				if (lIsLayerSelected) {
					mEditorBrushController.setAction(EditorLayerController.ACTION_OBJECT_SCALE_SELECTED_LAYER_X, "Scale Width", hashCode());

					mMouseDownX = mMouseX;
					mMouseDownY = mMouseY;
				}
			} else if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_H, this)) {
				mIsNewLeftClick = false;

				if (lIsLayerSelected) {
					mEditorBrushController.setAction(EditorLayerController.ACTION_OBJECT_SCALE_SELECTED_LAYER_Y, "Scale Height", hashCode());

					mMouseDownX = mMouseX;
					mMouseDownY = mMouseY;
				}
			}
		}

		final int lCurrentBrushAction = mEditorBrushController.brush().brushActionUid();
		// --- END ACTIONS

		// ----

		if (leftMouseDown && !mMouseDownLastFrame) {
			mIsNewLeftClick = true;
			mMouseDownLastFrame = true;
		}

		if (lCurrentBrushAction != EditorLayerBrush.NO_ACTION_UID) {

			final var lSelectedLayer = mEditorLayerController.selectedLayer();

			// do something with mouse
			switch (lCurrentBrushAction) {
			case EditorLayerController.ACTION_OBJECT_TRANSLATE_SELECTED_LAYER: {
				final var lTranslationAmtX = mMouseX - mMouseDownX;
				final var lTranslationAmtY = mMouseY - mMouseDownY;

				mMouseDownX = mMouseX;
				mMouseDownY = mMouseY;

				lSelectedLayer.centerX += lTranslationAmtX;
				lSelectedLayer.centerY += lTranslationAmtY;
			}

				break;

			case EditorLayerController.ACTION_OBJECT_SCALE_SELECTED_LAYER_X: {
				final var lTranslationAmtX = mMouseX - mMouseDownX;

				mMouseDownX = mMouseX;
				mMouseDownY = mMouseY;

				lSelectedLayer.width += lTranslationAmtX;
				break;
			}

			case EditorLayerController.ACTION_OBJECT_SCALE_SELECTED_LAYER_Y: {
				final var lTranslationAmtY = mMouseY - mMouseDownY;

				mMouseDownX = mMouseX;
				mMouseDownY = mMouseY;

				lSelectedLayer.height += lTranslationAmtY;
				break;
			}

			}

			if (mIsNewLeftClick) {
				mEditorBrushController.clearAction(hashCode());
			}

		} else if (leftMouseDown) {
			// mEditorLayerController.selectedLayer(null);
		}

		// ----

		if (!leftMouseDown && !mIsNewLeftClick) {
			mMouseDownLastFrame = false;
			mIsNewLeftClick = false;
		}

		return lInputHandled;
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
//							final var lSpritesheetDef = core.resources().spriteSheetManager().getSpriteSheet(lAssetDef.spritesheetDefinitionName, mEntityGroupUid);
//							if (lSpritesheetDef != null) {
//								lAsset.spriteInstance = lSpritesheetDef.getSpriteInstance(lAssetDef.spriteName);
//							} else {
//								lAsset.spriteStatus = SceneAssetInstance.TEXTURE_FAILED;
//							}
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

		final var lSelectedLayer = mEditorLayerController.selectedLayer();
		if (lSelectedLayer != null) {
			drawSelectedLayerDebug(core, lSelectedLayer);
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	protected void drawTextureLayer(LintfordCore core, SceneTextureLayer layer) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		final var aabb_c = core.gameCamera().boundingRectangle();
		final var cameraPositionX = aabb_c.centerX();
		final var cameraPositionY = aabb_c.centerY();

		if (layer.textureStatus == SceneTextureLayer.TEXTURE_UNLOADED) {
			layer.texture = mResourceManager.textureManager().loadTexture(layer.textureName(), layer.textureFilepath(), entityGroupID());
			layer.textureStatus = SceneTextureLayer.TEXTURE_LOADED;
			if (mResourceManager.textureManager().textureNotFound().equals(layer.texture)) {
				layer.texture = null;

				return;
			}

			if (layer.texture == null) {
				layer.textureStatus = SceneTextureLayer.TEXTURE_FAILED;
			}
		}

		if (layer.texture != null) {
			final var lCamOffsetX = -layer.centerX - cameraPositionX * layer.translationSpeedModX;
			final var lCamOffsetY = -layer.centerY - cameraPositionY * layer.translationSpeedModY;

			final var lSrcX = lCamOffsetX;
			final var lSrcY = lCamOffsetY;
			final var lSrcW = layer.texture.getTextureWidth();
			final var lSrcH = layer.texture.getTextureHeight();

			final var lDstX = layer.centerX - layer.width * .5f;
			final var lDstY = layer.centerY - layer.height * .5f;
			final var lDstWidth = layer.width;
			final var lDstHeight = layer.height;

			lSpriteBatch.begin(core.gameCamera());
			lSpriteBatch.draw(layer.texture, lSrcX, lSrcY, lSrcW, lSrcH, lDstX, lDstY, lDstWidth, lDstHeight, -.01f, ColorConstants.WHITE);
			lSpriteBatch.end();
			return;
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

		// TODO: Camera offset scrolling ?

		final var lDstX = layer.centerX;
		final var lDstY = layer.centerY;
		final var lDstWidth = layer.width;
		final var lDstHeight = layer.height;

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_R, this)) {
			mNoiseLayerShader.recompile();
		}

		// TODO: Only needs updating when dirty
		layer.worldMatrix.setIdentity();
		layer.worldMatrix.translate(lDstX, lDstY, .0f);
		layer.worldMatrix.scale(lDstWidth, lDstHeight, 1.f);

		mNoiseLayerShader.modelMatrix(layer.worldMatrix);
		mNoiseLayerShader.viewMatrix(core.gameCamera().view());
		mNoiseLayerShader.projectionMatrix(core.gameCamera().projection());
		mNoiseLayerShader.bind();

		mNoiseLayerShader.update(core);

		mTexturedQuad.draw(core);

		mNoiseLayerShader.unbind();

		return;
	}

	private void drawSelectedLayerDebug(LintfordCore core, SceneBaseLayer layer) {
		final var x = layer.centerX - layer.width * .5f;
		final var y = layer.centerY - layer.height * .5f;

		Debug.debugManager().drawers().drawRectImmediate(core.gameCamera(), x, y, layer.width, layer.height, 1f, 1f, 0f);
	}

	@Override
	public boolean allowKeyboardInput() {
		return true;
	}
}
