package lintfordpickle.harvest.data.scene;

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

	@SerializedName(value = "SceneName")
	private String mSceneName;

	@SerializedName(value = "HashGrid")
	private final HashGridDefinition mGridSettings = new HashGridDefinition();

	@SerializedName(value = "Layers")
	private final LayersManagerSaveDefinition mLayerSaveManager = new LayersManagerSaveDefinition();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public String sceneName() {
		return mSceneName;
	}

	public void sceneName(String newSceneName) {
		mSceneName = newSceneName;
	}

	public HashGridDefinition gridSettings() {
		return mGridSettings;
	}

	public LayersManagerSaveDefinition layers() {
		return mLayerSaveManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------

	public boolean isSceneDefinitionValid() {
		// TODO: Fix the scene definition validation rountine

		return true;
	}

}
