package lintfordpickle.harvest.data.scene;

import lintfordpickle.harvest.data.scene.savedefinitions.SceneSaveDefinition;
import net.lintfordlib.core.debug.Debug;
import net.lintfordlib.core.geometry.partitioning.GridEntity;
import net.lintfordlib.core.geometry.partitioning.SpatialHashGrid;

public class HashGridManager extends BaseInstanceManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private SpatialHashGrid<GridEntity> mHashGrid;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public SpatialHashGrid<GridEntity> hashGrid() {
		return mHashGrid;
	}

	// ---------------------------------------------
	// Constructors
	// ---------------------------------------------

	public HashGridManager() {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void createNewHashGrid(int boundaryWidth, int boundaryHeight, int tilesWide, int tilesHigh) {
		mHashGrid = new SpatialHashGrid<GridEntity>(boundaryWidth, boundaryHeight, tilesWide, tilesHigh);
	}

	// ---------------------------------------------
	// Inherited-Methods
	// ---------------------------------------------

	@Override
	public void initializeManager() {

	}

	@Override
	public void storeInTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		sceneSaveDefinition.gridSettings().updateSettings(mHashGrid);

	}

	@Override
	public void loadFromTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		final var lGridSettings = sceneSaveDefinition.gridSettings();

		if (lGridSettings.isInitialized() == false) {
			Debug.debugManager().logger().w(getClass().getSimpleName(), "Failed to load a valid HashGridSettings object. Default to sane values.");

			lGridSettings.hashGridWidth = 1000;
			lGridSettings.hashGridHeight = 1000;
			lGridSettings.hashGridTilesWide = 5;
			lGridSettings.hashGridTilesHigh = 5;
		}

		mHashGrid = new SpatialHashGrid<GridEntity>(lGridSettings.hashGridWidth, lGridSettings.hashGridHeight, lGridSettings.hashGridTilesWide, lGridSettings.hashGridTilesHigh);

	}

	@Override
	public void finalizeAfterLoading() {

	}

}
