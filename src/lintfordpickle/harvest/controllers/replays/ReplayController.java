package lintfordpickle.harvest.controllers.replays;

import lintfordpickle.harvest.data.players.ReplayManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class ReplayController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Replay Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ReplayManager mReplayState;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ReplayManager replayManager() {
		return mReplayState;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public ReplayController(ControllerManager controllerManager, ReplayManager replayState, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mReplayState = replayState;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

}