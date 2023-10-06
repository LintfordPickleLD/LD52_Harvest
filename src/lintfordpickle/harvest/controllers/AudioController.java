package lintfordpickle.harvest.controllers;

import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.audio.AudioFireAndForgetManager;
import net.lintford.library.core.audio.AudioManager;

public class AudioController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Audio Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private AudioManager mAudioManager;
	private AudioFireAndForgetManager mAudioFireAndForgetManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mAudioFireAndForgetManager != null;
	}

	public AudioManager audioManager() {
		return mAudioManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public AudioController(ControllerManager controllerManager, AudioManager audioManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mAudioManager = audioManager;
		mAudioFireAndForgetManager = new AudioFireAndForgetManager(audioManager);
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		mAudioFireAndForgetManager.acquireAudioSources(6);

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void playSound(String soundFxName) {
		mAudioFireAndForgetManager.play(soundFxName);

	}
}
