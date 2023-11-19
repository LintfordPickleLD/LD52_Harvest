package lintfordpickle.harvest.screens.editor.panels;

import lintfordpickle.harvest.controllers.editor.EditorPhysicsController;
import lintfordpickle.harvest.data.editor.EditorLayersData;
import lintfordpickle.harvest.data.editor.physics.EditorPhysicsObjectInstance;
import lintfordpickle.harvest.renderers.editor.EditorPhysicsRenderer;
import net.lintfordLib.editor.controllers.EditorBrushController;
import net.lintfordLib.editor.renderers.UiPanel;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.input.InputManager;
import net.lintfordlib.renderers.windows.UiWindow;
import net.lintfordlib.renderers.windows.components.UiButton;
import net.lintfordlib.renderers.windows.components.UiButtonToggle;
import net.lintfordlib.renderers.windows.components.UiLabelledInt;

public class LayerPhysicsPanel extends UiPanel {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int BUTTON_PLACE_FLOOR = 10;
	private static final int BUTTON_DELETE_SELECTED = 11;
	private static final int BUTTON_DELETE_ALL = 12;
	private static final int BUTTON_TOGGLE_SNAP = 13;
	private static final int BUTTON_TOGGLE_STATIC = 14;
	private static final int BUTTON_TOGGLE_DRAW_UIDS = 20;
	private static final int BUTTON_SET_OBJECT_CENTER_TO_CURSOR = 16;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UiButton mAddNewPolygon;
	private UiButton mSetFloorCenterToCursor;
	private UiButtonToggle mToggleStatic;
	private UiLabelledInt mNumFloorRegions;

	private EditorPhysicsController mEditorPhysicsController;
	private EditorPhysicsRenderer mEditorPhysicsRenderer;

	private EditorPhysicsObjectInstance mSelectedObjectInstance;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public int layerOwnerHashCode() {
		return mEditorPhysicsRenderer.hashCode();
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LayerPhysicsPanel(UiWindow parentWindow, int entityGroupUid) {
		super(parentWindow, "Physics Layer", entityGroupUid);

		mShowActiveLayerButton = true;
		mShowShowLayerButton = true;

		mEditorActiveLayerUid = EditorLayersData.Physics;

		mRenderPanelTitle = true;
		mPanelTitle = "Physics Scene";

		mNumFloorRegions = new UiLabelledInt(parentWindow);
		mNumFloorRegions.labelText("Number Objects: ");

		mAddNewPolygon = new UiButton(parentWindow);
		mAddNewPolygon.setUiWidgetListener(this, BUTTON_PLACE_FLOOR);
		mAddNewPolygon.buttonLabel("Add Polygon");

		mSetFloorCenterToCursor = new UiButton(parentWindow);
		mSetFloorCenterToCursor.setUiWidgetListener(this, BUTTON_SET_OBJECT_CENTER_TO_CURSOR);
		mSetFloorCenterToCursor.buttonLabel("Center");

		mToggleStatic = new UiButtonToggle(parentWindow);
		mToggleStatic.setUiWidgetListener(this, BUTTON_TOGGLE_STATIC);
		mToggleStatic.buttonLabel("Is Static");

		addWidget(mNumFloorRegions);

		addWidget(mAddNewPolygon);

		addWidget(mToggleStatic);
		addWidget(mSetFloorCenterToCursor);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mEditorBrushController = (EditorBrushController) lControllerManager.getControllerByNameRequired(EditorBrushController.CONTROLLER_NAME, mEntityGroupUid);
		mEditorBrushController.showPosition(true);

		mEditorPhysicsController = (EditorPhysicsController) lControllerManager.getControllerByNameRequired(EditorPhysicsController.CONTROLLER_NAME, mEntityGroupUid);

		final var lRendererManager = mParentWindow.rendererManager();
		mEditorPhysicsRenderer = (EditorPhysicsRenderer) lRendererManager.getRenderer(EditorPhysicsRenderer.RENDERER_NAME);
	}

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		if (isOpen() == false)
			return;

		if (mSelectedObjectInstance != mEditorPhysicsController.selectedPhysicsObject()) {
			mSelectedObjectInstance = mEditorPhysicsController.selectedPhysicsObject();

			if (mSelectedObjectInstance != null) {
				mToggleStatic.isEnabled(true);
			} else {
				mToggleStatic.isToggledOn(false);
				mToggleStatic.isEnabled(false);
			}
		}

		if (mSelectedObjectInstance != null) {
			mToggleStatic.isToggledOn(mSelectedObjectInstance.body_isStatic);

		}

		mNumFloorRegions.value(mEditorPhysicsController.physicsObjectsManager().physicsObjects().size());

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void widgetOnClick(InputManager inputManager, int entryUid) {
		final var lIsLayerActive = mEditorBrushController.isLayerActive(mEditorActiveLayerUid);
		final var lPhysicsObjectsHashCode = mEditorPhysicsRenderer.hashCode();

		switch (entryUid) {
		case BUTTON_PLACE_FLOOR: {
			if (lIsLayerActive && mEditorBrushController.setAction(EditorPhysicsController.ACTION_FLOORS_CREATE, "Creating Physics Object", lPhysicsObjectsHashCode)) {
				mEditorPhysicsRenderer.startPhysicsObjectCreation();

			}
			return;
		}

		case BUTTON_DELETE_SELECTED: {
			final var lSelectedPhysicsObject = mEditorPhysicsController.selectedPhysicsObject();
			mEditorPhysicsController.deletePhysicsObjectInstance(lSelectedPhysicsObject);
			mEditorPhysicsRenderer.clearSelectedRegion();

		}
			return;

		case BUTTON_DELETE_ALL:
			mEditorPhysicsController.deleteAllPolygons();
			return;

		case BUTTON_SHOW_LAYER:
			mEditorPhysicsRenderer.renderPhysicsObjects(isLayerVisible());
			return;

		case BUTTON_TOGGLE_STATIC:
			if (lIsLayerActive && mSelectedObjectInstance != null) {
				mSelectedObjectInstance.body_isStatic = mToggleStatic.isToggledOn();
			}

			return;

		}

	}

	@Override
	public void widgetOnDataChanged(InputManager inputManager, int entryUid) {

	}

}