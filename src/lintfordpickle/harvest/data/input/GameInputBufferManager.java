package lintfordpickle.harvest.data.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.time.LogicialCounter;

public class GameInputBufferManager {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private final static SatInputFrame empty_input_frame = new SatInputFrame();

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private boolean _tempFrameConsumed;
	private final SatInputFrame mTempFrameInput = new SatInputFrame();
	private final SatInputFrame mLastFrameInput = new SatInputFrame();

	// private static final int INPUT_TICK_SIZE_IN_BYTES = 3;

	private static final int HEADER_VERSION_BYTE_SIZE = 2; // short
	private static final int HEADER_TICKCOUNT_BYTE_SIZE = 4; // int

	private static final int HEADER_SIZE_IN_BYTES = HEADER_VERSION_BYTE_SIZE + HEADER_TICKCOUNT_BYTE_SIZE;

	private static final int MAX_BUFFER_SIZE_IN_BYTES = 5 * 1024 * 1024; // 5MB - I think this is ~8 hours at 3 bytes per tick?

	private ByteBuffer headerByteBuffer;
	private ByteBuffer inputByteBuffer;

	private int mTotalTicks;
	private int mCurrentTick;

	// Header
	private int mInputFrameVersion;
	private int mTotalNumTicks;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean endOfFileReached() {
		return mCurrentTick >= mTotalTicks;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameInputBufferManager() {
		_tempFrameConsumed = true;

		headerByteBuffer = MemoryUtil.memAlloc(HEADER_SIZE_IN_BYTES);
		inputByteBuffer = MemoryUtil.memAlloc(MAX_BUFFER_SIZE_IN_BYTES);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public SatInputFrame getCurrentFrame(LintfordCore core, LogicialCounter frameCounter) {

		final var lEventActionManager = core.input().eventActionManager();

		mLastFrameInput.frameNumber = (short) frameCounter.getCounter();

		mLastFrameInput.isSpaceDown = lEventActionManager.getCurrentControlActionState(SatInputFrame.INPUT_ACTION_EVENT_SPACE_DOWN);
		mLastFrameInput.isUpDown = lEventActionManager.getCurrentControlActionState(SatInputFrame.INPUT_ACTION_EVENT_UP_DOWN);
		mLastFrameInput.isDownDown = lEventActionManager.getCurrentControlActionState(SatInputFrame.INPUT_ACTION_EVENT_DOWN_DOWN);
		mLastFrameInput.isLeftDown = lEventActionManager.getCurrentControlActionState(SatInputFrame.INPUT_ACTION_EVENT_LEFT_DOWN);
		mLastFrameInput.isRightDown = lEventActionManager.getCurrentControlActionState(SatInputFrame.INPUT_ACTION_EVENT_RIGHT_DOWN);

		if (mLastFrameInput.isSpaceDown) {
			System.out.println("SPACE DOWN: " + mLastFrameInput.frameNumber);
		}

		mLastFrameInput.mouseX = core.gameCamera().getMouseWorldSpaceX();
		mLastFrameInput.mouseY = core.gameCamera().getMouseWorldSpaceY();

		writeFrameToBuffer(mLastFrameInput);

		return mLastFrameInput;
	}

	public SatInputFrame readNextFrame(LintfordCore core, LogicialCounter frameCounter) {
		// Read the next player input from the 'file' into temp
		if (_tempFrameConsumed && endOfFileReached() == false) {

			final var lRemaining = inputByteBuffer.limit() - inputByteBuffer.position();
			if (lRemaining < 3) {
				mCurrentTick++;
				System.out.println("END OF INPUT BUFFER REACHED BUT NO MARKER!");
				return empty_input_frame;
			}

			final short nextFrameNumber = inputByteBuffer.getShort();
			final int nextInput = inputByteBuffer.getInt();

			mTempFrameInput.isLeftMouseDown = (nextInput & SatInputFrame.BYTEMASK_LEFT_MOUSE) == SatInputFrame.BYTEMASK_LEFT_MOUSE;
			mTempFrameInput.isLeftShiftDown = (nextInput & SatInputFrame.BYTEMASK_LEFT_SHIFT) == SatInputFrame.BYTEMASK_LEFT_SHIFT;
			mTempFrameInput.isSpaceDown = (nextInput & SatInputFrame.BYTEMASK_SPACE) == SatInputFrame.BYTEMASK_SPACE;
			mTempFrameInput.isRDown = (nextInput & SatInputFrame.BYTEMASK_R) == SatInputFrame.BYTEMASK_R;
			mTempFrameInput.isUpDown = (nextInput & SatInputFrame.BYTEMASK_UP) == SatInputFrame.BYTEMASK_UP;
			mTempFrameInput.isDownDown = (nextInput & SatInputFrame.BYTEMASK_DOWN) == SatInputFrame.BYTEMASK_DOWN;
			mTempFrameInput.isLeftDown = (nextInput & SatInputFrame.BYTEMASK_LEFT) == SatInputFrame.BYTEMASK_LEFT;
			mTempFrameInput.isRightDown = (nextInput & SatInputFrame.BYTEMASK_RIGHT) == SatInputFrame.BYTEMASK_RIGHT;

			mTempFrameInput.frameNumber = nextFrameNumber;
			_tempFrameConsumed = false;
		}

		if (frameCounter.getCounter() == mTempFrameInput.frameNumber) {
			mLastFrameInput.copy(mTempFrameInput);
			_tempFrameConsumed = true;
			mCurrentTick = mTempFrameInput.frameNumber;
			return mLastFrameInput;
		}

		return empty_input_frame;
	}

	// ---- BUFFER

	public void writeFrameToBuffer(SatInputFrame satInput) {
		int result = 0;

		// TODO: separate mouse, keyboard and gamepad
		if (satInput.isLeftMouseDown)
			result |= SatInputFrame.BYTEMASK_LEFT_MOUSE;

		if (satInput.isLeftShiftDown)
			result |= SatInputFrame.BYTEMASK_LEFT_SHIFT;

		if (satInput.isSpaceDown)
			result |= SatInputFrame.BYTEMASK_SPACE;

		if (satInput.isRDown)
			result |= SatInputFrame.BYTEMASK_R;

		if (satInput.isUpDown)
			result |= SatInputFrame.BYTEMASK_UP;

		if (satInput.isDownDown)
			result |= SatInputFrame.BYTEMASK_DOWN;

		if (satInput.isLeftDown)
			result |= SatInputFrame.BYTEMASK_LEFT;

		if (satInput.isRightDown)
			result |= SatInputFrame.BYTEMASK_RIGHT;

		mCurrentTick = satInput.frameNumber;
		inputByteBuffer.putShort(satInput.frameNumber);
		inputByteBuffer.putInt(result);
	}

	// ---- FILE

	public void saveToFile(String filename) throws IOException {
		headerByteBuffer.putShort((short) 0);
		headerByteBuffer.putInt(mCurrentTick);

		headerByteBuffer.flip();
		inputByteBuffer.flip();

		var file = new File(filename);
		try (var output = new FileOutputStream(file)) {
			var dest = output.getChannel();

			// --- HEADER
			dest.write(headerByteBuffer);

			// --- DATA
			dest.write(inputByteBuffer);
			dest.close();
		}

		System.out.println("Saved input to file: " + filename);
		System.out.println("   number of frames: " + mCurrentTick);
		System.out.println("  aprox. length (s): " + (mTotalNumTicks / 60));
	}

	public boolean loadFromFile(String filename) {
		var file = new File(filename);

		if (!file.exists() || file.isDirectory()) {
			return false;
		}

		final var lLengthInBytes = file.length();

		try (var output = new FileInputStream(file)) {
			var src = output.getChannel();

			src.read(headerByteBuffer);
			src.read(inputByteBuffer);
			src.close();

			inputByteBuffer.flip();
			headerByteBuffer.flip();

			final var lVersion = headerByteBuffer.getShort();
			final var lNumTicks = headerByteBuffer.getInt();
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
