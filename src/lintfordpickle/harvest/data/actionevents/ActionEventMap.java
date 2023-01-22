package lintfordpickle.harvest.data.actionevents;

public class ActionEventMap {

	// ---------------------------------------------
	// Event Actions
	// ---------------------------------------------

	// @formatter:off
	public static final int BYTEMASK_CONTROL_END_GAME     = 0b00000001;
	public static final int BYTEMASK_CONTROL_KEYBOARD     = 0b00000010;
	public static final int BYTEMASK_CONTROL_MOUSE        = 0b00000100;
	public static final int BYTEMASK_CONTROL_GAMEPAD      = 0b00001000;

	public static final int BYTEMASK_UP                   = 0b00000001;
	public static final int BYTEMASK_DOWN                 = 0b00000010;
	public static final int BYTEMASK_LEFT                 = 0b00000100;
	public static final int BYTEMASK_RIGHT                = 0b00001000;
		
	// Input actions are bindable
	public static final int INPUT_ACTION_EVENT_SPACE_DOWN = 0;
	public static final int INPUT_ACTION_EVENT_LEFT_DOWN  = 1;
	public static final int INPUT_ACTION_EVENT_RIGHT_DOWN = 2;
	public static final int INPUT_ACTION_EVENT_UP_DOWN    = 3;
	public static final int INPUT_ACTION_EVENT_DOWN_DOWN  = 4;
	
	public static final int INPUT_ACTION_EVENT_W  		  = 5;
	public static final int INPUT_ACTION_EVENT_S          = 6;
	public static final int INPUT_ACTION_EVENT_A          = 7;
	public static final int INPUT_ACTION_EVENT_D          = 8;
	// @formatter:on

}
