package lintfordpickle.harvest.data.backgrounds;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<SceneLayer> layers = new ArrayList<>();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public List<SceneLayer> layers() {
		return layers;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SceneManager() {
		loadScenes();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	public void loadScenes() {
		final var lBackgroundOutsideLayer = new SceneLayer();
		final var backgroundInsideLayer = new SceneLayer();
		final var backgroundCollidableLayer = new SceneLayer();

		lBackgroundOutsideLayer.spritesheetName = "SPRITESHEET_SCENE";
		lBackgroundOutsideLayer.spriteName = "BACKGROUNDSKY";

		backgroundInsideLayer.spritesheetName = "SPRITESHEET_SCENE";
		backgroundInsideLayer.spriteName = "BACKGROUNDSCENE";

		backgroundCollidableLayer.spritesheetName = "SPRITESHEET_SCENE";
		backgroundCollidableLayer.spriteName = "BACKGROUNDCOLLIDABLES";

		layers.add(lBackgroundOutsideLayer);
		layers.add(backgroundInsideLayer);
		layers.add(backgroundCollidableLayer);
	}

}
