package lintfordpickle.harvest.data.scene.backgrounds;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<SceneLayer> layers = new ArrayList<>();

	// always centered on 0,0
	private float mSceneWidthInPx = 2048.f;
	private float mSceneHeightInPx = 2048.f;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public List<SceneLayer> layers() {
		return layers;
	}

	public float sceneWidthInPx() {
		return mSceneWidthInPx;
	}

	public float sceneHeightInPx() {
		return mSceneHeightInPx;
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
		lBackgroundOutsideLayer.centerX = 0.f;
		lBackgroundOutsideLayer.centerY = 0.f;
		lBackgroundOutsideLayer.widthInPx = mSceneWidthInPx;
		lBackgroundOutsideLayer.heightInPx = mSceneHeightInPx;

		lBackgroundBackgroundFarLayer.textureName = "TEXTURE_SCENE_BACKGROUND_FAR";
		lBackgroundBackgroundFarLayer.translation_speed = 1.f;
		lBackgroundBackgroundFarLayer.centerX = 0.f;
		lBackgroundBackgroundFarLayer.centerY = 0.f;
		lBackgroundBackgroundFarLayer.widthInPx = mSceneWidthInPx;
		lBackgroundBackgroundFarLayer.heightInPx = mSceneHeightInPx;

		lBackgroundBackgroundMidLayer.textureName = "TEXTURE_SCENE_BACKGROUND_MID";
		lBackgroundBackgroundMidLayer.translation_speed = 1.f;
		lBackgroundBackgroundMidLayer.centerX = 0.f;
		lBackgroundBackgroundMidLayer.centerY = 0.f;
		lBackgroundBackgroundMidLayer.widthInPx = mSceneWidthInPx;
		lBackgroundBackgroundMidLayer.heightInPx = mSceneHeightInPx;

		lBackgroundCollidableLayer.textureName = "TEXTURE_SCENE_COLLIDABLE";
		lBackgroundCollidableLayer.translation_speed = 1.f;
		lBackgroundCollidableLayer.centerX = 0.f;
		lBackgroundCollidableLayer.centerY = 0.f;
		lBackgroundCollidableLayer.widthInPx = mSceneWidthInPx;
		lBackgroundCollidableLayer.heightInPx = mSceneHeightInPx;

		layers.add(lBackgroundOutsideLayer);
		layers.add(lBackgroundBackgroundFarLayer);
		layers.add(lBackgroundBackgroundMidLayer);
		layers.add(lBackgroundCollidableLayer);
	}

}
