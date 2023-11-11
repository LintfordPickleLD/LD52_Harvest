package lintfordpickle.harvest.data.scene.layers;

import lintfordpickle.harvest.data.scene.layers.savedefinitions.BaseSceneLayerSaveDefinition;
import lintfordpickle.harvest.data.scene.layers.savedefinitions.ScenePhysicsLayerSaveDefinition;

public class ScenePhysicsLayer extends SceneBaseLayer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ScenePhysicsLayer(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public BaseSceneLayerSaveDefinition getSaveDefinition() {
		final var lSaveDefinition = new ScenePhysicsLayerSaveDefinition();

		fillBaseSceneLayerInfo(lSaveDefinition);
		
		// TODO Auto-generated method stub

		return lSaveDefinition;
	}
	
}
