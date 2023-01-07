package lintfordpickle.harvest;

import net.lintford.library.core.entity.BaseEntity;

public class ConstantsGame {

	// ---------------------------------------------
	// Setup
	// ---------------------------------------------

	public static final String FOOTER_TEXT = "Created by LintfordPickle";

	public static final String APPLICATION_NAME = "Harvest008";
	public static final String WINDOW_TITLE = "LD#52 - Harvest008";

	public static final float ASPECT_RATIO = 16.f / 9.f;

	public static final int GAME_CANVAS_WIDTH = 960;
	public static final int GAME_CANVAS_HEIGHT = 540;

	public static final int GAME_RESOURCE_GROUP_ID = BaseEntity.getEntityNumber();

	// ---------------------------------------------
	// Game
	// ---------------------------------------------

	public static final boolean LOCK_ZOOM_TO_ONE = true;

	// ---------------------------------------------
	// Debug
	// ---------------------------------------------

	public static final boolean IS_DEBUG_MODE = true;
	public static final boolean CAMERA_DEBUG_MODE = true;

	public static final boolean WRAP_OBJECTS_AROUND_SCREEN_EDGE = true;
	public static final boolean SKIP_MAIN_MENU_ON_STARTUP = true;
	public static final boolean ESCAPE_RESTART_MAIN_SCENE = true;

}
