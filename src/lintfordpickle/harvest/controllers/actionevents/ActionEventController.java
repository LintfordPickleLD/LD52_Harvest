package lintfordpickle.harvest.controllers.actionevents;

import lintfordpickle.harvest.data.actionevents.ActionEventManager;
import lintfordpickle.harvest.data.actionevents.ActionEventManager.PlaybackMode;
import lintfordpickle.harvest.data.actionevents.SatActionEventMap;
import lintfordpickle.harvest.data.actionevents.SatActionFrame;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.input.mouse.IProcessMouseInput;
import net.lintford.library.core.time.LogicialCounter;

public class ActionEventController extends BaseController implements IProcessMouseInput {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Action Event Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final SatActionFrame mLastActionEvents = new SatActionFrame();
	private final SatActionFrame mCurrentActionEvents = new SatActionFrame();

	private ActionEventManager mActionEventManager;
	private LogicialCounter mLogicialCounter;
	private float mMouseClickTimer;
	private static final float MOUSE_CLICK_COOLDOWN_TIME = 200; // ms

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public SatActionFrame currentInput() {
		return mCurrentActionEvents;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	// TODO: Need to make sure this is added as the first controller in the list
	public ActionEventController(ControllerManager controllerManager, ActionEventManager actionEventManager, LogicialCounter frameCounter, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mLogicialCounter = frameCounter;
		mActionEventManager = actionEventManager;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		// TODO Auto-generated method stub
		super.initialize(core);
	}

	@Override
	public void update(LintfordCore core) {

		mMouseClickTimer -= core.gameTime().elapsedTimeMilli();

		switch (mActionEventManager.mode()) {
		case Read:
			if (mActionEventManager.endOfFileReached()) {
				// Notify GameStateController to end current session
				// -->
			} else {
				// Pass the read input state to the GameInputController
				final var lTempActionFrame = mActionEventManager.readNextFrame(core, mLogicialCounter);
				mCurrentActionEvents.copy(lTempActionFrame);
			}
			break;

		case Record:

			mLastActionEvents.copy(mCurrentActionEvents);
			mCurrentActionEvents.reset();

			updateInputActionEvents(core);

			if (mCurrentActionEvents._isKeyboardChanged || mCurrentActionEvents._isMouseChanged) {

				mCurrentActionEvents.frameNumber = (short) mLogicialCounter.getCounter();

				mActionEventManager.saveInputEvent(mCurrentActionEvents);
			}

			mCurrentActionEvents.frameNumber = (short) mLogicialCounter.getCounter();

			break;

		default:
		case Normal:

			mLastActionEvents.copy(mCurrentActionEvents);
			mCurrentActionEvents.reset();

			updateInputActionEvents(core);

			break;
		}

		super.update(core);
	}

	public void onExitingGame() {
		if (mActionEventManager.mode() == PlaybackMode.Record) {
			mCurrentActionEvents.markEndOfGame = true;
			mActionEventManager.saveInputEvent(mCurrentActionEvents);

			mActionEventManager.saveToFile();
		}
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void updateInputActionEvents(LintfordCore core) {

		final var lEventActionManager = core.input().eventActionManager();

		// keyboard
		mCurrentActionEvents.isSpaceDown = lEventActionManager.getCurrentControlActionStateTimed(SatActionEventMap.INPUT_ACTION_EVENT_SPACE_DOWN);
		mCurrentActionEvents.isUpDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_UP_DOWN);
		mCurrentActionEvents.isDownDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_DOWN_DOWN);
		mCurrentActionEvents.isLeftDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_LEFT_DOWN);
		mCurrentActionEvents.isRightDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_RIGHT_DOWN);

		mCurrentActionEvents.mouseX = core.gameCamera().getMouseWorldSpaceX();
		mCurrentActionEvents.mouseY = core.gameCamera().getMouseWorldSpaceY();
		mCurrentActionEvents.isLeftMouseDown = core.input().mouse().isMouseLeftButtonDown();
		mCurrentActionEvents.isLeftMouseDownTimed = core.input().mouse().isMouseLeftButtonDownTimed(this);
		mCurrentActionEvents.isRightMouseDown = core.input().mouse().isMouseRightButtonDown();

		// detect changes in keyboard / mouse / gamepad and set the flags
		// (n.b. we don't consider the mouse movement as input by default - but we record the mouse position when the player clicks a mouse button.)

		mCurrentActionEvents.setChangeFlags(mLastActionEvents);

	}

	// ---------------------------------------------
	// Inherited-Methods (IProcessMouseInput)
	// ---------------------------------------------

	@Override
	public boolean isCoolDownElapsed() {
		return mMouseClickTimer <= 0;
	}

	@Override
	public void resetCoolDownTimer() {
		mMouseClickTimer = MOUSE_CLICK_COOLDOWN_TIME;

	}

}
