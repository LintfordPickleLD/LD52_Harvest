package lintfordpickle.harvest.data.platforms;

import net.lintford.library.core.geometry.Rectangle;

public class Platform extends Rectangle {

	private static final long serialVersionUID = -2811814489337499879L;

	public PlatformType platformType = PlatformType.Farm;

	public float refillTimer;

}
