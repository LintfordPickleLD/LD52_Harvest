package lintfordpickle.harvest.data.input;

// provides the game with a snapshot of the game relevent actions from all input devices
public class SatInputFrame {

	// ---------------------------------------------
	// Event Actions
	// ---------------------------------------------

	// @formatter:off
	// Input actions are bindable
	public static final int INPUT_ACTION_EVENT_SPACE_DOWN = 0;
	public static final int INPUT_ACTION_EVENT_LEFT_DOWN  = 1;
	public static final int INPUT_ACTION_EVENT_RIGHT_DOWN = 2;
	public static final int INPUT_ACTION_EVENT_UP_DOWN    = 3;
	public static final int INPUT_ACTION_EVENT_DOWN_DOWN  = 4;

	public static final int BYTEMASK_LEFT_MOUSE = 0x00000001;
	public static final int BYTEMASK_LEFT_SHIFT = 0x00000010;
	public static final int BYTEMASK_SPACE      = 0x00000100;
	public static final int BYTEMASK_R          = 0x00001000;
	public static final int BYTEMASK_UP         = 0x00010000;
	public static final int BYTEMASK_DOWN       = 0x00100000;
	public static final int BYTEMASK_LEFT       = 0x01000000;
	public static final int BYTEMASK_RIGHT      = 0x10000000;
	// @formatter:on

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	public short frameNumber;

	// Mouse
	public boolean isLeftMouseDown;
	public float mouseX;
	public float mouseY;

	// Keyboard
	public boolean isLeftShiftDown;
	public boolean isSpaceDown;
	public boolean isRDown;
	public boolean isUpDown;
	public boolean isDownDown;
	public boolean isLeftDown;
	public boolean isRightDown;

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void copy(SatInputFrame cpyFrm) {
		isLeftMouseDown = cpyFrm.isLeftMouseDown;

		isLeftShiftDown = cpyFrm.isLeftShiftDown;
		isSpaceDown = cpyFrm.isSpaceDown;
		isRDown = cpyFrm.isRDown;
		isUpDown = cpyFrm.isUpDown;
		isDownDown = cpyFrm.isDownDown;
		isLeftDown = cpyFrm.isLeftDown;
		isRightDown = cpyFrm.isRightDown;

		mouseX = cpyFrm.mouseX;
		mouseY = cpyFrm.mouseY;

		frameNumber = cpyFrm.frameNumber;
	}

	public void reset() {
		isLeftMouseDown = false;

		isLeftShiftDown = false;
		isSpaceDown = false;
		isRDown = false;
		isUpDown = false;
		isDownDown = false;
		isLeftDown = false;
		isRightDown = false;

		mouseX = 0;
		mouseY = 0;

		frameNumber = 0;
	}

}
