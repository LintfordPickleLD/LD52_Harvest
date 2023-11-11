package lintfordpickle.harvest.screens.editor;

import lintfordpickle.harvest.controllers.SceneController;
import lintfordpickle.harvest.controllers.editor.EditorAssetsController;
import lintfordpickle.harvest.controllers.editor.EditorLayerController;
import lintfordpickle.harvest.data.assets.SceneAssetsManager;
import lintfordpickle.harvest.renderers.editor.EditorSceneRenderer;
import net.lintfordLib.editor.ConstantsEditor;
import net.lintfordLib.editor.controllers.EditorBrushController;
import net.lintfordLib.editor.controllers.EditorFileController;
import net.lintfordLib.editor.controllers.EditorHashGridController;
import net.lintfordLib.editor.controllers.IEditorFileControllerListener;
import net.lintfordLib.editor.data.EditorLayerBrush;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordLib.editor.data.BaseSceneSettings;
import net.lintfordLib.editor.renderers.EditorBrushRenderer;
import net.lintfordLib.editor.renderers.EditorHashGridRenderer;
import net.lintfordLib.editor.renderers.UiDockedWindow;
import net.lintfordlib.controllers.camera.CameraMovementController;
import net.lintfordlib.controllers.camera.CameraZoomController;
import net.lintfordlib.controllers.camera.CameraBoundsController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.controllers.geometry.SpatialHashGridController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.geometry.partitioning.GridEntity;
import net.lintfordlib.core.geometry.partitioning.SpatialHashGrid;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class EditorScreen extends BaseGameScreen implements IEditorFileControllerListener {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	// Data
	private EditorLayerBrush mEditorBrush;
	private SpatialHashGrid<GridEntity> mHashGrid;
	private SceneAssetsManager mSceneAssetManager; // TODO: make this generic
	private SceneHeader mSceneHeader;

	// Controllers
	private SceneController mSceneController;
	private CameraZoomController mCameraZoomController;
	private CameraMovementController mCameraMoveController;
	private CameraBoundsController mCameraBoundsController;
	private SpatialHashGridController mSpatialHashGridController;
	private EditorAssetsController mEditorAssetsController;
	private EditorHashGridController mHashGridController;
	private EditorBrushController mEditorBrushController;
	private EditorFileController mEditorFileController;
	private EditorLayerController mEditorLayerController;

	// Renderers
	private UiDockedWindow mEditorGui;
	private EditorBrushRenderer mEditorBrushRenderer;
	private EditorHashGridRenderer mEditorHashGridRenderer;
	private EditorSceneRenderer mSceneRenderer;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public EditorScreen(ScreenManager screenManager, BaseSceneSettings fileInfoSettings) {
		this(screenManager, new SceneHeader(fileInfoSettings));
	}

	public EditorScreen(ScreenManager screenManager, SceneHeader sceneHeader) {
		super(screenManager);

		mSceneHeader = sceneHeader;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void loadResources(ResourceManager resourceManager) {
		super.loadResources(resourceManager);

		resourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetHud.json", ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	@Override
	public void handleInput(LintfordCore core) {
		super.handleInput(core);

		mScreenManager.core().controllerManager().handleInput(mScreenManager.core(), ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);

		mScreenManager.core().controllerManager().update(mScreenManager.core(), ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void createData(LintfordCore core) {
		mEditorBrush = new EditorLayerBrush();
		mHashGrid = new SpatialHashGrid<>(400, 400, 5, 5);

		mSceneAssetManager = new SceneAssetsManager();
	}

	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mSceneController = new SceneController(controllerManager, mSceneHeader, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mCameraMoveController = new CameraMovementController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mCameraZoomController = new CameraZoomController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mCameraBoundsController = new CameraBoundsController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorBrushController = new EditorBrushController(controllerManager, mEditorBrush, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mSpatialHashGridController = new SpatialHashGridController(controllerManager, mHashGrid, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mHashGridController = new EditorHashGridController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorFileController = new EditorFileController(controllerManager, mSceneHeader, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorLayerController = new EditorLayerController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorAssetsController = new EditorAssetsController(controllerManager, mSceneAssetManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorFileController.setCallbackListener(this);
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mSceneController.initialize(core);
		mCameraMoveController.initialize(core);
		mCameraZoomController.initialize(core);
		mCameraBoundsController.initialize(core);
		mHashGridController.initialize(core);
		mSpatialHashGridController.initialize(core);
		mEditorBrushController.initialize(core);
		mEditorFileController.initialize(core);
		mEditorLayerController.initialize(core);
		mEditorAssetsController.initialize(core);
	}

	// ---------------------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mSceneRenderer = new EditorSceneRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);

		mEditorGui = new EditorGui(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorBrushRenderer = new EditorBrushRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorHashGridRenderer = new EditorHashGridRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mSceneRenderer.initialize(core);
		mEditorGui.initialize(core);
		mEditorBrushRenderer.initialize(core);
		mEditorHashGridRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSceneRenderer.loadResources(resourceManager);
		mEditorGui.loadResources(resourceManager);
		mEditorBrushRenderer.loadResources(resourceManager);
		mEditorHashGridRenderer.loadResources(resourceManager);
	}

	// ---------------------------------------------

	@Override
	public void onSave() {
		final var lDataFilename = mSceneHeader.sceneDataFilename();

		// TODO: resolve to be a full path

		mSceneController.saveToFile(lDataFilename);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSceneNameChanged(String newSceneName) {
		mSceneHeader.sceneName(newSceneName);
	}

	@Override
	public void onFilepathChanged(String newBaseSceneDirectory) {

	}

}
