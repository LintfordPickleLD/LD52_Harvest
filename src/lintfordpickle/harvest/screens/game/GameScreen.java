package lintfordpickle.harvest.screens.game;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.contrllers.ShipController;
import lintfordpickle.harvest.data.Ship;
import lintfordpickle.harvest.data.ShipManager;
import lintfordpickle.harvest.data.players.PlayerGameContainer;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.renderers.ShipRenderer;
import lintfordpickle.harvest.screens.PauseScreen;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.camera.ICamera;
import net.lintford.library.core.collisions.PhysicsWorld;
import net.lintford.library.core.collisions.resolvers.CollisionResolverRotationAndFriction;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.geometry.partitioning.GridEntity;
import net.lintford.library.core.graphics.rendertarget.RTCamera;
import net.lintford.library.core.graphics.rendertarget.RenderTarget;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final int NUM_PHYSICS_ITERATIONS = 7;

	public class PlayerViewports {

		// ---------------------------------------------
		// Variables
		// ---------------------------------------------

		public PlayerManager mPlayerManager;
		private List<PlayerGameContainer> mPlayerContainers = new ArrayList<>();

		// ---------------------------------------------
		// Properties
		// ---------------------------------------------

		public List<PlayerGameContainer> playerContainers() {
			return mPlayerContainers;
		}

		// ---------------------------------------------
		// Constructor
		// ---------------------------------------------

		public PlayerViewports(PlayerManager playerManager) {
			mPlayerManager = playerManager;
		}

		// ---------------------------------------------
		// Methods
		// ---------------------------------------------

		public void initialize(LintfordCore core) {
			final float lHalfWidth = ConstantsGame.GAME_CANVAS_WIDTH / 2.f;
			final float lHalfHeight = ConstantsGame.GAME_CANVAS_HEIGHT / 2.f;
			int numPlayers = 1;

			switch (numPlayers) {
			default:
			case 1:
				mPlayerManager.playerSessions()[0].gameContainer().viewport().set(0, 0, lHalfWidth * 2, lHalfHeight * 2);

				mPlayerContainers.add(mPlayerManager.playerSessions()[0].gameContainer());
				break;

			case 2:
				// TODO: Options for split-screen orientation (2-players)
				// Assume vertical
				mPlayerManager.playerSessions()[0].gameContainer().viewport().set(-lHalfWidth * .5f, 0, lHalfWidth, lHalfHeight * 2);
				mPlayerManager.playerSessions()[1].gameContainer().viewport().set(lHalfWidth * .5f, 0, lHalfWidth, lHalfHeight * 2);

				mPlayerContainers.add(mPlayerManager.playerSessions()[0].gameContainer());
				mPlayerContainers.add(mPlayerManager.playerSessions()[1].gameContainer());
				break;

			case 3:
				mPlayerManager.playerSessions()[0].gameContainer().viewport().set(-lHalfWidth * .5f, -lHalfHeight * .5f, lHalfWidth, lHalfHeight);
				mPlayerManager.playerSessions()[1].gameContainer().viewport().set(lHalfWidth * .5f, -lHalfHeight * .5f, lHalfWidth, lHalfHeight);
				mPlayerManager.playerSessions()[2].gameContainer().viewport().set(-lHalfWidth * .5f, lHalfHeight * .5f, lHalfWidth, lHalfHeight);

				mPlayerContainers.add(mPlayerManager.playerSessions()[0].gameContainer());
				mPlayerContainers.add(mPlayerManager.playerSessions()[1].gameContainer());
				mPlayerContainers.add(mPlayerManager.playerSessions()[2].gameContainer());
				break;

			case 4:
				mPlayerManager.playerSessions()[0].gameContainer().viewport().set(-lHalfWidth * .5f, -lHalfHeight * .5f, lHalfWidth, lHalfHeight);
				mPlayerManager.playerSessions()[1].gameContainer().viewport().set(lHalfWidth * .5f, -lHalfHeight * .5f, lHalfWidth, lHalfHeight);
				mPlayerManager.playerSessions()[2].gameContainer().viewport().set(-lHalfWidth * .5f, lHalfHeight * .5f, lHalfWidth, lHalfHeight);
				mPlayerManager.playerSessions()[3].gameContainer().viewport().set(lHalfWidth * .5f, lHalfHeight * .5f, lHalfWidth, lHalfHeight);

				mPlayerContainers.add(mPlayerManager.playerSessions()[0].gameContainer());
				mPlayerContainers.add(mPlayerManager.playerSessions()[1].gameContainer());
				mPlayerContainers.add(mPlayerManager.playerSessions()[2].gameContainer());
				mPlayerContainers.add(mPlayerManager.playerSessions()[3].gameContainer());
				break;
			}
		}

		public void loadResource(ResourceManager resourceManager) {
			final var lDisplaySettings = resourceManager.config().display();
			final var lCanvasWidth = lDisplaySettings.gameResolutionWidth();
			final var lCanvasHeight = lDisplaySettings.gameResolutionHeight();

			final var lNumPlayerContainers = mPlayerContainers.size();
			for (int i = 0; i < lNumPlayerContainers; i++) {
				final var lContainer = mPlayerContainers.get(i);

				final var lRenderTarget = mRendererManager.createRenderTarget("Game Canvas P" + i, lCanvasWidth, lCanvasHeight, 1, GL11.GL_NEAREST, false);
				final var lRTCamera = new RTCamera(lCanvasWidth, lCanvasHeight);

				lContainer.init(lRTCamera, lRenderTarget);
			}
		}

		public void unloadResources() {
			final int lNumPlayerContainers = mPlayerContainers.size();
			for (int i = 0; i < lNumPlayerContainers; i++) {
				final var lContainer = mPlayerContainers.get(i);
				mRendererManager.unloadRenderTarget(lContainer.renderTarget());
				lContainer.reset();
			}
		}
	}

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PlayerViewports mPlayerViewports;

	private PhysicsWorld world;

	// Data
	private ShipManager mShipManager;

	// Controllers
	private ShipController mShipController;

	// Renderers
	private ShipRenderer mShipRenderer;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public GameScreen(ScreenManager screenManager, boolean showHelp) {
		super(screenManager);

		final var lPlayerManager = new PlayerManager();
		lPlayerManager.addPlayer();

		mPlayerViewports = new PlayerViewports(lPlayerManager);
		mPlayerViewports.initialize(screenManager.core());
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {

		mShipManager = new ShipManager();

		final var lPlayerShip = new Ship(GridEntity.getNewEntityUid());
		lPlayerShip.isPlayerControlled = true;
		mShipManager.playerShip(lPlayerShip);

		world = new PhysicsWorld(0.0f, 9.87f);
		world.setContactResolver(new CollisionResolverRotationAndFriction());

		world.addBody(lPlayerShip.body());
		world.initialize();

		super.initialize();
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		mPlayerViewports.loadResource(resourceManager);

		super.loadResources(resourceManager);
	}

	@Override
	public void unloadResources() {
		mPlayerViewports.unloadResources();

		super.unloadResources();
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE)) {
			screenManager().addScreen(new PauseScreen(screenManager()));
			return;
		}

	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		world.stepWorld((float) core.gameTime().elapsedTimeMilli() * 0.001f, NUM_PHYSICS_ITERATIONS);

		wrapBodies(core);

	}

	@Override
	public void draw(LintfordCore core) {
		final var lGameCam = mGameCamera; // orig

		final var lViewports = mPlayerViewports.playerContainers();
		final int lNumViewports = lViewports.size();
		for (int i = 0; i < lNumViewports; i++) {
			final var lPlayerContainer = lViewports.get(i);
			drawPlayerViewport(core, lPlayerContainer.renderTarget(), lPlayerContainer.playerCamera());
		}

		GL11.glClearColor(0.06f, 0.18f, 0.11f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// restore core
		core.setActiveGameCamera(lGameCam);
		core.config().display().reapplyGlViewport();

		for (int i = 0; i < lNumViewports; i++) {
			final var lPlayerContainer = lViewports.get(i);
			Debug.debugManager().drawers().drawRenderTargetImmediate(core, lPlayerContainer.viewport(), -0.001f, lPlayerContainer.renderTarget());
		}
	}

	private void drawPlayerViewport(LintfordCore core, RenderTarget target, ICamera camera) {
		target.bind();

		core.setActiveGameCamera(camera);
		mGameCamera.setPosition(camera.getPosition().x, camera.getPosition().y);

		GL11.glClearColor(0.06f, 0.18f, 0.31f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);

		target.unbind();
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {

		mShipController = new ShipController(controllerManager, mShipManager, entityGroupUid());

	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mShipController.initialize(core);
	}

	@Override
	protected void createRenderers(LintfordCore core) {
		mShipRenderer = new ShipRenderer(mRendererManager, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mShipRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void wrapBodies(LintfordCore core) {
		final var lCamAABB = core.HUD().boundingRectangle();

		final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();
		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final var lBodiesList = world.bodies();

		for (int i = 0; i < world.numBodies(); i++) {
			final var lBody = lBodiesList.get(i);
			final var lAABB = lBody.aabb();

			final var x = lAABB.left() * lUnitsToPixels;
			final var y = lAABB.top() * lUnitsToPixels;
			final var w = lAABB.width() * lUnitsToPixels;
			final var h = lAABB.height() * lUnitsToPixels;

			if (lCamAABB.intersectsAA(x, y, w, h) == false) {
				lBody.moveTo(lBody.x, lCamAABB.top() * lPixelsToUnits);
			}
		}
	}
}
