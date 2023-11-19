package lintfordpickle.harvest.data.editor.physics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.lintfordlib.core.maths.Vector2f;

// TODO: As with in RazorRunner - I don't think this class belongs in the data.editor subfolder. Check it out when you're less  tired
public class ScenePhysicsObjectSaveDefinition implements Serializable {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 6022580748130640053L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	public float worldCenterX;
	public float worldCenterY;
	public float rotation;

	public final List<Vector2f> localPoints = new ArrayList<>();
}
