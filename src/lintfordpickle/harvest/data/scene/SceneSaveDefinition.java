package lintfordpickle.harvest.data.scene;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lintfordpickle.harvest.data.scene.layers.savedefinitions.LayersManagerSaveDefinition;
import lintfordpickle.harvest.data.scene.savedefinitions.HashGridSaveManager;
import lintfordpickle.harvest.data.scene.savedefinitions.ScenePhysicsObjectsSaveManager;
import lintfordpickle.harvest.data.scene.savedefinitions.ScenePhysicsSettingsSaveManager;
import lintfordpickle.harvest.data.scene.savedefinitions.SceneSettingsSaveDefinition;

public class SceneSaveDefinition implements Serializable {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 7556775821334029016L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	@SerializedName(value = "SceneSettings")
	private final SceneSettingsSaveDefinition mSceneSettingsSaveDefinition = new SceneSettingsSaveDefinition();

	@SerializedName(value = "HashGrid")
	private final HashGridSaveManager mGridSettings = new HashGridSaveManager();

	@SerializedName(value = "Layers")
	private final LayersManagerSaveDefinition mLayerSaveManager = new LayersManagerSaveDefinition();

	@SerializedName(value = "PhysicsSettings")
	private final ScenePhysicsSettingsSaveManager mScenePhysicsSettingsSaveManager = new ScenePhysicsSettingsSaveManager();

	@SerializedName(value = "PhysicsObjects")
	private final ScenePhysicsObjectsSaveManager mPhysicsObjectsSaveManager = new ScenePhysicsObjectsSaveManager();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SceneSettingsSaveDefinition sceneSettingsSaveDefinition() {
		return mSceneSettingsSaveDefinition;
	}

	public HashGridSaveManager gridSettings() {
		return mGridSettings;
	}

	public LayersManagerSaveDefinition layers() {
		return mLayerSaveManager;
	}

	public ScenePhysicsSettingsSaveManager physicsSettings() {
		return mScenePhysicsSettingsSaveManager;
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
