package lintfordpickle.harvest.renderers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.ShipController;
import lintfordpickle.harvest.data.ships.Ship;
import lintfordpickle.harvest.data.ships.Ship.Cargo;
import lintfordpickle.harvest.renderers.trails.TrailBatchRenderer;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.batching.SpriteBatch;
import net.lintford.library.core.graphics.sprites.SpriteFrame;
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
		final var lShips = lShipManager.ships();
		final var lNumShips = lShips.size();
		for (int i = 0; i < lNumShips; i++) {
			final var lShip = lShips.get(i);

			drawShip(core, lShip);
		}

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void drawShip(LintfordCore core, Ship ship) {
		if (ship == null)
			return;

		final var lSpritebatch = rendererManager().uiSpriteBatch();

		lSpritebatch.begin(core.gameCamera());

		drawShipEngines(core, lSpritebatch, ship, ship.rearEngine, true);
		drawShipEngines(core, lSpritebatch, ship, ship.frontEngine, false);
		drawShipCargo(core, lSpritebatch, ship);

		drawShipComponents(core, ship, lSpritebatch);

		lSpritebatch.end();

		if (ConstantsGame.SHIP_DEBUG_MODE)
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

			var lShipColor = ColorConstants.WHITE;
			if (ship.isGhostShip)
				lShipColor = ColorConstants.getBlackWithAlpha(0.3f);

			spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrame, shipPosX, shipPosY, lDestW, lDestH, shipPosRot, 0f, 0f, lScale, lShipColor);
		}
	}

	private void drawShipEngines(LintfordCore core, SpriteBatch spriteBatch, Ship ship, Vector2f enginePostion, boolean leftEngine) {
		// Engine Glow
		final var lBody = ship.body();

		if (leftEngine) {
			if (ship.leftEngineSprite == null) {
				ship.leftEngineSprite = mShipSpritesheet.getSpriteInstance("engineLeft");
			}
		} else {
			if (ship.rightEngineSprite == null) {
				ship.rightEngineSprite = mShipSpritesheet.getSpriteInstance("engineRight");
			}
		}

		final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();

		final var shipPosX = enginePostion.x * lUnitsToPixels;
		final var shipPosY = enginePostion.y * lUnitsToPixels;
		final var shipRot = lBody.angle;

		if (ship.leftEngineSprite != null) {
			ship.leftEngineSprite.update(core);

			var lShipColor = ColorConstants.getWhiteWithAlpha(0.8f);
			if (ship.isGhostShip)
				lShipColor = ColorConstants.getBlackWithAlpha(0f);

			spriteBatch.drawAroundCenter(mShipSpritesheet, ship.leftEngineSprite.currentSpriteFrame(), shipPosX, shipPosY, 16.f * 2, 16.f * 2, shipRot, 0, 0, -0.01f, lShipColor);
		}

		final var r = ship.engineColorR * 2f;
		final var g = ship.engineColorG * 2f;
		final var b = ship.engineColorB * 2f;
		final var engineColor = ColorConstants.getColor(r, g, b, 0.75f);

		final float lPulse = 1.0f + (float) Math.cos(core.gameTime().totalTimeMilli()) * 2.f;

		final float lShipSpeed = (float) Math.abs(lBody.vx * lBody.vx + lBody.vy * lBody.vy) * 20.f;
		final float lSpeedSizeMod = 2.0f + lShipSpeed * .02f + lPulse;

		final var lSpriteFrameFlare = mShipSpritesheet.getSpriteFrame("TEXTUREENGINEGLOW");

		var lShipColor = ColorConstants.WHITE;
		if (ship.isGhostShip)
			lShipColor = ColorConstants.getBlackWithAlpha(0.3f);

		if (leftEngine) {
			if (ship.inputs.isLeftThrottle || ship.inputs.isUpThrottle) {
				spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod, 2, 0, 0, 0, -0.01f, engineColor);
				spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod * .5f, 2, 0, 0, 0, -0.01f, lShipColor);
			}
		} else {
			if (ship.inputs.isRightThrottle || ship.inputs.isUpThrottle) {
				spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod, 2, 0, 0, 0, -0.01f, engineColor);
				spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX, shipPosY, 4 * lSpeedSizeMod * .5f, 2, 0, 0, 0, -0.01f, lShipColor);
			}
		}
	}

	private void drawShipCargo(LintfordCore core, SpriteBatch spriteBatch, Ship ship) {
		final var lBody = ship.body();

		final var lUnitsToPixels = ConstantsPhysics.UnitsToPixels();

		float xx = 12;
		float yy = -3;

		final int lTotalNumberCargo = Cargo.TOTAL_CARGO_SPACE - ship.cargo.freeSpace;
		for (int i = 0; i < lTotalNumberCargo; i++) {

			final SpriteFrame lSpriteFrameFlare;
			if (i < ship.cargo.waterAmt) {
				lSpriteFrameFlare = mShipSpritesheet.getSpriteFrame("TEXTURECARGOWATER");
			} else {
				lSpriteFrameFlare = mShipSpritesheet.getSpriteFrame("TEXTURECARGOWHEAT");
			}

			final var shipPosX = lBody.x * lUnitsToPixels;
			final var shipPosY = lBody.y * lUnitsToPixels;
			final var shipRot = lBody.angle;

			float lLocalHalfX = xx - i * 11;
			float lLocalHalfY = yy;

			final float c = (float) Math.cos(shipRot);
			final float s = (float) Math.sin(shipRot);

			final float cargoX = -lLocalHalfX * c - lLocalHalfY * s;
			final float cargoY = -lLocalHalfX * s + lLocalHalfY * c;
			final float cargoW = lSpriteFrameFlare.width() * 2;
			final float cargoH = lSpriteFrameFlare.height() * 2;

			spriteBatch.drawAroundCenter(mShipSpritesheet, lSpriteFrameFlare, shipPosX + cargoX, shipPosY + cargoY, cargoW, cargoH, shipRot, 0, 0, -0.01f, ColorConstants.WHITE);
		}
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
