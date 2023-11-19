package lintfordpickle.harvest.data.editor.physics;

import net.lintfordlib.core.maths.Vector2f;

public class EditorPolygonPoint {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final EditorPhysicsObjectInstance parent;

	public final Vector2f worldPosition = new Vector2f();

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorPolygonPoint(EditorPhysicsObjectInstance parent) {
		this.parent = parent;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void setWorldPosition(float worldX, float worldY) {
		worldPosition.x = worldX;
		worldPosition.y = worldY;
	}
}
