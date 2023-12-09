package lintfordpickle.harvest.data.scene.savedefinitions;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

// TODO: This is a generic class for all games -> move it to the lib
public class HashGridSaveManager implements Serializable {

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

}