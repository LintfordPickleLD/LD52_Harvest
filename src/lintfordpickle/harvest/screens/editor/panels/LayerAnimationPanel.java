package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.renderers.windows.UiWindow;

public class LayerAnimationPanel extends LayerPanel<SceneAnimationLayer> {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public int layerOwnerHashCode() {
		return hashCode();
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LayerAnimationPanel(UiWindow parentWindow, int entityGroupUid) {
		super(parentWindow, "Animation Layer", entityGroupUid);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	protected void newLayerSelected(SceneBaseLayer selectedLayer) {
		if (selectedLayer instanceof SceneAnimationLayer) {
			selectLayer((SceneAnimationLayer) selectedLayer);
		}
	}

	protected void selectLayer(SceneAnimationLayer selectedLayer) {
		mSelectedLayer = selectedLayer;
		mIsExpandable = true;
		mIsPanelOpen = true;
	}

	// --------------------------------------

	@Override
	public void widgetOnClick(InputManager inputManager, int entryUid) {
		switch (entryUid) {

		}
	}

	@Override
	public void widgetOnDataChanged(InputManager inputManager, int entryUid) {

	}

}