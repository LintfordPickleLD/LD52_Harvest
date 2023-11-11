package lintfordpickle.harvest.data.scene.layers;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.assets.SceneAssetInstance;
import lintfordpickle.harvest.data.scene.layers.savedefinitions.BaseSceneLayerSaveDefinition;
import lintfordpickle.harvest.data.scene.layers.savedefinitions.SceneAnimationLayerSaveDefinition;

public class SceneAnimationLayer extends SceneBaseLayer {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private List<SceneAssetInstance> mSprites = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<SceneAssetInstance> spriteAssets() {
		return mSprites;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneAnimationLayer(int uid) {
		super(uid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addSprite(SceneAssetInstance assetInstance) {

	}

	public void removeAssetInstance(SceneAssetInstance assetInstance) {

	}

	@Override
	public BaseSceneLayerSaveDefinition getSaveDefinition() {
		final var lSaveDefinition = new SceneAnimationLayerSaveDefinition();

		fillBaseSceneLayerInfo(lSaveDefinition);

		// TODO Auto-generated method stub

		return lSaveDefinition;
	}

}
