package lintfordpickle.harvest.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.AudioController;
import lintfordpickle.harvest.controllers.CargoController;
import lintfordpickle.harvest.controllers.EnvironmentController;
import lintfordpickle.harvest.controllers.LevelController;
import lintfordpickle.harvest.controllers.PlatformController;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.controllers.TimeTrialGameStateController;
import lintfordpickle.harvest.controllers.actionevents.GameActionEventController;
import lintfordpickle.harvest.controllers.camera.CameraShipChaseController;
import lintfordpickle.harvest.data.CollisionHandler;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.renderers.PlatformsRenderer;
import lintfordpickle.harvest.renderers.ShipRenderer;
import lintfordpickle.harvest.renderers.hud.MinimapRenderer;
import lintfordpickle.harvest.renderers.hud.TimeTrialHudRenderer;
import lintfordpickle.harvest.renderers.scene.SceneAdWallRenderer;
import lintfordpickle.harvest.renderers.scene.SceneRenderer;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordlib.ConstantsPhysics;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.controllers.core.particles.ParticleFrameworkController;
import net.lintfordlib.controllers.debug.physics.DebugPhysicsWorldWatcher;
import net.lintfordlib.controllers.physics.IPhysicsControllerCallback;
import net.lintfordlib.controllers.physics.PhysicsController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.graphics.rendertarget.RenderTarget;
import net.lintfordlib.core.particles.ParticleFrameworkData;
import net.lintfordlib.core.physics.PhysicsSettings;
import net.lintfordlib.core.physics.PhysicsWorld;
import net.lintfordlib.core.physics.resolvers.CollisionResolverRotationAndFriction;
import net.lintfordlib.renderers.debug.physics.DebugPhysicsGridRenderer;
import net.lintfordlib.renderers.debug.physics.DebugPhysicsRenderer;
import net.lintfordlib.renderers.particles.ParticleFrameworkRenderer;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;
import net.lintfordlib.screenmanager.screens.LoadingScreen;

public class TimeTrialGameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private RenderTarget mRenderTarget;

	private CollisionHandler mCollisionHandler;

	// Data
	private final SceneHeader mSceneHeader;

	private PlayerManager mPlayerManager;

	private ParticleFrameworkData mParticleFrameworkData;

	// Controllers
	private AudioController mAudioController;
	private GameActionEventController mGameActionEventController;
	private CameraShipChaseController mCameraShipChaseController;
	private CargoController mCargoController;
	private LevelController mLevelController;
	private ShipController mShipController;
	private SceneController mSceneController;
	private PlatformController mPlatformsController;
	private TimeTrialGameStateController mGameStateController;
	private PhysicsController mPhysicsController;
	private ParticleFrameworkController mParticleFrameworkController;
	private EnvironmentController mEnvironmentController;

	// Renderers
	private DebugPhysicsWorldWatcher mPhysicsWorldDebugWatcher;
	private DebugPhysicsRenderer mPhysicsRenderer;
	private DebugPhysicsGridRenderer mPhysicsDebugGridRenderer;
	private ShipRenderer mShipRenderer;
	private SceneRenderer mSceneRenderer;
	private SceneAdWallRenderer mSceneAdWallRenderer;
	private PlatformsRenderer mPlatformsRenderer;
	private TimeTrialHudRenderer mHudRenderer;
	private MinimapRenderer mMinimapRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public SceneHeader sceneHeader() {
		return mSceneHeader;
	}

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public TimeTrialGameScreen(ScreenManager screenManager, SceneHeader sceneHeader, PlayerManager playerManager) {
		super(screenManager);

		mPlayerManager = playerManager;
		mPlayerManager.resetSessions();

		mSceneHeader = sceneHeader;

		ConstantsPhysics.setPhysicsWorldConstants(64.f);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		final var lDisplaySettings = resourceManager.config().display();
		final var lCanvasWidth = lDisplaySettings.gameResolutionWidth();
		final var lCanvasHeight = lDisplaySettings.gameResolutionHeight();

		mRenderTarget = mRendererManager.createRenderTarget("RT_MAIN", lCanvasWidth, lCanvasHeight, 1.f, GL11.GL_NEAREST, false);

		// TODO: Change this!
		resourceManager.textureManager().loadTexture("TEXTURE_PARTICLES", "res/textureParticles.png", entityGroupUid());
	}

	@Override
	public void unloadResources() {
		mRendererManager.unloadRenderTarget(mRenderTarget);

		super.unloadResources();
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE, this) || core.input().gamepads().isGamepadButtonDownTimed(GLFW.GLFW_GAMEPAD_BUTTON_START, this)) {
			if (ConstantsGame.ESCAPE_RESTART_MAIN_SCENE) {
				final var lLoadingScreen = new LoadingScreen(screenManager(), true, new TimeTrialGameScreen(screenManager(), mSceneHeader, mPlayerManager));
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
				return;
			}

			screenManager().addScreen(new PauseScreen(screenManager(), mSceneHeader, mPlayerManager));
			return;
		}
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		mGameCamera.setZoomFactor(1.f);

		if (otherScreenHasFocus == false) {
//			final var lPlayerScoreCard = mGameState.getScoreCard(0);
//
//			if (lPlayerScoreCard.isPlayerDead && mGameStateController.gameState().isGameRunning) {
//				mGameActionEventController.finalizeInputFile();
//
//				mMinimapRenderer.isActive(false);
//
//				mGameState.isGameRunning = false;
//				screenManager().addScreen(new TimeTrialEndScreen(mScreenManager, mPlayerManager, false, mGameState.timeAliveInMs, mGameActionEventController.fastestTimeOnExitReached()));
//
//				return;
//			}
		}

	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);
		mRendererManager.drawWindowRenderers(core);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createData(LintfordCore core) {
		mParticleFrameworkData = new ParticleFrameworkData();
		mParticleFrameworkData.loadFromMetaFiles();
		mCollisionHandler = new CollisionHandler();

		mGameCamera.setPosition(ConstantsPhysics.toPixels(-1.2f), ConstantsPhysics.toPixels(13.1f));
	}

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		final float lToUnits = ConstantsPhysics.PixelsToUnits();

		var lPhysicsCallback = new IPhysicsControllerCallback() {
			@Override
			public PhysicsWorld createPhysicsWorld() {
				final var lPhysicsWorldSettings = new PhysicsSettings();
				lPhysicsWorldSettings.hashGridWidthInUnits = (int) (2048.f * lToUnits);
				lPhysicsWorldSettings.hashGridHeightInUnits = (int) (2048.f * lToUnits);
				lPhysicsWorldSettings.hashGridCellsWide = 5;
				lPhysicsWorldSettings.hashGridCellsHigh = 5;

				final var lPhysicsWorld = new PhysicsWorld(lPhysicsWorldSettings);

				lPhysicsWorld.initialize();
				lPhysicsWorld.setGravity(0.f, 5.87f);

				lPhysicsWorld.setContactResolver(new CollisionResolverRotationAndFriction());
				lPhysicsWorld.addCollisionCallback(mCollisionHandler);
				return lPhysicsWorld;
			}
		};

		mAudioController = new AudioController(controllerManager, screenManager().core().resources().audioManager(), entityGroupUid());
		mGameActionEventController = new GameActionEventController(controllerManager, mPlayerManager, inputCounter(), entityGroupUid());
		mPhysicsController = new PhysicsController(controllerManager, lPhysicsCallback, entityGroupUid());
		mLevelController = new LevelController(controllerManager, entityGroupUid());
		mCameraShipChaseController = new CameraShipChaseController(controllerManager, mGameCamera, null, entityGroupUid());
		mSceneController = new SceneController(controllerManager, mSceneHeader, entityGroupUid());
		mGameStateController = new TimeTrialGameStateController(controllerManager, mPlayerManager, entityGroupUid());
		mCargoController = new CargoController(controllerManager, entityGroupUid());
		mShipController = new ShipController(controllerManager, mPlayerManager, entityGroupUid());
		mPlatformsController = new PlatformController(controllerManager, entityGroupUid());
		mParticleFrameworkController = new ParticleFrameworkController(controllerManager, mParticleFrameworkData, entityGroupUid());
		mEnvironmentController = new EnvironmentController(controllerManager, mGameCamera, entityGroupUid());
		mPhysicsWorldDebugWatcher = new DebugPhysicsWorldWatcher(controllerManager, entityGroupUid());
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mAudioController.initialize(core);
		mParticleFrameworkController.initialize(core);
		mGameActionEventController.initialize(core);
		mPhysicsController.initialize(core);
		mLevelController.initialize(core);
		mCameraShipChaseController.initialize(core);
		mSceneController.initialize(core);
		mCargoController.initialize(core);
		mShipController.initialize(core);
		mPlatformsController.initialize(core);
		mGameStateController.initialize(core);
		mEnvironmentController.initialize(core);
		mPhysicsWorldDebugWatcher.initialize(core);
		mPhysicsWorldDebugWatcher.physicsWorld(mPhysicsController.world());

	}

	@Override
	protected void createRenderers(LintfordCore core) {
		mSceneRenderer = new SceneRenderer(mRendererManager, entityGroupUid());
		if (ConstantsGame.PHYICS_DEBUG_MODE) {
			mPhysicsRenderer = new DebugPhysicsRenderer(mRendererManager, entityGroupUid());
			mPhysicsDebugGridRenderer = new DebugPhysicsGridRenderer(mRendererManager, entityGroupUid());
		}

		mShipRenderer = new ShipRenderer(mRendererManager, entityGroupUid());
		mSceneAdWallRenderer = new SceneAdWallRenderer(mRendererManager, entityGroupUid());
		mPlatformsRenderer = new PlatformsRenderer(mRendererManager, entityGroupUid());
		mParticleFrameworkRenderer = new ParticleFrameworkRenderer(mRendererManager, entityGroupUid());

		mHudRenderer = new TimeTrialHudRenderer(mRendererManager, entityGroupUid());
		mMinimapRenderer = new MinimapRenderer(mRendererManager, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mShipRenderer.initialize(core);
		if (ConstantsGame.PHYICS_DEBUG_MODE) {
			mPhysicsRenderer.initialize(core);
			mPhysicsDebugGridRenderer.initialize(core);
		}

		mSceneRenderer.initialize(core);
		mSceneAdWallRenderer.initialize(core);
		mPlatformsRenderer.initialize(core);
		mParticleFrameworkRenderer.initialize(core);
		mHudRenderer.initialize(core);
		mMinimapRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSceneRenderer.loadResources(resourceManager);
		mShipRenderer.loadResources(resourceManager);

		mSceneAdWallRenderer.loadResources(resourceManager);
		if (ConstantsGame.PHYICS_DEBUG_MODE) {
			mPhysicsRenderer.loadResources(resourceManager);
			mPhysicsDebugGridRenderer.loadResources(resourceManager);
		}

		mPlatformsRenderer.loadResources(resourceManager);
		mParticleFrameworkRenderer.loadResources(resourceManager);
		mHudRenderer.loadResources(resourceManager);
		mMinimapRenderer.loadResources(resourceManager);
	}

	@Override
	public void exitScreen() {
		super.exitScreen();

		mGameActionEventController.finalizeInputFile();
	}

}
