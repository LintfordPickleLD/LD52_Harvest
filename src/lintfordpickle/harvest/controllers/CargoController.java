package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.data.cargo.Cargo;
import lintfordpickle.harvest.data.cargo.CargoManager;
import lintfordpickle.harvest.data.cargo.CargoType;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.core.ControllerManager;

public class CargoController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Cargo Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private CargoManager mCargoManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public CargoManager cargoManager() {
		return mCargoManager;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public CargoController(ControllerManager controllerManager, CargoManager cargoManager, int entityGroupID) {
		super(controllerManager, CONTROLLER_NAME, entityGroupID);

		mCargoManager = cargoManager;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public Cargo createNewCargo(int parentPlatformUid, CargoType cargoType) {
		final var lNewCargo = new Cargo(mCargoManager.getNewCaregoUid(), parentPlatformUid, cargoType);
		mCargoManager.cargo().add(lNewCargo);
		return lNewCargo;
	}

	public void removeCargo(int cargoUid) {
		final var lCargo = mCargoManager.getCargoByEntityUid(cargoUid);
		if (lCargo != null) {

		}
	}

}
