package lintfordpickle.harvest.data.scene.layers.savedefinitions;

import java.util.ArrayList;
import java.util.List;

public class LayersManagerSaveDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final List<SceneTextureLayerSaveDefinition> textureLayers = new ArrayList<>();
	public final List<SceneAnimationLayerSaveDefinition> animationLayers = new ArrayList<>();
	public final List<SceneNoiseLayerSaveDefinition> noiseLayers = new ArrayList<>();
	public final List<ScenePhysicsLayerSaveDefinition> physicsLayers = new ArrayList<>();

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LayersManagerSaveDefinition() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addLayer(BaseSceneLayerSaveDefinition layerSaveDefinition) {
		if (layerSaveDefinition instanceof SceneTextureLayerSaveDefinition) {
			textureLayers.add((SceneTextureLayerSaveDefinition) layerSaveDefinition);

		} else if (layerSaveDefinition instanceof SceneAnimationLayerSaveDefinition) {
			animationLayers.add((SceneAnimationLayerSaveDefinition) layerSaveDefinition);

		} else if (layerSaveDefinition instanceof SceneNoiseLayerSaveDefinition) {
			noiseLayers.add((SceneNoiseLayerSaveDefinition) layerSaveDefinition);

		} else if (layerSaveDefinition instanceof ScenePhysicsLayerSaveDefinition) {
			physicsLayers.add((ScenePhysicsLayerSaveDefinition) layerSaveDefinition);

		}
	}

}
