package lintfordpickle.harvest;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.opengl.GL11;

import net.lintford.library.GameInfo;
import net.lintford.library.GameResourceLoader;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.camera.Camera;
import net.lintford.library.core.graphics.fonts.BitmapFontManager;
import net.lintford.library.core.maths.RandomNumbers;
import net.lintford.library.renderers.RendererManager;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.toast.ToastManager;

public abstract class BaseHarvestApp extends LintfordCore {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	protected int mEntityGroupID;

	protected GameResourceLoader mGameResourceLoader;
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

	public BaseHarvestApp(GameInfo pGameInfo, String[] pArgs) {
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

		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_TOOLTIP_NAME, "res/fonts/fontNulshock12.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_ENTRY_NAME, "res/fonts/fontNulshock12.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_BOLD_ENTRY_NAME, "res/fonts/fontNulshock12.json");
		ScreenManager.ScreenManagerFonts.AddOrUpdate(ScreenManager.FONT_MENU_TITLE_NAME, "res/fonts/fontNulshock22.json");

		ScreenManager.ScreenManagerFonts.AddOrUpdate(ToastManager.FONT_TOAST_NAME, "res/fonts/fontNulshock12.json");

		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.HUD_FONT_TEXT_BOLD_SMALL_NAME, "res/fonts/fontNulshock12.json");

		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_NAME, "res/fonts/fontNulshock12.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_BOLD_NAME, "res/fonts/fontNulshock12.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_HEADER_NAME, "res/fonts/fontNulshock12.json");
		RendererManager.RendererManagerFonts.AddOrUpdate(RendererManager.UI_FONT_TITLE_NAME, "res/fonts/fontNulshock22.json");
	}

	@Override
	protected void onLoadResources() {
		super.onLoadResources();

		mGameResourceLoader = new ResourceLoader(mResourceManager, config().display());

		mGameResourceLoader.loadResources(mResourceManager);
		mGameResourceLoader.setMinimumTimeToShowLogosMs(ConstantsGame.IS_DEBUG_MODE ? 0 : 2000);
		mGameResourceLoader.loadResourcesInBackground(this);

		mGameCamera = new Camera(mMasterConfig.display());
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
