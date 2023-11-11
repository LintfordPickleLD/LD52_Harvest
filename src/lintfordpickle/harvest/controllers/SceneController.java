package lintfordpickle.harvest.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import lintfordpickle.harvest.data.game.SceneData;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.storage.FileUtils;

// TODO: This is the same as the TrackController in RazerRunnner - consider adding to lib to make setup faster.
// TODO: I think this can be made 'generic' 
public class SceneController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Scene Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final SceneHeader mSceneHeader;
	private final SceneData mSceneData;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mSceneData != null;
	}

	public SceneHeader sceneHeader() {
		return mSceneHeader;
	}

	public SceneData sceneData() {
		return mSceneData;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SceneController(ControllerManager controllerManager, SceneHeader sceneHeader, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mSceneHeader = sceneHeader;
		mSceneData = new SceneData(); // We fill *the components* of the SceneData, by deserilizing objects using the SceneHeader.

		if (mSceneHeader != null && mSceneHeader.isSceneValid()) {
			loadTrackDefinitionFromFile(mSceneHeader.sceneFilename());
		} else {
			createNewScene();
		}
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void createNewScene() {

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

		mSceneData.createSceneFromSaveDefinition(lSceneSaveDefinition);
	}

	public void saveToFile(String filename) {
		final var lSceneSaveDefinition = mSceneData.getSceneDefinitionToSave();

		try (Writer writer = new FileWriter(filename)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(lSceneSaveDefinition, writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return;
	}

}
