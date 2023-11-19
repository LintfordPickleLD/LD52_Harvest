package lintfordpickle.harvest.data.scene.layers.savedefinitions;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import lintfordpickle.harvest.data.scene.layers.SceneTextureLayer;
import net.lintfordlib.core.graphics.textures.Texture;

public class SceneTextureLayerSaveDefinition extends BaseSceneLayerSaveDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 7569238335511621943L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public Texture texture;
	public String textureName;
	public String texturePath;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneTextureLayerSaveDefinition() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public SceneBaseLayer getSceneLayer() {
		final var lTextureLayer = new SceneTextureLayer(layerUid);
		lTextureLayer.zDepth = layerZDepth;
		lTextureLayer.name = layerName;

		lTextureLayer.setTextureName(textureName);
		lTextureLayer.setTextureFilepath(texturePath);

		lTextureLayer.translationSpeedModX = translationSpeedModX;
		lTextureLayer.translationSpeedModY = translationSpeedModY;

		lTextureLayer.centerX = centerX;
		lTextureLayer.centerY = centerY;

		lTextureLayer.scaleX = scaleX;
		lTextureLayer.scaleY = scaleY;

		// ensure some sane defaults:
		if (lTextureLayer.scaleX <= 0.f)
			lTextureLayer.scaleX = 1.f;

		if (lTextureLayer.scaleY <= 0.f)
			lTextureLayer.scaleY = 1.f;

		return lTextureLayer;
	}
}
