package lintfordpickle.harvest.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.ConstantsGame;
import net.lintford.library.GameVersion;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.geometry.Rectangle;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.SpriteInstance;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.core.maths.RandomNumbers;
import net.lintford.library.core.maths.Vector2f;
import net.lintford.library.core.maths.Vector3f;
import net.lintford.library.screenmanager.Screen;
import net.lintford.library.screenmanager.ScreenManager;

public class MenuBackgroundScreen extends Screen {

	public List<String> lowerGreen = Arrays.asList("TEXTURE_LOWER_GREEN_00");
	public List<String> upperRed = Arrays.asList("TEXTURE_UPPER_RED_00", "TEXTURE_UPPER_RED_01");
	public List<String> lowerRed = Arrays.asList("TEXTURE_LOWER_RED_00");

	public static final int SCREEN_WIDTH = 960;
	public static final int SCREEN_HEIGHT = 540;

	final int maxNumVehilcesPerStream = 11;

	public class StreamPoint {
		public float screenPositionX;
		public float screenPositionY;

		public float absPositionX;
		public float absPositionY;

		public StreamPoint otherPoint;

		public float scale = 1.f;
		public float zDepth = -.001f;
	}

	public class VehicleStream {
		public final StreamPoint startPoint = new StreamPoint();
		public final StreamPoint endPoint = new StreamPoint();

		public final List<Vehicle> vehicles = new ArrayList<>();

		public void init(float sx, float sy, float ex, float ey, float screenSizeW, float screenSizeH) {
			float offsetX = (screenSizeW / 2.f);
			float offsetY = (screenSizeH / 2.f);

			startPoint.absPositionX = sx;
			startPoint.absPositionY = sy;
			startPoint.screenPositionX = -offsetX + sx;
			startPoint.screenPositionY = -offsetY + sy;

			endPoint.absPositionX = ex;
			endPoint.absPositionY = ey;
			endPoint.screenPositionX = -offsetX + ex;
			endPoint.screenPositionY = -offsetY + ey;
		}

		public void update(LintfordCore core) {
			final int lNumVehicles = vehicles.size();
			for (int i = 0; i < lNumVehicles; i++) {
				final var v = vehicles.get(i);
				v.t += core.gameTime().elapsedTimeMilli() * .0002f * v.s;

				if (v.t > 1.f)
					v.t = 0.f;

			}
		}
	}

	public class Vehicle {
		public float t;
		public float s;
		public float oy;
		public String n;
		public float r, g, b;

		public Vehicle(float t, float oy, String spritesheetName, Vector3f colorMod) {
			this.t = t;
			this.oy = oy;
			this.n = spritesheetName;
			r = colorMod.x;
			g = colorMod.y;
			b = colorMod.z;
		}
	}

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private VehicleStream mUpperRed;
	private VehicleStream mLowerRed;

	private VehicleStream mUpperGreen;
	private VehicleStream mLowerGreen;

	private Texture mBackgroundTextureSky;
	private Texture mBackgroundTexture;
	private Texture mForegroundLeftTexture;
	private Texture mForegroundRightTexture;

	private final List<Vector2f> points = new ArrayList<>(); // DEBUG
	private final Rectangle srcRect = new Rectangle();// DEBUG
	private final Vector3f tempColor = new Vector3f();

	private SpriteSheetDefinition mPropsSpritesheet;
	private SpriteSheetDefinition mAdWallSpritesheet;

	private SpriteInstance mAdWallSoup;
	private SpriteInstance mAirCondAnim;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public MenuBackgroundScreen(ScreenManager screenManager) {
		super(screenManager);

		mScreenManager.core().createNewGameCamera();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		mUpperRed = new VehicleStream();
		mUpperRed.init(959.f, 78.f, -50.f, 267.f, SCREEN_WIDTH, SCREEN_HEIGHT);

		mUpperRed.startPoint.zDepth = -.1f;
		mUpperRed.endPoint.zDepth = -.4f;

		mUpperRed.startPoint.scale = 1.5f;
		mUpperRed.endPoint.scale = .5f;

		mLowerRed = new VehicleStream();
		mLowerRed.init(-50.f, 327.f, 960.f, 573.f, SCREEN_WIDTH, SCREEN_HEIGHT);

		mLowerRed.startPoint.zDepth = -.5f;
		mLowerRed.endPoint.zDepth = -.1f;

		mLowerRed.startPoint.scale = 0.5f;
		mLowerRed.endPoint.scale = 1.5f;

		mUpperGreen = new VehicleStream();
		mUpperGreen.init(-50, 200, 634, 275, SCREEN_WIDTH, SCREEN_HEIGHT);

		mUpperGreen.startPoint.zDepth = -0.1f;
		mUpperGreen.endPoint.zDepth = -0.5f;

		mLowerGreen = new VehicleStream();
		mLowerGreen.init(634.f, 338.f, -50.f, 342.f, SCREEN_WIDTH, SCREEN_HEIGHT);

		mLowerGreen.startPoint.zDepth = -.5f;
		mLowerGreen.endPoint.zDepth = -.1f;

		final float stepSize = 1.f / (float) maxNumVehilcesPerStream;
		for (int i = 0; i < maxNumVehilcesPerStream; i++) {
			float t = (float) i;
			if (RandomNumbers.getRandomChance(16.f))
				mUpperRed.vehicles.add(new Vehicle(t * stepSize, RandomNumbers.random(-5, 5), getRandomVehicleName(upperRed), getRandomVehicleColor()));

			if (RandomNumbers.getRandomChance(16.f))
				mLowerRed.vehicles.add(new Vehicle(t * stepSize, RandomNumbers.random(-5, 5), getRandomVehicleName(lowerRed), getRandomVehicleColor()));

			if (RandomNumbers.getRandomChance(16.f))
				mLowerGreen.vehicles.add(new Vehicle(t * stepSize, RandomNumbers.random(-5, 5), getRandomVehicleName(lowerGreen), getRandomVehicleColor()));
		}

		points.add(new Vector2f(-100.f, 75.f));
		points.add(new Vector2f(-100.f, -20.f));
		points.add(new Vector2f(0.f, -28.f));
		points.add(new Vector2f(0.f, 85.f));

	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mBackgroundTextureSky = resourceManager.textureManager().loadTexture("TEXTURE_MENU_BACKGROUND_SKY", "res/textures/textureMenuBackgroundSky.png", entityGroupUid());
		mBackgroundTexture = resourceManager.textureManager().loadTexture("TEXTURE_MENU_BACKGROUND", "res/textures/textureMenuBackground.png", entityGroupUid());
		mForegroundLeftTexture = resourceManager.textureManager().loadTexture("TEXTURE_MENU_FOREGROUND_LEFT", "res/textures/textureMenuForegroundLeft.png", entityGroupUid());
		mForegroundRightTexture = resourceManager.textureManager().loadTexture("TEXTURE_MENU_FOREGROUND_RIGHT", "res/textures/textureMenuForegroundRight.png", entityGroupUid());

		mPropsSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_PROPS", ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mAdWallSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_ADWALLSOUP", ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mAdWallSoup = mAdWallSpritesheet.getSpriteInstance("play");
		mAirCondAnim = mPropsSpritesheet.getSpriteInstance("aircond");
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		mUpperRed.update(core);
		mLowerRed.update(core);
		mUpperGreen.update(core);
		mLowerGreen.update(core);

	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		final var lCanvasBox = core.gameCamera().boundingRectangle();
		final var lTextureBatch = rendererManager().uiSpriteBatch();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		core.gameCamera().update(core);
		core.config().display().reapplyGlViewport();

		lTextureBatch.begin(core.gameCamera());
		lTextureBatch.draw(mBackgroundTextureSky, 0, 0, 960, 540, lCanvasBox.left(), lCanvasBox.top(), lCanvasBox.width(), lCanvasBox.height(), -0.9f, ColorConstants.WHITE);
		lTextureBatch.draw(mBackgroundTexture, 0, 0, 960, 540, lCanvasBox.left(), lCanvasBox.top(), lCanvasBox.width(), lCanvasBox.height(), -0.85f, ColorConstants.WHITE);
		lTextureBatch.end();

		drawVehicleStream(core, true, false, mUpperRed);
		drawVehicleStream(core, true, false, mLowerRed);
		drawVehicleStream(core, false, false, mUpperGreen);
		drawVehicleStream(core, false, false, mLowerGreen);

		lTextureBatch.begin(core.gameCamera());

		final var lRightWidth = mForegroundRightTexture.getTextureWidth();
		final var lRightHeight = mForegroundRightTexture.getTextureHeight();
		final var lLeftWidth = mForegroundLeftTexture.getTextureWidth();
		final var lLeftHeight = mForegroundLeftTexture.getTextureHeight();

		lTextureBatch.draw(mForegroundRightTexture, 0, 0, lRightWidth, lRightHeight, lCanvasBox.right() - lRightWidth, lCanvasBox.top(), lRightWidth, lRightHeight, -0.75f, ColorConstants.WHITE);
		lTextureBatch.draw(mForegroundLeftTexture, 0, 0, lLeftWidth, lLeftHeight, lCanvasBox.left(), lCanvasBox.top(), lLeftWidth, lLeftHeight, -0.75f, ColorConstants.WHITE);
		lTextureBatch.end();

		drawWallAd(core);

		drawVehicleStream(core, false, true, mUpperRed);
		drawVehicleStream(core, false, true, mLowerRed);
		drawVehicleStream(core, true, true, mUpperGreen);
		drawVehicleStream(core, true, true, mLowerGreen);

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		drawVersion(core);
	}

	private void drawVersion(LintfordCore core) {
		final var lHudBounds = core.HUD().boundingRectangle();
		final var lVersionFont = mRendererManager.uiTextFont();

		final var lVersionText = GameVersion.GAME_VERSION;

		lVersionFont.begin(core.HUD());
		lVersionFont.drawText(lVersionText, lHudBounds.left() + 5.f, lHudBounds.bottom() - lVersionFont.fontHeight(), -0.01f, 1.f);
		lVersionFont.end();
	}

	// ---

	public void drawWallAd(LintfordCore core) {
		final var lSpriteBatch = rendererManager().uiSpriteBatch();

		srcRect.x(0);
		srcRect.y(0);
		srcRect.width(128);
		srcRect.height(128);

		lSpriteBatch.begin(core.gameCamera());
		mAdWallSoup.update(core);
		mAirCondAnim.update(core);
		mAirCondAnim.setPosition(-297, 167);
		lSpriteBatch.drawQuadrilateral(mAdWallSpritesheet, mAdWallSoup, points, -.8f, ColorConstants.WHITE);
		lSpriteBatch.draw(mPropsSpritesheet, mAirCondAnim, -0.01f, ColorConstants.WHITE);
		lSpriteBatch.end();

	}

	public void drawVehicleStream(LintfordCore core, boolean drawLeftSide, boolean zHigh, VehicleStream vs) {
		final var lSpriteBatch = rendererManager().uiSpriteBatch();

		final var sx = vs.startPoint.screenPositionX;
		final var sy = vs.startPoint.screenPositionY;
		final var ss = vs.startPoint.scale;
		final var ex = vs.endPoint.screenPositionX;
		final var ey = vs.endPoint.screenPositionY;
		final var es = vs.endPoint.scale;

		lSpriteBatch.begin(core.gameCamera());
		final int lNumVehicles = vs.vehicles.size();
		for (int i = 0; i < lNumVehicles; i++) {
			final var v = vs.vehicles.get(i);

			final var posX = sx + ((ex - sx) * v.t);

			if (drawLeftSide && posX > 0.f)
				continue;

			if (!drawLeftSide && posX < 0.f)
				continue;

			final var posY = sy + ((ey - sy) * v.t);
			v.s = ss + ((es - ss) * v.t);

			final var lSpriteFrame = mPropsSpritesheet.getSpriteFrame(v.n);
			final var lColorMod = ColorConstants.getColor(v.r, v.g, v.b);
			lSpriteBatch.draw(mPropsSpritesheet, lSpriteFrame, posX, posY + v.oy, lSpriteFrame.width() * v.s, lSpriteFrame.height() * v.s, zHigh ? -0.65f : -0.85f, lColorMod);

			final var lSpriteGlassFrame = mPropsSpritesheet.getSpriteFrame(v.n + "_GLASS");
			lSpriteBatch.draw(mPropsSpritesheet, lSpriteGlassFrame, posX, posY + v.oy, lSpriteGlassFrame.width() * v.s, lSpriteGlassFrame.height() * v.s, zHigh ? -0.65f : -0.85f, ColorConstants.WHITE);

		}

		lSpriteBatch.end();
	}

	private String getRandomVehicleName(List<String> names) {
		return names.get(RandomNumbers.random(0, names.size()));
	}

	private Vector3f getRandomVehicleColor() {
		final var lMinBrightness = .1f;
		final var lMaxBrightness = .6f;
		tempColor.x = RandomNumbers.random(lMinBrightness, lMaxBrightness);
		tempColor.y = RandomNumbers.random(lMinBrightness, lMaxBrightness);
		tempColor.z = RandomNumbers.random(lMinBrightness, lMaxBrightness);
		return tempColor;
	}

}
