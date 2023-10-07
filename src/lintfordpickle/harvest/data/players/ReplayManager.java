package lintfordpickle.harvest.data.players;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryUtil;

import lintfordpickle.harvest.data.actionevents.ActionEventFileHeader;
import net.lintfordlib.core.debug.Debug;

public class ReplayManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final ActionEventFileHeader mActionEventFileHeader;

	protected ByteBuffer mRecordFileHeader;

	public static final String RecordedGameFilename = "res/saves/record.lmp";

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ActionEventFileHeader header() {
		return mActionEventFileHeader;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public ReplayManager() {
		mActionEventFileHeader = new ActionEventFileHeader();
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public boolean loadRecordedGame() {
		var file = new File(RecordedGameFilename);

		if (!file.exists() || file.isDirectory()) {
			Debug.debugManager().logger().i(getClass().getSimpleName(), "No replay file found (" + RecordedGameFilename + ")");
			return false;
		}

		Debug.debugManager().logger().i(getClass().getSimpleName(), "Last recorded game input file found");

		mRecordFileHeader = MemoryUtil.memAlloc(ActionEventFileHeader.HEADER_SIZE_IN_BYTES);

		try (var output = new FileInputStream(file)) {
			var src = output.getChannel();

			src.read(mRecordFileHeader);
			src.close();

			mRecordFileHeader.flip();
			mActionEventFileHeader.loadHeaderFromByteBuffer(mRecordFileHeader);

		} catch (FileNotFoundException e) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Failed to load recorded file header: " + RecordedGameFilename);
			Debug.debugManager().logger().printException(getClass().getSimpleName(), e);
			return false;
		} catch (IOException e) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Failed to load recorded file header: " + RecordedGameFilename);
			Debug.debugManager().logger().printException(getClass().getSimpleName(), e);
			return false;
		} catch (Exception e) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Failed to load recorded file header: " + RecordedGameFilename);
			Debug.debugManager().logger().printException(getClass().getSimpleName(), e);
			return false;
		}

		finally {
			if (mRecordFileHeader != null) {
				mRecordFileHeader.clear();
				MemoryUtil.memFree(mRecordFileHeader);
				mRecordFileHeader = null;
			}
		}

		Debug.debugManager().logger().i(getClass().getSimpleName(), "Input file loaded: " + RecordedGameFilename);
		return true;
	}

	public boolean isRecordedGameAvailable() {
		return mActionEventFileHeader.isValidFileLoaded();
	}
}
