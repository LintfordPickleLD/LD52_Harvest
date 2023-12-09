package lintfordpickle.harvest.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lintfordpickle.harvest.data.scene.SceneData;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.debug.Debug;

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

	public SceneController(ControllerManager controllerManager, SceneHeader header, SceneData data, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mSceneHeader = header;
		mSceneData = data;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		mSceneData.finalizeAfterLoading();

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void saveToFile(String filename) {
		if (filename == null || filename.length() == 0) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Unable to save the scene data into " + filename);
			return;
		}

		// TODO: This isn't the correct place to be doing logical on the file system.

		final var lSaveDirectory = new File(filename);
		final var lParentDirectory = lSaveDirectory.getParentFile();

		if (lParentDirectory.exists() == false) {
			if (lParentDirectory.mkdirs() == false) {
				Debug.debugManager().logger().e(getClass().getSimpleName(), "Unable to save the scene data into " + filename);
				return;
			}
		}

		// -------

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
