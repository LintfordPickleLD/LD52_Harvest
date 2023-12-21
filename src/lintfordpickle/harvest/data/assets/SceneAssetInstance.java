package lintfordpickle.harvest.data.assets;

import net.lintfordlib.core.entities.Entity;
import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;

public class SceneAssetInstance extends Entity {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final int TEXTURE_UNLOADED = 0;
	public static final int TEXTURE_LOADED = 1;
	public static final int TEXTURE_FAILED = 2;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public String definitionName;
	public transient SceneAssetDefinition definition;
	public SpriteInstance spriteInstance;

	public final Rectangle destRect = new Rectangle();

	// TODO: This is used in a few places, make a texturestatus enum and cache object (loaded, unloaded, failed)
	public int spriteStatus;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneAssetInstance(int entityUid) {
		super(entityUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialize(SceneAssetDefinition assetDefinition, float x, float y, float rotation, float width, float height, float radius) {
		if (assetDefinition == null) {
			definitionName = null;
			definition = null;
			return;
		}

		definitionName = assetDefinition.definitionName();
		definition = assetDefinition;
	}
}
