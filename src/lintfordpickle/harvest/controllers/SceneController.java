package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.backgrounds.SceneManager;
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

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public SceneManager sceneManager() {
		return mSceneManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SceneController(ControllerManager controllerManager, SceneManager sceneManager, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mSceneManager = sceneManager;
	}

	@Override
	public void unload() {
		mSceneManager = null;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

}
