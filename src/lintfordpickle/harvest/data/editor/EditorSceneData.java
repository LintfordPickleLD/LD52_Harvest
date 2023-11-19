package lintfordpickle.harvest.data.editor;

import lintfordpickle.harvest.data.editor.physics.EditorPhysicsObjectsManager;
import lintfordpickle.harvest.data.editor.platforms.EditorPlatformManager;
import lintfordpickle.harvest.data.game.GameState;
import lintfordpickle.harvest.data.game.GameState.GameMode;
import lintfordpickle.harvest.data.scene.HashGridManager;
import lintfordpickle.harvest.data.scene.layers.LayersManager;
import lintfordpickle.harvest.data.scene.savedefinitions.SceneSaveDefinition;
import lintfordpickle.harvest.data.scene.ships.ShipManager;

public class EditorSceneData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private int mSceneWidthInPx = 2048;
	private int mSceneHeightInPx = 2048;

	// TODO: Player setup (num players etc.) - in GameState?

	private HashGridManager mHashGridManager;
	private EditorPhysicsObjectsManager mPhysicsManager;
	private GameState mGameState;
	private LayersManager mLayersManager;
	private EditorPlatformManager mPlatformManager;
	private ShipManager mShipManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public float sceneWidthInPx() {
		return mSceneWidthInPx;
	}

	public void sceneWidthInPx(int v) {
		mSceneWidthInPx = v;
	}

	public float sceneHeightInPx() {
		return mSceneHeightInPx;
	}

	public void sceneHeightInPx(int v) {
		mSceneHeightInPx = v;
	}

	public EditorPhysicsObjectsManager physicsObjectsManager() {
		return mPhysicsManager;
	}

	public LayersManager layersManager() {
		return mLayersManager;
	}

	public GameState gameState() {
		return mGameState;
	}

	public EditorPlatformManager platformManager() {
		return mPlatformManager;
	}

	public ShipManager shipManager() {
		return mShipManager;
	}

	public HashGridManager hashGridManager() {
		return mHashGridManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorSceneData() {
		mGameState = new GameState();
		mGameState.startNewGame(GameMode.TimeTrial);

		mLayersManager = new LayersManager();
		mShipManager = new ShipManager();
		mPlatformManager = new EditorPlatformManager();
		mPhysicsManager = new EditorPhysicsObjectsManager();
		mHashGridManager = new HashGridManager();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	// SAVE
	public SceneSaveDefinition getSceneDefinitionToSave() {
		final var lSceneSaveDefinition = new SceneSaveDefinition();

		lSceneSaveDefinition.widthInPixels(mSceneWidthInPx);
		lSceneSaveDefinition.heightInPixels(mSceneHeightInPx);

		mGameState.storeInTrackDefinition(lSceneSaveDefinition);
		mLayersManager.storeInTrackDefinition(lSceneSaveDefinition);
		mPlatformManager.storeInTrackDefinition(lSceneSaveDefinition);
		mShipManager.storeInTrackDefinition(lSceneSaveDefinition);
		mPhysicsManager.storeInTrackDefinition(lSceneSaveDefinition);
		mHashGridManager.storeInTrackDefinition(lSceneSaveDefinition);

		return lSceneSaveDefinition;
	}

	public void createSceneFromSaveDefinition(SceneSaveDefinition sceneSaveDefinition) {
		mGameState.loadFromTrackDefinition(sceneSaveDefinition);
		mLayersManager.loadFromTrackDefinition(sceneSaveDefinition);
		mPlatformManager.loadFromTrackDefinition(sceneSaveDefinition);
		mShipManager.loadFromTrackDefinition(sceneSaveDefinition);
		mPhysicsManager.loadFromTrackDefinition(sceneSaveDefinition);
		mHashGridManager.loadFromTrackDefinition(sceneSaveDefinition);
	}

	public void finalizeAfterLoading() {
		mGameState.finalizeAfterLoading();
		mLayersManager.finalizeAfterLoading();
		mPlatformManager.finalizeAfterLoading(this);
		mShipManager.finalizeAfterLoading();
		mPhysicsManager.finalizeAfterLoading(this);
		mHashGridManager.finalizeAfterLoading();
	}

}
