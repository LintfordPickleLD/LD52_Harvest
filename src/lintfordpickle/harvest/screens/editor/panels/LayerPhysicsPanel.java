package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.ScenePhysicsLayer;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.renderers.windows.UiWindow;

public class LayerPhysicsPanel extends LayerPanel<ScenePhysicsLayer> {

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

	public LayerPhysicsPanel(UiWindow parentWindow, int entityGroupUid) {
		super(parentWindow, "Physics Layer", entityGroupUid);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	protected void newLayerSelected(SceneBaseLayer selectedLayer) {
		if (selectedLayer instanceof ScenePhysicsLayer) {
			selectLayer((ScenePhysicsLayer) selectedLayer);
			return;
		}
	}

	protected void selectLayer(ScenePhysicsLayer selectedLayer) {
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