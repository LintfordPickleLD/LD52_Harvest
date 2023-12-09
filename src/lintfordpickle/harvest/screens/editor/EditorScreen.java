package lintfordpickle.harvest.screens.editor;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import lintfordpickle.harvest.controllers.editor.EditorAssetsController;
import lintfordpickle.harvest.controllers.editor.EditorLayerController;
import lintfordpickle.harvest.controllers.editor.EditorPhysicsController;
import lintfordpickle.harvest.controllers.editor.EditorSceneController;
import lintfordpickle.harvest.data.assets.SceneAssetsManager;
import lintfordpickle.harvest.data.editor.EditorSceneData;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;
import lintfordpickle.harvest.renderers.editor.EditorPhysicsRenderer;
import lintfordpickle.harvest.renderers.editor.EditorPhysicsSettingsRenderer;
import lintfordpickle.harvest.renderers.editor.EditorSceneRenderer;
import net.lintfordLib.editor.ConstantsEditor;
import net.lintfordLib.editor.controllers.EditorBrushController;
import net.lintfordLib.editor.controllers.EditorCameraMovementController;
import net.lintfordLib.editor.controllers.EditorFileController;
import net.lintfordLib.editor.controllers.EditorHashGridController;
import net.lintfordLib.editor.controllers.EditorPhysicsSettingsController;
import net.lintfordLib.editor.controllers.IEditorFileControllerListener;
import net.lintfordLib.editor.data.BaseSceneSettings;
import net.lintfordLib.editor.data.EditorLayerBrush;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordLib.editor.renderers.EditorBrushRenderer;
import net.lintfordLib.editor.renderers.EditorHashGridRenderer;
import net.lintfordLib.editor.renderers.UiDockedWindow;
import net.lintfordlib.controllers.camera.CameraBoundsController;
import net.lintfordlib.controllers.camera.CameraZoomController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.controllers.geometry.SpatialHashGridController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.ResourceManager;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.storage.FileUtils;
import net.lintfordlib.renderers.debug.DebugCameraBoundsDrawer;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.screens.BaseGameScreen;

public class EditorScreen extends BaseGameScreen implements IEditorFileControllerListener {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	// Data
	private EditorLayerBrush mEditorBrush;
	private EditorSceneData mEditorSceneData;
	private SceneAssetsManager mSceneAssetManager; // TODO: make this generic
	private SceneHeader mSceneHeader;

	// Controllers
	private CameraZoomController mCameraZoomController;
	private EditorCameraMovementController mCameraMoveController;
	private CameraBoundsController mCameraBoundsController;
	private SpatialHashGridController mSpatialHashGridController;
	private EditorSceneController mEditorSceneController;
	private EditorPhysicsSettingsController mEditorPhysicsSettingsController;
	private EditorAssetsController mEditorAssetsController;
	private EditorPhysicsController mEditorPhysicsController;
	private EditorHashGridController mHashGridController;
	private EditorBrushController mEditorBrushController;
	private EditorFileController mEditorFileController;
	private EditorLayerController mEditorLayerController;

	// Renderers
	private UiDockedWindow mEditorGui;
	private EditorBrushRenderer mEditorBrushRenderer;
	private EditorHashGridRenderer mEditorHashGridRenderer;
	private EditorPhysicsSettingsRenderer mEditorPhysicsSettingsRenderer;
	private EditorSceneRenderer mSceneRenderer;
	private EditorPhysicsRenderer mEditorPhysicsRenderer;
	private DebugCameraBoundsDrawer mDebugCameraBoundsDrawer;

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
		// This creates an empty scene
		mEditorSceneData = new EditorSceneData();

		if (mSceneHeader != null && mSceneHeader.isSceneValid()) {
			loadTrackDefinitionFromFile(mSceneHeader.sceneDataFilepath());
		}
		mEditorSceneData.finalizeAfterLoading();

		mEditorBrush = new EditorLayerBrush();
		mSceneAssetManager = new SceneAssetsManager();
	}

	public void loadTrackDefinitionFromFile(String filename) {
		final var lGson = new GsonBuilder().create();

		String lSceneRawFileContents = null;
		SceneSaveDefinition lSceneSaveDefinition = null;

		try {
			lSceneRawFileContents = FileUtils.loadString(filename);
			lSceneSaveDefinition = lGson.fromJson(lSceneRawFileContents, SceneSaveDefinition.class);

		} catch (JsonSyntaxException ex) {
			Debug.debugManager().logger().printException(getClass().getSimpleName(), ex);
		}

		if (lSceneSaveDefinition == null) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "There was an error reading the scene save definition file (" + filename + ")");
			return;
		}

		mEditorSceneData.createSceneFromSaveDefinition(lSceneSaveDefinition);
	}

	// ---------------------------------------------

	@Override
	protected void createControllers(ControllerManager controllerManager) {
		mCameraMoveController = new EditorCameraMovementController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mCameraZoomController = new CameraZoomController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mCameraBoundsController = new CameraBoundsController(controllerManager, mGameCamera, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorPhysicsSettingsController = new EditorPhysicsSettingsController(controllerManager, mEditorSceneData.physicsSettingsManager().physicsSettings(), ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mSpatialHashGridController = new SpatialHashGridController(controllerManager, mEditorSceneData.hashGridManager().hashGrid(), ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorSceneController = new EditorSceneController(controllerManager, mSceneHeader, mEditorSceneData, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorPhysicsController = new EditorPhysicsController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorBrushController = new EditorBrushController(controllerManager, mEditorBrush, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mHashGridController = new EditorHashGridController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorFileController = new EditorFileController(controllerManager, mSceneHeader, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorLayerController = new EditorLayerController(controllerManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorAssetsController = new EditorAssetsController(controllerManager, mSceneAssetManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);

		mEditorFileController.setCallbackListener(this);
	}

	@Override
	protected void initializeControllers(LintfordCore core) {
		mEditorSceneController.initialize(core);
		mCameraMoveController.initialize(core);
		mCameraZoomController.initialize(core);
		mCameraBoundsController.initialize(core);
		mHashGridController.initialize(core);
		mEditorPhysicsSettingsController.initialize(core);
		mSpatialHashGridController.initialize(core);
		mEditorBrushController.initialize(core);
		mEditorFileController.initialize(core);
		mEditorLayerController.initialize(core);
		mEditorAssetsController.initialize(core);
		mEditorPhysicsController.initialize(core);
	}

	// ---------------------------------------------

	@Override
	protected void createRenderers(LintfordCore core) {
		mSceneRenderer = new EditorSceneRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);

		mEditorGui = new EditorGui(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorBrushRenderer = new EditorBrushRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorHashGridRenderer = new EditorHashGridRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorPhysicsSettingsRenderer = new EditorPhysicsSettingsRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mEditorPhysicsRenderer = new EditorPhysicsRenderer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
		mDebugCameraBoundsDrawer = new DebugCameraBoundsDrawer(mRendererManager, ConstantsEditor.EDITOR_RESOURCE_GROUP_ID);
	}

	@Override
	protected void initializeRenderers(LintfordCore core) {
		mSceneRenderer.initialize(core);
		mEditorGui.initialize(core);
		mEditorBrushRenderer.initialize(core);
		mEditorHashGridRenderer.initialize(core);
		mDebugCameraBoundsDrawer.initialize(core);
		mEditorPhysicsSettingsRenderer.initialize(core);
		mEditorPhysicsRenderer.initialize(core);
	}

	@Override
	protected void loadRendererResources(ResourceManager resourceManager) {
		mSceneRenderer.loadResources(resourceManager);
		mEditorGui.loadResources(resourceManager);
		mEditorBrushRenderer.loadResources(resourceManager);
		mEditorHashGridRenderer.loadResources(resourceManager);
		mEditorPhysicsSettingsRenderer.loadResources(resourceManager);
		mEditorPhysicsRenderer.loadResources(resourceManager);
		mDebugCameraBoundsDrawer.loadResources(resourceManager);
	}

	// ---------------------------------------------

	@Override
	public void onSave() {
		final var lDataFilename = mSceneHeader.sceneDataFilepath();
		mEditorSceneController.saveToFile(lDataFilename);
		mSceneHeader.saveSceneHeaderFile();
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSceneNameChanged(String newSceneName) {

	}

	@Override
	public void onFilepathChanged(String newBaseSceneDirectory) {

	}

}
