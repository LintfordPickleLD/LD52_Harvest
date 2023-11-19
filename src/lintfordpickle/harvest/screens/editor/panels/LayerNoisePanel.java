package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneNoiseLayer;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.core.input.keyboard.IUiInputKeyPressCallback;
import net.lintfordlib.renderers.windows.UiWindow;
import net.lintfordlib.renderers.windows.components.UiInputText;
import net.lintfordlib.renderers.windows.components.UiLabel;

public class LayerNoisePanel extends LayerPanel<SceneNoiseLayer> implements IUiInputKeyPressCallback {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int INPUT_NAME_KEY_UID = 100;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UiLabel mNameLabel;
	private UiInputText mLayerName;

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

		mNameLabel = new UiLabel(parentWindow, "Name");
		mLayerName = new UiInputText(parentWindow);
		mLayerName.setKeyUpdateListener(this, INPUT_NAME_KEY_UID);

		addWidget(mNameLabel);
		addWidget(mLayerName);

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

		mLayerName.inputString(selectedLayer.name);
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

	@Override
	public void keyPressUpdate(int codePoint) {
	}

	@Override
	public void UiInputEnded(int inputUid) {
		if (inputUid == INPUT_NAME_KEY_UID && mSelectedLayer != null) {
			mSelectedLayer.name = mLayerName.inputString().toString();

			refreshLayerPanels();
		}
	}
}