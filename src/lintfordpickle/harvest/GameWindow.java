package lintfordpickle.harvest;

import org.lwjgl.glfw.GLFW;

import lintfordpickle.harvest.data.actionevents.SatActionEventMap;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.screens.MainMenu;
import lintfordpickle.harvest.screens.MenuBackgroundScreen;
import lintfordpickle.harvest.screens.game.SurvivalGameScreen;
import net.lintford.library.GameInfo;
import net.lintford.library.core.debug.Debug.DebugLogLevel;
import net.lintford.library.core.input.KeyEventActionManager;

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
			final var lPlayerManager = new PlayerManager();
			final var lGhostPlayer = lPlayerManager.addNewPlayer();
			lGhostPlayer.setPlayback("ghost.lms");

			mScreenManager.addScreen(new SurvivalGameScreen(screenManager(), lPlayerManager));

			mScreenManager.initialize();
			return;
		}

		mScreenManager.addScreen(new MenuBackgroundScreen(mScreenManager));
		mScreenManager.addScreen(new MainMenu(mScreenManager));
		mScreenManager.initialize();
	}

	@Override
	protected void onInitializeInputActions(KeyEventActionManager eventActionManager) {

		eventActionManager.registerNewKeyboardEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_UP, GLFW.GLFW_KEY_W);
		eventActionManager.registerNewGamepadEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_UP, GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP);

		eventActionManager.registerNewKeyboardEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_LEFT, GLFW.GLFW_KEY_A);
		eventActionManager.registerNewGamepadEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_LEFT, GLFW.GLFW_HAT_LEFT);

		eventActionManager.registerNewKeyboardEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_RIGHT, GLFW.GLFW_KEY_D);
		eventActionManager.registerNewGamepadEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_RIGHT, GLFW.GLFW_HAT_RIGHT);

		eventActionManager.registerNewKeyboardEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_DOWN, GLFW.GLFW_KEY_S);
		eventActionManager.registerNewGamepadEventAction(SatActionEventMap.INPUT_ACTION_EVENT_THRUSTER_DOWN, GLFW.GLFW_HAT_DOWN);

		super.onInitializeInputActions(eventActionManager);
	}
}
