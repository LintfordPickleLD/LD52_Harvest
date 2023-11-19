package lintfordpickle.harvest;

import net.lintfordLib.editor.data.BaseSceneSettings;
import net.lintfordlib.core.AppResources;

public class GameSceneSettings extends BaseSceneSettings {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String DataFileExentsion = ".scene";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameSceneSettings(AppResources appResources) {
		super(appResources);

		sceneDataExtension(DataFileExentsion);
	}
}
