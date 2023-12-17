package lintfordpickle.harvest.data.editor;

import lintfordpickle.harvest.data.editor.physics.EditorPhysicsObjectsManager;
import lintfordpickle.harvest.data.editor.platforms.EditorPlatformManager;
import lintfordpickle.harvest.data.game.GameState;
import lintfordpickle.harvest.data.game.GameState.GameMode;
import lintfordpickle.harvest.data.scene.HashGridManager;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;
import lintfordpickle.harvest.data.scene.SceneSettingsManager;
import lintfordpickle.harvest.data.scene.layers.LayersManager;
import lintfordpickle.harvest.data.scene.physics.PhysicsSettingsManager;
import lintfordpickle.harvest.data.scene.ships.ShipManager;

public class EditorSceneData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneSettingsManager mSceneSettingsManager;
	private GameState mGameState;
	private HashGridManager mHashGridManager;
	private LayersManager mLayersManager;
	private EditorPlatformManager mPlatformManager;
	private ShipManager mShipManager;
	private PhysicsSettingsManager mPhysicsSettingsManager;
	private EditorPhysicsObjectsManager mPhysicsManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SceneSettingsManager sceneSettingsManager() {
		return mSceneSettingsManager;
	}

	public GameState gameState() {
		return mGameState;
	}

	public HashGridManager hashGridManager() {
		return mHashGridManager;
	}

	public LayersManager layersManager() {
		return mLayersManager;
	}

	public EditorPlatformManager platformManager() {
		return mPlatformManager;
	}

	public ShipManager shipManager() {
		return mShipManager;
	}

	public PhysicsSettingsManager physicsSettingsManager() {
		return mPhysicsSettingsManager;
	}

	public EditorPhysicsObjectsManager physicsObjectsManager() {
		return mPhysicsManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorSceneData() {
		mGameState = new GameState();
		mGameState.startNewGame(GameMode.TimeTrial);

		mSceneSettingsManager = new SceneSettingsManager();
		mHashGridManager = new HashGridManager();
		mLayersManager = new LayersManager();
		mPlatformManager = new EditorPlatformManager();
		mShipManager = new ShipManager();
		mPhysicsSettingsManager = new PhysicsSettingsManager();
		mPhysicsManager = new EditorPhysicsObjectsManager();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public SceneSaveDefinition getSceneDefinitionToSave() {
		final var lSceneSaveDefinition = new SceneSaveDefinition();

		mSceneSettingsManager.storeInTrackDefinition(lSceneSaveDefinition);
		mGameState.storeInTrackDefinition(lSceneSaveDefinition);
		mHashGridManager.storeInTrackDefinition(lSceneSaveDefinition);
		mLayersManager.storeInTrackDefinition(lSceneSaveDefinition);
		mPlatformManager.storeInTrackDefinition(lSceneSaveDefinition);
		mShipManager.storeInTrackDefinition(lSceneSaveDefinition);
		mPhysicsSettingsManager.storeInTrackDefinition(lSceneSaveDefinition);
		mPhysicsManager.storeInTrackDefinition(lSceneSaveDefinition);

		return lSceneSaveDefinition;
	}

	public void createSceneFromSaveDefinition(SceneSaveDefinition sceneSaveDefinition) {
		mSceneSettingsManager.loadFromTrackDefinition(sceneSaveDefinition);
		mGameState.loadFromTrackDefinition(sceneSaveDefinition);
		mHashGridManager.loadFromTrackDefinition(sceneSaveDefinition);
		mLayersManager.loadFromTrackDefinition(sceneSaveDefinition);
		mPlatformManager.loadFromTrackDefinition(sceneSaveDefinition);
		mShipManager.loadFromTrackDefinition(sceneSaveDefinition);
		mPhysicsSettingsManager.loadFromTrackDefinition(sceneSaveDefinition);
		mPhysicsManager.loadFromTrackDefinition(sceneSaveDefinition);
	}

	public void finalizeAfterLoading() {
		mSceneSettingsManager.finalizeAfterLoading();
		mGameState.finalizeAfterLoading();
		mHashGridManager.finalizeAfterLoading();
		mLayersManager.finalizeAfterLoading();
		mPlatformManager.finalizeAfterLoading(this);
		mShipManager.finalizeAfterLoading();
		mPhysicsSettingsManager.finalizeAfterLoading();
		mPhysicsManager.finalizeAfterLoading(this);
	}

}
