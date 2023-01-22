package lintfordpickle.harvest.controllers.actionevents;

import java.nio.ByteBuffer;

import lintfordpickle.harvest.data.actionevents.ActionEventMap;
import lintfordpickle.harvest.data.actionevents.ActionFrame;
import net.lintford.library.controllers.actionevents.ActionEventController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.time.LogicialCounter;

public class GameActionEventController extends ActionEventController<ActionFrame> {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int HEADER_VERSION_BYTE_SIZE = 2; // short
	private static final int HEADER_TICKCOUNT_BYTE_SIZE = 4; // int
	private static final int HEADER_SIZE_IN_BYTES = HEADER_VERSION_BYTE_SIZE + HEADER_TICKCOUNT_BYTE_SIZE;

	private static final int INPUT_MAX_BUFFER_SIZE_IN_BYTES = 5 * 1024 * 1024;

	// ---------------------------------------------

	public static final boolean IS_RECORD_MODE = true;
	public static final String mRecordFilename = "input_new.lms";

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameActionEventController(ControllerManager controllerManager, LogicialCounter frameCounter, int entityGroupUid) {
		super(controllerManager, frameCounter, entityGroupUid);
	}

	// ---------------------------------------------

	@Override
	protected int getHeaderSizeInBytes() {
		return HEADER_SIZE_IN_BYTES;
	}

	@Override
	protected int getInputSizeInBytes() {
		return INPUT_MAX_BUFFER_SIZE_IN_BYTES;
	}

	@Override
	protected ActionFrame createActionFrameInstance() {
		return new ActionFrame();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	protected void updateInputActionEvents(LintfordCore core, ActionEventPlayer player) {

		final var lEventActionManager = core.input().eventActionManager();

		// keyboard
		player.currentActionEvents.isThrottleDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_UP_DOWN);
		player.currentActionEvents.isDownDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_DOWN_DOWN);
		player.currentActionEvents.isThrottleLeftDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_LEFT_DOWN);
		player.currentActionEvents.isThrottleRightDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_RIGHT_DOWN);

		// detect changes in keyboard / mouse / gamepad and set the flags
		// (n.b. we don't consider the mouse movement as input by default - but we record the mouse position when the player clicks a mouse button.)

		player.currentActionEvents.setChangeFlags(player.lastActionEvents);

	}

	// RECORDING -----------------------------------

	@Override
	protected void saveHeaderToBuffer(ActionFrame currentFrame, ByteBuffer headerBuffer) {
		headerBuffer.putShort((short) 0);
		headerBuffer.putInt(mLogicialCounter.getCounter());
	}

	@Override
	protected void saveActionEvents(ActionFrame frame, ByteBuffer dataBuffer) {
		var controlByte = (byte) 0;

		// control
		if (frame.markEndOfGame)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_END_GAME;

		if (frame._isKeyboardChanged)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_KEYBOARD;

		if (frame._isMouseChanged)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_MOUSE;

		if (frame._isGamepadChanged)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_GAMEPAD;

		mCurrentTick = frame.frameNumber;
		dataBuffer.putShort((short) frame.frameNumber);
		dataBuffer.put(controlByte);

		System.out.println("Writing new input frame");
		System.out.println("  frame number: " + frame.frameNumber);
		System.out.println("       control: " + (byte) controlByte);

		// keyboard
		if (frame._isKeyboardChanged) {
			System.out.println("  + keyboard");
			byte keyboardInputValue0 = 0;

			if (frame.isThrottleDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_UP;

			if (frame.isDownDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_DOWN;

			if (frame.isThrottleLeftDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_LEFT;

			if (frame.isThrottleRightDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_RIGHT;

			dataBuffer.put(keyboardInputValue0);
		}

		// gamepad
		// --->
	}

	@Override
	protected void saveEndOfFile(ActionFrame frame, ByteBuffer dataBuffer) {
		frame.markEndOfGame = true;
		saveActionEvents(frame, dataBuffer);
	}

	// PLAYBACK -----------------------------------

	@Override
	protected void readHeaderBuffer(ByteBuffer headerBuffer) {
		headerBuffer.getShort(); // consume version
		mTotalTicks = headerBuffer.getInt();

	}

	@Override
	protected void readNextFrame(ByteBuffer dataBuffer, ActionEventPlayer player) {
		// Read the next player input from the 'file' into temp
		if (mIsTempFrameConsumed) {
			final var lRemaining = dataBuffer.limit() - dataBuffer.position();
			if (lRemaining < 2) { // only check for the exisitence of the tick number
				System.out.println("END OF INPUT BUFFER REACHED BUT NO MARKER!");
				return;
			}

			final short nextFrameNumber = dataBuffer.getShort();
			final var nextControlByte = dataBuffer.get();

			System.out.println("next frame input on: " + nextFrameNumber);
			System.out.println("            control: " + nextControlByte);

			if ((nextControlByte & ActionEventMap.BYTEMASK_CONTROL_KEYBOARD) == ActionEventMap.BYTEMASK_CONTROL_KEYBOARD) {
				final var nextInput = dataBuffer.get();

				player.tempFrameInput.isThrottleDown = (nextInput & ActionEventMap.BYTEMASK_UP) == ActionEventMap.BYTEMASK_UP;
				player.tempFrameInput.isThrottleLeftDown = (nextInput & ActionEventMap.BYTEMASK_LEFT) == ActionEventMap.BYTEMASK_LEFT;
				player.tempFrameInput.isThrottleRightDown = (nextInput & ActionEventMap.BYTEMASK_RIGHT) == ActionEventMap.BYTEMASK_RIGHT;
			}

			player.tempFrameInput.frameNumber = nextFrameNumber;
			mIsTempFrameConsumed = false;
		}
	}

}
