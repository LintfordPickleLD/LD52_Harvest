package lintfordpickle.harvest.data.scene.backgrounds;

import net.lintfordlib.core.graphics.textures.Texture;

public class SceneLayer {

	public String name;

	public String textureName;
	public transient Texture texture;

	public float centerX;
	public float centerY;
	public float widthInPx;
	public float heightInPx;
	public boolean isForeground;

	public float translation_speed = 1.f;

	public SceneLayer() {

	}

}
