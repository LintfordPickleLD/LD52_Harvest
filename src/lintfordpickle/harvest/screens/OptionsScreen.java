package lintfordpickle.harvest.screens;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_ALIGNMENT;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.AudioOptionsScreen;

public class OptionsScreen extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String SCREEN_TITLE = "Options";

	private static final int BUTTON_AUDIO = 10;
	private static final int BUTTON_GRAPHICS = 11;
	private static final int BUTTON_KEY_BINDS = 12;
	private static final int BUTTON_BACK = 30;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public OptionsScreen(ScreenManager pScreenManager) {
		super(pScreenManager, SCREEN_TITLE);

		final var lLayout = new ListLayout(this);
		lLayout.setDrawBackground(true, ColorConstants.WHITE);
		lLayout.layoutWidth(LAYOUT_WIDTH.HALF);
		lLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		lLayout.showTitle(true);
		lLayout.title("Options");
		lLayout.cropPaddingTop(10.f);
		lLayout.cropPaddingBottom(10.f);

		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lKeyBindsEntry = new MenuEntry(mScreenManager, this, "Key Binds");
		lKeyBindsEntry.desiredWidth(lDesiredEntryWidth);
		lKeyBindsEntry.desiredHeight(lDesiredEntryHeight);
		lKeyBindsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lKeyBindsEntry.registerClickListener(this, BUTTON_KEY_BINDS);

		final var lGraphicsEntry = new MenuEntry(mScreenManager, this, "Graphics");
		lGraphicsEntry.desiredWidth(lDesiredEntryWidth);
		lGraphicsEntry.desiredHeight(lDesiredEntryHeight);
		lGraphicsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lGraphicsEntry.registerClickListener(this, BUTTON_GRAPHICS);

		final var lAudioSettingsEntry = new MenuEntry(mScreenManager, this, "Audio");
		lAudioSettingsEntry.desiredWidth(lDesiredEntryWidth);
		lAudioSettingsEntry.desiredHeight(lDesiredEntryHeight);
		lAudioSettingsEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lAudioSettingsEntry.registerClickListener(this, BUTTON_AUDIO);

		final var lBackEntry = new MenuEntry(mScreenManager, this, "Back");
		lBackEntry.desiredWidth(lDesiredEntryWidth);
		lBackEntry.desiredHeight(lDesiredEntryHeight);
		lBackEntry.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		lBackEntry.registerClickListener(this, BUTTON_BACK);

		lLayout.addMenuEntry(lKeyBindsEntry);
		lLayout.addMenuEntry(lGraphicsEntry);
		lLayout.addMenuEntry(lAudioSettingsEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lBackEntry);

		mPaddingTopNormalized = 30.f;
		INNER_PADDING_W = 50.f;
		mLayoutAlignment = LAYOUT_ALIGNMENT.LEFT;

		mShowBackgroundScreens = false;

		mLayouts.add(lLayout);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void update(LintfordCore core, boolean otherScreenHasFocus, boolean coveredByOtherScreen) {
		super.update(core, otherScreenHasFocus, coveredByOtherScreen);
	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case BUTTON_AUDIO:
			screenManager().addScreen(new AudioOptionsScreen(mScreenManager));
			break;

		case BUTTON_BACK:
			exitScreen();
			break;
		}
	}

}
