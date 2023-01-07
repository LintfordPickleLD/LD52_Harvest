package lintfordpickle.harvest.renderers;

import org.lwjgl.opengl.GL11;

import lintfordpickle.harvest.data.ships.Ship;
import net.lintford.library.ConstantsPhysics;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.collisions.PhysicsWorld;
import net.lintford.library.core.collisions.RigidBody;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class PhysicsDebugRenderer extends BaseRenderer {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String RENDERER_NAME = "Physics World Debug Renderer";

	public static final boolean ScaleToScreenCoords = true;
	public static final boolean RenderAABB = false;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private PhysicsWorld mWorld;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mWorld != null;

	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public PhysicsDebugRenderer(RendererManager rendererManager, PhysicsWorld world, int entityGroupID) {
		super(rendererManager, RENDERER_NAME, entityGroupID);

		mWorld = world;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {

	}

	@Override
	public void draw(LintfordCore core) {
		final var lLineBatch = rendererManager().uiLineBatch();

		final var lRigidBodies = mWorld.bodies();
		final int lNumOfBodies = lRigidBodies.size();

		lLineBatch.begin(core.gameCamera());
		for (int i = 0; i < lNumOfBodies; i++) {
			final var lBody = lRigidBodies.get(i);

			debugDrawRigidBody(core, lBody);
		}
		lLineBatch.end();
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void debugDrawRigidBody(LintfordCore core, RigidBody body) {
		final var lLineBatch = rendererManager().uiLineBatch();
		lLineBatch.lineWidth(2.f);
		lLineBatch.lineType(GL11.GL_LINE_STRIP);

		float r = 0.6f;
		float g = 0.7f;
		float b = body.isStatic() ? 1.f : 0.2f;

		final var lUnitToPixels = ConstantsPhysics.UnitsToPixels();

		final var lVertices = body.getTransformedVertices();

		switch (body.shapeType()) {
		case Polygon:
		case Box: {
			if (!ScaleToScreenCoords) {
				lLineBatch.begin(core.gameCamera());
				lLineBatch.draw(lVertices.get(0).x, lVertices.get(0).y, lVertices.get(1).x, lVertices.get(1).y, -0.01f, r, g, b, 1.f);
				lLineBatch.draw(lVertices.get(1).x, lVertices.get(1).y, lVertices.get(2).x, lVertices.get(2).y, -0.01f, r, g, b, 1.f);
				lLineBatch.draw(lVertices.get(2).x, lVertices.get(2).y, lVertices.get(3).x, lVertices.get(3).y, -0.01f, r, g, b, 1.f);
				lLineBatch.draw(lVertices.get(3).x, lVertices.get(3).y, lVertices.get(0).x, lVertices.get(0).y, -0.01f, r, g, b, 1.f);
				lLineBatch.end();

			} else {
				lLineBatch.begin(core.gameCamera());
				lLineBatch.draw(lVertices.get(0).x * lUnitToPixels, lVertices.get(0).y * lUnitToPixels, lVertices.get(1).x * lUnitToPixels, lVertices.get(1).y * lUnitToPixels, -0.01f, r, g, b, 1.f);
				lLineBatch.draw(lVertices.get(1).x * lUnitToPixels, lVertices.get(1).y * lUnitToPixels, lVertices.get(2).x * lUnitToPixels, lVertices.get(2).y * lUnitToPixels, -0.01f, r, g, b, 1.f);
				lLineBatch.draw(lVertices.get(2).x * lUnitToPixels, lVertices.get(2).y * lUnitToPixels, lVertices.get(3).x * lUnitToPixels, lVertices.get(3).y * lUnitToPixels, -0.01f, r, g, b, 1.f);
				lLineBatch.draw(lVertices.get(3).x * lUnitToPixels, lVertices.get(3).y * lUnitToPixels, lVertices.get(0).x * lUnitToPixels, lVertices.get(0).y * lUnitToPixels, -0.01f, r, g, b, 1.f);
				lLineBatch.end();
			}

			break;
		}

		case Line: {
			lLineBatch.begin(core.gameCamera());
			lLineBatch.draw(lVertices.get(0).x * lUnitToPixels, lVertices.get(0).y * lUnitToPixels, lVertices.get(1).x * lUnitToPixels, lVertices.get(1).y * lUnitToPixels, -0.01f, r, g, b, 1.f);
			lLineBatch.draw(lVertices.get(1).x * lUnitToPixels, lVertices.get(1).y * lUnitToPixels, lVertices.get(2).x * lUnitToPixels, lVertices.get(2).y * lUnitToPixels, -0.01f, r, g, b, 1.f);
			lLineBatch.end();
			break;
		}

		case Circle: {

			if (!ScaleToScreenCoords)
				Debug.debugManager().drawers().drawCircleImmediate(core.gameCamera(), body.x, body.y, body.radius);

			else {
				lLineBatch.begin(core.gameCamera());
				lLineBatch.drawCircle(lVertices.get(0).x * lUnitToPixels, lVertices.get(0).y * lUnitToPixels, body.angle, body.radius * lUnitToPixels, 20, 1.f, 1.f, 1.f, true);
				lLineBatch.end();
			}

			break;
		}

		}

		if (RenderAABB)
			Debug.debugManager().drawers().drawRectImmediate(core.gameCamera(), body.aabb().x() * lUnitToPixels, body.aabb().y() * lUnitToPixels, body.aabb().width() * lUnitToPixels, body.aabb().height() * lUnitToPixels, .93f, .06f, .98f);
	}

	private void drawDebugInfo(LintfordCore core, Ship ship) {
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
