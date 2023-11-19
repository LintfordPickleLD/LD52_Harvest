package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.core.input.keyboard.IUiInputKeyPressCallback;
import net.lintfordlib.renderers.windows.UiWindow;
import net.lintfordlib.renderers.windows.components.UiButton;
import net.lintfordlib.renderers.windows.components.UiHorizontalEntryGroup;
import net.lintfordlib.renderers.windows.components.UiInputFloat;
import net.lintfordlib.renderers.windows.components.UiInputInteger;
import net.lintfordlib.renderers.windows.components.UiInputText;
import net.lintfordlib.renderers.windows.components.UiLabel;

public class LayerTexturePanel extends LayerPanel<SceneTextureLayer> implements IUiInputKeyPressCallback {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private final static int BUTTON_TEXTURE_NAME = 5;
	private final static int BUTTON_TEXTURE_PATH = 6;
	private final static int BUTTON_REFRESH = 10;

	private final static int SLIDER_TRANSLATION_SPEED_X = 15;
	private final static int SLIDER_TRANSLATION_SPEED_Y = 16;

	private final static int SLIDER_CENTER_X = 17;
	private final static int SLIDER_CENTER_Y = 18;

	private final static int SLIDER_SCALE_X = 19;
	private final static int SLIDER_SCALE_Y = 20;

	private static final int INPUT_NAME_KEY_UID = 100;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UiLabel mLayerNameLabel;
	private UiInputText mLayerName;

	private UiLabel mTextureNameLabel;
	private UiInputText mTextureName;

	private UiLabel mTexturePathLabel;
	private UiInputText mTexturePath;
	private UiButton mRefreshButton;

	private UiInputInteger mCenterXInput;
	private UiInputInteger mCenterYInput;

	private UiInputFloat mTranslationSpeedModX;
	private UiInputFloat mTranslationSpeedModY;

	private UiInputFloat mScaleX;
	private UiInputFloat mScaleY;

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

	public LayerTexturePanel(UiWindow parentWindow, int entityGroupUid) {
		super(parentWindow, "Texture Layer", entityGroupUid);

		mLayerNameLabel = new UiLabel(parentWindow, "Layer Name");
		mLayerName = new UiInputText(parentWindow);

		mTextureNameLabel = new UiLabel(parentWindow, "Name");
		mTextureName = new UiInputText(parentWindow);
		mTexturePathLabel = new UiLabel(parentWindow, "Path");
		mTexturePath = new UiInputText(parentWindow);
		mTexturePath.maxnumInputCharacters(160);
		mRefreshButton = new UiButton(parentWindow, "Refresh");

		mTextureName.setUiWidgetListener(this, BUTTON_TEXTURE_NAME);
		mTexturePath.setUiWidgetListener(this, BUTTON_TEXTURE_PATH);
		mTexturePath.inputString("res/textures/");
		mTexturePath.emptyString("res/textures/");
		mRefreshButton.setUiWidgetListener(this, BUTTON_REFRESH);
		mLayerName.setKeyUpdateListener(this, INPUT_NAME_KEY_UID);

		mCenterXInput = new UiInputInteger(parentWindow);
		mCenterXInput.setUiWidgetListener(this, SLIDER_CENTER_X);
		mCenterXInput.label("CenterX");
		mCenterXInput.setMinMax(0, 0);

		mCenterYInput = new UiInputInteger(parentWindow);
		mCenterYInput.setUiWidgetListener(this, SLIDER_CENTER_Y);
		mCenterYInput.label("CenterY");
		mCenterYInput.setMinMax(0, 0);

		mTranslationSpeedModX = new UiInputFloat(parentWindow);
		mTranslationSpeedModX.setUiWidgetListener(this, SLIDER_TRANSLATION_SPEED_X);
		mTranslationSpeedModX.label("Mod X");
		mTranslationSpeedModX.setMinMax(-20.f, 20.f);
		mTranslationSpeedModX.stepSize(.1f);
		
		mTranslationSpeedModY = new UiInputFloat(parentWindow);
		mTranslationSpeedModY.setUiWidgetListener(this, SLIDER_TRANSLATION_SPEED_Y);
		mTranslationSpeedModY.label("Mod Y");
		mTranslationSpeedModY.setMinMax(0.f, 10.f);
		mTranslationSpeedModY.stepSize(.1f);

		mScaleX = new UiInputFloat(parentWindow);
		mScaleX.setUiWidgetListener(this, SLIDER_SCALE_X);
		mScaleX.label("Scale X");
		mScaleX.setMinMax(0.f, 10.f);
		mScaleX.stepSize(.1f);
		mScaleY = new UiInputFloat(parentWindow);
		mScaleY.setUiWidgetListener(this, SLIDER_SCALE_Y);
		mScaleY.label("Scale Y");
		mScaleY.setMinMax(0.f, 10.f);
		mScaleY.stepSize(.1f);

		final var lHorizontalGroup0 = new UiHorizontalEntryGroup(parentWindow);
		lHorizontalGroup0.widgets().add(mCenterXInput);
		lHorizontalGroup0.widgets().add(mCenterYInput);

		final var lHorizontalGroup1 = new UiHorizontalEntryGroup(parentWindow);
		lHorizontalGroup1.widgets().add(mTranslationSpeedModX);
		lHorizontalGroup1.widgets().add(mTranslationSpeedModY);

		final var lHorizontalGroup2 = new UiHorizontalEntryGroup(parentWindow);
		lHorizontalGroup2.widgets().add(mScaleX);
		lHorizontalGroup2.widgets().add(mScaleY);

		addWidget(mLayerNameLabel);
		addWidget(mLayerName);

		addWidget(mTextureNameLabel);
		addWidget(mTextureName);
		addWidget(mTexturePathLabel);
		addWidget(mTexturePath);

		addWidget(mRefreshButton);

		addWidget(lHorizontalGroup0);
		addWidget(lHorizontalGroup1);
		addWidget(lHorizontalGroup2);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	protected void newLayerSelected(SceneBaseLayer selectedLayer) {
		if (selectedLayer instanceof SceneTextureLayer) {
			selectLayer((SceneTextureLayer) selectedLayer);
		}
	}

	protected void selectLayer(SceneTextureLayer selectedLayer) {
		mSelectedLayer = selectedLayer;
		mIsExpandable = true;
		mIsPanelOpen = true;

		mLayerName.inputString(selectedLayer.name);
		if (selectedLayer.textureName() != null)
			mTextureName.inputString(selectedLayer.textureName());

		if (selectedLayer.textureFilepath() != null)
			mTexturePath.inputString(selectedLayer.textureFilepath());

		mTranslationSpeedModX.inputString(String.valueOf(selectedLayer.translationSpeedModX));
		mTranslationSpeedModY.inputString(String.valueOf(selectedLayer.translationSpeedModY));

		mScaleX.inputString(String.valueOf(selectedLayer.scaleX));
		mScaleY.inputString(String.valueOf(selectedLayer.scaleY));

		// TODO:
//		mCenterX.currentValue(selectedLayer.centerX);
//		mCenterY.currentValue(selectedLayer.centerY);
	}

	// --------------------------------------

	@Override
	public void widgetOnClick(InputManager inputManager, int entryUid) {
		switch (entryUid) {
		case BUTTON_REFRESH:

			if (mSelectedLayer == null)
				return;

			mSelectedLayer.name = mLayerName.inputString().toString();
			mSelectedLayer.setTextureName(mTextureName.inputString().toString());
			mSelectedLayer.setTextureFilepath(mTexturePath.inputString().toString());
			mSelectedLayer.texture = null;

			break;

		}
	}

	@Override
	public void widgetOnDataChanged(InputManager inputManager, int entryUid) {
		switch (entryUid) {
		case INPUT_NAME_KEY_UID:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.name = mLayerName.inputString().toString();
			break;

		case BUTTON_TEXTURE_NAME:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.setTextureName(mTextureName.inputString().toString());
			break;

		case BUTTON_TEXTURE_PATH:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.setTextureFilepath(mTexturePath.inputString().toString());
			break;

		case SLIDER_SCALE_X:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.scaleX = mScaleX.currentValue();
			break;
			
		case SLIDER_SCALE_Y:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.scaleY = mScaleY.currentValue();
			break;
			
		case SLIDER_CENTER_X:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.centerX = mCenterXInput.currentValue();
			break;
			
		case SLIDER_CENTER_Y:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.centerY = mCenterYInput.currentValue();
			break;
			
		case SLIDER_TRANSLATION_SPEED_X:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.translationSpeedModX = mTranslationSpeedModX.currentValue();

			break;
		case SLIDER_TRANSLATION_SPEED_Y:
			if (mSelectedLayer == null)
				return;

			mSelectedLayer.translationSpeedModY = mTranslationSpeedModY.currentValue();

			break;
		}
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