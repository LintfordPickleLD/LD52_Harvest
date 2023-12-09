package lintfordpickle.harvest.data.scene.savedefinitions;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import net.lintfordlib.core.maths.Vector2f;

public class ScenePhysicsSettingsSaveManager implements Serializable {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 1433319979369040506L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	@SerializedName(value = "Gravity")
	public final Vector2f gravity = new Vector2f();

	@SerializedName(value = "GridWidthUnits")
	public float hashGridWidthInUnits;

	@SerializedName(value = "GridHeightUnits")
	public float hashGridHeightInUnits;

	@SerializedName(value = "GridCellsWide")
	public int hashGridCellsWide;

	@SerializedName(value = "GridCellsHigh")
	public int hashGridCellsHigh;

}
