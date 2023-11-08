package lintfordpickle.harvest.renderers.scene;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.scene.backgrounds.SceneLayer;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;
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

	private final List<SpriteInstance> sceneAnimationSprites = new ArrayList<>();

	private SceneController mSceneController;
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

		mPropsSpritesheetDefintion = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_PROPS", ConstantsGame.GAME_RESOURCE_GROUP_ID);

		final var lSceneManager = mSceneController.sceneManager();
		final var lLayers = lSceneManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			final var lLayer = lLayers.get(i);
			lLayer.texture = resourceManager.textureManager().getTexture(lLayer.textureName, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		}

		addCarStream("carstream", 256);
		addCarStream("carstream", 610);

	}

	@Override
	public void unloadResources() {
		super.unloadResources();

	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lNumAnimations = sceneAnimationSprites.size();
		for (int i = 0; i < lNumAnimations; i++) {
			sceneAnimationSprites.get(i).update(core);
		}
	}

	@Override
	public void draw(LintfordCore core) {
		final var lSceneManager = mSceneController.sceneManager();
		final var lLayers = lSceneManager.layers();
		final var lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers + 1; i++) {
			

			// TODO: Animations on custom layers

			// TODO: Cloud layer / smoke layer

			// TODO HACK for now
			if (i < 3) {
				final var lLayer = lLayers.get(i);

				if (lLayer.isForeground == true)
					continue;
				
				drawScene(core, lLayers.get(i));
				continue;
			} else if (i == 3) {
				drawAnimations(core);
				continue;
			}

			drawScene(core, lLayers.get(i - 1));

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

		final var lTextureBatch = mRendererManager.uiSpriteBatch();
		lTextureBatch.begin(core.gameCamera());

		// TODO: Should come from the scene layer definition
		final int srcX = 0;
		final int srcY = 0;
		final int srcW = WORLD_WIDTH_IN_PX;
		final int srcH = 1024;

		final float lDstW = sceneLayer.widthInPx;
		final float lDstH = sceneLayer.heightInPx;

		final float lDstX = sceneLayer.centerX - sceneLayer.widthInPx * .5f;
		final float lDstY = sceneLayer.centerY - sceneLayer.heightInPx * .5f;

		lTextureBatch.draw(lTexture, srcX, srcY, srcW, srcH, lDstX, lDstY, lDstW, lDstH, -0.01f, ColorConstants.WHITE);

		lTextureBatch.end();
	}

	// mPropsSpritesheetDefintion
	private void addCarStream(String animationSpriteName, float positionY) {
		final var lSpriteFrameDef = mPropsSpritesheetDefintion.getSpriteDefinition(animationSpriteName);
		if (lSpriteFrameDef == null) {
			Debug.debugManager().logger().w(getClass().getSimpleName(), "Couldn't find carstream sprite definition in SpriteSheetDefinition: " + animationSpriteName);
			return;
		}

		final var lSpriteFrame = lSpriteFrameDef.frames().get(0);
		final var lSpriteWidth = lSpriteFrame.width();
		final var lSpriteHeight = lSpriteFrame.height();

		float posX = -512;
		float posY = -512 + positionY;

		for (int i = 0; i <= WORLD_WIDTH_IN_PX; i += lSpriteWidth) {
			final var lNewAnimationSprite = mPropsSpritesheetDefintion.getSpriteInstance(lSpriteFrameDef);
			lNewAnimationSprite.set(posX, posY, lSpriteWidth * 2, lSpriteHeight * 2);
			sceneAnimationSprites.add(lNewAnimationSprite);

			posX += lSpriteWidth;
		}

	}

	private void drawAnimations(LintfordCore core) {
		final var lSpriteBatch = mRendererManager.uiSpriteBatch();

		lSpriteBatch.begin(core.gameCamera());

		final var lNumAnimations = sceneAnimationSprites.size();
		for (int i = 0; i < lNumAnimations; i++) {
			final var lCarStream = sceneAnimationSprites.get(i);

			lSpriteBatch.draw(mPropsSpritesheetDefintion, lCarStream, lCarStream, 2.f, -0.01f, ColorConstants.WHITE);
		}

		lSpriteBatch.end();
	}
}
