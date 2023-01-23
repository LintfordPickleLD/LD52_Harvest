package lintfordpickle.harvest.data.ships;

import java.util.ArrayList;
import java.util.List;

public class ShipManager {

	private final List<Ship> mShips = new ArrayList<>();

	public List<Ship> ships() {
		return mShips;
	}

	public Ship playerShip() {
		return mShips.get(0);
	}

}
