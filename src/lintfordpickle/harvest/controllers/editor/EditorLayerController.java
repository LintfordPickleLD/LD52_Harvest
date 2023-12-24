package lintfordpickle.harvest.controllers.editor;

import java.util.List;

import lintfordpickle.harvest.controllers.layers.EditorAnimationLayerController;
import lintfordpickle.harvest.controllers.layers.EditorNoiseLayerController;
import lintfordpickle.harvest.controllers.layers.EditorParticleLayerController;
import lintfordpickle.harvest.controllers.layers.EditorTextureLayerController;
import lintfordpickle.harvest.data.scene.layers.LayersManager;
import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneParticleLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;

public class EditorLayerController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Editor Layer Controller";

	public static final int ACTION_OBJECT_TRANSLATE_SELECTED_LAYER = 1;
	public static final int ACTION_OBJECT_SCALE_SELECTED_LAYER_X = 2;
	public static final int ACTION_OBJECT_SCALE_SELECTED_LAYER_Y = 3;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LayersManager mLayersManager;
	private SceneBaseLayer mSelectedLayer;

	private EditorTextureLayerController mEditorTextureLayerController;
	private EditorNoiseLayerController mEditorNoiseLayerController;
	private EditorAnimationLayerController mEditorAnimationLayerController;
	private EditorParticleLayerController mEditorParticleLayerController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SceneBaseLayer getLayerByUid(int layerUid) {
		final var lLayers = mLayersManager.layers();
		final int lNumLayers = lLayers.size();
		for (int i = 0; i < lNumLayers; i++) {
			if (lLayers.get(i).layerUid == layerUid)
				return lLayers.get(i);
		}
		return null;
	}

	public SceneBaseLayer selectedLayer() {
		return mSelectedLayer;
	}

	public void selectedLayer(SceneBaseLayer newSelectedLayer) {
		mSelectedLayer = newSelectedLayer;
	}

	public LayersManager layersManager() {
		return mLayersManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorLayerController(ControllerManager controllerManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		final var lSceneController = (EditorSceneController) lControllerManager.getControllerByNameRequired(EditorSceneController.CONTROLLER_NAME, entityGroupUid());
		mLayersManager = lSceneController.sceneData().layersManager();

		mEditorTextureLayerController = (EditorTextureLayerController) lControllerManager.getControllerByNameRequired(EditorTextureLayerController.CONTROLLER_NAME, entityGroupUid());
		mEditorNoiseLayerController = (EditorNoiseLayerController) lControllerManager.getControllerByNameRequired(EditorNoiseLayerController.CONTROLLER_NAME, entityGroupUid());
		mEditorAnimationLayerController = (EditorAnimationLayerController) lControllerManager.getControllerByNameRequired(EditorAnimationLayerController.CONTROLLER_NAME, entityGroupUid());
		mEditorParticleLayerController = (EditorParticleLayerController) lControllerManager.getControllerByNameRequired(EditorParticleLayerController.CONTROLLER_NAME, entityGroupUid());

		mEditorTextureLayerController.setLayerManager(mLayersManager);
		mEditorNoiseLayerController.setLayerManager(mLayersManager);
		mEditorAnimationLayerController.setLayerManager(mLayersManager);
		mEditorParticleLayerController.setLayerManager(mLayersManager);

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void refreshLayers() {

	}

	public void loadLayersFromSceneData() {

	}

	public void saveLayersIntoSceneData() {

	}

	public void deleteSelectedLayerByUid(int layerUidToDelete) {
		final var lLayer = getLayerByUid(layerUidToDelete);

		if (lLayer != null)
			mLayersManager.removedLayer(lLayer);
	}

	public void deleteSelectedLayer(SceneBaseLayer layerToDelete) {
		mLayersManager.removedLayer(layerToDelete);

		if (layerToDelete instanceof SceneTextureLayer) {
			mEditorTextureLayerController.removeTextureLayer((SceneTextureLayer) layerToDelete);
		} else if (layerToDelete instanceof SceneNoiseLayer) {
			mEditorNoiseLayerController.removeNoiseLayer((SceneNoiseLayer) layerToDelete);
		} else if (layerToDelete instanceof SceneAnimationLayer) {
			mEditorAnimationLayerController.removeAnimationLayer((SceneAnimationLayer) layerToDelete);
		} else if (layerToDelete instanceof SceneParticleLayer) {
			mEditorParticleLayerController.removeParticleLayer((SceneParticleLayer) layerToDelete);
		}

	}

	public SceneBaseLayer addNewTextureLayer() {
		final var lTextureLayer = new SceneTextureLayer(mLayersManager.getNewInstanceUid());
		mLayersManager.addLayer(lTextureLayer);

		mEditorTextureLayerController.addTextureLayer(lTextureLayer);

		lTextureLayer.name = "Tex Layer " + lTextureLayer.layerUid;
		return lTextureLayer;
	}

	public SceneBaseLayer addNewAnimationLayer() {
		final var lAnimationLayer = new SceneAnimationLayer(mLayersManager.getNewInstanceUid());
		mLayersManager.addLayer(lAnimationLayer);

		lAnimationLayer.name = "Anim Layer " + lAnimationLayer.layerUid;
		return lAnimationLayer;
	}

	public SceneBaseLayer addNewNoiseLayer() {
		final var lNoiseLayer = new SceneNoiseLayer(mLayersManager.getNewInstanceUid());
		mLayersManager.addLayer(lNoiseLayer);

		lNoiseLayer.name = "Noise Layer " + lNoiseLayer.layerUid;
		return lNoiseLayer;
	}

	public void setSelectedLayer(int selectedLayerUid) {
		mSelectedLayer = getLayerByUid(selectedLayerUid);
	}

	public void reorderLayersPerZDepth(List<SceneBaseLayer> layersOrdered) {
		if (layersOrdered.size() != mLayersManager.layers().size()) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Cannot recorder the scene layers - count mismatch!");
			return;
		}

		final var lLayersList = mLayersManager.layers();
		lLayersList.clear();

		final var lNumOrderedList = layersOrdered.size();
		for (int i = 0; i < lNumOrderedList; i++) {
			lLayersList.add(layersOrdered.get(i));
		}
	}
}
