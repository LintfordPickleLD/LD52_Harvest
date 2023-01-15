package lintfordpickle.harvest;

import net.lintford.library.ResourceLoader;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.options.DisplayManager;

public class HarvestResourceLoader extends ResourceLoader {

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public HarvestResourceLoader(ResourceManager resourceManager, DisplayManager displayManager) {
		super(resourceManager, displayManager, true);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	protected void resourcesToLoadInBackground() {
		Debug.debugManager().logger().i(getClass().getSimpleName(), "Loading game assets into group: " + ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mResourceManager.addProtectedEntityGroupUid(ConstantsGame.GAME_RESOURCE_GROUP_ID);

		currentStatusMessage("loading resources");

		mResourceManager.textureManager().loadTexturesFromMetafile("res/textures/_meta.json", ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mResourceManager.spriteSheetManager().loadSpriteSheet("/res/spritesheets/core/spritesheetCore.json", LintfordCore.CORE_ENTITY_GROUP_ID);
		mResourceManager.spriteSheetManager().loadSpriteSheetFromMeta("res/spritesheets/_meta.json", ConstantsGame.GAME_RESOURCE_GROUP_ID);

		mResourceManager.fontManager().loadBitmapFont("FONT_NULSCHOCK_12", "res/fonts/fontNulshock12.json");
		mResourceManager.fontManager().loadBitmapFont("FONT_NULSCHOCK_22", "res/fonts/fontNulshock22.json");
	}
}
