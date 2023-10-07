package lintfordpickle.harvest.renderers.scene;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.scene.backgrounds.SceneLayer;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.graphics.ColorConstants;
import net.lintfordlib.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintfordlib.renderers.BaseRenderer;
import net.lintfordlib.renderers.RendererManager;

public class SceneForegroundRenderer extends BaseRenderer {

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

	public static final String RENDERER_NAME = "Scene Foreground Renderer";

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

	public SceneForegroundRenderer(RendererManager rendererManager, int entityGroupID) {
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
			final var lLayer = lLayers.get(i);

			if (lLayer.isForeground == false)
				continue;

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

		final var lTextureBatch = mRendererManager.uiSpriteBatch();
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

}
