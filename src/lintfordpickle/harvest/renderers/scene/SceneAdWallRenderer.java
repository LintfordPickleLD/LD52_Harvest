package lintfordpickle.harvest.renderers.scene;

import org.lwjgl.opengl.GL20;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.data.scene.AdWall;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.shaders.ShaderMVP_PCT;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class SceneAdWallRenderer extends BaseRenderer {

	public class AdWallShader extends ShaderMVP_PCT {

		private static final String FRAG_FILEPATH = "res/shaders/shader_adwall_pct.frag";
		private static final String VERT_FILENAME = "/res/shaders/shader_batch_pct.vert";

		private float mTimeAcc;

		private int mTimeShaderLocation;

		public AdWallShader(String shaderName) {
			super(shaderName, VERT_FILENAME, FRAG_FILEPATH);

			mTimeShaderLocation = -1;
		}

		// --------------------------------------
		// Core-Methods
		// --------------------------------------

		public void update(LintfordCore core) {
			mTimeAcc += (float) core.gameTime().elapsedTimeMilli() * 0.001f;
		}

		// --------------------------------------
		// Methods
		// --------------------------------------

		@Override
		protected void updateUniforms() {
			super.updateUniforms();

			if (mTimeShaderLocation != -1)
				GL20.glUniform1f(mTimeShaderLocation, mTimeAcc);

		}

		@Override
		protected void getUniformLocations() {
			super.getUniformLocations();

			mTimeShaderLocation = GL20.glGetUniformLocation(shaderID(), "time");
		}

	}

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Scene Ad-Wall Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private SceneController mSceneController;
	private AdWallShader mAdWallShader;

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

	public SceneAdWallRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);

		mAdWallShader = new AdWallShader("SHADER_ADWALL");
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

		mAdWallShader.loadResources(resourceManager);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mAdWallShader.unloadResources();
	}

	@Override
	public boolean handleInput(LintfordCore core) {
		return super.handleInput(core);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		mAdWallShader.update(core);
	}

	@Override
	public void draw(LintfordCore core) {
		final var horAds = mSceneController.horizontalAdWall();

		// drawAdWall(core, horAds);
		drawAdWall(core, mSceneController.verticalAdWall());
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void drawAdWall(LintfordCore core, AdWall adWall) {
		if (adWall == null)
			return;

		final var lTextureBatch = mRendererManager.uiTextureBatch();

		final var lTexture = core.resources().textureManager().getTexture(adWall.adWallTextureName, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		if (lTexture == null)
			return;

		lTextureBatch.begin(core.gameCamera(), mAdWallShader);

		final var srcX = 0.f;
		final var srcY = 0.f;// (float) core.gameTime().totalTimeMilli() * .005f;
		final var srcW = lTexture.getTextureWidth();
		final var srcH = lTexture.getTextureHeight();

		final var lWhiteWithAlpha = ColorConstants.getWhiteWithAlpha(1.5f);
		lTextureBatch.draw(lTexture, srcX, srcY, srcW, srcH, adWall, -0.01f, lWhiteWithAlpha);

		lTextureBatch.end();
	}
}
