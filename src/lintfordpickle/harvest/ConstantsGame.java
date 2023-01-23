package lintfordpickle.harvest;

import net.lintford.library.core.entity.BaseEntity;

public class ConstantsGame {

	// ---------------------------------------------
	// Setup
	// ---------------------------------------------

	public static final String FOOTER_TEXT = "LintfordPickle LD#52 - Harvester 008";

	public static final String APPLICATION_NAME = "Harvester 008";
	public static final String WINDOW_TITLE = "LD#52 - Harvester 008";

	public static final float ASPECT_RATIO = 16.f / 9.f;

	public static final int GAME_CANVAS_WIDTH = 960;
	public static final int GAME_CANVAS_HEIGHT = 540;

	public static final int GAME_RESOURCE_GROUP_ID = BaseEntity.getEntityNumber();

	// ---------------------------------------------
	// Game
	// ---------------------------------------------

	public static final boolean LOCK_ZOOM_TO_ONE = true;

	public static final float TOTAL_GAME_TIME_AT_START = 60 * 4 * 1000;

	// ---------------------------------------------
	// Debug
	// ---------------------------------------------

	public static final boolean IS_DEBUG_MODE = false;
	public static final boolean CAMERA_DEBUG_MODE = true;
	public static final boolean PHYICS_DEBUG_MODE = false;
	public static final boolean SHIP_DEBUG_MODE = false;

	public static final boolean WRAP_OBJECTS_AROUND_SCREEN_EDGE = false;
	public static final boolean SKIP_MAIN_MENU_ON_STARTUP = false;
	public static final boolean LAUNCH_SAT_TEST_SCREEN_ON_STARTUP = false;
	public static final boolean ESCAPE_RESTART_MAIN_SCENE = false;
}
