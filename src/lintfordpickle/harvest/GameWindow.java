package lintfordpickle.harvest;

import lintfordpickle.harvest.screens.MainMenu;
import lintfordpickle.harvest.screens.MenuBackgroundScreen;
import lintfordpickle.harvest.screens.game.GameScreen;
import net.lintford.library.GameInfo;
import net.lintford.library.core.debug.Debug.DebugLogLevel;

public class GameWindow extends BaseHarvestApp {

	// ---------------------------------------------
	// Entry Point
	// ---------------------------------------------

	public static void main(String[] args) {

		final var lGameInfo = new GameInfo() {
			@Override
			public DebugLogLevel debugLogLevel() {
				return DebugLogLevel.info;
			}

			@Override
			public String applicationName() {
				return ConstantsGame.APPLICATION_NAME;
			}

			@Override
			public String windowTitle() {
				return ConstantsGame.WINDOW_TITLE;
			}

			@Override
			public int minimumWindowWidth() {
				return ConstantsGame.GAME_CANVAS_WIDTH;
			}

			@Override
			public int minimumWindowHeight() {
				return ConstantsGame.GAME_CANVAS_HEIGHT;
			}

			@Override
			public int gameCanvasResolutionWidth() {
				return ConstantsGame.GAME_CANVAS_WIDTH;
			}

			@Override
			public int gameCanvasResolutionHeight() {
				return ConstantsGame.GAME_CANVAS_HEIGHT;
			}

			@Override
			public boolean stretchGameResolution() {
				return true;
			}

			@Override
			public boolean windowResizeable() {
				return true;
			}
		};

		final var lClient = new GameWindow(lGameInfo, args);
		lClient.createWindow();
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameWindow(GameInfo pGameInfo, String[] pArgs) {
		super(pGameInfo, pArgs);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	protected void onInitializeApp() {
		super.onInitializeApp();

		if (ConstantsGame.SKIP_MAIN_MENU_ON_STARTUP) {
			mScreenManager.addScreen(new GameScreen(screenManager(), true));

			mScreenManager.initialize();
			return;
		}

		mScreenManager.addScreen(new MenuBackgroundScreen(mScreenManager));
		mScreenManager.addScreen(new MainMenu(mScreenManager));

	}

}
