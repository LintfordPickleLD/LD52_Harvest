package lintfordpickle.harvest.data;

import java.util.ArrayList;
import java.util.List;

public class ShipManager {

	private Ship mPlayerShip;
	private final List<Ship> mShips = new ArrayList<>();

	public List<Ship> ships() {
		return mShips;
	}

	public Ship playerShip() {
		return mPlayerShip;
	}

	public void playerShip(Ship newShip) {
		mPlayerShip = newShip;
	}

}
