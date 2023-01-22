package lintfordpickle.harvest.controllers.actionevents;

import java.nio.ByteBuffer;

import lintfordpickle.harvest.data.actionevents.SatActionEventMap;
import lintfordpickle.harvest.data.actionevents.SatActionFrame;
import net.lintford.library.controllers.actionevents.ActionEventController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.time.LogicialCounter;

public class SatActionEventController extends ActionEventController<SatActionFrame> {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int HEADER_VERSION_BYTE_SIZE = 2; // short
	private static final int HEADER_TICKCOUNT_BYTE_SIZE = 4; // int
	private static final int HEADER_SIZE_IN_BYTES = HEADER_VERSION_BYTE_SIZE + HEADER_TICKCOUNT_BYTE_SIZE;

	private static final int INPUT_MAX_BUFFER_SIZE_IN_BYTES = 5 * 1024 * 1024; // 5MB - I think this is ~8 hours at 3 bytes per tick?

	// ---------------------------------------------

	public static final boolean IS_RECORD_MODE = false;
	public static final String mRecordFilename = "input_new.lms";

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SatActionEventController(ControllerManager controllerManager, LogicialCounter frameCounter, int entityGroupUid) {
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
	protected SatActionFrame createActionFrameInstance() {
		return new SatActionFrame();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	protected void updateInputActionEvents(LintfordCore core, ActionEventPlayer player) {

		final var lEventActionManager = core.input().eventActionManager();

		// keyboard
		player.currentActionEvents.isSpaceDown = lEventActionManager.getCurrentControlActionStateTimed(SatActionEventMap.INPUT_ACTION_EVENT_SPACE_DOWN);
		player.currentActionEvents.isUpDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_UP_DOWN);
		player.currentActionEvents.isDownDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_DOWN_DOWN);
		player.currentActionEvents.isLeftDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_LEFT_DOWN);
		player.currentActionEvents.isRightDown = lEventActionManager.getCurrentControlActionState(SatActionEventMap.INPUT_ACTION_EVENT_RIGHT_DOWN);

		// mouse
		player.currentActionEvents.mouseX = core.gameCamera().getMouseWorldSpaceX();
		player.currentActionEvents.mouseY = core.gameCamera().getMouseWorldSpaceY();
		player.currentActionEvents.isLeftMouseDown = core.input().mouse().isMouseLeftButtonDown();
		player.currentActionEvents.isLeftMouseDownTimed = core.input().mouse().isMouseLeftButtonDownTimed(this);
		player.currentActionEvents.isRightMouseDown = core.input().mouse().isMouseRightButtonDown();

		// detect changes in keyboard / mouse / gamepad and set the flags
		// (n.b. we don't consider the mouse movement as input by default - but we record the mouse position when the player clicks a mouse button.)

		player.currentActionEvents.setChangeFlags(player.lastActionEvents);

	}

	// RECORDING -----------------------------------

	@Override
	protected void saveHeaderToBuffer(SatActionFrame currentFrame, ByteBuffer headerBuffer) {
		headerBuffer.putShort((short) 0);
		headerBuffer.putInt(mLogicialCounter.getCounter());
	}

	@Override
	protected void saveActionEvents(SatActionFrame frame, ByteBuffer dataBuffer) {
		var controlByte = (byte) 0;

		// control
		if (frame.markEndOfGame)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_END_GAME;

		if (frame._isKeyboardChanged)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_KEYBOARD;

		if (frame._isMouseChanged)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_MOUSE;

		if (frame._isGamepadChanged)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_GAMEPAD;

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

			if (frame.isLeftShiftDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_LEFT_SHIFT;

			if (frame.isSpaceDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_SPACE;

			if (frame.isRDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_R;

			if (frame.isUpDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_UP;

			if (frame.isDownDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_DOWN;

			if (frame.isLeftDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_LEFT;

			if (frame.isRightDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_RIGHT;

			dataBuffer.put(keyboardInputValue0);
		}

		// gamepad
		// --->

		// mouse
		if (frame._isMouseChanged) {
			System.out.println("   + mouse");
			byte mouseButtonValues = 0;

			if (frame.isLeftMouseDown)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_LEFT;

			if (frame.isMiddleMouseDown)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_MIDDLE;

			if (frame.isRightMouseDown)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_RIGHT;

			if (frame.isLeftMouseDownTimed)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_LEFT_TIMED;

			if (frame.isRightMouseDownTimed)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_RIGHT_TIMED;

			dataBuffer.put(mouseButtonValues);
			dataBuffer.putFloat(frame.mouseX);
			dataBuffer.putFloat(frame.mouseY);
		}
	}

	@Override
	protected void saveEndOfFile(SatActionFrame frame, ByteBuffer dataBuffer) {
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

			if ((nextControlByte & SatActionEventMap.BYTEMASK_CONTROL_KEYBOARD) == SatActionEventMap.BYTEMASK_CONTROL_KEYBOARD) {
				final var nextInput = dataBuffer.get();

				player.tempFrameInput.isLeftShiftDown = (nextInput & SatActionEventMap.BYTEMASK_LEFT_SHIFT) == SatActionEventMap.BYTEMASK_LEFT_SHIFT;
				player.tempFrameInput.isSpaceDown = (nextInput & SatActionEventMap.BYTEMASK_SPACE) == SatActionEventMap.BYTEMASK_SPACE;
				player.tempFrameInput.isRDown = (nextInput & SatActionEventMap.BYTEMASK_R) == SatActionEventMap.BYTEMASK_R;
				player.tempFrameInput.isUpDown = (nextInput & SatActionEventMap.BYTEMASK_UP) == SatActionEventMap.BYTEMASK_UP;
				player.tempFrameInput.isDownDown = (nextInput & SatActionEventMap.BYTEMASK_DOWN) == SatActionEventMap.BYTEMASK_DOWN;
				player.tempFrameInput.isLeftDown = (nextInput & SatActionEventMap.BYTEMASK_LEFT) == SatActionEventMap.BYTEMASK_LEFT;
				player.tempFrameInput.isRightDown = (nextInput & SatActionEventMap.BYTEMASK_RIGHT) == SatActionEventMap.BYTEMASK_RIGHT;
			}

			if ((nextControlByte & SatActionEventMap.BYTEMASK_CONTROL_MOUSE) == SatActionEventMap.BYTEMASK_CONTROL_MOUSE) {
				final var nextMouseInput = dataBuffer.get();

				player.tempFrameInput.mouseX = dataBuffer.getFloat();
				player.tempFrameInput.mouseY = dataBuffer.getFloat();

				player.tempFrameInput.isLeftMouseDown = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_LEFT) == SatActionEventMap.BYTEMASK_MOUSE_LEFT;
				player.tempFrameInput.isRightMouseDown = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_RIGHT) == SatActionEventMap.BYTEMASK_MOUSE_RIGHT;
				player.tempFrameInput.isLeftMouseDownTimed = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_LEFT_TIMED) == SatActionEventMap.BYTEMASK_MOUSE_LEFT_TIMED;
				player.tempFrameInput.isRightMouseDownTimed = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_RIGHT_TIMED) == SatActionEventMap.BYTEMASK_MOUSE_RIGHT_TIMED;
			}

			player.tempFrameInput.frameNumber = nextFrameNumber;
			mIsTempFrameConsumed = false;
		}
	}

}
