package lintfordpickle.harvest.controllers.input;

import java.io.IOException;

import lintfordpickle.harvest.data.input.GameInputBufferManager;
import lintfordpickle.harvest.data.input.SatInputFrame;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.time.LogicialCounter;

// TODO: This needs to go into the LibraryCore and should become the defacto method of getting input from the user
// TODO: This needs to incorporate input for players 1,2,3,4
// TODO: This needs to incorporate input from keyboard, mouse and gamepads
// TODO: This needs to incorporate Keybindings
// TODO: This needs to incorporate the input sent to server
public class GameInputController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Input Frame Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private GameInputBufferManager mGameInputBufferManager;
	private LogicialCounter mLogicialCounter;

	// This is the input we need to use within the scene
	private SatInputFrame mCurrentInputFrame;

	private boolean mIsWriteMode;
	private String mFilename = "input.lmp";

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public SatInputFrame currentInput() {
		return mCurrentInputFrame;
	}

	public boolean isPlaybackFinished() {
		return !mIsWriteMode && mGameInputBufferManager.endOfFileReached();
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameInputController(ControllerManager controllerManager, GameInputBufferManager inputManager, LogicialCounter frameCounter, boolean isWriteMode, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mLogicialCounter = frameCounter;
		mGameInputBufferManager = inputManager;
		mIsWriteMode = isWriteMode;

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		if (mIsWriteMode == false) {
			if (mGameInputBufferManager.loadFromFile(mFilename) == false) {

			}
		}
	}

	@Override
	public void update(LintfordCore core) {
		// Here we need to retrieve the current input

		if (mIsWriteMode) {
			mCurrentInputFrame = mGameInputBufferManager.getCurrentFrame(core, mLogicialCounter);
		} else {
			if (mGameInputBufferManager.endOfFileReached()) {

			} else {
				mCurrentInputFrame = mGameInputBufferManager.readNextFrame(core, mLogicialCounter);
			}
		}

		super.update(core);
	}

	public void finishWritingToFile() {
		if (mIsWriteMode) {
			try {
				mGameInputBufferManager.saveToFile(mFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

}
