package lintfordpickle.harvest.screens.editor;

import net.lintfordLib.editor.data.BaseSceneSettings;
import net.lintfordLib.editor.data.scene.SceneHeader;
import net.lintfordlib.screenmanager.MenuEntry;
import net.lintfordlib.screenmanager.MenuScreen;
import net.lintfordlib.screenmanager.ScreenManager;
import net.lintfordlib.screenmanager.entries.MenuDropDownEntry;
import net.lintfordlib.screenmanager.layouts.ListLayout;

public class EditorSceneSelectionScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "Editor Scene Selection";

	private static final int BUTTON_LOAD_ID = 0;
	private static final int BUTTON_CREATE_NEW_ID = 1;
	private static final int BUTTON_BACK_ID = 2;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private BaseSceneSettings mSceneSettings;
	private MenuDropDownEntry<SceneHeader> mSceneFilenameEntries;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public EditorSceneSelectionScreen(ScreenManager screenManager, BaseSceneSettings sceneSettings, boolean enableBackButton) {
		super(screenManager, TITLE);

		mSceneSettings = sceneSettings;
		final var lListLayout = new ListLayout(this);

		mSceneFilenameEntries = new MenuDropDownEntry<SceneHeader>(screenManager, this);
		populateDropDownListWithTrackFilenames(mSceneFilenameEntries);

		final var lCreateNewTrack = new MenuEntry(screenManager, this, "Create New");
		lCreateNewTrack.registerClickListener(this, BUTTON_CREATE_NEW_ID);

		final var lLoadTrack = new MenuEntry(screenManager, this, "Load");
		lLoadTrack.registerClickListener(this, BUTTON_LOAD_ID);

		lListLayout.addMenuEntry(lCreateNewTrack);

		lListLayout.addMenuEntry(MenuEntry.menuSeparator());
		lListLayout.addMenuEntry(mSceneFilenameEntries);
		lListLayout.addMenuEntry(lLoadTrack);

		lListLayout.addMenuEntry(MenuEntry.menuSeparator());

		if (enableBackButton) {
			final var lBackButton = new MenuEntry(screenManager, this, "Back");
			lBackButton.registerClickListener(this, BUTTON_BACK_ID);
			lListLayout.addMenuEntry(lBackButton);
		}

		addLayout(lListLayout);

		if (enableBackButton == false)
			mESCBackEnabled = false; // cannot esc out

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case BUTTON_CREATE_NEW_ID:
			screenManager().addScreen(new EditorScreen(screenManager(), mSceneSettings));
			break;

		case BUTTON_LOAD_ID:
			if (mSceneFilenameEntries.selectedItem() != null) {
				final var lGameFileHeader = mSceneFilenameEntries.selectedItem().value;
				screenManager().addScreen(new EditorScreen(screenManager(), lGameFileHeader));
			}

			break;

		case BUTTON_BACK_ID:
			exitScreen();
			break;
		}
	}

	private void populateDropDownListWithTrackFilenames(MenuDropDownEntry<SceneHeader> pEntry) {
		final var lListOfSceneHeaderFilesInDirectory = mSceneSettings.getListOfHeaderFilesInScenesDirectory();

		final int lSceneHeaderCount = lListOfSceneHeaderFilesInDirectory.size();
		for (int i = 0; i < lSceneHeaderCount; i++) {
			final var lWorldFile = lListOfSceneHeaderFilesInDirectory.get(i);

			final var lSceneHeader = SceneHeader.loadSceneHeaderFileFromFilepath(lWorldFile.getAbsoluteFile().getAbsolutePath());
			lSceneHeader.initialize(lWorldFile.getParentFile().getAbsolutePath(), mSceneSettings);

			final var lNewEntry = pEntry.new MenuEnumEntryItem(lSceneHeader.sceneName(), lSceneHeader);
			pEntry.addItem(lNewEntry);
		}
	}
}
