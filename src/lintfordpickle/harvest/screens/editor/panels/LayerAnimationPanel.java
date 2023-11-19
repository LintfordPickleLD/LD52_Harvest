package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.controllers.editor.EditorAssetsController;
import lintfordpickle.harvest.data.scene.layers.SceneAnimationLayer;
import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.core.input.keyboard.IUiInputKeyPressCallback;
import net.lintfordlib.renderers.windows.UiWindow;
import net.lintfordlib.renderers.windows.components.UiButton;
import net.lintfordlib.renderers.windows.components.UiHorizontalEntryGroup;
import net.lintfordlib.renderers.windows.components.UiHorizontalListBox;
import net.lintfordlib.renderers.windows.components.UiInputText;
import net.lintfordlib.renderers.windows.components.UiLabel;
import net.lintfordlib.renderers.windows.components.UiListBoxItem;

public class LayerAnimationPanel extends LayerPanel<SceneAnimationLayer> implements IUiInputKeyPressCallback {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int INPUT_NAME_KEY_UID = 100;

	private static final int BUTTON_ADD_SPRITE = 150;
	private static final int BUTTON_DEL_SPRITE = 151;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private EditorAssetsController mEditorAssetsController;
	private UiHorizontalListBox mAnimationAssetList;

	private UiLabel mNameLabel;
	private UiInputText mLayerName;

	private UiButton mAddAnimationButton;
	private UiButton mRemoveAnimationButton;

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

		mNameLabel = new UiLabel(parentWindow, "Name");
		mLayerName = new UiInputText(parentWindow);
		mLayerName.setKeyUpdateListener(this, INPUT_NAME_KEY_UID);

		mAnimationAssetList = new UiHorizontalListBox(parentWindow, entityGroupUid);
		mAnimationAssetList.desiredHeight(170);

		mAddAnimationButton = new UiButton(parentWindow, "Add");
		mAddAnimationButton.setUiWidgetListener(this, BUTTON_ADD_SPRITE);
		mRemoveAnimationButton = new UiButton(parentWindow, "Delete");
		mRemoveAnimationButton.setUiWidgetListener(this, BUTTON_DEL_SPRITE);

		final var lHorizontalGroup = new UiHorizontalEntryGroup(parentWindow);
		lHorizontalGroup.widgets().add(mAddAnimationButton);
		lHorizontalGroup.widgets().add(mRemoveAnimationButton);

		addWidget(mNameLabel);
		addWidget(mLayerName);
		addWidget(mAnimationAssetList);
		addWidget(lHorizontalGroup);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mEditorAssetsController = (EditorAssetsController) lControllerManager.getControllerByNameRequired(EditorAssetsController.CONTROLLER_NAME, mEntityGroupUid);

		loadAssets(core);
	}

	private void loadAssets(LintfordCore core) {
		final var lAssetDefinitionManager = mEditorAssetsController.sceneAssetsManager().definitionManager();
		final var lPropDefinitions = lAssetDefinitionManager.definitions();
		final var lPropCollectionIterator = lPropDefinitions.iterator();

		while (lPropCollectionIterator.hasNext()) {
			final var lAssetDefinition = lPropCollectionIterator.next();

			if (lAssetDefinition == null)
				continue;

			final var lNewItem = new UiListBoxItem((int) lAssetDefinition.definitionUid());
			lNewItem.set(lAssetDefinition.name, lAssetDefinition.displayName);

			mAnimationAssetList.addItem(lNewItem);
		}
	}

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

		mLayerName.inputString(selectedLayer.name);
	}

	// --------------------------------------

	@Override
	public void widgetOnClick(InputManager inputManager, int entryUid) {
		switch (entryUid) {
		// Add from asset list
		case BUTTON_ADD_SPRITE: {
			if (mSelectedLayer == null)
				return;

			final var lSelectedAsset = mAnimationAssetList.getSelectedItem();
			if (lSelectedAsset != null) {
				final var lAssetDefinitionName = lSelectedAsset.definitionName;
				final var lNewAssetInstance = mEditorAssetsController.sceneAssetsManager().getAssetInstanceFromDefinitionName(lAssetDefinitionName);

				if (lNewAssetInstance != null)
					mSelectedLayer.addSprite(lNewAssetInstance);

			}
			break;
		}

		// Remove currently selected sprite
		case BUTTON_DEL_SPRITE: {
			if (mSelectedLayer == null)
				return;

			final var lSelectedAssetInstance = mEditorAssetsController.selectedAssetinstance();
			if (lSelectedAssetInstance != null) {
				mSelectedLayer.removeAssetInstance(lSelectedAssetInstance);
				mEditorAssetsController.selectedAssetinstance(null);
			}
		}
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