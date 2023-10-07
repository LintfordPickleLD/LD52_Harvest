package lintfordpickle.harvest;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.data.actionevents.SatActionEventMap;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.players.ReplayManager;
import lintfordpickle.harvest.screens.MainMenu;
import lintfordpickle.harvest.screens.MenuBackgroundScreen;
import lintfordpickle.harvest.screens.game.TimeTrialGameScreen;
import net.lintfordlib.GameInfo;
import net.lintfordlib.ResourceLoader;
import net.lintfordlib.controllers.music.MusicController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.graphics.fonts.BitmapFontManager;
import net.lintfordlib.core.input.KeyEventActionManager;
import net.lintfordlib.core.maths.RandomNumbers;
import net.lintfordlib.renderers.RendererManager;
import net.lintfordlib.screenmanager.IMenuAction;
import net.lintfordlib.screenmanager.Screen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.TimedIntroScreen;
import net.lintfordlib.screenmanager.toast.ToastManager;

public abstract class HarvestGame extends LintfordCore {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	protected int mEntityGroupID;

	protected ResourceLoader mGameResourceLoader;
	protected ScreenManager mScreenManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public ScreenManager screenManager() {
		return mScreenManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public HarvestGame(GameInfo pGameInfo, String[] pArgs) {
		super(pGameInfo, pArgs, false);

		mEntityGroupID = RandomNumbers.RANDOM.nextInt();
		mIsFixedTimeStep = true;

		mScreenManager = new ScreenManager(this);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	protected void showStartUpLogo(long pWindowHandle) {
		glClearColor(0f, 0f, 0f, 1f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		glfwSwapBuffers(pWindowHandle);
	}

	@Override
	protected void onInitializeBitmapFontSources(BitmapFontManager fontManager) {
		super.onInitializeBitmapFontSources(fontManager);

		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_TOOLTIP_NAME, "res/fonts/fontNulshock16.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_ENTRY_NAME, "res/fonts/fontNulshock16.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_BOLD_ENTRY_NAME, "res/fonts/fontNulshock16.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_TITLE_NAME, "res/fonts/fontNulshock22.json");

		ScreenManager.ScreenManagerFonts.AddOrUpdate(ToastManager.FONT_TOAST_NAME, "res/fonts/fontNulshock16.json");

		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.HUD_FONT_TEXT_BOLD_SMALL_NAME, "res/fonts/fontNulshock16.json");

		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_NAME, "res/fonts/fontNulshock16.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_BOLD_NAME, "res/fonts/fontNulshock16.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_HEADER_NAME, "res/fonts/fontNulshock16.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TITLE_NAME, "res/fonts/fontNulshock22.json");
	}

	@Override
	protected void onInitializeApp() {
		super.onInitializeApp();

		final var lBestReplayManager = new ReplayManager();
		final var lReplayController = new ReplayController(mControllerManager, lBestReplayManager, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		lReplayController.initialize(this);

		if (ConstantsGame.QUICK_LAUNCH_GAME) {
			final var lPlayerManager = new PlayerManager();
			final var lGhostPlayer = lPlayerManager.addNewPlayer();
			lGhostPlayer.setPlayback("ghost.lms");
			mScreenManager.addScreen(new TimeTrialGameScreen(screenManager(), lPlayerManager));
		}

		final var lSplashScreen = new TimedIntroScreen(mScreenManager, "res/textures/textureHud.png", 4f);
		lSplashScreen.stretchBackgroundToFit(true);

		lSplashScreen.setTimerFinishedCallback(new IMenuAction() {
			@Override
			public void TimerFinished(Screen pScreen) {
				mScreenManager.addScreen(new MenuBackgroundScreen(mScreenManager));
				mScreenManager.addScreen(new MainMenu(mScreenManager));
			}
		});

		mScreenManager.addScreen(lSplashScreen);

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

	@Override
	protected void onLoadResources() {
		super.onLoadResources();

		mGameResourceLoader = new HarvestResourceLoader(mResourceManager, config().display());

		mGameResourceLoader.loadResources(mResourceManager);
		mGameResourceLoader.setMinimumTimeToShowLogosMs(ConstantsGame.IS_DEBUG_MODE ? 0 : 2000);
		mGameResourceLoader.loadResourcesInBackground(this);

		mResourceManager.audioManager().loadAudioFilesFromMetafile("res/audio/_meta.json");
		mResourceManager.musicManager().loadMusicFromMetaFile("res/music/meta.json");

		var lMusic = new MusicController(mControllerManager, mResourceManager.musicManager(), LintfordCore.CORE_ENTITY_GROUP_ID);
		lMusic.playFromGroup(0, "game");

		mScreenManager.loadResources(mResourceManager);
	}

	@Override
	protected void onUnloadResources() {
		super.onUnloadResources();

		mScreenManager.unloadResources();
	}

	@Override
	protected void onHandleInput() {
		super.onHandleInput();

		gameCamera().handleInput(this);
		mScreenManager.handleInput(this);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();

		mScreenManager.update(this);
	}

	@Override
	protected void onDraw() {
		super.onDraw();

		mScreenManager.draw(this);
	}

}
