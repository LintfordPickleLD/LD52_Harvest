package lintfordpickle.harvest.data.assets;

import net.lintfordlib.core.entities.definitions.BaseDefinition;
import net.lintfordlib.core.graphics.sprites.SpriteContainer;

public class SceneAssetDefinition extends BaseDefinition {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final SpriteContainer sceneSpriteContainer = new SpriteContainer();
	public final SpriteContainer iconSpriteContainer = new SpriteContainer();

	public int propDefaultRadius;
	public int propDefaultWidth;
	public int propDefaultHeight;

}
