package lintfordpickle.harvest.data.game;

import net.lintfordLib.editor.data.scene.FileInfoSettings;

public class SceneHeader {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String FILE_SEPERATOR = System.getProperty("file.separator");

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private boolean mIsValid;
	private String mSceneName;
	private FileInfoSettings mSceneSettings;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public String sceneName() {
		return mSceneName;
	}

	public void sceneName(String trackName) {
		mSceneName = trackName;

		final var lPointOccursAt = mSceneName.lastIndexOf('.');
		if (lPointOccursAt > -1)
			mSceneName = mSceneName.substring(0, lPointOccursAt);

		validateHeader();
	}

	public String sceneDirectory() {
		return mSceneSettings.scenesDirectory;
	}

	public void sceneDirectory(String directoryName) {
		mSceneSettings.scenesDirectory = directoryName;
		validateHeader();
	}

	public String sceneFilename() {
		return mSceneSettings.scenesDirectory + mSceneName + mSceneSettings.sceneFileExtension;
	}

	public boolean isSceneValid() {
		return mIsValid;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SceneHeader(String trackName, FileInfoSettings settings) {
		mSceneName = trackName;
		mSceneSettings = settings;

		validateHeader();
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void validateHeader() {
		// TODO: Validate the directory + filename
		mIsValid = mSceneName != null && mSceneName != null;
	}
}
