package lintfordpickle.harvest.data.scene.platforms;

import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.graphics.sprites.SpriteInstance;

public class Platform extends Rectangle {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final long serialVersionUID = -2811814489337499879L;

	public PlatformType platformType = PlatformType.Farm;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	public boolean refilPrerequisteFulfilled; // water for farm
	public float timeUntilRefillStarts;

	public boolean isRefillingStock;
	public boolean isStockFull;

	public float cargoLocalCreationPointX;
	public float cargoLocalCreationPointY;

	// used in time_trial to track if platform closed
	// TODO: The platforms are very non-generic
	public boolean isWatered;
	public boolean isHarvested;

	public int stockValueI;
	public float stockValueF;

	public String spriteName;
	public SpriteInstance platformSprite;
	public SpriteInstance containerSprite;

	public final int uid;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Platform(int uid) {
		this.uid = uid;

		isWatered = false;
		isHarvested = false;
	}
}
