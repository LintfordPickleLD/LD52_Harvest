package lintfordpickle.harvest.data.backgrounds;

import net.lintford.library.core.graphics.textures.Texture;

public class SceneLayer {

	public String name;

	public String textureName;
	public transient Texture texture;

	public float translation_speed = 1.f;

	public SceneLayer() {

	}

}
