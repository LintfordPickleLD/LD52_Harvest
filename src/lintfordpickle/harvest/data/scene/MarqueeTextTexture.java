package lintfordpickle.harvest.data.scene;

import java.util.ArrayList;
import java.util.List;

import net.lintford.library.core.geometry.Rectangle;
import net.lintford.library.core.graphics.rendertarget.RenderTarget;
import net.lintford.library.core.maths.Vector2f;

public class MarqueeTextTexture {

	private final Rectangle srcRectangle = new Rectangle();
	private final List<Vector2f> dstRectangle = new ArrayList<>();

	RenderTarget offscreenTexture;

	public MarqueeTextTexture() {

	}

}
