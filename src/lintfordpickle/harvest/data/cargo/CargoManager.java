package lintfordpickle.harvest.data.cargo;

import java.util.ArrayList;
import java.util.List;

public class CargoManager {

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

}