package lintfordpickle.harvest.screens;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.controllers.DebugCameraController;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.maths.RandomNumbers;
import net.lintford.library.core.physics.PhysicsWorld;
import net.lintford.library.core.physics.dynamics.RigidBody;
import net.lintford.library.core.physics.resolvers.CollisionResolverRotationAndFriction;
import net.lintford.library.renderers.debug.DebugPhysicsGridRenderer;
import net.lintford.library.renderers.debug.DebugPhysicsRenderer;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class TestSatScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final boolean ScaleToScreenCoords = true;
	public static final boolean RenderAABB = true;

	public static final int NUM_PHYSICS_ITERATIONS = 8;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final PhysicsWorld world;
	private RigidBody mPlayerBody;

	private DebugCameraController mDebugCameraController;

	private DebugPhysicsGridRenderer mPhysicsDebugGridRenderer;
	private DebugPhysicsRenderer mPhysicsDebugRenderer;

	private boolean simulationOn;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public TestSatScreen(ScreenManager screenManager) {
		super(screenManager);

		ConstantsPhysics.setPhysicsWorldConstants(16);
		world = new PhysicsWorld((int) (1024 * ConstantsPhysics.PixelsToUnits()), (int) (1024 * ConstantsPhysics.PixelsToUnits()), 7, 7);
		world.setGravity(.0f, 9.87f);

		simulationOn = true;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		createStaticWorld();

		final float staticFriction = 0.8f;
		final float dynamicFriction = 0.3f;

		final var lNewPlayerBody = RigidBody.createCircleBody(/* lRandomRadius */ 16f * ConstantsPhysics.PixelsToUnits(), 1.f, .5f, staticFriction, dynamicFriction, false);
		lNewPlayerBody.moveTo(0, 0);

		world.addBody(lNewPlayerBody);
		mPlayerBody = lNewPlayerBody;

		world.setContactResolver(new CollisionResolverRotationAndFriction());
		world.initialize();
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		world.unload();
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE)) {

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new MenuBackgroundScreen(mScreenManager), new MainMenu(screenManager())));
			} else {
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, new TestSatScreen(screenManager())));
			}

			return;
		}

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_SPACE)) {
			simulationOn = !simulationOn;
		}

		if (core.input().mouse().isMouseLeftButtonDown()) {
			final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

			final var lMouseX = core.gameCamera().getMouseWorldSpaceX();
			final var lMouseY = core.gameCamera().getMouseWorldSpaceY();

			final float lRandomWidth = RandomNumbers.random(.5f, 2.f);
			final float lRandomHeight = RandomNumbers.random(.5f, 1.f);

			final float staticFriction = 0.8f;
			final float dynamicFriction = 0.3f;

			if (!core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				RigidBody newBody = null;
				switch (RandomNumbers.random(0, 2)) {
				case 0:
					newBody = RigidBody.createPolygonBody(lRandomWidth, lRandomHeight, 5.f, .5f, staticFriction, dynamicFriction, false);
					break;
				default:
					newBody = RigidBody.createCircleBody(/* lRandomRadius */ 1.f, 5.77f, .5f, staticFriction, dynamicFriction, false);
					break;
				}

				newBody.moveTo(lMouseX * lPixelsToUnits, lMouseY * lPixelsToUnits);
				world.addBody(newBody);
			}
		}

		final float lSpeed = 2400.f;
		if (mPlayerBody != null) {
			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_R)) {
				mPlayerBody.angle += (float) (Math.PI * core.gameTime().elapsedTimeMilli() * 0.001f);
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_UP)) {
				mPlayerBody.addForce(0, -lSpeed);
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
				mPlayerBody.addForce(0, lSpeed);
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT)) {
				mPlayerBody.addForce(-lSpeed, 0);
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
				mPlayerBody.addForce(lSpeed, 0);
			}
		}
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		if (simulationOn) {
			world.stepWorld((float) core.gameTime().elapsedTimeMilli() * 0.001f, NUM_PHYSICS_ITERATIONS);
			clearBodiesBelowGround(core);
		}
	}

	@Override
	public void draw(LintfordCore core) {
		GL11.glClearColor(0.06f, 0.48f, 0.91f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		super.draw(core);

		core.gameCamera().setZoomFactor(0.7f);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void clearBodiesBelowGround(LintfordCore core) {
		final var lHudBb = core.HUD().boundingRectangle();
		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();
		final var lDeathHeight = lHudBb.height() / 2 * lPixelsToUnits;

		final var lBodies = world.bodies();
		final var lNumBodies = lBodies.size();
		for (int i = lNumBodies - 1; i >= 0; i--) {
			final var lBody = lBodies.get(i);
			if (!lBody.isStatic() && lBody.y > lDeathHeight)
				world.removeBody(lBody);
		}
	}

	private void createStaticWorld() {
		final var lBoundingBox = mGameCamera.boundingRectangle();

		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final var staticFriction = 0.8f;
		final var dynamicFriction = 0.5f;

		final var lGroundBox = RigidBody.createPolygonBody(lBoundingBox.width() * .5f * lPixelsToUnits, 10 * lPixelsToUnits, 1.f, .5f, .6f, .4f, true);
		lGroundBox.moveTo(0, (lBoundingBox.height() / 2.f - 190) * lPixelsToUnits);

		final var lLedge0 = RigidBody.createPolygonBody(200.f * lPixelsToUnits, 10.f * lPixelsToUnits, 1.f, .5f, staticFriction, dynamicFriction, true);
		lLedge0.moveTo(-250.f * lPixelsToUnits, -250.f * lPixelsToUnits);
		lLedge0.angle = (float) Math.toRadians(25.f);
		final var lLedge1 = RigidBody.createPolygonBody(200.f * lPixelsToUnits, 10.f * lPixelsToUnits, 1.f, .5f, staticFriction, dynamicFriction, true);
		lLedge1.moveTo(250.f * lPixelsToUnits, -250.f * lPixelsToUnits);
		lLedge1.angle = (float) Math.toRadians(-15.f);

		world.addBody(lGroundBox);
		world.addBody(lLedge0);
		world.addBody(lLedge1);

		mPlayerBody = world.getBodyByIndex(0);
	}

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mDebugCameraController = new DebugCameraController(controllerManager, mGameCamera, entityGroupUid());
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mDebugCameraController.initialize(core);
	}

	@Override
	protected void createRenderers(LintfordCore core) {
		// mPhysicsDebugGridRenderer = new PhysicsDebugGridRenderer(mRendererManager, world, entityGroupUid());
		mPhysicsDebugRenderer = new DebugPhysicsRenderer(mRendererManager, world, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		// mPhysicsDebugGridRenderer.initialize(core);
		mPhysicsDebugRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		// mPhysicsDebugGridRenderer.loadResources(resourceManager);
		mPhysicsDebugRenderer.loadResources(resourceManager);
	}

}
