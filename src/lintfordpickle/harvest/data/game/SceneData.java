package lintfordpickle.harvest.data.game;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;

public class SceneData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final List<SceneBaseLayer> layers = new ArrayList<>();

	public void addLayer(SceneBaseLayer layerToAd) {
		if (layers.contains(layerToAd) == false) {
			layers.add(layerToAd);
		}

	}

	public void removedLayer(SceneBaseLayer layerToDelete) {
		if (layers.contains(layerToDelete)) {
			layers.remove(layerToDelete);
		}

	}

}
