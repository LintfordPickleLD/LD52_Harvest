package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.scene.AdWall;
import lintfordpickle.harvest.data.scene.backgrounds.SceneManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;

public class SceneController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Scene Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private SceneManager mSceneManager;

	private AdWall mVerticalAdWall;
	private AdWall mHorizontalAdWall;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public AdWall verticalAdWall() {
		return mVerticalAdWall;
	}

	public AdWall horizontalAdWall() {
		return mHorizontalAdWall;
	}

	public SceneManager sceneManager() {
		return mSceneManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SceneController(ControllerManager controllerManager, SceneManager sceneManager, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mSceneManager = sceneManager;

		mVerticalAdWall = new AdWall();
		mVerticalAdWall.set(-1028 + 1027, -1028 + 0, 60 * 2, 710 * 2);
		mVerticalAdWall.adWallTextureName = "TEXTURE_ADWALL_VERT";

		mHorizontalAdWall = new AdWall();
		mHorizontalAdWall.set(-1028 + 480, -1028 + 430, 402, 137);
		mHorizontalAdWall.adWallTextureName = "TEXTURE_ADWALL_HORIZONTAL";
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void unloadController() {
		mSceneManager = null;
	}

}
