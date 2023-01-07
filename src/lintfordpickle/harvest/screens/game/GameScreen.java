package lintfordpickle.harvest.screens.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.CameraShipChaseController;
import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.data.backgrounds.SceneManager;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.data.ships.ShipManager;
import lintfordpickle.harvest.renderers.PhysicsDebugRenderer;
import lintfordpickle.harvest.renderers.SceneRenderer;
import lintfordpickle.harvest.renderers.ShipRenderer;
import lintfordpickle.harvest.screens.PauseScreen;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.camera.ICamera;
import net.lintford.library.core.collisions.PhysicsWorld;
import net.lintford.library.core.collisions.RigidBody;
import net.lintford.library.core.collisions.resolvers.CollisionResolverRotationAndFriction;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.geometry.partitioning.GridEntity;
import net.lintford.library.core.graphics.rendertarget.RenderTarget;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class GameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int NUM_PHYSICS_ITERATIONS = 7;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private RenderTarget mRenderTarget;

	private PhysicsWorld world;

	// Data
	private ShipManager mShipManager;
	private SceneManager mSceneManager;

	// Controllers
	private CameraShipChaseController mCameraShipChaseController;
	private ShipController mShipController;
	private SceneController mSceneController;

	// Renderers
	private PhysicsDebugRenderer mPhysicsRenderer;
	private ShipRenderer mShipRenderer;
	private SceneRenderer mSceneRenderer;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public GameScreen(ScreenManager screenManager, boolean showHelp) {
		super(screenManager);

		ConstantsPhysics.setPhysicsWorldConstants(64.f);

		final var lPlayerManager = new PlayerManager();
		lPlayerManager.addPlayer();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {

		mShipManager = new ShipManager();
		mSceneManager = new SceneManager();

		final var lPlayerShip = new Ship(GridEntity.getNewEntityUid());
		lPlayerShip.isPlayerControlled = true;
		mShipManager.playerShip(lPlayerShip);

		world = new PhysicsWorld(0.0f, 9.87f);
		world.setContactResolver(new CollisionResolverRotationAndFriction());

		world.addBody(lPlayerShip.body());
		world.initialize();

		createWorldCollidables();

		super.initialize();
	}

	private void createStaticTestWorld() {
		final float lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final float staticFriction = 0.8f;
		final float dynamicFriction = 0.5f;

		final var lBoundingBox = mScreenManager.core().HUD().boundingRectangle();

		final var lGroundBox = RigidBody.createPolygonBody((lBoundingBox.width() * .95f) * ConstantsPhysics.PixelsToUnits(), .5f, 1.f, .5f, .6f, .4f, true);
		lGroundBox.moveTo(0.f, (lBoundingBox.height() * .4f) * ConstantsPhysics.PixelsToUnits());

		final var lLedge0 = RigidBody.createPolygonBody(100.f * lPixelsToUnits, 10.f * lPixelsToUnits, 1.f, .5f, staticFriction, dynamicFriction, true);
		lLedge0.angle((float) Math.toRadians(-25.f));
		lLedge0.moveTo(90.f * lPixelsToUnits, 0.f * lPixelsToUnits);

		final var lLedge1 = RigidBody.createPolygonBody(200.f * lPixelsToUnits, 20.f * lPixelsToUnits, 1.f, .5f, staticFriction, dynamicFriction, true);
		lLedge1.angle((float) Math.toRadians(25.f));
		lLedge1.moveTo(-110.f * lPixelsToUnits, -50.f * lPixelsToUnits);

		world.addBody(lGroundBox);
		world.addBody(lLedge0);
		world.addBody(lLedge1);
	}

	private void createWorldCollidables() {
		createStaticPolygon(602, 1312, 128, 128, 0);
		createStaticPolygon(448, 1392, 154, 48, 0);
		createStaticPolygon(1358, 928, 130, 124, 0);
		createStaticPolygon(1698, 1168, 128, 270, 0);
		createStaticPolygon(1954, 1232, 48, 206, 0);
		createStaticPolygon(1696, 1438, 352, 40, 0);
		createStaticPolygon(1184, 640, 130, 192, 0);
		createStaticPolygon(1150, 768, 130, 284, 0);
		createStaticPolygon(1150, 1052, 338, 22, 0);
		createStaticPolygon(864, 576, 128, 658, 0);
		createStaticPolygon(320, 224, 128, 1146, 0);

		createStaticPolygon(320, 224, 128, 1146, 0);
		createStaticPolygon(384, 160, 64, 64, 0);
		createStaticPolygon(298, 224, 22, 288, 0);
		createStaticPolygon(448, 512, 20, 128, 0);
		createStaticPolygon(448, 512, 20, 128, 0);
		createStaticPolygon(300, 640, 20, 64, 0);
		createStaticPolygon(448, 768, 288, 32, 0);
		createStaticPolygon(300, 800, 20, 96, 0);
		createStaticPolygon(300, 960, 20, 160, 0);
		createStaticPolygon(448, 1088, 20, 96, 0);
		createStaticPolygon(0, 1248, 128, 192, 0);
		createStaticPolygon(128, 1370, 320, 70, 0);
		createStaticPolygon(640, 968, 224, 24, 0);
		createStaticPolygon(864, 358, 128, 58, 0);
		createStaticPolygon(892, 416, 100, 160, 0);
		createStaticPolygon(992, 512, 18, 64, 0);
		createStaticPolygon(992, 512, 18, 64, 0);
		createStaticPolygon(864, 1234, 352, 8, 0);
		createStaticPolygon(1344, 1234, 144, 8, 0);
		createStaticPolygon(1934, 1032, 100, 12, 0);
		createStaticPolygon(1826, 622, 104, 9, 0);

		createStaticPolygon(930, 1438, 607, 40, 0);
		createStaticPolygon(1440, 1328, 80, 110, 0);
		createStaticPolygon(1440, 1234, 32, 94, 0);
		createStaticPolygon(1696, 622, 130, 466, 0);
		createStaticPolygon(1696, 542, 32, 80, 0);

		// walls
		createStaticPolygon(0, 0, 14, 2048, 0);
		createStaticPolygon(2034, 0, 14, 2048, 0);
		createStaticPolygon(0, 0, 2048, 14, 0);
		createStaticPolygon(0, 1996, 2048, 52, 0);

		// Platforms
		createStaticPolygon(529, 1863, 159, 23, 0);
		createStaticPolygon(1472, 1869, 158, 23, 0);
	}

	private void createStaticPolygon(float x, float y, float w, float h, float r) {
		final float worldXOffset = -1024;
		final float worldYOffset = -1024;

		final float lPxToUts = ConstantsPhysics.PixelsToUnits();

		final float staticFriction = 0.8f;
		final float dynamicFriction = 0.5f;

		final var lPolygon = RigidBody.createPolygonBody(w * lPxToUts, h * lPxToUts, 1.f, staticFriction, dynamicFriction, .4f, true);
		lPolygon.moveTo((worldXOffset + x + w / 2) * lPxToUts, (worldYOffset + y + h / 2) * lPxToUts);

		world.addBody(lPolygon);
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
			if (ConstantsGame.ESCAPE_RESTART_MAIN_SCENE) {
				final var lLoadingScreen = new LoadingScreen(screenManager(), true, new GameScreen(screenManager(), true));
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
				return;
			}
			screenManager().addScreen(new PauseScreen(screenManager()));
			return;
		}

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_R)) {
			final var lPlayerBody = mShipManager.playerShip().body();

			lPlayerBody.angularVelocity = 0.f;
			lPlayerBody.vx = 0.f;
			lPlayerBody.vy = 0.f;
			lPlayerBody.x = 0.f;
			lPlayerBody.y = 0.f;

			lPlayerBody.torque = 0.f;
			lPlayerBody.angularVelocity = 0.f;
			lPlayerBody.angle = 0.f;
		}
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		world.stepWorld((float) core.gameTime().elapsedTimeMilli() * 0.001f, NUM_PHYSICS_ITERATIONS);

		if (ConstantsGame.WRAP_OBJECTS_AROUND_SCREEN_EDGE)
			wrapBodies(core);
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
		mCameraShipChaseController = new CameraShipChaseController(controllerManager, mGameCamera, mShipManager.playerShip(), entityGroupUid());

		mSceneController = new SceneController(controllerManager, mSceneManager, entityGroupUid());
		mShipController = new ShipController(controllerManager, mShipManager, entityGroupUid());

	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mCameraShipChaseController.initialize(core);
		mSceneController.initialize(core);
		mShipController.initialize(core);
	}

	@Override
	protected void createRenderers(LintfordCore core) {
		mSceneRenderer = new SceneRenderer(mRendererManager, entityGroupUid());
		mPhysicsRenderer = new PhysicsDebugRenderer(mRendererManager, world, entityGroupUid());
		mShipRenderer = new ShipRenderer(mRendererManager, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mShipRenderer.initialize(core);
		mPhysicsRenderer.initialize(core);
		mSceneRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSceneRenderer.loadResources(resourceManager);
		mShipRenderer.loadResources(resourceManager);
		mPhysicsRenderer.loadResources(resourceManager);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void wrapBodies(LintfordCore core) {
		final var lCamAABB = core.HUD().boundingRectangle();

		final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();
		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final var lBodiesList = world.bodies();

		final float lPaddingPx = 50.f * lPixelsToUnits;

		for (int i = 0; i < world.numBodies(); i++) {
			final var lBody = lBodiesList.get(i);
			final var lAABB = lBody.aabb();

			final var x = lAABB.left() * lUnitsToPixels;
			final var y = lAABB.top() * lUnitsToPixels;
			final var w = lAABB.width() * lUnitsToPixels;
			final var h = lAABB.height() * lUnitsToPixels;

			if (lCamAABB.intersectsAA(x, y, w, h) == false) {
				if (lBody.x * lUnitsToPixels < lCamAABB.left() - lPaddingPx)
					lBody.x = lCamAABB.right() * lPixelsToUnits;

				if (lBody.x * lUnitsToPixels > lCamAABB.right() + lPaddingPx)
					lBody.x = lCamAABB.left() * lPixelsToUnits;

				if (lBody.y * lUnitsToPixels > lCamAABB.bottom() + lPaddingPx)
					lBody.y = lCamAABB.top() * lPixelsToUnits;

				if (lBody.y * lUnitsToPixels < lCamAABB.top() - lPaddingPx)
					lBody.y = lCamAABB.bottom() * lPixelsToUnits;
			}
		}
	}
}
