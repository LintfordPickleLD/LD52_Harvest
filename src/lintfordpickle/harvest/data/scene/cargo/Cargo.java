package lintfordpickle.harvest.data.scene.cargo;

public class Cargo {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	public int cargoUid;
	public int parentPlatformUid;
	public CargoType cargoType;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Cargo(int cargoUid, int parentPlatformUid, CargoType cargoType) {
		this.cargoUid = cargoUid;
		this.cargoType = cargoType;
		this.parentPlatformUid = parentPlatformUid;
	}
}
