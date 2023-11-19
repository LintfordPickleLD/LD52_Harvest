package lintfordpickle.harvest.data.scene.platforms;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.scene.BaseInstanceManager;
import lintfordpickle.harvest.data.scene.savedefinitions.SceneSaveDefinition;

public class PlatformManager extends BaseInstanceManager {

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

	public PlatformManager() {

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
	public void finalizeAfterLoading() {
		// TODO Auto-generated method stub

	}

}
