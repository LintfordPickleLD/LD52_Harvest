package lintfordpickle.harvest.screens.editor;

import lintfordpickle.harvest.controllers.editor.EditorLayerController;
import lintfordpickle.harvest.data.game.SceneData;
import lintfordpickle.harvest.data.game.SceneHeader;
import net.lintfordLib.editor.ConstantsEditor;
import net.lintfordLib.editor.controllers.EditorBrushController;
import net.lintfordLib.editor.controllers.EditorFileController;
import net.lintfordLib.editor.controllers.EditorHashGridController;
import net.lintfordLib.editor.data.EditorLayerBrush;
import net.lintfordLib.editor.data.scene.FileInfoSettings;
import net.lintfordLib.editor.renderers.EditorBrushRenderer;
import net.lintfordLib.editor.renderers.EditorHashGridRenderer;
import net.lintfordLib.editor.renderers.UiDockedWindow;
import net.lintfordlib.controllers.camera.CameraMovementController;
import net.lintfordlib.controllers.camera.CameraZoomController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.controllers.geometry.SpatialHashGridController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.geometry.partitioning.GridEntity;
import net.lintfordlib.core.geometry.partitioning.SpatialHashGrid;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class EditorScreen extends BaseGameScreen {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	// Data
	private EditorLayerBrush mEditorBrush;
	private SpatialHashGrid<GridEntity> mHashGrid;

	private SceneHeader mSceneHeader;
	private SceneData mSceneData;

	// Controllers
	private CameraZoomController mCameraZoomController;
	private CameraMovementController mCameraMoveController;
	private SpatialHashGridController mSpatialHashGridController;
	private EditorHashGridController mHashGridController;
	private EditorBrushController mEditorBrushController;
	private EditorFileController mEditorFileController;
	private EditorLayerController mEditorLayerController;

	// Renderers
	private UiDockedWindow mEditorGui;
	private EditorBrushRenderer mEditorBrushRenderer;
	private EditorHashGridRenderer mEditorHashGridRenderer;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public EditorScreen(ScreenManager screenManager) {
		super(screenManager);

	}

	// TODO: add possibility to accept a SceneHeader in the constructor

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		resourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetHud.json", ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createData(LintfordCore core) {

		// TODO: Create the game data objects from a scene, and not from the editor.

		mEditorBrush = new EditorLayerBrush();
		mHashGrid = new SpatialHashGrid<>(400, 400, 5, 5);

		// TODO:
		mSceneHeader = new SceneHeader("", new FileInfoSettings());
		mSceneData = new SceneData();

	}

	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mCameraMoveController = new CameraMovementController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mCameraZoomController = new CameraZoomController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorBrushController = new EditorBrushController(controllerManager, mEditorBrush, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mSpatialHashGridController = new SpatialHashGridController(controllerManager, mHashGrid, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mHashGridController = new EditorHashGridController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorFileController = new EditorFileController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorLayerController = new EditorLayerController(controllerManager, mSceneData, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mCameraMoveController.initialize(core);
		mCameraZoomController.initialize(core);
		mHashGridController.initialize(core);
		mSpatialHashGridController.initialize(core);
		mEditorBrushController.initialize(core);
		mEditorFileController.initialize(core);
		mEditorLayerController.initialize(core);
	}

	// ---------------------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mEditorGui = new EditorGui(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorBrushRenderer = new EditorBrushRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorHashGridRenderer = new EditorHashGridRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mEditorGui.initialize(core);
		mEditorBrushRenderer.initialize(core);
		mEditorHashGridRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mEditorGui.loadResources(resourceManager);
		mEditorBrushRenderer.loadResources(resourceManager);
		mEditorHashGridRenderer.loadResources(resourceManager);
	}

}
