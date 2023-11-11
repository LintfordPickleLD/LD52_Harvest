package lintfordpickle.harvest.data.scene.layers.savedefinitions;

import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;

public class SceneAnimationLayerSaveDefinition extends BaseSceneLayerSaveDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 7569238335511621943L;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneAnimationLayerSaveDefinition() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public SceneBaseLayer getSceneLayer() {
		final var lAnimationLayer = new SceneAnimationLayer(layerUid);
		lAnimationLayer.zDepth = layerZDepth;
		lAnimationLayer.name = layerName;

		return lAnimationLayer;
	}

}
