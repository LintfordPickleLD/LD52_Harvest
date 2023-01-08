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

	private AdWall mAdWall;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public AdWall adWall() {
		return mAdWall;
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

		mAdWall = new AdWall();
		mAdWall.set(1028 - 512, 0 - 512, 97 * 2, 1408 * 2);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void unloadController() {
		mSceneManager = null;
	}

}
