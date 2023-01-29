package lintfordpickle.harvest.data.platforms;

import net.lintford.library.core.geometry.Rectangle;
import net.lintford.library.core.graphics.sprites.SpriteInstance;

public class Platform extends Rectangle {

	private static final long serialVersionUID = -2811814489337499879L;

	public PlatformType platformType = PlatformType.Farm;

	public boolean refilPrerequisteFulfilled; // water for farm
	public float timeUntilRefillStarts;

	public boolean isRefillingStock;
	public boolean isStockFull;

	public int stockValueI;
	public float stockValueF;

	public String spriteName;
	public SpriteInstance sprite;

	public final int uid;

	public Platform(int uid) {
		this.uid = uid;
	}

}
