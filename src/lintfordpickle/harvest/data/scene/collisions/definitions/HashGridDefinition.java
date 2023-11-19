package lintfordpickle.harvest.data.scene.collisions.definitions;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import net.lintfordlib.core.geometry.partitioning.GridEntity;
import net.lintfordlib.core.geometry.partitioning.SpatialHashGrid;

// TODO: This is a generic class for all games -> move it to the lib
public class HashGridDefinition implements Serializable {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final long serialVersionUID = 6171527068937184017L;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	@SerializedName(value = "Width")
	public int hashGridWidth;

	@SerializedName(value = "Height")
	public int hashGridHeight;

	@SerializedName(value = "TilesWide")
	public int hashGridTilesWide;

	@SerializedName(value = "TilesHigh")
	public int hashGridTilesHigh;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean isInitialized() {
		final var lValuesNotPresent = hashGridWidth == 0 || hashGridHeight == 0 || hashGridTilesWide == 0 || hashGridTilesHigh == 0;
		return !lValuesNotPresent;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void updateSettings(SpatialHashGrid<GridEntity> hashgrid) {
		hashGridWidth = hashgrid.boundaryWidth();
		hashGridHeight = hashgrid.boundaryHeight();

		hashGridTilesWide = hashgrid.numTilesWide();
		hashGridTilesHigh = hashgrid.numTilesHigh();
	}

}