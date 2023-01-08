package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.renderers.trails.TrailBatchRenderer;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.batching.SpriteBatch;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.maths.Vector2f;
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

	private SpriteSheetDefinition mShipSpritesheet;

	private TrailBatchRenderer mTrailRenderer;

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

		mTrailRenderer = new TrailBatchRenderer();
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

		mShipSpritesheet = resourceManager.spriteSheetManager().getSpriteSheet("SPRITESHEET_PROPS", ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mTrailRenderer.loadResources(resourceManager);
	}

	@Override
	public void unloadResources() {
		super.unloadResources();

		mTrailRenderer.unloadResources();
	}

	@Override
	public void draw(LintfordCore core) {
		final var lShipManager = mShipController.shipManager();
		final var lPlayerShip = lShipManager.playerShip();

		drawShip(core, lPlayerShip);
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

		drawShipEngines(core, lSpritebatch, ship, ship.rearEngine);
		drawShipEngines(core, lSpritebatch, ship, ship.frontEngine);

		lSpritebatch.end();

		drawShipDebugVectors(core, ship);
		drawShipDebugInfo(core, ship);
	}

	// ---------------------------------------------

	private void drawShipComponents(LintfordCore core, Ship ship, SpriteBatch spriteBatch) {
		{// MainBody
			final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();
			final var lSpriteFrame = mShipSpritesheet.getSpriteFrame("HARVESTER");

			final var lScale = 1.f;
			final var lDestW = lSpriteFrame.width() * 2;
			final var lDestH = lSpriteFrame.height() * 2;

			final var lBody = ship.body();

			final var shipPosX = lBody.x * lUnitsToPixels;
			final var shipPosY = lBody.y * lUnitsToPixels;
			final var shipPosRot = lBody.angle;

			spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrame, shipPosX, shipPosY, lDestW, lDestH, shipPosRot, 0f, 0f, lScale, ColorConstants.WHITE);
		}
	}

	private void drawShipEngines(LintfordCore core, SpriteBatch spriteBatch, Ship ship, Vector2f enginePostion) {
		// Engine Glow
		final var lBody = ship.body();

		final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();

		final var shipPosX = enginePostion.x * lUnitsToPixels;
		final var shipPosY = enginePostion.y * lUnitsToPixels;
		final var shipRot = lBody.angle;

		final var r = ship.engineColorR * 2f;
		final var g = ship.engineColorG * 2f;
		final var b = ship.engineColorB * 2f;
		final var engineColor = ColorConstants.getColor(r, g, b, 0.75f);

		final float lPulse = 1.0f + (float) Math.cos(core.gameTime().totalTimeMilli()) * 2.f;

		final float lShipSpeed = (float) Math.abs(lBody.vx * lBody.vx + lBody.vy * lBody.vy) * 20.f;
		final float lSpeedSizeMod = 2.0f + lShipSpeed * .002f + lPulse;

		final var lSpriteFrameFlare = mShipSpritesheet.getSpriteFrame("TEXTUREENGINEGLOW");
		spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 + lSpeedSizeMod, 4 + lSpeedSizeMod, shipRot, 0, 0, -0.01f, engineColor);
		spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 + lSpeedSizeMod * .25f, 4 + lSpeedSizeMod * .25f, shipRot, 0, 0, -0.01f, ColorConstants.WHITE);

		// horizontal flare
		spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod, 2, 0, 0, 0, -0.01f, engineColor);
		spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod * .5f, 2, 0, 0, 0, -0.01f, ColorConstants.WHITE);

		final var lFlareAlpha = ColorConstants.getColor(r, g, b, .75f);
		spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod * 2.f, 2, shipRot + (float) core.gameTime().totalTimeSeconds() * 0.1f, 0, 0, -0.01f, lFlareAlpha);

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

			final var lFontUnit = rendererManager().uiTextFont();
			final var lBoundingBox = core.HUD().boundingRectangle();

			final var shipAX = String.format(java.util.Locale.US, "%.1f", lBody.accX);
			final var shipAY = String.format(java.util.Locale.US, "%.1f", lBody.accY);

			final var shipVX = String.format(java.util.Locale.US, "%.1f", lBody.vx);
			final var shipVY = String.format(java.util.Locale.US, "%.1f", lBody.vy);

			final var shipX = String.format(java.util.Locale.US, "%.1f", lBody.x);
			final var shipY = String.format(java.util.Locale.US, "%.1f", lBody.y);

			final var shipTorque = String.format(java.util.Locale.US, "%.1f", lBody.torque);
			final var shipAV = String.format(java.util.Locale.US, "%.1f", lBody.angularVelocity);
			final var shipR = String.format(java.util.Locale.US, "%.3f", lBody.angle);

			final var lFontScale = 1.0f;
			final var lLineHeight = 18.f;

			float yPos = lBoundingBox.top() + 5.f - 20.f;

			lFontUnit.begin(core.HUD());
			lFontUnit.drawText("force: " + shipAX + "," + shipAY, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.drawText("velocity: " + shipVX + "," + shipVY, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.drawText("position: " + shipX + "," + shipY, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);

			lFontUnit.drawText(" ", lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);

			lFontUnit.drawText("torque: " + shipTorque, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.drawText("angular: " + shipAV, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.drawText("angle: " + shipR, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);

			lFontUnit.drawText(" ", lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);

			lFontUnit.drawText("free space: " + ship.cargo.freeSpace, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.drawText("water: " + ship.cargo.waterAmt, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.drawText("wheat: " + ship.cargo.wheatAmt, lBoundingBox.left() + 5.f, yPos += lLineHeight, -0.01f, lFontScale);
			lFontUnit.end();
		}
	}
}
