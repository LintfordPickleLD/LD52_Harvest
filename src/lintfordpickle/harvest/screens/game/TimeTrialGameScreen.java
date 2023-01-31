package lintfordpickle.harvest.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
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
import lintfordpickle.harvest.renderers.scene.SceneRenderer;
import lintfordpickle.harvest.screens.PauseScreen;
import lintfordpickle.harvest.screens.endscreens.SurvivalEndScreen;
import lintfordpickle.harvest.screens.endscreens.TimeTrialEndScreen;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.PhysicsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.rendertarget.RenderTarget;
import net.lintford.library.core.physics.PhysicsWorld;
import net.lintford.library.core.physics.resolvers.CollisionResolverRotationAndFriction;
import net.lintford.library.renderers.debug.DebugPhysicsGridRenderer;
import net.lintford.library.renderers.debug.DebugPhysicsRenderer;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class TimeTrialGameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int NUM_PHYSICS_ITERATIONS = 7;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private RenderTarget mRenderTarget;

	private PhysicsWorld world;
	private CollisionHandler mCollisionHandler;

	// Data
	private PlayerManager mPlayerManager;
	private CargoManager mCargoManager;
	private GameState mGameState;
	private ShipManager mShipManager;
	private SceneManager mSceneManager;
	private PlatformManager mPlatformManager;

	// Controllers
	private GameActionEventController mGameActionEventController;
	private CameraShipChaseController mCameraShipChaseController;
	private CargoController mCargoController;
	private LevelController mLevelController;
	private ShipController mShipController;
	private SceneController mSceneController;
	private PlatformController mPlatformsController;
	private TimeTrialGameStateController mGameStateController;
	private PhysicsController mPhysicsController;

	// Renderers
	private DebugPhysicsRenderer mPhysicsRenderer;
	private DebugPhysicsGridRenderer mPhysicsDebugGridRenderer;
	private ShipRenderer mShipRenderer;
	private SceneRenderer mSceneRenderer;
	private SceneAdWallRenderer mSceneAdWallRenderer;
	private PlatformsRenderer mPlatformsRenderer;
	private TimeTrialHudRenderer mHudRenderer;
	private MinimapRenderer mMinimapRenderer;

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

		mCollisionHandler = new CollisionHandler();

		world = new PhysicsWorld();
		world.setGravity(0, 5.87f);
		world.setContactResolver(new CollisionResolverRotationAndFriction());

		world.addCollisionCallback(mCollisionHandler);
		world.initialize();

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
	}

	@Override
	public void unloadResources() {
		mRendererManager.unloadRenderTarget(mRenderTarget);

		super.unloadResources();
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE)) {
			mGameActionEventController.onExitingGame();

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

			if (lPlayerScoreCard.allPlatformsDelivered()) {
				mGameActionEventController.onExitingGame();

				mGameState.isGameRunning = false;
				screenManager().addScreen(new TimeTrialEndScreen(mScreenManager, mPlayerManager, mGameState.timeAliveInMs, mGameActionEventController.fastestTimeOnExitReached()));
				return;
			}

			if (lPlayerScoreCard.isPlayerDead && mGameStateController.gameState().isGameRunning) {
				mGameActionEventController.onExitingGame();

				mGameState.isGameRunning = false;
				screenManager().addScreen(new SurvivalEndScreen(mScreenManager, mPlayerManager, true, lPlayerScoreCard.foodDelivered));

				return;
			}
		}

		world.stepWorld((float) core.gameTime().elapsedTimeMilli() * 0.001f, NUM_PHYSICS_ITERATIONS);
	}

	@Override
	public void draw(LintfordCore core) {
		final var lGameCam = mGameCamera; // orig

		mRenderTarget.bind();

		GL11.glClearColor(0.06f, 0.18f, 0.31f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);

		mRenderTarget.unbind();

		GL11.glClearColor(0.06f, 0.18f, 0.11f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		core.setActiveGameCamera(lGameCam);
		core.config().display().reapplyGlViewport();

		Debug.debugManager().drawers().drawRenderTargetImmediate(core, 0, 0, 960, 540, -0.001f, mRenderTarget);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mGameActionEventController = new GameActionEventController(controllerManager, inputCounter(), entityGroupUid());
		mPhysicsController = new PhysicsController(controllerManager, world, entityGroupUid());
		mLevelController = new LevelController(controllerManager, entityGroupUid());
		mCameraShipChaseController = new CameraShipChaseController(controllerManager, mGameCamera, null, entityGroupUid());
		mGameStateController = new TimeTrialGameStateController(controllerManager, mGameState, mPlayerManager, entityGroupUid());
		mSceneController = new SceneController(controllerManager, mSceneManager, entityGroupUid());
		mCargoController = new CargoController(controllerManager, mCargoManager, entityGroupUid());
		mShipController = new ShipController(controllerManager, mShipManager, mPlayerManager, entityGroupUid());
		mPlatformsController = new PlatformController(controllerManager, mPlatformManager, entityGroupUid());
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
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
			mPhysicsRenderer = new DebugPhysicsRenderer(mRendererManager, world, entityGroupUid());
			mPhysicsDebugGridRenderer = new DebugPhysicsGridRenderer(mRendererManager, world, entityGroupUid());
		}

		mShipRenderer = new ShipRenderer(mRendererManager, entityGroupUid());
		mSceneAdWallRenderer = new SceneAdWallRenderer(mRendererManager, entityGroupUid());
		mPlatformsRenderer = new PlatformsRenderer(mRendererManager, entityGroupUid());
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
		mHudRenderer.loadResources(resourceManager);
		mMinimapRenderer.loadResources(resourceManager);
	}

}
