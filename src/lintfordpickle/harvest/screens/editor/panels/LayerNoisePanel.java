package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.renderers.windows.UiWindow;

public class LayerNoisePanel extends LayerPanel<SceneNoiseLayer> {

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

	public LayerNoisePanel(UiWindow parentWindow, int entityGroupUid) {
		super(parentWindow, "Noise Layer", entityGroupUid);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	protected void newLayerSelected(SceneBaseLayer selectedLayer) {
		if (selectedLayer instanceof SceneNoiseLayer) {
			selectLayer((SceneNoiseLayer) selectedLayer);
		}
	}

	protected void selectLayer(SceneNoiseLayer selectedLayer) {
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