package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.core.input.keyboard.IUiInputKeyPressCallback;
import net.lintfordlib.renderers.windows.UiWindow;
import net.lintfordlib.renderers.windows.components.UiButton;
import net.lintfordlib.renderers.windows.components.UiFloatSlider;
import net.lintfordlib.renderers.windows.components.UiHorizontalEntryGroup;
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

	private UiFloatSlider mCenterX;
	private UiFloatSlider mCenterY;

	private UiFloatSlider mTranslationSpeedModX;
	private UiFloatSlider mTranslationSpeedModY;

	private UiFloatSlider mScaleX;
	private UiFloatSlider mScaleY;

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

		mTextureName.setClickListener(this, BUTTON_TEXTURE_NAME);
		mTexturePath.setClickListener(this, BUTTON_TEXTURE_PATH);
		mTexturePath.inputString("res/textures/");
		mTexturePath.emptyString("res/textures/");
		mRefreshButton.setClickListener(this, BUTTON_REFRESH);
		mLayerName.setKeyUpdateListener(this, INPUT_NAME_KEY_UID);

		mCenterX = new UiFloatSlider(parentWindow);
		mCenterX.setClickListener(this, SLIDER_CENTER_X);
		mCenterX.sliderLabel("Center X");
		mCenterX.setMinMax(0.f, 10.f);
		mCenterY = new UiFloatSlider(parentWindow);
		mCenterY.setClickListener(this, SLIDER_CENTER_Y);
		mCenterY.sliderLabel("Center Y");
		mCenterY.setMinMax(0.f, 10.f);

		mTranslationSpeedModX = new UiFloatSlider(parentWindow);
		mTranslationSpeedModX.setClickListener(this, SLIDER_TRANSLATION_SPEED_X);
		mTranslationSpeedModX.sliderLabel("Mod X");
		mTranslationSpeedModX.setMinMax(0.f, 10.f);
		mTranslationSpeedModY = new UiFloatSlider(parentWindow);
		mTranslationSpeedModY.setClickListener(this, SLIDER_TRANSLATION_SPEED_Y);
		mTranslationSpeedModY.sliderLabel("Mod Y");
		mTranslationSpeedModY.setMinMax(0.f, 10.f);

		mScaleX = new UiFloatSlider(parentWindow);
		mScaleX.setClickListener(this, SLIDER_SCALE_X);
		mScaleX.sliderLabel("Scale X");
		mScaleX.setMinMax(0.f, 10.f);
		mScaleY = new UiFloatSlider(parentWindow);
		mScaleY.setClickListener(this, SLIDER_SCALE_Y);
		mScaleY.sliderLabel("Scale Y");
		mScaleY.setMinMax(0.f, 10.f);

		final var lHorizontalGroup0 = new UiHorizontalEntryGroup(parentWindow);
		lHorizontalGroup0.widgets().add(mCenterX);
		lHorizontalGroup0.widgets().add(mCenterY);

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

		mTranslationSpeedModX.currentValue(selectedLayer.translationSpeedModX);
		mTranslationSpeedModY.currentValue(selectedLayer.translationSpeedModY);

		mScaleX.currentValue(selectedLayer.scaleX);
		mScaleY.currentValue(selectedLayer.scaleY);

		mCenterX.currentValue(selectedLayer.centerX);
		mCenterY.currentValue(selectedLayer.centerY);
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