package lintfordpickle.harvest.data.scene.layers.savedefinitions;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;

public class SceneNoiseLayerSaveDefinition extends BaseSceneLayerSaveDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 7569238335511621943L;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneNoiseLayerSaveDefinition() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public SceneBaseLayer getSceneLayer() {
		final var lNoiseLayer = new SceneNoiseLayer(layerUid);

		lNoiseLayer.zDepth = layerZDepth;
		lNoiseLayer.name = layerName;

		return lNoiseLayer;
	}
}
