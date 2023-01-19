package lintfordpickle.harvest.data.input;

import java.util.LinkedList;

// 00h = thrust
// 01h = left
// 02h = right

// @formatter:off
public class InputFrameManager {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------
	
	private static final int THROTTLE_UP_BITMASK    = 0x00000001;
	private static final int THROTTLE_LEFT_BITMASK  = 0x00000010;
	private static final int THROTTLE_RIGHT_BITMASK = 0x00000100;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------
	
	private final ShipInput mLastFrameInput = new ShipInput();
	private final LinkedList<Integer> input = new LinkedList<>();

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------
	public void queueFrame(ShipInput t) {

		int result = 0;
		
		if(t.isUpThrottle)
			result |= THROTTLE_UP_BITMASK;
		
		if(t.isLeftThrottle)
			result |= THROTTLE_LEFT_BITMASK;
		
		if(t.isRightThrottle)
			result |= THROTTLE_RIGHT_BITMASK;
		
		input.addLast(result);
	}

	public ShipInput dequeueFrame() {
		final var dt =  input.remove(0);
		
		mLastFrameInput.isUpThrottle    = (dt & THROTTLE_UP_BITMASK)    == THROTTLE_UP_BITMASK;
		mLastFrameInput.isLeftThrottle  = (dt & THROTTLE_LEFT_BITMASK)  == THROTTLE_LEFT_BITMASK;
		mLastFrameInput.isRightThrottle = (dt & THROTTLE_RIGHT_BITMASK) == THROTTLE_RIGHT_BITMASK;
		return mLastFrameInput;
	}

	public void saveToFile() {

	}

	public void loadFromFile() {

	}

}
