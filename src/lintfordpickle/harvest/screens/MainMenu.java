package lintfordpickle.harvest.screens;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.data.players.PlayerManager;
import lintfordpickle.harvest.data.players.ReplayManager;
import lintfordpickle.harvest.screens.game.GameScreen;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class MainMenu extends MenuScreen {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String TITLE = "Main Menu";

	private static final int SCREEN_BUTTON_PLAY_SURVIVAL = 10;
	private static final int SCREEN_BUTTON_PLAY_TIME_TRIAL = 11;
	private static final int SCREEN_BUTTON_EXIT = 15;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ReplayController mReplayController;

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public MainMenu(ScreenManager pScreenManager) {
		super(pScreenManager, TITLE);

		final var lLayout = new ListLayout(this);

		// As we know the game canvas size
		final float lDesiredEntryWidth = 56.f;
		final float lDesiredEntryHeight = 17.f;

		// ---
		final var lPlaySurvivaEntry = new MenuEntry(mScreenManager, lLayout, "Start Survival");
		lPlaySurvivaEntry.desiredWidth(lDesiredEntryWidth);
		lPlaySurvivaEntry.desiredHeight(lDesiredEntryHeight);
		lPlaySurvivaEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_SURVIVAL);
		lPlaySurvivaEntry.setToolTip("Starts a new survival game. You need to deliver as much food to the HQ before the time runs out.");

		final var lPlayTimeEntry = new MenuEntry(mScreenManager, lLayout, "Time Trial");
		lPlayTimeEntry.desiredWidth(lDesiredEntryWidth);
		lPlayTimeEntry.desiredHeight(lDesiredEntryHeight);
		lPlayTimeEntry.registerClickListener(this, SCREEN_BUTTON_PLAY_TIME_TRIAL);
		lPlayTimeEntry.setToolTip("You need ot harvest and deliver food from each of the farms. Fastest time wins.");

		final var lExitEntry = new MenuEntry(mScreenManager, lLayout, "Exit");
		lExitEntry.desiredWidth(lDesiredEntryWidth);
		lExitEntry.desiredHeight(lDesiredEntryHeight);
		lExitEntry.registerClickListener(this, SCREEN_BUTTON_EXIT);

		lLayout.addMenuEntry(lPlaySurvivaEntry);
		lLayout.addMenuEntry(lPlayTimeEntry);
		lLayout.addMenuEntry(MenuEntry.menuSeparator());
		lLayout.addMenuEntry(lExitEntry);

		mLayouts.add(lLayout);

		mIsPopup = false;
		mShowBackgroundScreens = true;
		mESCBackEnabled = false;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		final var lControllerManager = mScreenManager.core().controllerManager();
		mReplayController = (ReplayController) lControllerManager.getControllerByNameRequired(ReplayController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);

		final var lReplayManager = mReplayController.replayManager();
		lReplayManager.loadRecordedGame();
	}

	@Override
	public void draw(LintfordCore core) {
		super.draw(core);

		mPaddingTopNormalized = 300f;
	}

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case SCREEN_BUTTON_PLAY_SURVIVAL: {
			final var lPlayerManager = new PlayerManager();
			lPlayerManager.getPlayer(0).setPlayerControlled(true);

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new GameScreen(screenManager(), lPlayerManager, true));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;
		}

		case SCREEN_BUTTON_PLAY_TIME_TRIAL: {
			// TODO: Default player is created automatically - and will be controlled by the player.
			final var lPlayerManager = new PlayerManager();
			lPlayerManager.getPlayer(0).setRecorder("player.lmp");
			lPlayerManager.getPlayer(0).setPlayerControlled(true);

			final var lReplayManager = mReplayController.replayManager();
			if (lReplayManager.isRecordedGameAvailable()) {
				final var lGhostPlayer = lPlayerManager.addNewPlayer();
				lGhostPlayer.setPlayback(ReplayManager.RecordedGameFilename);
				lGhostPlayer.setPlayerControlled(false);
				lGhostPlayer.isGhostMode(true);
			}

			final var lLoadingScreen = new LoadingScreen(screenManager(), true, new GameScreen(screenManager(), lPlayerManager, true));
			screenManager().createLoadingScreen(new LoadingScreen(screenManager(), true, lLoadingScreen));
			break;
		}

		case SCREEN_BUTTON_EXIT:
			screenManager().exitGame();
			break;
		}
	}
}
