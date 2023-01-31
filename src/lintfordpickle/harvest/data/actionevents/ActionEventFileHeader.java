package lintfordpickle.harvest.data.actionevents;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ActionEventFileHeader {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int VERSION_LENGTH_IN_BYTES = 2;
	private static final int NAME_LENGTH_IN_BYTES = 255;
	private static final int DELIVERED_FOOD_LENGTH_IN_BYTES = 2;
	private static final int RUNTIME_LENGTH_IN_BYTES = 4;

	public static final int HEADER_SIZE_IN_BYTES = VERSION_LENGTH_IN_BYTES + NAME_LENGTH_IN_BYTES + DELIVERED_FOOD_LENGTH_IN_BYTES + RUNTIME_LENGTH_IN_BYTES;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private short mFileversion;
	private String mRecordName;
	private short mDeliveredFood;
	private int mRunTimeInSeconds;

	private boolean mIsValidFileLoaded;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public String name() {
		return mRecordName;
	}

	public short fileVersion() {
		return mFileversion;
	}

	public short numberdeliveredFood() {
		return mDeliveredFood;
	}

	public int runtimeInSeconds() {
		return mRunTimeInSeconds;
	}

	public boolean isValidFileLoaded() {
		return mIsValidFileLoaded;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public ActionEventFileHeader() {
		reset();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	public void initialize(short fileVersion, String recordName, short numFoodDelivered, int runtimeInMs) {
		mFileversion = fileVersion;
		mRecordName = recordName;
		mDeliveredFood = numFoodDelivered;
		mRunTimeInSeconds = runtimeInMs;
	}

	public void reset() {
		mFileversion = 0;
		mRecordName = null;
		mDeliveredFood = 0;
		mRunTimeInSeconds = 0;

		mIsValidFileLoaded = false;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void saveHeaderToByteBuffer(ByteBuffer headerBuffer) {
		if (headerBuffer == null || headerBuffer.capacity() == 0) {
			mIsValidFileLoaded = false;
		}

		final var lCutString = mRecordName.substring(0, Math.min(mRecordName.length(), NAME_LENGTH_IN_BYTES));
		final var lPreparedRecordName = String.format("%-" + NAME_LENGTH_IN_BYTES + "s", lCutString).getBytes(StandardCharsets.US_ASCII);

		headerBuffer.position(0);

		headerBuffer.putShort(mFileversion);
		headerBuffer.put(lPreparedRecordName);
		headerBuffer.putShort(mDeliveredFood);
		headerBuffer.putInt(mRunTimeInSeconds);
	}

	public void loadHeaderFromByteBuffer(ByteBuffer headerBuffer) {
		if (headerBuffer == null || headerBuffer.capacity() == 0) {
			mIsValidFileLoaded = false;
		}

		headerBuffer.position(0);

		mFileversion = headerBuffer.getShort();
		mRecordName = readString(headerBuffer);
		mDeliveredFood = headerBuffer.getShort();
		mRunTimeInSeconds = headerBuffer.getInt();

		mIsValidFileLoaded = (headerBuffer.limit() - headerBuffer.position()) == 0;
	}

	private static String readString(ByteBuffer headerBuffer) {
		if (headerBuffer.position() + NAME_LENGTH_IN_BYTES > headerBuffer.capacity() - 1 || NAME_LENGTH_IN_BYTES < 0)
			throw new IllegalStateException("Tried to read string of length " + NAME_LENGTH_IN_BYTES + " at " + headerBuffer.position());

		byte[] buf = new byte[NAME_LENGTH_IN_BYTES];
		headerBuffer.get(buf);
		try {
			return new String(buf, "ISO-8859-15");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
