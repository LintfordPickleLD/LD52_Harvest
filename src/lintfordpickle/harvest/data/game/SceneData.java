package lintfordpickle.harvest.data.game;

import lintfordpickle.harvest.data.game.GameState.GameMode;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;
import lintfordpickle.harvest.data.scene.cargo.CargoManager;
import lintfordpickle.harvest.data.scene.layers.LayersManager;
import lintfordpickle.harvest.data.scene.platforms.PlatformManager;
import lintfordpickle.harvest.data.scene.ships.ShipManager;

public class SceneData {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private float mSceneWidthInPx = 2048.f;
	private float mSceneHeightInPx = 2048.f;

	// TODO: Player setup (num players etc.) - in GameState?

	private GameState mGameState;
	private LayersManager mLayersManager;
	private PlatformManager mPlatformManager;
	private CargoManager mCargoManager;
	private ShipManager mShipManager;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public LayersManager layersManager() {
		return mLayersManager;
	}

	public GameState gameState() {
		return mGameState;
	}

	public CargoManager cargoManager() {
		return mCargoManager;
	}

	public PlatformManager platformManager() {
		return mPlatformManager;
	}

	public ShipManager shipManager() {
		return mShipManager;
	}

	public float sceneWidthInPx() {
		return mSceneWidthInPx;
	}

	public void sceneWidthInPx(float v) {
		mSceneWidthInPx = v;
	}

	public float sceneHeightInPx() {
		return mSceneHeightInPx;
	}

	public void sceneHeightInPx(float v) {
		mSceneHeightInPx = v;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneData() {
		mGameState = new GameState();
		mGameState.startNewGame(GameMode.TimeTrial);

		mLayersManager = new LayersManager();
		mShipManager = new ShipManager();
		mCargoManager = new CargoManager();
		mPlatformManager = new PlatformManager();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	// SAVE
	public SceneSaveDefinition getSceneDefinitionToSave() {
		final var lSceneSaveDefinition = new SceneSaveDefinition();

		mGameState.storeInTrackDefinition(lSceneSaveDefinition);
		mLayersManager.storeInTrackDefinition(lSceneSaveDefinition);
		mPlatformManager.storeInTrackDefinition(lSceneSaveDefinition);
		mCargoManager.storeInTrackDefinition(lSceneSaveDefinition);
		mShipManager.storeInTrackDefinition(lSceneSaveDefinition);

		return lSceneSaveDefinition;
	}

	public void createSceneFromSaveDefinition(SceneSaveDefinition sceneSaveDefinition) {
		mGameState.loadFromTrackDefinition(sceneSaveDefinition);
		mLayersManager.loadFromTrackDefinition(sceneSaveDefinition);
		mPlatformManager.loadFromTrackDefinition(sceneSaveDefinition);
		mCargoManager.loadFromTrackDefinition(sceneSaveDefinition);
		mShipManager.loadFromTrackDefinition(sceneSaveDefinition);

	}

	public void finalizeAfterLoading() {
		mGameState.finalizeAfterLoading();
		mLayersManager.finalizeAfterLoading();
		mPlatformManager.finalizeAfterLoading();
		mCargoManager.finalizeAfterLoading();
		mShipManager.finalizeAfterLoading();
	}

}
