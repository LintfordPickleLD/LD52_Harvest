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

		return lTextureLayer;
	}
}
