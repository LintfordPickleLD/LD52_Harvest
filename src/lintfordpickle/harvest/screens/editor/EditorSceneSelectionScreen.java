package lintfordpickle.harvest.screens.editor;

import net.lintfordLib.editor.data.BaseSceneSettings;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordLib.editor.screens.BaseEditorSceneSelectionScreen;
import net.lintfordlib.screenmanager.ScreenManager;

public class EditorSceneSelectionScreen extends BaseEditorSceneSelectionScreen {

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorSceneSelectionScreen(ScreenManager screenManager, BaseSceneSettings sceneSettings, boolean enableBackButton) {
		super(screenManager, sceneSettings, enableBackButton);
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected void onCreateNewScene() {
		screenManager().addScreen(new EditorScreen(mScreenManager, sceneSettings()));

	}

	@Override
	protected void OnLoadScene(SceneHeader sceneHeader) {
		screenManager().addScreen(new EditorScreen(mScreenManager, sceneHeader));

	}

}
