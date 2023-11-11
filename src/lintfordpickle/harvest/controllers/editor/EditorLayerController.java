package lintfordpickle.harvest.controllers.editor;

import lintfordpickle.harvest.data.game.SceneData;
import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import lintfordpickle.harvest.data.scene.layers.ScenePhysicsLayer;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.core.ControllerManager;

public class EditorLayerController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Editor Layer Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneData mSceneData;

	// TODO: when loading, make sure this is set to highest index + 1;
	private int LayerIndexCounter;

	public int getNewLayerUid() {
		return LayerIndexCounter++;
	}

	private SceneBaseLayer mSelectedLayer;

	public SceneBaseLayer getLayerByUid(int layerUid) {
		final var lLayers = mSceneData.layers;
		final int lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			if (lLayers.get(i).layerUid == layerUid)
				return lLayers.get(i);
		}
		return null;
	}

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SceneBaseLayer selectedLayer() {
		return mSelectedLayer;
	}

	public void selectedLayer(SceneBaseLayer newSelectedLayer) {
		mSelectedLayer = newSelectedLayer;
	}

	public SceneData sceneData() {
		return mSceneData;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorLayerController(ControllerManager controllerManager, SceneData sceneData, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mSceneData = sceneData;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void loadLayersFromSceneData() {

	}

	public void saveLayersIntoSceneData() {

	}

	public void deleteSelectedLayerByUid(int layerUidToDelete) {
		final var lLayer = getLayerByUid(layerUidToDelete);

		if (lLayer != null)
			mSceneData.removedLayer(lLayer);
	}

	public void deleteSelectedLayer(SceneBaseLayer layerToDelete) {
		mSceneData.removedLayer(layerToDelete);
	}

	public SceneBaseLayer addNewAnimationLayer() {
		final var lAnimationLayer = new SceneAnimationLayer(getNewLayerUid());
		mSceneData.addLayer(lAnimationLayer);

		lAnimationLayer.name = "Anim Layer " + lAnimationLayer.layerUid;
		return lAnimationLayer;
	}

	public SceneBaseLayer addNewNoiseLayer() {
		final var lNoiseLayer = new SceneNoiseLayer(getNewLayerUid());
		mSceneData.addLayer(lNoiseLayer);

		lNoiseLayer.name = "Noise Layer " + lNoiseLayer.layerUid;
		return lNoiseLayer;
	}

	public SceneBaseLayer addNewPhysicsLayer() {
		final var lPhysicsLayer = new ScenePhysicsLayer(getNewLayerUid());
		mSceneData.addLayer(lPhysicsLayer);

		lPhysicsLayer.name = "Phys Layer " + lPhysicsLayer.layerUid;
		return lPhysicsLayer;
	}

	public void setSelectedLayer(int selectedLayerUid) {
		mSelectedLayer = getLayerByUid(selectedLayerUid);

	}

}
