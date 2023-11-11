package lintfordpickle.harvest.data.editor;

import lintfordpickle.harvest.data.scene.layers.SceneBaseLayer;
import net.lintfordlib.renderers.windows.components.UiListBoxItem;

public class LayerListBoxItem extends UiListBoxItem {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SceneBaseLayer mLayer;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SceneBaseLayer layer() {
		return mLayer;
	}

	public void layer(SceneBaseLayer layer) {
		mLayer = layer;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LayerListBoxItem(int itemUid) {
		super(itemUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

}
