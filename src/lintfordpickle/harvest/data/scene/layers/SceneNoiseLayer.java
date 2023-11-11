package lintfordpickle.harvest.data.scene.layers;

import lintfordpickle.harvest.data.scene.layers.savedefinitions.BaseSceneLayerSaveDefinition;
import lintfordpickle.harvest.data.scene.layers.savedefinitions.SceneNoiseLayerSaveDefinition;

public class SceneNoiseLayer extends SceneBaseLayer {

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

	public SceneNoiseLayer(int uid) {
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
		final var lSaveDefinition = new SceneNoiseLayerSaveDefinition();

		fillBaseSceneLayerInfo(lSaveDefinition);

		// TODO Auto-generated method stub

		return lSaveDefinition;
	}

}
