package lintfordpickle.harvest.controllers;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.data.ships.Ship;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.camera.ICamera;
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
	public Vector2f mPosition;

	public float mZoomFactor;
	public float mZoomVelocity;

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
		mPosition = new Vector2f();

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
	public void unloadController() {

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
			// updateSpring(pCore);

			if (mTrackedEntity != null) {
				mPosition.x = mTrackedEntity.body().x * ConstantsPhysics.UnitsToPixels();
				mPosition.y = mTrackedEntity.body().y * ConstantsPhysics.UnitsToPixels();
			}

			mGameCamera.setPosition(mPosition.x, mPosition.y);
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void zoomIn(float zoomFactor) {
		mGameCamera.setZoomFactor(zoomFactor);
	}
}
