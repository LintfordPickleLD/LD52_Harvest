package lintfordpickle.harvest.data.actionevents;

public class GameActionEventMap {

	// ---------------------------------------------
	// Event Actions
	// ---------------------------------------------

	// @formatter:off
	public static final int BYTEMASK_CONTROL_END_GAME       = 0b00000001;
	public static final int BYTEMASK_CONTROL_INPUT          = 0b00000010;
	public static final int BYTEMASK_CONTROL_PHYSICS_STATE  = 0b00000100;

	public static final int BYTEMASK_INPUT_UP               = 0b00000001;
	public static final int BYTEMASK_INPUT_DOWN             = 0b00000010;
	public static final int BYTEMASK_INPUT_LEFT             = 0b00000100;
	public static final int BYTEMASK_INPUT_RIGHT            = 0b00001000;
		
	// Input actions are bindable
	public static final int INPUT_ACTION_EVENT_THRUSTER_UP    = 0;
	public static final int INPUT_ACTION_EVENT_THRUSTER_LEFT  = 1;
	public static final int INPUT_ACTION_EVENT_THRUSTER_RIGHT = 2;
	public static final int INPUT_ACTION_EVENT_THRUSTER_DOWN  = 4;
	
	public static final int INPUT_ACTION_EVENT_W  		    = 5;
	public static final int INPUT_ACTION_EVENT_S            = 6;
	public static final int INPUT_ACTION_EVENT_A            = 7;
	public static final int INPUT_ACTION_EVENT_D            = 8;
	// @formatter:on

}
