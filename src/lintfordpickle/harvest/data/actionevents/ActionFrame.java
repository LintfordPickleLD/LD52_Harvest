package lintfordpickle.harvest.data.actionevents;

import net.lintford.library.core.actionevents.IActionFrame;

public class ActionFrame implements IActionFrame {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public final static ActionFrame EmptySatActionFrame = new ActionFrame();

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	// control
	public int frameNumber;
	public boolean markEndOfGame;
	public boolean _isKeyboardChanged;
	public boolean _isMouseChanged;
	public boolean _isGamepadChanged;

	// Keyboard
	public boolean isThrottleDown;
	public boolean isDownDown;
	public boolean isThrottleLeftDown;
	public boolean isThrottleRightDown;

	// Custom
	public float positionX;
	public float positionY;
	public float velocityX;
	public float velocityY;
	public float angle;
	public float angularV;

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
		final var cpyFrm = (ActionFrame) other;

		isThrottleDown = cpyFrm.isThrottleDown;
		isDownDown = cpyFrm.isDownDown;
		isThrottleLeftDown = cpyFrm.isThrottleLeftDown;
		isThrottleRightDown = cpyFrm.isThrottleRightDown;

		frameNumber = cpyFrm.frameNumber;

		_isKeyboardChanged = false;
		_isMouseChanged = false;
		_isGamepadChanged = false;
	}

	// @formatter:off
	@Override
	public void setChangeFlags(IActionFrame other) {
		final var last = (ActionFrame) other;
		final var cur = this;
		
		// for the keyboard, we need only record the changes in the key presses
		_isKeyboardChanged =  
				   cur.isThrottleDown  != last.isThrottleDown
				|| cur.isDownDown  != last.isDownDown
				|| cur.isThrottleLeftDown  != last.isThrottleLeftDown
				|| cur.isThrottleRightDown != last.isThrottleRightDown;
		
		_isGamepadChanged = false;
		
	}
	// @formatter:on

	public void reset() {
		markEndOfGame = false;
		frameNumber = 0;
		_isKeyboardChanged = false;
		_isMouseChanged = false;
		_isGamepadChanged = false;

		isThrottleDown = false;
		isDownDown = false;
		isThrottleLeftDown = false;
		isThrottleRightDown = false;

	}

	@Override
	public boolean hasChanges() {
		return _isKeyboardChanged || _isMouseChanged || _isGamepadChanged;
	}
}
