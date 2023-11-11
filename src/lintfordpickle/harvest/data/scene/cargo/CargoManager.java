package lintfordpickle.harvest.data.scene.cargo;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.scene.BaseInstanceManager;
import lintfordpickle.harvest.data.scene.SceneSaveDefinition;

public class CargoManager extends BaseInstanceManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private int mCargoUidCounter;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public int getNewCaregoUid() {
		return mCargoUidCounter;
	}

	private final List<Cargo> mCargo = new ArrayList<>();

	public List<Cargo> cargo() {
		return mCargo;
	}

	public Cargo getCargoByEntityUid(int cargoUid) {
		final var lNumCargo = mCargo.size();
		for (var i = 0; i < lNumCargo; i++) {
			if (mCargo.get(i).cargoUid == cargoUid)
				return mCargo.get(i);
		}

		return null;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public CargoManager() {
		mCargoUidCounter = 0;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	public void initializeManager() {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeInTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFromTrackDefinition(SceneSaveDefinition sceneSaveDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finalizeAfterLoading() {
		// TODO Auto-generated method stub

	}

}