package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.contrllers.ShipController;
import lintfordpickle.harvest.data.Ship;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.batching.SpriteBatch;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class ShipRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Ship Renderer";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ShipController mShipController;

	private Texture mShipTexturePlayer;
	private Texture mShipTextureEnemy;
	private Texture mShipEngineGlow;

	private SpriteSheetDefinition mShipSpritesheet;

	// private TrailBatchRenderer mTrailRenderer;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mShipController != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public ShipRenderer(RendererManager rendererManager, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);

		// mTrailRenderer = new TrailBatchRenderer();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		mShipController = (ShipController) core.controllerManager().getControllerByNameRequired(ShipController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		mShipTexturePlayer = resourceManager.textureManager().loadTexture("TEXTURE_VEHICLE_01", "res/textures/textureShip.png", entityGroupID());
		mShipTextureEnemy = resourceManager.textureManager().loadTexture("TEXTURE_VEHICLE_02", "res/textures/textureShip.png", entityGroupID());
		mShipEngineGlow = resourceManager.textureManager().loadTexture("TEXTURE_ENGINE_GLOW", "res/textures/textureTrail.png", entityGroupID());

		mShipSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_SHIPS", ConstantsGame.GAME_RESOURCE_GROUP_ID);

//		mTrailRenderer.loadResources(resourceManager);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

//		mTrailRenderer.unloadResources();
	}

	@Override
	public void draw(LintfordCore core) {
		final var lShipManager = mShipController.shipManager();
		final var lPlayerShip = lShipManager.playerShip();
		drawShip(core, lPlayerShip);

		final var lListOfOpponents = lShipManager.ships();
		final int lNumOfOpponents = lListOfOpponents.size();

		for (int i = 0; i < lNumOfOpponents; i++) {
			final var lOpponentShip = lListOfOpponents.get(i);
			drawShip(core, lOpponentShip);
		}

		// ---

//		mTrailRenderer.update(core);
//		mTrailRenderer.begin(core.gameCamera());
//
//		for (int i = 0; i < lNumOfOpponents; i++) {
//			final var lOpponentShip = lListOfOpponents.get(i);
//			final var lTrailComponent = lOpponentShip.mTrailRendererComponent;
//			mTrailRenderer.draw(mShipEngineGlow, lTrailComponent.vertices(), lTrailComponent.vertexCount());
//		}
//
//		mTrailRenderer.end();

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void drawShip(LintfordCore core, Ship ship) {
		if (ship == null)
			return;

		final var lSpritebatch = rendererManager().uiSpriteBatch();

		lSpritebatch.begin(core.gameCamera());

		drawShipComponents(core, ship, lSpritebatch);
		drawShipEngines(core, ship, lSpritebatch);

		lSpritebatch.end();

		drawShipDebugVectors(core, ship);
		drawShipDebugInfo(core, ship);
	}

	private void drawShipComponents(LintfordCore core, Ship ship, SpriteBatch spriteBatch) {
		{// MainBody
			final var lSourceX = 32.f * ship.tiltLevel;
			final var lSourceY = ship.tiltAmount > 0.f ? 32.f : 0.f;
			final var lSourceW = 32.f;
			final var lSourceH = ship.tiltAmount > 0.f ? -32.f : 32.f;

			final var lDestW = 32;
			final var lDestH = 32;

			final var lScale = 1.f;

			var lTexture = mShipTextureEnemy;
			if (ship.isPlayerControlled)
				lTexture = mShipTexturePlayer;

			final var lBody = ship.body();

			final var shipPosX = lBody.x;
			final var shipPosY = lBody.y;
			final var shipPosRot = lBody.angle;

			spriteBatch.drawAroundCenter(lTexture, lSourceX, lSourceY + 32, lSourceW, -lSourceH, shipPosX, shipPosY, lDestW, lDestH, -0.01f, shipPosRot, 0f, 0f, lScale, ColorConstants.WHITE);
		}
	}

	private void drawShipEngines(LintfordCore core, Ship ship, SpriteBatch spriteBatch) {
		// Engine Glow
		final var lBody = ship.body();

		final var shipPosX = lBody.x;
		final var shipPosY = lBody.y;
		final var shipRot = lBody.angle;

		final var c = (float) Math.cos(shipRot);
		final var s = (float) Math.sin(shipRot);

		final var engineOffsetX = -15;
		final var engineOffsetY = 0;

		final var xx = engineOffsetX;
		final var yy = engineOffsetY;

		final var engineX = xx * c - yy * s;
		final var engineY = yy * c + xx * s;

		// ----

//		final var r = ship.mEngineColorR * 2f;
//		final var g = ship.mEngineColorG * 2f;
//		final var b = ship.mEngineColorB * 2f;
//		final var engineColor = ColorConstants.getColor(r, g, b, 0.75f);
//
//		final float lPulse = 1.0f + (float) Math.cos(core.gameTime().totalTimeMilli()) * 2.f;
//
//		final float lShipSpeed = ship.speed;
//		final float lSpeedSizeMod = 1.0f + lShipSpeed * 0.02f + lPulse;
//
//		spriteBatch.drawAroundCenter(mShipSpritesheet, mShipSpritesheet.getSpriteFrame(0), shipPosX + engineX, shipPosY + engineY, 4 + lSpeedSizeMod, 4 + lSpeedSizeMod, shipRot, 0, 0, -0.01f, engineColor);
//		spriteBatch.drawAroundCenter(mShipSpritesheet, mShipSpritesheet.getSpriteFrame(0), shipPosX + engineX, shipPosY + engineY, 4 + lSpeedSizeMod * .25f, 4 + lSpeedSizeMod * .25f, shipRot, 0, 0, -0.01f, ColorConstants.WHITE);
//
//		// horizontal flare
//		spriteBatch.drawAroundCenter(mShipSpritesheet, mShipSpritesheet.getSpriteFrame(0), shipPosX + engineX, shipPosY + engineY, 4 * lSpeedSizeMod, 2, 0, 0, 0, -0.01f, engineColor);
//		spriteBatch.drawAroundCenter(mShipSpritesheet, mShipSpritesheet.getSpriteFrame(0), shipPosX + engineX, shipPosY + engineY, 4 * lSpeedSizeMod * .5f, 2, 0, 0, 0, -0.01f, ColorConstants.WHITE);
//
//		final var lFlareAlpha = ColorConstants.getColor(r, g, b, .25f);
//		spriteBatch.drawAroundCenter(mShipSpritesheet, mShipSpritesheet.getSpriteFrame(0), shipPosX + engineX, shipPosY + engineY, 4 * lSpeedSizeMod * 2.f, 2, shipRot + (float) core.gameTime().totalTimeSeconds() * 0.1f, 0, 0, -0.01f, lFlareAlpha);

	}

	@SuppressWarnings("unused")
	private void drawShipDebugVectors(LintfordCore core, Ship ship) {
		// DEBUG DRAWERS
//		final var lBody = ship.body();
//
//		final var shipPosX = lBody.x;
//		final var shipPosY = lBody.y;
//		final var shipRad = lBody.radius;
//		final var shipRot = lBody.angle;
//
//		{
//			final var lShipVelocityPosX = shipPosX + MathHelper.clamp(lBody.vx * 25.f, 0.1f, 15.f);
//			final var lShipVelocityPosY = shipPosY + MathHelper.clamp(lBody.vy * 25.f, 0.1f, 15.f);
//
//			Debug.debugManager().drawers().drawLineImmediate(core.gameCamera(), shipPosX, shipPosY, lShipVelocityPosX, lShipVelocityPosY, -0.01f, 3.0f, 2.4f, 0.8f);
//		}
//
//		final var lShipHeadingPosX = shipPosX + (float) Math.cos(shipRot) * 20.f;
//		final var lShipHeadingPosY = shipPosY + (float) Math.sin(shipRot) * 20.f;
//
//		Debug.debugManager().drawers().drawLineImmediate(core.gameCamera(), shipPosX, shipPosY, lShipHeadingPosX, lShipHeadingPosY, -0.01f, 1.0f, 0.4f, 0.8f);
//
//		final var lShipSteering = shipRot + ship.steeringAngle;
//		final var lShipSteeringPosX = shipPosX + (float) Math.cos(lShipSteering) * 15.f;
//		final var lShipSteeringPosY = shipPosY + (float) Math.sin(lShipSteering) * 15.f;
//
//		Debug.debugManager().drawers().drawLineImmediate(core.gameCamera(), shipPosX, shipPosY, lShipSteeringPosX, lShipSteeringPosY, -0.01f, 0.4f, 0.9f, 0.8f);
//
//		// Draw ship radius
//		Debug.debugManager().drawers().drawCircleImmediate(core.gameCamera(), shipPosX, shipPosY, shipRad, 32, GL11.GL_LINE_STRIP, 1.f);
//
//		// Draw altitude marker
//		{
//
//			final float topOffset = 30.f;
//			final var bb = core.HUD().boundingRectangle();
//			Debug.debugManager().drawers().drawLineImmediate(core.HUD(), bb.centerX() - 10, bb.top() + topOffset, bb.centerX() + 10, bb.top() + topOffset, -0.01f, 0.9f, 0.9f, 0.2f);
//
//			final float lHeightOffset = ship.altitude * 2.f;
//			Debug.debugManager().drawers().drawLineImmediate(core.HUD(), bb.centerX() - 10, bb.top() + topOffset - lHeightOffset, bb.centerX() + 10, bb.top() + topOffset - lHeightOffset, -0.01f, 0.9f, 0.2f, 0.2f);
//		}
	}

	@SuppressWarnings("unused")
	private void drawShipDebugInfo(LintfordCore core, Ship ship) {
		if (ship.isPlayerControlled) {
			final var lBody = ship.body();

			final var shipPosX = lBody.x;
			final var shipPosY = lBody.y;

			final var lFontUnit = rendererManager().uiTextFont();
			final var lBoundingBox = core.HUD().boundingRectangle();

			final var shipX = String.format(java.util.Locale.US, "%.1f", shipPosX);
			final var shipY = String.format(java.util.Locale.US, "%.1f", shipPosY);

			final var lFontScale = 1.0f;
			final var lLineHeight = 18.f;

			float yPos = lBoundingBox.top() + 5.f - 20.f;

			lFontUnit.begin(core.HUD());
			lFontUnit.drawText("position: " + shipX + "," + shipY, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.end();
		}
	}
}
