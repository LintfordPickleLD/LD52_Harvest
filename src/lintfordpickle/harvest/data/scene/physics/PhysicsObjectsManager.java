package lintfordpickle.harvest.data.scene.physics;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.scene.BaseInstanceManager;
import lintfordpickle.harvest.data.scene.savedefinitions.SceneSaveDefinition;

public class PhysicsObjectsManager extends BaseInstanceManager {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	private List<ScenePhysicsobjectInstance> worldSceneBodies = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PhysicsObjectsManager() {
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void initializeManager() {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeInTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFromTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finalizeAfterLoading() {
		// TODO Auto-generated method stub

	}
}
