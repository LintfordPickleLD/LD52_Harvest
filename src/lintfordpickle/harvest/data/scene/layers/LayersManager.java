package lintfordpickle.harvest.data.scene.layers;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.scene.BaseInstanceManager;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;

public class LayersManager extends BaseInstanceManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<SceneBaseLayer> mLayers = new ArrayList<>();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public List<SceneBaseLayer> layers() {
		return mLayers;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public LayersManager() {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void addLayer(SceneBaseLayer layerToAd) {
		if (mLayers.contains(layerToAd) == false) {
			mLayers.add(layerToAd);
		}
	}

	public void removedLayer(SceneBaseLayer layerToDelete) {
		if (mLayers.contains(layerToDelete)) {
			mLayers.remove(layerToDelete);
		}
	}

	@Override
	public void initializeManager() {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeInTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {

		final int lNumLayers = mLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			final var lSceneLayerToSerialize = mLayers.get(i);

			sceneSaveDefinition.layers().addLayer(lSceneLayerToSerialize.getSaveDefinition());

		}
	}

	@Override
	public void loadFromTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		final var lLayerSaveManager = sceneSaveDefinition.layers();

		final var lTextureLayers = lLayerSaveManager.textureLayers;
		final var lNumTextureLayers = lTextureLayers.size();
		for (int i = 0; i < lNumTextureLayers; i++) {
			final var lLayerToImport = lTextureLayers.get(i);
			layers().add(lLayerToImport.getSceneLayer());
		}

		final var lAnimationLayers = lLayerSaveManager.animationLayers;
		final var lNumAnimationLayers = lAnimationLayers.size();
		for (int i = 0; i < lNumAnimationLayers; i++) {
			final var lLayerToImport = lAnimationLayers.get(i);
			layers().add(lLayerToImport.getSceneLayer());
		}

		final var lNoiseLayers = lLayerSaveManager.noiseLayers;
		final var lNumNoiseLayers = lNoiseLayers.size();
		for (int i = 0; i < lNumNoiseLayers; i++) {
			final var lLayerToImport = lNoiseLayers.get(i);
			layers().add(lLayerToImport.getSceneLayer());
		}

		final var lPhysicsLayers = lLayerSaveManager.physicsLayers;
		final var lNumphysicsLayers = lPhysicsLayers.size();
		for (int i = 0; i < lNumphysicsLayers; i++) {
			final var lLayerToImport = lPhysicsLayers.get(i);
			layers().add(lLayerToImport.getSceneLayer());
		}

	}

	@Override
	public void finalizeAfterLoading() {

	}

}
