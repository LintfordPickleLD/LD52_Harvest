package lintfordpickle.harvest.screens;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.controllers.PhysicsCollisionCallback;
import lintfordpickle.harvest.controllers.actionevents.SatActionEventController;
import lintfordpickle.harvest.controllers.camera.DebugCameraController;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.physics.PhysicsWorld;
import net.lintford.library.core.physics.dynamics.RigidBody;
import net.lintford.library.core.physics.resolvers.CollisionResolverRotationAndFriction;
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

	private PhysicsWorld world;
	private RigidBody mPlayerBody;

	private SatActionEventController mActionEventController;
	private DebugCameraController mDebugCameraController;

	private DebugPhysicsRenderer mPhysicsDebugRenderer;

	private boolean simulationOn;
	private int mActiveActionPlayeruid;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public TestSatScreen(ScreenManager screenManager) {
		super(screenManager);

		setupPhysicsWorld();
	}

	private void setupPhysicsWorld() {
		ConstantsPhysics.setPhysicsWorldConstants(16);

		world = new PhysicsWorld((int) (1024 * ConstantsPhysics.PixelsToUnits()), (int) (1024 * ConstantsPhysics.PixelsToUnits()), 7, 7);
		world.setGravity(.0f, 9.87f);
		world.addCollisionCallback(new PhysicsCollisionCallback());
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
	public void unloadResources() {
		super.unloadResources();

		world.unload();
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		if (core.input().keyboard().isKeyDownTimed(GLFW.GLFW_KEY_ESCAPE, this)) {
			mActionEventController.onExitingGame();

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), false, new MenuBackgroundScreen(mScreenManager), new MainMenu(screenManager())));
			} else {
				screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, new TestSatScreen(screenManager())));
			}

			return;
		}

		final var lActionEventPlayer = mActionEventController.actionEventPlayer(mActiveActionPlayeruid);
		final var lInputFrame = lActionEventPlayer.currentActionEvents;

		if (lInputFrame.isSpaceDown) {
			simulationOn = !simulationOn;
		}

		if (lInputFrame.isLeftMouseDownTimed) {
			final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

			final var lMouseX = lInputFrame.mouseX;
			final var lMouseY = lInputFrame.mouseY;

			final float staticFriction = 0.8f;
			final float dynamicFriction = 0.3f;

			if (!core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
				final var newBody = RigidBody.createCircleBody(/* lRandomRadius */ 1.f, 5.77f, .5f, staticFriction, dynamicFriction, false);

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

		final var lHudBb = core.HUD().boundingRectangle();
		final var lFontUnit = mRendererManager.uiTextFont();
		lFontUnit.begin(core.HUD());
		lFontUnit.drawText("h_frame: " + inputCounter().getCounter(), lHudBb.left() + 5.f, lHudBb.top() + 5.f, -0.01f, 1.f);
		lFontUnit.end();

		core.gameCamera().setZoomFactor(0.7f);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void clearBodiesBelowGround(LintfordCore core) {
		final var lHudBb = mGameCamera.boundingRectangle();
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
		final var lPixelsToUnits = ConstantsPhysics.PixelsToUnits();

		final var staticFriction = 0.8f;
		final var dynamicFriction = 0.5f;

		final var lGroundBox = RigidBody.createPolygonBody(1200 * .5f * lPixelsToUnits, 10 * lPixelsToUnits, 1.f, .5f, .6f, .4f, true);
		lGroundBox.moveTo(0, 370.f * lPixelsToUnits);

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
		mActionEventController = new SatActionEventController(controllerManager, inputCounter(), entityGroupUid());

		boolean recMode = false;

		if (recMode)
			mActiveActionPlayeruid = mActionEventController.createActionRecorder(1, "input_new.lms");

		else
			mActiveActionPlayeruid = mActionEventController.createActionPlayback(1, "input_new.lms");

	}

	@Override
	protected void initializeControllers(LintfordCore core) {

		mDebugCameraController.initialize(core);
	}

	@Override
	protected void createRenderers(LintfordCore core) {
		mPhysicsDebugRenderer = new DebugPhysicsRenderer(mRendererManager, world, entityGroupUid());
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mPhysicsDebugRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mPhysicsDebugRenderer.loadResources(resourceManager);
	}

}
