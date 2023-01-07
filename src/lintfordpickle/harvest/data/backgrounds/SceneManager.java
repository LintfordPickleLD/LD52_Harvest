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
		final var lBackgroundBackgroundFarLayer = new SceneLayer();
		final var lBackgroundBackgroundMidLayer = new SceneLayer();
		final var lBackgroundCollidableLayer = new SceneLayer();

		lBackgroundOutsideLayer.textureName = "TEXTURE_SCENE_SKY";
		lBackgroundOutsideLayer.translation_speed = 1.f;

		lBackgroundBackgroundFarLayer.textureName = "TEXTURE_SCENE_BACKGROUND_FAR";
		lBackgroundBackgroundFarLayer.translation_speed = 1.f;

		lBackgroundBackgroundMidLayer.textureName = "TEXTURE_SCENE_BACKGROUND_MID";
		lBackgroundBackgroundMidLayer.translation_speed = 1.f;

		lBackgroundCollidableLayer.textureName = "TEXTURE_SCENE_COLLIDABLE";
		lBackgroundCollidableLayer.translation_speed = 1.f;

		layers.add(lBackgroundOutsideLayer);
		layers.add(lBackgroundBackgroundFarLayer);
		layers.add(lBackgroundBackgroundMidLayer);
		layers.add(lBackgroundCollidableLayer);
	}

}
