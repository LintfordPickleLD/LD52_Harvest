package lintfordpickle.harvest.data.platforms;

import java.util.ArrayList;
import java.util.List;

public class PlatformManager {

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
		mPlatforms.add(platform);
	}

}
