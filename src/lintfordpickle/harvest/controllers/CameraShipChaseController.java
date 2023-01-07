package lintfordpickle.harvest.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.data.ships.Ship;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.camera.ICamera;
import net.lintford.library.core.maths.MathHelper;
import net.lintford.library.core.maths.Vector2f;

public class CameraShipChaseController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Camera Ship Chase Controller";

	private static final float CAMERA_MAN_MOVE_SPEED = 0.2f;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ICamera mGameCamera;
	private Ship mTrackedEntity;
	private boolean mAllowManualControl;
	private boolean mIsTrackingPlayer;

	private Vector2f mVelocity;
	public Vector2f mDesiredPosition;
	public Vector2f mPosition;
	public Vector2f mLookAhead;

	public float mZoomFactor;
	public float mZoomVelocity;

	private float mStiffness = 18.0f;
	private float mDamping = 6.0f;
	private float mMass = .5f;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ICamera gameCamera() {
		return mGameCamera;
	}

	public boolean trackPlayer() {
		return mIsTrackingPlayer;
	}

	public void trackPlayer(boolean pNewValue) {
		mIsTrackingPlayer = pNewValue;
	}

	public boolean allowManualControl() {
		return mAllowManualControl;
	}

	public void allowManualControl(boolean pNewValue) {
		mAllowManualControl = pNewValue;
	}

	@Override
	public boolean isInitialized() {
		return mGameCamera != null;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public CameraShipChaseController(ControllerManager controllerManager, ICamera camera, Ship trackedShip, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mVelocity = new Vector2f();
		mDesiredPosition = new Vector2f();
		mPosition = new Vector2f();
		mLookAhead = new Vector2f();

		final var lBody = trackedShip.body();
		mPosition.x = lBody.x;
		mPosition.y = lBody.y;

		mGameCamera = camera;
		mTrackedEntity = trackedShip;
		mIsTrackingPlayer = true;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {

	}

	public void setTrackedEntity(ICamera cameCamera, Ship trackedEntity) {
		mGameCamera = cameCamera;
		mTrackedEntity = trackedEntity;
	}

	@Override
	public void unload() {

	}

	@Override
	public boolean handleInput(LintfordCore core) {
		if (mGameCamera == null)
			return false;

		if (mAllowManualControl) {
			final float speed = CAMERA_MAN_MOVE_SPEED;

			// Just listener for clicks - couldn't be easier !!?!
			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_A)) {
				mVelocity.x -= speed;
				mIsTrackingPlayer = false;
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_D)) {
				mVelocity.x += speed;
				mIsTrackingPlayer = false;
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_S)) {
				mVelocity.y += speed;
				mIsTrackingPlayer = false;
			}

			if (core.input().keyboard().isKeyDown(GLFW.GLFW_KEY_W)) {
				mVelocity.y -= speed;
				mIsTrackingPlayer = false;
			}
		}

		return false;
	}

	@Override
	public void update(LintfordCore pCore) {
		if (mGameCamera == null)
			return;

		if (mTrackedEntity != null) {
			updateSpring(pCore);
			mGameCamera.setPosition(mPosition.x, mPosition.y);
		}
	}

	private void updateSpring(LintfordCore core) {
		mStiffness = 10000.0f;
		mDamping = 1000.0f;
		mMass = 100.f;

		updatewWorldPositions(core);
		updateWorldZoomFactor(core);

		float elapsed = (float) core.gameTime().elapsedTimeMilli() * 0.001f;

		// Calculate spring force
		float stretchX = mPosition.x - mDesiredPosition.x;
		float stretchY = mPosition.y - mDesiredPosition.y;

		float forceX = -mStiffness * stretchX - mDamping * mVelocity.x;
		float forceY = -mStiffness * stretchY - mDamping * mVelocity.y;

		// Apply acceleration
		float accelerationX = forceX / mMass;
		float accelerationY = forceY / mMass;

		mVelocity.x += accelerationX * elapsed;
		mVelocity.y += accelerationY * elapsed;

		// Apply velocity
		mPosition.x += mVelocity.x * elapsed;
		mPosition.y += mVelocity.y * elapsed;
	}

	private void updatewWorldPositions(LintfordCore core) {
		final var lBody = mTrackedEntity.body();
		float lAngle = lBody.angle;

		mLookAhead.x = (float) Math.cos(lAngle);
		mLookAhead.y = (float) Math.sin(lAngle);

		// TODO: Tracking physics objects requires a different* caemra chase controller
		final float lPixelToUnits = ConstantsPhysics.UnitsToPixels();

		float lSpeedMod = mTrackedEntity.velocityMagnitude * 0.15f;
		mDesiredPosition.x = lBody.x * lPixelToUnits + mLookAhead.x * lSpeedMod;
		mDesiredPosition.y = lBody.y * lPixelToUnits + mLookAhead.y * lSpeedMod;
	}

	private void updateWorldZoomFactor(LintfordCore core) {
		final float lZoomOutLimit = 0.8f;
		final float lZoomInLimit = 1.5f;
		final float lDefaultZoom = lZoomInLimit;

		float lEntitySpeed = mTrackedEntity.velocityMagnitude;
		if (Math.abs(lEntitySpeed) < 0.001f)
			lEntitySpeed = 0.f;
		float lTargetZoom = lDefaultZoom - lEntitySpeed;
		lTargetZoom = MathHelper.clamp(lTargetZoom, lZoomOutLimit, lZoomInLimit);

		final float lVelStepSize = 0.175f;

		if (lTargetZoom > mZoomFactor)
			mZoomVelocity += lVelStepSize;
		else
			mZoomVelocity -= lVelStepSize;

		mZoomFactor += mZoomVelocity * core.gameTime().elapsedTimeMilli() * 0.001f;

		mZoomVelocity = MathHelper.clamp(mZoomVelocity, -0.025f, 0.025f);
		mZoomFactor = MathHelper.clamp(mZoomFactor, lZoomOutLimit, lZoomInLimit);
		mGameCamera.setZoomFactor(1);

		mZoomVelocity *= 0.0987f;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void zoomIn(float zoomFactor) {
		mGameCamera.setZoomFactor(zoomFactor);
	}
}
