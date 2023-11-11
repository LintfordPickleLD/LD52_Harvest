package lintfordpickle.harvest.data.scene.layers;

import lintfordpickle.harvest.data.scene.layers.savedefinitions.BaseSceneLayerSaveDefinition;

public abstract class SceneBaseLayer {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final int layerUid;

	public int zDepth;
	public String name;
	public float translationSpeedModX;
	public float translationSpeedModY;
	public float centerX;
	public float centerY;
	public float scaleX;
	public float scaleY;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneBaseLayer(int uid) {
		layerUid = uid;

		scaleX = 1.f;
		scaleY = 1.f;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public abstract BaseSceneLayerSaveDefinition getSaveDefinition();

	protected void fillBaseSceneLayerInfo(BaseSceneLayerSaveDefinition saveDefinition) {
		saveDefinition.layerZDepth = zDepth;
		saveDefinition.layerUid = layerUid;
		saveDefinition.layerName = name;
	}

}
