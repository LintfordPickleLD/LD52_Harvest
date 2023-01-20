package lintfordpickle.harvest.data.actionevents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.time.LogicialCounter;

public class ActionEventManager {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	// @formatter:off
	public enum PlaybackMode {
		Normal,   // manager is inactive
		Record, // manager records input and state and stores into file
		Read    // manager provides data from input file
	}
	// @formatter:on

	private static final int HEADER_VERSION_BYTE_SIZE = 2; // short
	private static final int HEADER_TICKCOUNT_BYTE_SIZE = 4; // int
	private static final int HEADER_SIZE_IN_BYTES = HEADER_VERSION_BYTE_SIZE + HEADER_TICKCOUNT_BYTE_SIZE;

	private static final int INPUT_MAX_BUFFER_SIZE_IN_BYTES = 5 * 1024 * 1024; // 5MB - I think this is ~8 hours at 3 bytes per tick?

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private boolean mIsTempFrameConsumed;
	private final SatActionFrame mTempFrameInput = new SatActionFrame();
	private final SatActionFrame mLastFrameInput = new SatActionFrame();

	private ByteBuffer mHeaderByteBuffer;
	private ByteBuffer mInputByteBuffer;

	private PlaybackMode mPlaybackMode = PlaybackMode.Normal;
	private String mFilename;

	private int mTotalTicks;
	private int mCurrentTick;

	// Header
	private int mTotalNumTicks;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public PlaybackMode mode() {
		return mPlaybackMode;
	}

	/** Returns true if we are in READ mode and the end of the input file has been reached. */
	public boolean endOfFileReached() {
		return mPlaybackMode == PlaybackMode.Read && mCurrentTick >= mTotalTicks;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public ActionEventManager() {
		mIsTempFrameConsumed = true;

		mHeaderByteBuffer = MemoryUtil.memAlloc(HEADER_SIZE_IN_BYTES);
		mInputByteBuffer = MemoryUtil.memAlloc(INPUT_MAX_BUFFER_SIZE_IN_BYTES);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void setNormalMode() {
		mPlaybackMode = PlaybackMode.Normal;
	}

	public void setRecordingMode(String filename) {
		if (mPlaybackMode != PlaybackMode.Normal)
			return;

		mFilename = filename;
		mPlaybackMode = PlaybackMode.Record;
	}

	public void setPlaybackMode(String filename) {
		if (mPlaybackMode != PlaybackMode.Normal)
			return;

		mFilename = filename;
		if (loadFromFile(filename)) {
			mPlaybackMode = PlaybackMode.Read;
		}
	}

	public SatActionFrame readNextFrame(LintfordCore core, LogicialCounter frameCounter) {
		// Read the next player input from the 'file' into temp
		if (mIsTempFrameConsumed && endOfFileReached() == false) {

			final var lRemaining = mInputByteBuffer.limit() - mInputByteBuffer.position();
			if (lRemaining < 2) { // only check for the exisitence of the tick number
				mCurrentTick++;
				System.out.println("END OF INPUT BUFFER REACHED BUT NO MARKER!");
				return SatActionFrame.EmptySatActionFrame;
			}

			final short nextFrameNumber = mInputByteBuffer.getShort();
			final short nextControlByte = mInputByteBuffer.get();

			System.out.println("next frame input on: " + nextFrameNumber);
			System.out.println("            control: " + nextControlByte);

			if ((nextControlByte & SatActionEventMap.BYTEMASK_CONTROL_KEYBOARD) == SatActionEventMap.BYTEMASK_CONTROL_KEYBOARD) {
				final int nextInput = mInputByteBuffer.getInt();

				mTempFrameInput.isLeftShiftDown = (nextInput & SatActionEventMap.BYTEMASK_LEFT_SHIFT) == SatActionEventMap.BYTEMASK_LEFT_SHIFT;
				mTempFrameInput.isSpaceDown = (nextInput & SatActionEventMap.BYTEMASK_SPACE) == SatActionEventMap.BYTEMASK_SPACE;
				mTempFrameInput.isRDown = (nextInput & SatActionEventMap.BYTEMASK_R) == SatActionEventMap.BYTEMASK_R;
				mTempFrameInput.isUpDown = (nextInput & SatActionEventMap.BYTEMASK_UP) == SatActionEventMap.BYTEMASK_UP;
				mTempFrameInput.isDownDown = (nextInput & SatActionEventMap.BYTEMASK_DOWN) == SatActionEventMap.BYTEMASK_DOWN;
				mTempFrameInput.isLeftDown = (nextInput & SatActionEventMap.BYTEMASK_LEFT) == SatActionEventMap.BYTEMASK_LEFT;
				mTempFrameInput.isRightDown = (nextInput & SatActionEventMap.BYTEMASK_RIGHT) == SatActionEventMap.BYTEMASK_RIGHT;
			}

			if ((nextControlByte & SatActionEventMap.BYTEMASK_CONTROL_MOUSE) == SatActionEventMap.BYTEMASK_CONTROL_MOUSE) {
				final int nextMouseInput = mInputByteBuffer.getInt();

				mTempFrameInput.mouseX = mInputByteBuffer.getFloat();
				mTempFrameInput.mouseY = mInputByteBuffer.getFloat();

				mTempFrameInput.isLeftMouseDown = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_LEFT) == SatActionEventMap.BYTEMASK_MOUSE_LEFT;
				mTempFrameInput.isRightMouseDown = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_RIGHT) == SatActionEventMap.BYTEMASK_MOUSE_RIGHT;
				mTempFrameInput.isLeftMouseDownTimed = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_LEFT_TIMED) == SatActionEventMap.BYTEMASK_MOUSE_LEFT_TIMED;
				mTempFrameInput.isRightMouseDownTimed = (nextMouseInput & SatActionEventMap.BYTEMASK_MOUSE_RIGHT_TIMED) == SatActionEventMap.BYTEMASK_MOUSE_RIGHT_TIMED;
			}

			mTempFrameInput.frameNumber = nextFrameNumber;
			mIsTempFrameConsumed = false;
		}

		// Only update the current frame input when we reach the correct point in time
		if (frameCounter.getCounter() == mTempFrameInput.frameNumber) {
			mLastFrameInput.copy(mTempFrameInput);
			mIsTempFrameConsumed = true;

			mCurrentTick = mTempFrameInput.frameNumber;
			return mLastFrameInput;
		}

		return SatActionFrame.EmptySatActionFrame;
	}

	// ---- BUFFER

	public void saveInputEvent(SatActionFrame eventFrame) {
		writeFrameToBuffer(eventFrame);
	}

	private void writeFrameToBuffer(SatActionFrame satInput) {
		var controlByte = (byte) 0;

		// control
		if (satInput.markEndOfGame)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_END_GAME;

		if (satInput._isKeyboardChanged)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_KEYBOARD;

		if (satInput._isMouseChanged)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_MOUSE;

		if (satInput._isGamepadChanged)
			controlByte |= SatActionEventMap.BYTEMASK_CONTROL_GAMEPAD;

		mCurrentTick = satInput.frameNumber;
		mInputByteBuffer.putShort(satInput.frameNumber);
		mInputByteBuffer.put(controlByte);

		System.out.println("Writing new input frame");
		System.out.println("  frame number: " + satInput.frameNumber);
		System.out.println("       control: " + (byte) controlByte);

		// keyboard
		if (satInput._isKeyboardChanged) {
			System.out.println("  + keyboard");
			int keyboardInputValue0 = 0;
			// int keyboardInputValue1 = 0;

			if (satInput.isLeftShiftDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_LEFT_SHIFT;

			if (satInput.isSpaceDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_SPACE;

			if (satInput.isRDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_R;

			if (satInput.isUpDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_UP;

			if (satInput.isDownDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_DOWN;

			if (satInput.isLeftDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_LEFT;

			if (satInput.isRightDown)
				keyboardInputValue0 |= SatActionEventMap.BYTEMASK_RIGHT;

			mInputByteBuffer.putInt(keyboardInputValue0);
		}

		// gamepad
		// --->

		// mouse
		if (satInput._isMouseChanged) {
			System.out.println("   + mouse");
			int mouseButtonValues = 0;

			if (satInput.isLeftMouseDown)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_LEFT;

			if (satInput.isMiddleMouseDown)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_MIDDLE;

			if (satInput.isRightMouseDown)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_RIGHT;

			if (satInput.isLeftMouseDownTimed)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_LEFT_TIMED;

			if (satInput.isRightMouseDownTimed)
				mouseButtonValues |= SatActionEventMap.BYTEMASK_MOUSE_RIGHT_TIMED;

			mInputByteBuffer.putInt(mouseButtonValues);
			mInputByteBuffer.putFloat(satInput.mouseX);
			mInputByteBuffer.putFloat(satInput.mouseY);
		}
	}

	// ---- FILE

	public void saveToFile() {
		if (mFilename == null || mFilename.length() == 0)
			return;

		// HEADER
		mHeaderByteBuffer.putShort((short) 0); // version
		mHeaderByteBuffer.putInt(mCurrentTick); // total ticks

		mHeaderByteBuffer.flip();
		mInputByteBuffer.flip();

		var file = new File(mFilename);
		try (var output = new FileOutputStream(file)) {
			var dest = output.getChannel();

			// --- HEADER
			dest.write(mHeaderByteBuffer);

			// --- DATA
			dest.write(mInputByteBuffer);
			dest.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Failed to save input to file: " + mFilename);
			System.out.println(e.getLocalizedMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to save input to file: " + mFilename);
			System.out.println(e.getLocalizedMessage());
			return;
		}

		System.out.println("Saved input to file: " + mFilename);
		System.out.println("   number of frames: " + mCurrentTick);
		System.out.println("  aprox. length (s): " + (mTotalNumTicks / 60));
	}

	private boolean loadFromFile(String filename) {
		var file = new File(filename);

		if (!file.exists() || file.isDirectory()) {
			return false;
		}

		final var lLengthInBytes = file.length();

		try (var output = new FileInputStream(file)) {
			var src = output.getChannel();

			src.read(mHeaderByteBuffer);
			src.read(mInputByteBuffer);
			src.close();

			mInputByteBuffer.flip();
			mHeaderByteBuffer.flip();

			final var lVersion = mHeaderByteBuffer.getShort();
			final var lNumTicks = mHeaderByteBuffer.getInt();
			mTotalTicks = lNumTicks;

			System.out.println("Loading inputs from file: " + filename);
			System.out.println("           file size (b): " + lLengthInBytes);
			System.out.println("            file version: " + lVersion);
			System.out.println("         number of ticks: " + mTotalTicks);
			System.out.println("      approx. length (s): " + (mTotalTicks / 3 / 60));

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
