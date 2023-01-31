package lintfordpickle.harvest.controllers.actionevents;

import java.nio.ByteBuffer;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.controllers.GameStateController;
import lintfordpickle.harvest.controllers.replays.ReplayController;
import lintfordpickle.harvest.data.actionevents.ActionEventFileHeader;
import lintfordpickle.harvest.data.actionevents.ActionEventMap;
import lintfordpickle.harvest.data.actionevents.ActionFrame;
import lintfordpickle.harvest.data.players.ReplayManager;
import net.lintford.library.controllers.actionevents.ActionEventController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.actionevents.ActionEventManager.PlaybackMode;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.time.LogicialCounter;

public class GameActionEventController extends ActionEventController<ActionFrame> {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final int INPUT_MAX_BUFFER_SIZE_IN_BYTES = 5 * 1024 * 1024;

	// ---------------------------------------------

	public static final boolean IS_RECORD_MODE = true;
	public static final String mRecordFilename = "input_new.lms";

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	private GameStateController mGameStateController;
	private ReplayController mReplayController;

	private boolean mFastestTimeOnExitReached;

	private GameActionEventListener mGameActionEventListener;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean fastestTimeOnExitReached() {
		return mFastestTimeOnExitReached;
	}

	public void setActionEventListener(GameActionEventListener listener) {
		mGameActionEventListener = listener;

		mFastestTimeOnExitReached = false;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public GameActionEventController(ControllerManager controllerManager, LogicialCounter frameCounter, int entityGroupUid) {
		super(controllerManager, frameCounter, entityGroupUid);
	}

	// ---------------------------------------------

	@Override
	protected int getHeaderSizeInBytes() {
		return ActionEventFileHeader.HEADER_SIZE_IN_BYTES;
	}

	@Override
	protected int getInputSizeInBytes() {
		return INPUT_MAX_BUFFER_SIZE_IN_BYTES;
	}

	@Override
	protected ActionFrame createActionFrameInstance() {
		return new ActionFrame();
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();
		mReplayController = (ReplayController) lControllerManager.getControllerByNameRequired(ReplayController.CONTROLLER_NAME, ConstantsGame.GAME_RESOURCE_GROUP_ID);
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupUid());

	}

	protected void updateInputActionEvents(LintfordCore core, ActionEventPlayer player) {

		final var lEventActionManager = core.input().eventActionManager();

		// keyboard
		player.currentActionEvents.isThrottleDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_UP_DOWN);
		player.currentActionEvents.isDownDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_DOWN_DOWN);
		player.currentActionEvents.isThrottleLeftDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_LEFT_DOWN);
		player.currentActionEvents.isThrottleRightDown = lEventActionManager.getCurrentControlActionState(ActionEventMap.INPUT_ACTION_EVENT_RIGHT_DOWN);

		// detect changes in keyboard / mouse / gamepad and set the flags
		// (n.b. we don't consider the mouse movement as input by default - but we record the mouse position when the player clicks a mouse button.)

		player.currentActionEvents.setChangeFlags(player.lastActionEvents);

	}

	@Override
	public void onExitingGame() {
		super.onExitingGame();

		mFastestTimeOnExitReached = false;

		final var lGameState = mGameStateController.gameState();
		final var lReplayManager = mReplayController.replayManager();

		final var isPreviousAvailable = lReplayManager.isRecordedGameAvailable();

		final var lMainPlayerUid = 0;
		final var lPlayerScoreCard = lGameState.getScoreCard(lMainPlayerUid);

		final var justGotMoreFood = lPlayerScoreCard.foodDelivered >= lReplayManager.header().numberdeliveredFood();
		final var justGotFasterTime = lGameState.timeAliveInMs <= lReplayManager.header().runtimeInSeconds();

		var shouldWeKeepThisRecording = !isPreviousAvailable || justGotMoreFood && justGotFasterTime;
		if (!shouldWeKeepThisRecording)
			return;

		mFastestTimeOnExitReached = true;

		Debug.debugManager().logger().i(getClass().getSimpleName(), "Best time so far - replay will be recorded");

		final int numActionPlayers = actionEventPlayers().size();
		for (int i = 0; i < numActionPlayers; i++) {
			final var actionPlayer = actionEventPlayers().get(i);
			if (actionPlayer.isPlayerControlled) {
				final var actionManager = actionPlayer.actionEventManager;

				if (actionPlayer.actionEventManager.mode() == PlaybackMode.Record) {
					actionManager.filename(ReplayManager.RecordedGameFilename);
					actionManager.saveToFile();

					Debug.debugManager().logger().i(getClass().getSimpleName(), "  done saving replay");

				}

				return;
			}
		}
	}

	// RECORDING -----------------------------------

	@Override
	protected void saveHeaderToBuffer(ActionFrame currentFrame, ByteBuffer headerBuffer) {
		final var lGameState = mGameStateController.gameState();
		final var lHeaderDefinition = new ActionEventFileHeader();

		final var lMainPlayerUid = 0;
		final var lPlayerScoreCard = lGameState.getScoreCard(lMainPlayerUid);

		lHeaderDefinition.initialize((short) 0, "Just a test", lPlayerScoreCard.foodDelivered, lGameState.timeAliveInMs);

		lHeaderDefinition.saveHeaderToByteBuffer(headerBuffer);
	}

	@Override
	protected void saveActionEvents(ActionFrame frame, ByteBuffer dataBuffer) {
		var controlByte = (byte) 0;

		// control
		if (frame.markEndOfGame)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_END_GAME;

		if (frame._isKeyboardChanged)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_KEYBOARD;

		if (frame._isMouseChanged)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_MOUSE;

		if (frame._isGamepadChanged)
			controlByte |= ActionEventMap.BYTEMASK_CONTROL_GAMEPAD;

		mCurrentTick = frame.frameNumber;
		dataBuffer.putShort((short) frame.frameNumber);
		dataBuffer.put(controlByte);

		if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS) {
			Debug.debugManager().logger().i(getClass().getSimpleName(), "Writing new input frame");
			Debug.debugManager().logger().i(getClass().getSimpleName(), "  frame number: " + frame.frameNumber);
			Debug.debugManager().logger().i(getClass().getSimpleName(), "       control: " + (byte) controlByte);
		}

		// keyboard
		if (frame._isKeyboardChanged) {

			if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS)
				Debug.debugManager().logger().i(getClass().getSimpleName(), "  + keyboard");

			byte keyboardInputValue0 = 0;

			if (frame.isThrottleDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_INPUT_UP;

			if (frame.isDownDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_INPUT_DOWN;

			if (frame.isThrottleLeftDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_INPUT_LEFT;

			if (frame.isThrottleRightDown)
				keyboardInputValue0 |= ActionEventMap.BYTEMASK_INPUT_RIGHT;

			dataBuffer.put(keyboardInputValue0);
		}

		// gamepad
		// --->
	}

	@Override
	protected void saveCustomActionEvents(ActionFrame frame, ByteBuffer dataBuffer) {
		var controlByte = (byte) ActionEventMap.BYTEMASK_CONTROL_PHYSICS_SATE;

		if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS) {
			Debug.debugManager().logger().i(getClass().getSimpleName(), "Writing new custom frame");
			Debug.debugManager().logger().i(getClass().getSimpleName(), "  frame number: " + frame.frameNumber);
			Debug.debugManager().logger().i(getClass().getSimpleName(), "       control: " + (byte) controlByte);
		}

		dataBuffer.putShort((short) frame.frameNumber);
		dataBuffer.put(controlByte);

		dataBuffer.putFloat(frame.positionX);
		dataBuffer.putFloat(frame.positionY);

		dataBuffer.putFloat(frame.velocityX);
		dataBuffer.putFloat(frame.velocityY);

		dataBuffer.putFloat(frame.angle);
		dataBuffer.putFloat(frame.angularV);

	}

	@Override
	protected void saveEndOfFile(ActionFrame frame, ByteBuffer dataBuffer) {
		if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS)
			Debug.debugManager().logger().i(getClass().getSimpleName(), "Marking end of recording");

		frame.markEndOfGame = true;
		saveActionEvents(frame, dataBuffer);
	}

	// PLAYBACK -----------------------------------

	@Override
	protected void readHeaderBuffer(ByteBuffer headerBuffer) {
		final var lLmpVersion = headerBuffer.getShort(); // consume version
		mTotalTicks = headerBuffer.getInt();

		if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS) {
			Debug.debugManager().logger().i(getClass().getSimpleName(), "Reading Header buffer");
			Debug.debugManager().logger().i(getClass().getSimpleName(), "  lmp version: " + lLmpVersion);
			Debug.debugManager().logger().i(getClass().getSimpleName(), " total frames: " + mTotalTicks);
		}

	}

	@Override
	protected void readNextFrame(ByteBuffer dataBuffer, ActionEventPlayer player) {
		// Read the next player input from the 'file' into temp
		final var lRemaining = dataBuffer.limit() - dataBuffer.position();
		if (lRemaining < 2) { // only check for the exisitence of the tick number

			if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS)
				Debug.debugManager().logger().i(getClass().getSimpleName(), "End of input buffer reached - but no marker! Incorrectly saved?");

			return;
		}

		final short nextFrameNumber = dataBuffer.getShort();
		final var nextControlByte = dataBuffer.get();

		if (ConstantsGame.DEBUG_OUTPUT_ACTIONEVENT_LOGS) {
			Debug.debugManager().logger().i(getClass().getSimpleName(), "next frame input on: " + nextFrameNumber);
			Debug.debugManager().logger().i(getClass().getSimpleName(), "            control: " + nextControlByte);
		}

		if ((nextControlByte & ActionEventMap.BYTEMASK_CONTROL_KEYBOARD) == ActionEventMap.BYTEMASK_CONTROL_KEYBOARD) {
			final var nextInput = dataBuffer.get();

			player.tempFrameInput.isThrottleDown = (nextInput & ActionEventMap.BYTEMASK_INPUT_UP) == ActionEventMap.BYTEMASK_INPUT_UP;
			player.tempFrameInput.isThrottleLeftDown = (nextInput & ActionEventMap.BYTEMASK_INPUT_LEFT) == ActionEventMap.BYTEMASK_INPUT_LEFT;
			player.tempFrameInput.isThrottleRightDown = (nextInput & ActionEventMap.BYTEMASK_INPUT_RIGHT) == ActionEventMap.BYTEMASK_INPUT_RIGHT;
		}

		if ((nextControlByte & ActionEventMap.BYTEMASK_CONTROL_PHYSICS_SATE) == ActionEventMap.BYTEMASK_CONTROL_PHYSICS_SATE) {
			player.tempFrameInput.positionX = dataBuffer.getFloat();
			player.tempFrameInput.positionY = dataBuffer.getFloat();

			player.tempFrameInput.velocityX = dataBuffer.getFloat();
			player.tempFrameInput.velocityY = dataBuffer.getFloat();

			player.tempFrameInput.angle = dataBuffer.getFloat();
			player.tempFrameInput.angularV = dataBuffer.getFloat();
		}

		player.tempFrameInput.frameNumber = nextFrameNumber;
	}
}
