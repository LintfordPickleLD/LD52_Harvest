package lintfordpickle.harvest.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.AudioController;
import lintfordpickle.harvest.controllers.CargoController;
import lintfordpickle.harvest.controllers.LevelController;
import lintfordpickle.harvest.controllers.PlatformController;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.controllers.TimeTrialGameStateController;
import lintfordpickle.harvest.controllers.actionevents.GameActionEventController;
import lintfordpickle.harvest.controllers.camera.CameraShipChaseController;
import lintfordpickle.harvest.data.CollisionHandler;
import lintfordpickle.harvest.data.cargo.CargoManager;
import lintfordpickle.harvest.data.game.GameState;
import lintfordpickle.harvest.data.game.GameState.GameMode;
import lintfordpickle.harvest.data.platforms.PlatformManager;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.scene.backgrounds.SceneManager;
import lintfordpickle.harvest.data.ships.ShipManager;
import lintfordpickle.harvest.renderers.PlatformsRenderer;
import lintfordpickle.harvest.renderers.ShipRenderer;
import lintfordpickle.harvest.renderers.hud.MinimapRenderer;
import lintfordpickle.harvest.renderers.hud.TimeTrialHudRenderer;
import lintfordpickle.harvest.renderers.scene.SceneAdWallRenderer;
import lintfordpickle.harvest.renderers.scene.SceneForegroundRenderer;
import lintfordpickle.harvest.renderers.scene.SceneRenderer;
import lintfordpickle.harvest.screens.PauseScreen;
import lintfordpickle.harvest.screens.endscreens.SurvivalEndScreen;
import lintfordpickle.harvest.screens.endscreens.TimeTrialEndScreen;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
import net.lintford.library.controllers.physics.IPhysicsControllerCallback;
import net.lintford.library.controllers.physics.PhysicsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.rendertarget.RenderTarget;
import net.lintford.library.core.particles.ParticleFrameworkData;
import net.lintford.library.core.physics.PhysicsWorld;
import net.lintford.library.core.physics.resolvers.CollisionResolverRotationAndFriction;
import net.lintford.library.renderers.debug.DebugPhysicsGridRenderer;
import net.lintford.library.renderers.debug.DebugPhysicsRenderer;
import net.lintford.library.renderers.particles.ParticleFrameworkRenderer;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class TimeTrialGameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private RenderTarget mRenderTarget;

	private CollisionHandler mCollisionHandler;

	// Data
	private PlayerManager mPlayerManager;
	private CargoManager mCargoManager;
	private GameState mGameState;
	private ShipManager mShipManager;
	private SceneManager mSceneManager;
	private PlatformManager mPlatformManager;
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

	// Renderers
	private DebugPhysicsRenderer mPhysicsRenderer;
	private DebugPhysicsGridRenderer mPhysicsDebugGridRenderer;
	private ShipRenderer mShipRenderer;
	private SceneRenderer mSceneRenderer;
	private SceneForegroundRenderer mSceneForegroundRenderer;
	private SceneAdWallRenderer mSceneAdWallRenderer;
	private PlatformsRenderer mPlatformsRenderer;
	private TimeTrialHudRenderer mHudRenderer;
	private MinimapRenderer mMinimapRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public TimeTrialGameScreen(ScreenManager screenManager, PlayerManager playerManager) {
		super(screenManager);

		mPlayerManager = playerManager;
		mPlayerManager.resetSessions();

		ConstantsPhysics.setPhysicsWorldConstants(64.f);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		mGameState = new GameState();
		mGameState.startNewGame(GameMode.TimeTrial);

		mShipManager = new ShipManager();
		mCargoManager = new CargoManager();
		mSceneManager = new SceneManager();
		mPlatformManager = new PlatformManager();
		
		mParticleFrameworkData = new ParticleFrameworkData();

		mCollisionHandler = new CollisionHandler();

		super.initialize();

		mGameCamera.setPosition(ConstantsPhysics.toPixels(-1.2f), ConstantsPhysics.toPixels(13.1f));
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		final var lDisplaySettings = resourceManager.config().display();
		final var lCanvasWidth = lDisplaySettings.gameResolutionWidth();
		final var lCanvasHeight = lDisplaySettings.gameResolutionHeight();

		mRenderTarget = mRendererManager.createRenderTarget("RT_MAIN", lCanvasWidth, lCanvasHeight, 1.f, GL11.GL_NEAREST, false);
		
		// TODO: Change this!
		resourceManager.textureManager().loadTexture("TEXTURE_PARTICLES", "res/textures/textureParticles.png", entityGroupUid());
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
				final var lLoadingScreen = new LoadingScreen(screenManager(), true, new TimeTrialGameScreen(screenManager(), mPlayerManager));
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
				return;
			}

			screenManager().addScreen(new PauseScreen(screenManager(), mPlayerManager));
			return;
		}
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		if (otherScreenHasFocus == false) {
			final var lPlayerScoreCard = mGameState.getScoreCard(0);

			if (lPlayerScoreCard.allPlatformsDelivered() || core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_K, this)) {
				mGameActionEventController.finalizeInputFile();

				mGameState.isGameRunning = false;
				screenManager().addScreen(new TimeTrialEndScreen(mScreenManager, mPlayerManager, mGameState.timeAliveInMs, mGameActionEventController.fastestTimeOnExitReached()));
				return;
			}

			if (lPlayerScoreCard.isPlayerDead && mGameStateController.gameState().isGameRunning) {
				mGameActionEventController.finalizeInputFile();

				mGameState.isGameRunning = false;
				screenManager().addScreen(new SurvivalEndScreen(mScreenManager, mPlayerManager, true, lPlayerScoreCard.foodDelivered));

				return;
			}
		}

	}

	@Override
	public void draw(LintfordCore core) {
		GL11.glClearColor(0.06f, 0.18f, 0.31f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		var lPhysicsCallback = new IPhysicsControllerCallback() {
			@Override
			public PhysicsWorld createPhysicsWorld() {
				final var lPhysicsWorld = new PhysicsWorld(400, 400, 5, 5);
				lPhysicsWorld.initialize();
				lPhysicsWorld.setGravity(0.f,  5.87f);

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
		mGameStateController = new TimeTrialGameStateController(controllerManager, mGameState, mPlayerManager, entityGroupUid());
		mSceneController = new SceneController(controllerManager, mSceneManager, entityGroupUid());
		mCargoController = new CargoController(controllerManager, mCargoManager, entityGroupUid());
		mShipController = new ShipController(controllerManager, mShipManager, mPlayerManager, entityGroupUid());
		mPlatformsController = new PlatformController(controllerManager, mPlatformManager, entityGroupUid());
		mParticleFrameworkController = new ParticleFrameworkController(controllerManager, mParticleFrameworkData, entityGroupUid());
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
		mSceneForegroundRenderer = new SceneForegroundRenderer(mRendererManager, entityGroupUid());
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
		mSceneForegroundRenderer.initialize(core);
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
		mSceneForegroundRenderer.loadResources(resourceManager);
		mHudRenderer.loadResources(resourceManager);
		mMinimapRenderer.loadResources(resourceManager);
	}

	@Override
	public void exitScreen() {
		super.exitScreen();

		mGameActionEventController.finalizeInputFile();
	}

}
