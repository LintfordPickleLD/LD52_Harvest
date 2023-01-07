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

	public static final int GAME_CANVAS_WIDTH = (int) 960;
	public static final int GAME_CANVAS_HEIGHT = (int) 540;

	public static final int GAME_RESOURCE_GROUP_ID = BaseEntity.getEntityNumber();

	// ---------------------------------------------
	// Game
	// ---------------------------------------------

	// ---------------------------------------------
	// Debug
	// ---------------------------------------------

	public static final boolean IS_DEBUG_MODE = true;
	public static final boolean CAMERA_DEBUG_MODE = true;

}
