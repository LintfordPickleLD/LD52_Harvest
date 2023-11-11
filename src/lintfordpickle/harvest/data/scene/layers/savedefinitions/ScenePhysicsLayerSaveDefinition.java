package lintfordpickle.harvest.data.scene.layers.savedefinitions;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.ScenePhysicsLayer;

public class ScenePhysicsLayerSaveDefinition extends BaseSceneLayerSaveDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 7569238335511621943L;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ScenePhysicsLayerSaveDefinition() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public SceneBaseLayer getSceneLayer() {
		final var lPhysicsLayer = new ScenePhysicsLayer(layerUid);

		lPhysicsLayer.zDepth = layerZDepth;
		lPhysicsLayer.name = layerName;

		return lPhysicsLayer;
	}
}
