package lintfordpickle.harvest.data.scene.layers;

public abstract class SceneBaseLayer {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final int layerUid;

	public int zDepth;
	public String name;
	public float xMovementMod;
	public float yMovementMod;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SceneBaseLayer(int uid) {
		layerUid = uid;
	}

}
