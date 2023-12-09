package lintfordpickle.harvest.data.editor.physics;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.editor.BaseEditorInstanceManager;
import lintfordpickle.harvest.data.editor.EditorSceneData;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;
import net.lintfordlib.core.geometry.partitioning.GridEntity;
import net.lintfordlib.core.maths.Vector2f;

public class EditorPhysicsObjectsManager extends BaseEditorInstanceManager {

	// --------------------------------------
	// Constants
	// --------------------------------------

	// --------------------------------------
	// Variables
	// --------------------------------------

	private List<EditorPhysicsObjectInstance> mPhysicsObjects = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<EditorPhysicsObjectInstance> physicsObjects() {
		return mPhysicsObjects;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorPhysicsObjectsManager() {
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addPhyiscsObjectInstance(EditorPhysicsObjectInstance physicsObjectInstance) {
		if (mPhysicsObjects.contains(physicsObjectInstance))
			return;

		mPhysicsObjects.add(physicsObjectInstance);
	}

	public void removePhysicsObjectInstance(EditorPhysicsObjectInstance physicsObjectInstance) {
		if (!mPhysicsObjects.contains(physicsObjectInstance))
			return;

		mPhysicsObjects.remove(physicsObjectInstance);
	}

	// ---------------------------------------------
	// Inherited-Methods
	// ---------------------------------------------

	@Override
	public void initializeManager() {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeInTrackDefinition(SceneSaveDefinition sceneDefinition) {
		final var lPhysicsObjectsToSave = sceneDefinition.physicsObjects().physicsObjects;
		lPhysicsObjectsToSave.clear();

		final int lNumFloorDefinitions = mPhysicsObjects.size();
		for (int i = 0; i < lNumFloorDefinitions; i++) {
			final var lPhysicsObject = mPhysicsObjects.get(i);
			final var lPhysicsObjectToSave = new ScenePhysicsObjectSaveDefinition();

			lPhysicsObjectToSave.worldCenterX = lPhysicsObject.wcx;
			lPhysicsObjectToSave.worldCenterY = lPhysicsObject.wcy;
			lPhysicsObjectToSave.rotation = lPhysicsObject.angle;

			lPhysicsObjectToSave.localPoints.add(new Vector2f(lPhysicsObject.a.worldPosition.x - lPhysicsObject.wcx, lPhysicsObject.a.worldPosition.y - lPhysicsObject.wcy));
			lPhysicsObjectToSave.localPoints.add(new Vector2f(lPhysicsObject.b.worldPosition.x - lPhysicsObject.wcx, lPhysicsObject.b.worldPosition.y - lPhysicsObject.wcy));
			lPhysicsObjectToSave.localPoints.add(new Vector2f(lPhysicsObject.c.worldPosition.x - lPhysicsObject.wcx, lPhysicsObject.c.worldPosition.y - lPhysicsObject.wcy));
			lPhysicsObjectToSave.localPoints.add(new Vector2f(lPhysicsObject.d.worldPosition.x - lPhysicsObject.wcx, lPhysicsObject.d.worldPosition.y - lPhysicsObject.wcy));

			lPhysicsObjectsToSave.add(lPhysicsObjectToSave);
		}

	}

	@Override
	public void loadFromTrackDefinition(SceneSaveDefinition sceneDefinition) {
		final var lFloorDefinitions = sceneDefinition.physicsObjects().physicsObjects;
		final var lNumFloorDefinitions = lFloorDefinitions.size();
		for (int i = 0; i < lNumFloorDefinitions; i++) {
			final var lFloorSaveDefinition = lFloorDefinitions.get(i);
			final var lNewPhyiscsObjectInstance = new EditorPhysicsObjectInstance(GridEntity.getNewEntityUid());

			// @formatter:off
			lNewPhyiscsObjectInstance.initialize(
					lFloorSaveDefinition.worldCenterX, 
					lFloorSaveDefinition.worldCenterY, 
					lFloorSaveDefinition.rotation);
			
			final var wcx = lFloorSaveDefinition.worldCenterX;
			final var wcy = lFloorSaveDefinition.worldCenterY;
			
			lNewPhyiscsObjectInstance.setWorldVertices(
					lFloorSaveDefinition.localPoints.get(0).add(wcx, wcy), 
					lFloorSaveDefinition.localPoints.get(1).add(wcx, wcy), 
					lFloorSaveDefinition.localPoints.get(2).add(wcx, wcy), 
					lFloorSaveDefinition.localPoints.get(3).add(wcx, wcy));

			mPhysicsObjects.add(lNewPhyiscsObjectInstance);
		}

	}

	@Override
	public void finalizeAfterLoading(EditorSceneData editorSceneInstance) {
		final var lHashGrid = editorSceneInstance.hashGridManager().hashGrid();

		final int lNumPhysicsObjectInstances = mPhysicsObjects.size();
		for (int i = 0; i < lNumPhysicsObjectInstances; i++) {
			final var lPhysicsObject = mPhysicsObjects.get(i);

			lHashGrid.addEntity(lPhysicsObject);
		}
	}

}
