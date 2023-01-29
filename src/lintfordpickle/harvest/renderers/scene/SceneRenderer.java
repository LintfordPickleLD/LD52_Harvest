package lintfordpickle.harvest.renderers.scene;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.scene.backgrounds.SceneLayer;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class SceneRenderer extends BaseRenderer {

	public class CarStream {
		public float x;
		public float y;

		public float intSrcPositionX;
		public float intSrcPositionY;
		public float speedX;

		public String spriteStreamName;
	}

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Scene Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<CarStream> carStreams = new ArrayList<>();

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

		final var lCarStream = new CarStream();
		lCarStream.x = 0;
		lCarStream.y = 0;
		lCarStream.spriteStreamName = "TEXTURE_CAR_STREAM_00";

		carStreams.add(lCarStream);

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

		drawCarStreams(core);
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

		// TODO: Should come from the scene layer definition
		final int srcX = 0;
		final int srcY = 0;
		final int srcW = 1024;
		final int srcH = 1024;

		final float lDstW = sceneLayer.widthInPx;
		final float lDstH = sceneLayer.heightInPx;

		final float lDstX = sceneLayer.centerX - sceneLayer.widthInPx * .5f;
		final float lDstY = sceneLayer.centerY - sceneLayer.heightInPx * .5f;

		lTextureBatch.draw(lTexture, srcX, srcY, srcW, srcH, lDstX, lDstY, lDstW, lDstH, -0.01f, ColorConstants.WHITE);

		lTextureBatch.end();
	}

	private void drawCarStreams(LintfordCore core) {
		final var lTextureBatch = mRendererManager.uiTextureBatch();

		lTextureBatch.begin(core.gameCamera());

		final var lNumCarStreams = carStreams.size();
		for (int i = 0; i < lNumCarStreams; i++) {
			final var lCarStream = carStreams.get(i);
			final var lSpriteFrame = mPropsSpritesheetDefintion.getSpriteFrame(lCarStream.spriteStreamName);
			if (lSpriteFrame == null)
				continue;

			lCarStream.intSrcPositionX += lCarStream.speedX;

			final float srcX = lCarStream.intSrcPositionX;
			final float srcY = lCarStream.intSrcPositionY;
			final int srcW = (int) lSpriteFrame.width();
			final int srcH = (int) lSpriteFrame.height();

			final float lDstX = 0;
			final float lDstY = 0;
			final float lDstW = srcW;
			final float lDstH = srcH;

			lTextureBatch.draw(mPropsSpritesheetDefintion.texture(), srcX, srcY, srcW, srcH, lDstX, lDstY, lDstW, lDstH, -0.01f, ColorConstants.WHITE);
		}

		lTextureBatch.end();
	}
}
