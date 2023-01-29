package lintfordpickle.harvest.data.ships;

import java.util.ArrayList;
import java.util.List;

public class ShipManager {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private final List<Ship> mShips = new ArrayList<>();

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public List<Ship> ships() {
		return mShips;
	}

	public Ship playerShip() {
		return mShips.get(0);
	}

}
