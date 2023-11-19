package lintfordpickle.harvest.data.scene.savedefinitions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lintfordpickle.harvest.data.editor.physics.EditorPhysicsObjectInstance;
import lintfordpickle.harvest.data.editor.physics.ScenePhysicsObjectSaveDefinition;
import net.lintfordlib.core.maths.Vector2f;

public class ScenePhysicsObjectsSaveManager implements Serializable {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 1433319979369040506L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	@SerializedName(value = "PhysicsObjects")
	private final List<ScenePhysicsObjectSaveDefinition> mPhysicsObjects = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<ScenePhysicsObjectSaveDefinition> physicsObjects() {
		return mPhysicsObjects;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ScenePhysicsObjectsSaveManager() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void storeFloorDefinitions(List<EditorPhysicsObjectInstance> floorDefinition) {
		mPhysicsObjects.clear();

		final int lNumFloorDefinitions = floorDefinition.size();
		for (int i = 0; i < lNumFloorDefinitions; i++) {
			final var f = floorDefinition.get(i);

			final var lFloorDefinition = new ScenePhysicsObjectSaveDefinition();

			lFloorDefinition.worldCenterX = f.wcx;
			lFloorDefinition.worldCenterY = f.wcy;
			lFloorDefinition.rotation = f.angle;

			lFloorDefinition.localPoints.add(new Vector2f(f.a.worldPosition.x - f.wcx, f.a.worldPosition.y - f.wcy));
			lFloorDefinition.localPoints.add(new Vector2f(f.b.worldPosition.x - f.wcx, f.b.worldPosition.y - f.wcy));
			lFloorDefinition.localPoints.add(new Vector2f(f.c.worldPosition.x - f.wcx, f.c.worldPosition.y - f.wcy));
			lFloorDefinition.localPoints.add(new Vector2f(f.d.worldPosition.x - f.wcx, f.d.worldPosition.y - f.wcy));

			mPhysicsObjects.add(lFloorDefinition);
		}

	}

}
