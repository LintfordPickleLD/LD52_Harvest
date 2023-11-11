package lintfordpickle.harvest.data.assets;

import com.google.gson.GsonBuilder;

import net.lintfordlib.core.entities.EntityLocationProvider;
import net.lintfordlib.core.entities.definitions.DefinitionManager;

public class SceneAssetsManager {

	private static final String META_FILENAME = "res/def/assets/_meta.json";

	// ---------------------------------------------
	// Inner-Classes
	// ---------------------------------------------

	public class AssetsDefinitionManager extends DefinitionManager<SceneAssetDefinition> {

		// --------------------------------------
		// Constructor
		// --------------------------------------

		public AssetsDefinitionManager() {
			loadDefinitionsFromMetaFile(META_FILENAME);
		}

		// --------------------------------------
		// Core-Methods
		// --------------------------------------

		@Override
		public void loadDefinitionsFromFolderWatcher(EntityLocationProvider entityLocationProvider) {
			final var lGson = new GsonBuilder().create();
			loadDefinitionsFromFolderWatcherItems(entityLocationProvider, lGson, SceneAssetDefinition.class);
		}

		@Override
		public void loadDefinitionsFromMetaFile(String metaFilepath) {
			final var lGson = new GsonBuilder().create();
			loadDefinitionsFromMetaFileItems(metaFilepath, lGson, SceneAssetDefinition.class);
		}

		@Override
		public void loadDefinitionFromFile(String filepath) {
			final var lGson = new GsonBuilder().create();
			loadDefinitionFromFile(filepath, lGson, SceneAssetDefinition.class);
		}

		@Override
		public void afterDefinitionLoaded(SceneAssetDefinition newDefinition) {

		}

	}

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private AssetsDefinitionManager mDefinitionManager = new AssetsDefinitionManager();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public AssetsDefinitionManager definitionManager() {
		return mDefinitionManager;
	}

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public SceneAssetsManager() {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public SceneAssetInstance getAssetInstanceFromDefinitionName(String definitionName) {
		final var lDefinition = mDefinitionManager.getByName(definitionName);

		return null;
	}

}
