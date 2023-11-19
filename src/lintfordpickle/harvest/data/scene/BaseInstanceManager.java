package lintfordpickle.harvest.data.scene;

import lintfordpickle.harvest.data.scene.savedefinitions.SceneSaveDefinition;

// TODO: This belongs in the lib
public abstract class BaseInstanceManager {

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public BaseInstanceManager() {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public abstract void initializeManager();

	public abstract void storeInTrackDefinition(SceneSaveDefinition sceneSaveDefinition);

	public abstract void loadFromTrackDefinition(SceneSaveDefinition sceneSaveDefinition);

	public abstract void finalizeAfterLoading();

}
