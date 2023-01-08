package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.platforms.PlatformManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class PlatformsController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Platforms Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PlatformManager mPlatformManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public PlatformManager platformManager() {
		return mPlatformManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PlatformsController(ControllerManager controllerManager, PlatformManager platformManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mPlatformManager = platformManager;
	}

	@Override
	public void unloadController() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void update(LintfordCore core) {
		super.update(core);
	}

}
