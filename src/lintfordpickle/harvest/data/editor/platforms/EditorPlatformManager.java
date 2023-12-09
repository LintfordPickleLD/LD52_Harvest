package lintfordpickle.harvest.data.editor.platforms;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.editor.BaseEditorInstanceManager;
import lintfordpickle.harvest.data.editor.EditorSceneData;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;
import lintfordpickle.harvest.data.scene.platforms.Platform;

public class EditorPlatformManager extends BaseEditorInstanceManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<Platform> mPlatforms = new ArrayList<>();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public List<Platform> platforms() {
		return mPlatforms;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public EditorPlatformManager() {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void addPlatform(Platform platform) {
		if (mPlatforms.contains(platform) == false)
			mPlatforms.add(platform);
	}

	public void removePlatform(Platform platform) {
		if (mPlatforms.contains(platform))
			mPlatforms.remove(platform);
	}

	@Override
	public void initializeManager() {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeInTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFromTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finalizeAfterLoading(EditorSceneData editorSceneInstance) {
		// TODO Auto-generated method stub
		
	}

}
