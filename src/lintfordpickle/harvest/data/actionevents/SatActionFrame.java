package lintfordpickle.harvest.data.actionevents;

import net.lintfordlib.core.actionevents.IActionFrame;

public class SatActionFrame implements IActionFrame {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public final static SatActionFrame EmptySatActionFrame = new SatActionFrame();

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	// control
	public int frameNumber;
	public boolean markEndOfGame;
	public boolean _isKeyboardChanged;
	public boolean _isMouseChanged;
	public boolean _isGamepadChanged;

	// Mouse
	public boolean isLeftMouseDown;
	public boolean isLeftMouseDownTimed;
	public boolean isMiddleMouseDown;
	public boolean isRightMouseDown;
	public boolean isRightMouseDownTimed;
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
	// Properties
	// ---------------------------------------------

	@Override
	public int tickNumber() {
		return frameNumber;
	}

	@Override
	public void tickNumber(int tickNumber) {
		frameNumber = tickNumber;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	public void copy(IActionFrame other) {
		final var cpyFrm = (SatActionFrame) other;

		isLeftMouseDown = cpyFrm.isLeftMouseDown;
		isLeftMouseDownTimed = cpyFrm.isLeftMouseDownTimed;
		isMiddleMouseDown = cpyFrm.isMiddleMouseDown;
		isRightMouseDown = cpyFrm.isRightMouseDown;
		isRightMouseDownTimed = cpyFrm.isRightMouseDownTimed;

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

		_isKeyboardChanged = false;
		_isMouseChanged = false;
		_isGamepadChanged = false;
	}

	// @formatter:off
	@Override
	public void setChangeFlags(IActionFrame other) {
		final var last = (SatActionFrame) other;
		final var cur = this;
		
		// for the keyboard, we need only record the changes in the key presses
		_isKeyboardChanged = 
				   // cur.isLeftShiftDown != last.isLeftShiftDown 
				   cur.isSpaceDown != last.isSpaceDown
				|| cur.isRDown  != last.isRDown
				|| cur.isUpDown  != last.isUpDown
				|| cur.isDownDown  != last.isDownDown
				|| cur.isLeftDown  != last.isLeftDown
				|| cur.isRightDown != last.isRightDown;
		
		// because it is assumed
		final var lMouseDownReleased = last.isLeftMouseDownTimed && !cur.isLeftMouseDownTimed ||  last.isRightMouseDownTimed && !cur.isRightMouseDownTimed;
		
		// any one of these events requires we save the frame (for mouse position)
		_isMouseChanged = cur.isLeftMouseDown || cur.isLeftMouseDownTimed || cur.isMiddleMouseDown || cur.isRightMouseDown || cur.isRightMouseDownTimed || lMouseDownReleased;
		
		_isGamepadChanged = false;
		
	}
	// @formatter:on

	public void reset() {
		markEndOfGame = false;
		frameNumber = 0;
		_isKeyboardChanged = false;
		_isMouseChanged = false;
		_isGamepadChanged = false;

		isLeftShiftDown = false;
		isSpaceDown = false;
		isRDown = false;
		isUpDown = false;
		isDownDown = false;
		isLeftDown = false;
		isRightDown = false;

		isLeftMouseDown = false;
		isLeftMouseDownTimed = false;
		isMiddleMouseDown = false;
		isRightMouseDown = false;
		isRightMouseDownTimed = false;
		mouseX = 0;
		mouseY = 0;

	}

	@Override
	public boolean hasChanges() {
		return _isKeyboardChanged || _isMouseChanged || _isGamepadChanged;
	}
}
