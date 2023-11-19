package lintfordpickle.harvest.data.scene.savedefinitions;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lintfordpickle.harvest.data.scene.collisions.definitions.HashGridDefinition;
import lintfordpickle.harvest.data.scene.layers.savedefinitions.LayersManagerSaveDefinition;

public class SceneSaveDefinition implements Serializable {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 7556775821334029016L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	@SerializedName(value = "SceneWidth")
	private int mSceneWidthInPx;

	@SerializedName(value = "SceneHeight")
	private int mSceneHeightInPx;

	@SerializedName(value = "HashGrid")
	private final HashGridDefinition mGridSettings = new HashGridDefinition();

	@SerializedName(value = "Layers")
	private final LayersManagerSaveDefinition mLayerSaveManager = new LayersManagerSaveDefinition();

	private final ScenePhysicsObjectsSaveManager mPhysicsObjectsSaveManager = new ScenePhysicsObjectsSaveManager();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public int widthInPixels() {
		return mSceneWidthInPx;
	}

	public void widthInPixels(int width) {
		mSceneWidthInPx = width;
	}

	public int heightInPixels() {
		return mSceneHeightInPx;
	}

	public void heightInPixels(int height) {
		mSceneHeightInPx = height;
	}

	public HashGridDefinition gridSettings() {
		return mGridSettings;
	}

	public LayersManagerSaveDefinition layers() {
		return mLayerSaveManager;
	}

	public ScenePhysicsObjectsSaveManager physicsObjects() {
		return mPhysicsObjectsSaveManager;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public boolean isSceneDefinitionValid() {
		// TODO: Fix the scene definition validation rountine

		return true;
	}

}
