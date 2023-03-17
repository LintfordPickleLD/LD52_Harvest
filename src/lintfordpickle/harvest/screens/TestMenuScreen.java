package lintfordpickle.harvest.screens;

import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.screenmanager.DualMenuScreen;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.ScreenManagerConstants.FILLTYPE;
import net.lintford.library.screenmanager.ScreenManagerConstants.LAYOUT_WIDTH;
import net.lintford.library.screenmanager.entries.MenuDropDownEntry;
import net.lintford.library.screenmanager.entries.MenuEnumEntry;
import net.lintford.library.screenmanager.entries.MenuInputEntry;
import net.lintford.library.screenmanager.entries.MenuSliderEntry;
import net.lintford.library.screenmanager.entries.MenuToggleEntry;
import net.lintford.library.screenmanager.layouts.HorizontalLayout;
import net.lintford.library.screenmanager.layouts.ListLayout;

public class TestMenuScreen extends DualMenuScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int BUTTON_GENERATE = 0;

	// --------------------------------------
	// Variables
	// --------------------------------------

	// Left Layout
	private MenuInputEntry mLeftInputEntry00;
	private MenuEnumEntry mLeftInputEntry01;
	private MenuEnumEntry mLeftInputEntry02;
	private MenuDropDownEntry<String> mLeftInputEntry03;
	private MenuEntry mLeftInputEntry04;
	private MenuInputEntry mLeftInputEntry05;
	private MenuToggleEntry mLeftInputEntry06;
	private MenuEnumEntry mLeftInputEntry07;
	private MenuSliderEntry mLeftInputEntry08;
	private MenuEnumEntry mLeftInputEntry09;
	private MenuEnumEntry mLeftInputEntry10;

	// Right Layout
	private MenuInputEntry mRightInputEntry00;
	private MenuEnumEntry mRightInputEntry01;
	private MenuEnumEntry mRightInputEntry02;
	private MenuDropDownEntry<String> mRightInputEntry03;
	private MenuEntry mRightInputEntry04;
	private MenuInputEntry mRightInputEntry05;
	private MenuToggleEntry mRightInputEntry06;
	private MenuEnumEntry mRightInputEntry07;
	private MenuSliderEntry mRightInputEntry08;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public TestMenuScreen(ScreenManager pScreenManager) {
		super(pScreenManager, "TEST SCREEN");

		final var lLeftLayout = new ListLayout(this);
		lLeftLayout.setDrawBackground(true, ColorConstants.MenuPanelSecondaryColor);
		lLeftLayout.title("Left Column");
		lLeftLayout.setEntryOffsetY(32.f);
		lLeftLayout.showTitle(true);
		lLeftLayout.cropPaddingTop(10.f);
		lLeftLayout.cropPaddingBottom(10.f);
		lLeftLayout.layoutWidth(LAYOUT_WIDTH.FULL);
		lLeftLayout.layoutFillType(FILLTYPE.TAKE_WHATS_NEEDED);

		createLeftLayout(lLeftLayout);

		final var lRightLayout = new ListLayout(this);
		lRightLayout.setDrawBackground(true, ColorConstants.MenuPanelSecondaryColor);
		lRightLayout.title("Right Column");
		lRightLayout.setEntryOffsetY(32.f);
		lRightLayout.showTitle(true);
		lRightLayout.cropPaddingTop(10.f);
		lRightLayout.cropPaddingBottom(10.f);

		createRightLayout(lRightLayout);

		final var lRightBottomLayout = new ListLayout(this);
		lRightBottomLayout.setDrawBackground(true, ColorConstants.MenuPanelSecondaryColor);
		lRightBottomLayout.title("Right Column");
		lRightBottomLayout.setEntryOffsetY(32.f);
		lRightBottomLayout.showTitle(true);
		lRightBottomLayout.cropPaddingTop(10.f);
		lRightBottomLayout.cropPaddingBottom(10.f);

		createRightLayout(lRightBottomLayout);

		// footer
		final var lFooterLayout = new HorizontalLayout(this);

		final var lGenerateWorldButton = new MenuEntry(screenManager(), this, "Generate New");
		lGenerateWorldButton.registerClickListener(this, BUTTON_GENERATE);
		lGenerateWorldButton.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		lFooterLayout.addMenuEntry(lGenerateWorldButton);

		// Add the three layouts
		addLayout(lLeftLayout);
		// addLayout(lFooterLayout);

		mShowBackgroundScreens = false;

		mSelectedEntryIndex = 0;
		mSelectedLayoutIndex = 0;

		mRightLayouts.add(lRightLayout);
		mRightLayouts.add(lRightBottomLayout);

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	private void createLeftLayout(ListLayout layout) {
		mLeftInputEntry04 = new MenuEntry(mScreenManager, this, "Push Button");
		mLeftInputEntry04.registerClickListener(this, BUTTON_GENERATE);

		mLeftInputEntry00 = new MenuInputEntry(mScreenManager, this);
		mLeftInputEntry00.label("World Name");
		mLeftInputEntry00.setDefaultText("Test Text", true);
		mLeftInputEntry00.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry03 = new MenuDropDownEntry<>(mScreenManager, this, "drop");
		mLeftInputEntry03.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry03.addItem(mLeftInputEntry03.new MenuEnumEntryItem("Item 1", "test item 1"));
		mLeftInputEntry03.addItem(mLeftInputEntry03.new MenuEnumEntryItem("Item 2", "test item 2"));
		mLeftInputEntry03.addItem(mLeftInputEntry03.new MenuEnumEntryItem("Item 3", "test item 3"));
		mLeftInputEntry03.addItem(mLeftInputEntry03.new MenuEnumEntryItem("Item 4", "test item 4"));

		mLeftInputEntry05 = new MenuInputEntry(screenManager(), this);
		mLeftInputEntry05.label("Seed");

		mLeftInputEntry05.inputString("rnand rext 01");
		mLeftInputEntry05.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry01 = new MenuEnumEntry(screenManager(), this, "Multiplayer");
		mLeftInputEntry01.addItems("Closed", "Friends", "Public");
		mLeftInputEntry01.setToolTip("Select whether or not other people can join your world:\n\n[CLOSED] A single player game\n\n[FRIENDS] Only people in your friends list can join your games.\n\n[PUBLIC] Your game is open for anyone with your IP to join");
		mLeftInputEntry01.showInfoButton(true);
		mLeftInputEntry01.setButtonsEnabled(true);
		mLeftInputEntry01.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry02 = new MenuEnumEntry(screenManager(), this, "Game Mode");
		mLeftInputEntry02.addItems("Survival", "Creative");
		mLeftInputEntry02.setToolTip("[Survival] In survival mode its you against the elements, just as the game was meant to be played.\n[Creative] In creative mode, you have all the blocks and objects available to you at any time. You also have access to the creative console.");
		mLeftInputEntry02.showInfoButton(true);
		mLeftInputEntry02.setButtonsEnabled(true);
		mLeftInputEntry02.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry08 = new MenuSliderEntry(mScreenManager, this);
		mLeftInputEntry08.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		mLeftInputEntry08.label("Slider Test");
		mLeftInputEntry08.setBounds(0, 10, 1);
		mLeftInputEntry08.setValue(3);

		mLeftInputEntry07 = new MenuEnumEntry(screenManager(), this, "Metals");
		mLeftInputEntry07.setToolTip("Specify the frequency of metals on the map");
		mLeftInputEntry07.addItems("Sparse", "Normal", "Abundant");
		mLeftInputEntry07.setButtonsEnabled(true);
		mLeftInputEntry07.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry06 = new MenuToggleEntry(screenManager(), this);
		mLeftInputEntry06.entryText("Gems Spawn in Dirt?");
		mLeftInputEntry06.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry09 = new MenuEnumEntry(screenManager(), this, "Mtest");
		mLeftInputEntry09.setToolTip("Specify the frequency of metals on the map");
		mLeftInputEntry09.addItems("Sparse", "Normal", "Abundant");
		mLeftInputEntry09.setButtonsEnabled(true);
		mLeftInputEntry09.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mLeftInputEntry10 = new MenuEnumEntry(screenManager(), this, "test");
		mLeftInputEntry10.setToolTip("Specify the frequency of metals on the map");
		mLeftInputEntry10.addItems("Sparse", "Normal", "Abundant");
		mLeftInputEntry10.setButtonsEnabled(true);
		mLeftInputEntry10.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		layout.addMenuEntry(mLeftInputEntry04);
		layout.addMenuEntry(mLeftInputEntry00);
		layout.addMenuEntry(mLeftInputEntry05);
		layout.addMenuEntry(mLeftInputEntry03);
		layout.addMenuEntry(mLeftInputEntry01);
		layout.addMenuEntry(mLeftInputEntry02);
		layout.addMenuEntry(mLeftInputEntry07);
		layout.addMenuEntry(mLeftInputEntry06);
		layout.addMenuEntry(mLeftInputEntry08);
		layout.addMenuEntry(mLeftInputEntry09);
		layout.addMenuEntry(mLeftInputEntry10);
	}

	private void createRightLayout(ListLayout layout) {
		mRightInputEntry04 = new MenuEntry(mScreenManager, this, "Push Button");
		mRightInputEntry04.registerClickListener(this, BUTTON_GENERATE);

		mRightInputEntry00 = new MenuInputEntry(mScreenManager, this);
		mRightInputEntry00.label("World Name");
		mRightInputEntry00.setDefaultText("Test Text", true);
		mRightInputEntry00.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mRightInputEntry03 = new MenuDropDownEntry<>(mScreenManager, this, "big list");
		mRightInputEntry03.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 1", "test item 1"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 2", "test item 2"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 3", "test item 3"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 4", "test item 4"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 5", "test item 5"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 6", "test item 6"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 7", "test item 7"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 8", "test item 8"));
		mRightInputEntry03.addItem(mRightInputEntry03.new MenuEnumEntryItem("Item 9", "test item 9"));

		mRightInputEntry05 = new MenuInputEntry(screenManager(), this);
		mRightInputEntry05.label("Seed");

		mRightInputEntry05.inputString("rnand rext 01");
		mRightInputEntry05.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mRightInputEntry01 = new MenuEnumEntry(screenManager(), this, "Multiplayer");
		mRightInputEntry01.addItems("Closed", "Friends", "Public");
		mRightInputEntry01.setToolTip("Select whether or not other people can join your world:\n\n[CLOSED] A single player game\n\n[FRIENDS] Only people in your friends list can join your games.\n\n[PUBLIC] Your game is open for anyone with your IP to join");
		mRightInputEntry01.showInfoButton(true);
		mRightInputEntry01.setButtonsEnabled(true);
		mRightInputEntry01.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mRightInputEntry02 = new MenuEnumEntry(screenManager(), this, "Game Mode");
		mRightInputEntry02.addItems("Survival", "Creative");
		mRightInputEntry02.setToolTip("[Survival] In survival mode its you against the elements, just as the game was meant to be played.\n[Creative] In creative mode, you have all the blocks and objects available to you at any time. You also have access to the creative console.");
		mRightInputEntry02.showInfoButton(true);
		mRightInputEntry02.setButtonsEnabled(true);
		mRightInputEntry02.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mRightInputEntry08 = new MenuSliderEntry(mScreenManager, this);
		mRightInputEntry08.horizontalFillType(FILLTYPE.FILL_CONTAINER);
		mRightInputEntry08.label("Slider Test");
		mRightInputEntry08.setBounds(0, 10, 1);
		mRightInputEntry08.setValue(3);

		mRightInputEntry07 = new MenuEnumEntry(screenManager(), this, "Metals");
		mRightInputEntry07.setToolTip("Specify the frequency of metals on the map");
		mRightInputEntry07.addItems("Sparse", "Normal", "Abundant");
		mRightInputEntry07.setButtonsEnabled(true);
		mRightInputEntry07.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		mRightInputEntry06 = new MenuToggleEntry(screenManager(), this);
		mRightInputEntry06.entryText("Gems Spawn in Dirt?");
		mRightInputEntry06.horizontalFillType(FILLTYPE.FILL_CONTAINER);

		layout.addMenuEntry(mRightInputEntry04);
		layout.addMenuEntry(mRightInputEntry00);
		layout.addMenuEntry(mRightInputEntry05);
		layout.addMenuEntry(mRightInputEntry03);
		layout.addMenuEntry(mRightInputEntry01);
		layout.addMenuEntry(mRightInputEntry02);
		layout.addMenuEntry(mRightInputEntry07);
		layout.addMenuEntry(mRightInputEntry06);
		layout.addMenuEntry(mRightInputEntry08);
	}

}
