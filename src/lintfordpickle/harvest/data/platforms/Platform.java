package lintfordpickle.harvest.data.platforms;

import net.lintford.library.core.geometry.Rectangle;

public class Platform extends Rectangle {

	private static final long serialVersionUID = -2811814489337499879L;

	public PlatformType platformType = PlatformType.Farm;

	public boolean refilPrerequisteFulfilled; // water for farm
	public float timeUntilRefillStarts;

	public boolean isRefillingStock;
	public boolean isStockFull;

	public int stockValueI;
	public float stockValueF;

}
