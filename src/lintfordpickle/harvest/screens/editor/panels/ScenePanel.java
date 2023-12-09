package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.controllers.editor.EditorSceneController;
import net.lintfordLib.editor.controllers.EditorBrushController;
import net.lintfordLib.editor.renderers.EditorBrushRenderer;
import net.lintfordLib.editor.renderers.UiPanel;
import net.lintfordlib.controllers.camera.CameraBoundsController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.renderers.windows.UiWindow;
import net.lintfordlib.renderers.windows.components.UiButtonToggle;
import net.lintfordlib.renderers.windows.components.UiInputText;

public class ScenePanel extends UiPanel {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final int SCENE_WIDTH_IN_PX = 10;
	public static final int SCENE_HEIGHT_IN_PX = 11;

	public static final int BUTTON__DRAW_SCENE_BORDER = 12;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private EditorBrushRenderer mEditorBrushRenderer;
	private EditorBrushController mEditorBrushController;

	private UiButtonToggle mToggleSceneBoundRendering;
	private UiInputText mSceneWidth;
	private UiInputText mSceneHeight;

	private EditorSceneController mSceneController;
	private CameraBoundsController mCameraBoundsController;

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

	public ScenePanel(UiWindow parentWindow, int entityGroupUid) {
		super(parentWindow, "Scene Panel", entityGroupUid);

		mShowActiveLayerButton = false;
		mShowShowLayerButton = false;

		mRenderPanelTitle = true;
		mPanelTitle = "Scene";

		mToggleSceneBoundRendering = new UiButtonToggle(parentWindow);
		mToggleSceneBoundRendering.setUiWidgetListener(this, BUTTON__DRAW_SCENE_BORDER);
		mToggleSceneBoundRendering.buttonLabel("Toggle Scene Border");

		mSceneWidth = new UiInputText(parentWindow);
		mSceneWidth.numericInputOnly(true);
		mSceneWidth.label("Width in Px");
		mSceneWidth.setUiWidgetListener(this, SCENE_WIDTH_IN_PX);

		mSceneHeight = new UiInputText(parentWindow);
		mSceneHeight.numericInputOnly(true);
		mSceneHeight.label("Height in Px");
		mSceneHeight.setUiWidgetListener(this, SCENE_HEIGHT_IN_PX);

		addWidget(mSceneWidth);
		addWidget(mSceneHeight);
		addWidget(mToggleSceneBoundRendering);

		isLayerVisible(true);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mEditorBrushController = (EditorBrushController) lControllerManager.getControllerByNameRequired(EditorBrushController.CONTROLLER_NAME, mEntityGroupUid);
		mEditorBrushController.showPosition(true);

		mSceneController = (EditorSceneController) lControllerManager.getControllerByNameRequired(EditorSceneController.CONTROLLER_NAME, mEntityGroupUid);
		mCameraBoundsController = (CameraBoundsController) lControllerManager.getControllerByNameRequired(CameraBoundsController.CONTROLLER_NAME, mEntityGroupUid);

		final var lRendererManager = mParentWindow.rendererManager();
		mEditorBrushRenderer = (EditorBrushRenderer) lRendererManager.getRendererRequired(EditorBrushRenderer.RENDERER_NAME);
		mEditorBrushRenderer.renderBrush(isLayerVisible());

		final var lSceneSettings = mSceneController.sceneData().sceneSettingsManager();
		if (lSceneSettings != null) {
			mSceneWidth.inputString(String.valueOf(lSceneSettings.sceneWidthInPx()));
			mSceneHeight.inputString(String.valueOf(lSceneSettings.sceneHeightInPx()));
		}

	}

	// --------------------------------------

	@Override
	public void onLayerSelected() {
		super.onLayerSelected();
	}

	@Override
	public void widgetOnClick(InputManager inputManager, int entryUid) {
		switch (entryUid) {

		case BUTTON_SHOW_LAYER:
			mEditorBrushRenderer.renderBrush(isLayerVisible());
			break;

		case BUTTON__DRAW_SCENE_BORDER:
			final var lCurrent = mCameraBoundsController.drawBounds();
			mCameraBoundsController.drawBounds(!lCurrent);
			break;
		}
	}

	@Override
	public void widgetOnDataChanged(InputManager inputManager, int entryUid) {
		switch (entryUid) {
		case SCENE_WIDTH_IN_PX: {
			try {
				final int lNewWidth = Integer.parseInt(mSceneWidth.inputString().toString());
				mSceneController.setSceneWidth(lNewWidth);
				mCameraBoundsController.widthBoundInPx(lNewWidth);
			} catch (NumberFormatException e) {
				mSceneWidth.inputString("1024");
				mSceneController.setSceneWidth(1024);
				mCameraBoundsController.widthBoundInPx(1024);
			}

		}
			break;

		case SCENE_HEIGHT_IN_PX: {
			try {
				final int lNewHeight = Integer.parseInt(mSceneHeight.inputString().toString());
				mSceneController.setSceneHeight(lNewHeight);
				mCameraBoundsController.heightBoundInPx(lNewHeight);
			} catch (NumberFormatException e) {
				mSceneHeight.inputString("1024");
				mSceneController.setSceneHeight(1024);
				mCameraBoundsController.heightBoundInPx(1024);
			}

		}
			break;
		}
	}

}