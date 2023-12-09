package lintfordpickle.harvest.data.scene.savedefinitions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lintfordpickle.harvest.data.editor.physics.ScenePhysicsObjectSaveDefinition;

public class ScenePhysicsObjectsSaveManager implements Serializable {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 1433319979369040506L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	@SerializedName(value = "ObjectInstances")
	public final List<ScenePhysicsObjectSaveDefinition> physicsObjects = new ArrayList<>();

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ScenePhysicsObjectsSaveManager() {
	}

}
