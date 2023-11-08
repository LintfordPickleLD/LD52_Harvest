package lintfordpickle.harvest.controllers;

import lintfordpickle.harvest.ConstantsGame;
import lintfordpickle.harvest.data.platforms.Platform;
import lintfordpickle.harvest.data.platforms.PlatformManager;
import lintfordpickle.harvest.data.platforms.PlatformType;
import net.lintfordlib.ConstantsPhysics;
import net.lintfordlib.controllers.BaseController;
import net.lintfordlib.controllers.core.ControllerManager;
import net.lintfordlib.controllers.physics.PhysicsController;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.physics.PhysicsWorld;
import net.lintfordlib.core.physics.dynamics.RigidBody;
import net.lintfordlib.core.physics.shapes.PolygonShape;

public class LevelController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Level Controller";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private boolean mLevelCreated;
	private PhysicsWorld mPhysicsWorld;
	private PlatformManager mPlatformManager;

	private PhysicsController mPhysicsController;
	private PlatformController mPlatformsController;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean isLevelCreated() {
		return mLevelCreated;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public LevelController(ControllerManager controllerManager, int entityGroupUid) {
		super(controllerManager, CONTROLLER_NAME, entityGroupUid);

		mLevelCreated = false;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore core) {
		super.initialize(core);

		final var lControllerManager = core.controllerManager();

		mPhysicsController = (PhysicsController) lControllerManager.getControllerByNameRequired(PhysicsController.CONTROLLER_NAME, mEntityGroupUid);
		mPlatformsController = (PlatformController) lControllerManager.getControllerByNameRequired(PlatformController.CONTROLLER_NAME, mEntityGroupUid);

		mPlatformManager = mPlatformsController.platformManager();
		mPhysicsWorld = mPhysicsController.world();

		createWorldCollidables();
		createWorldPlatforms();

		mLevelCreated = true;
	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	private void createWorldCollidables() {
		createStaticPolygon(1696, 479, 18, 63, 0);
		createStaticPolygon(1679, 576, 17, 65, 0);
		createStaticPolygon(602, 1312, 128, 128, 0);
		createStaticPolygon(448, 1392, 154, 48, 0);
		createStaticPolygon(1358, 928, 130, 124, 0);
		createStaticPolygon(1698, 1168, 128, 270, 0);
		createStaticPolygon(1954, 1232, 48, 206, 0);
		createStaticPolygon(1696, 1438, 352, 40, 0);
		createStaticPolygon(1183, 543, 130, 289, 0);
		createStaticPolygon(1727, 223, 307, 33, 0);
		createStaticPolygon(1695, 255, 17, 97, 0);
		createStaticPolygon(1503, 256, 192, 20, 0);
		createStaticPolygon(1823, 160, 160, 63, 0);
		createStaticPolygon(1150, 768, 130, 284, 0);
		createStaticPolygon(1150, 1052, 338, 22, 0);
		createStaticPolygon(864, 576, 128, 658, 0);
		createStaticPolygon(320, 224, 128, 899, 0);
		createStaticPolygon(384, 160, 64, 64, 0);
		createStaticPolygon(298, 224, 22, 288, 0);
		createStaticPolygon(448, 512, 20, 128, 0);
		createStaticPolygon(300, 640, 20, 64, 0);
		createStaticPolygon(448, 768, 288, 32, 0);
		createStaticPolygon(194, 800, 125, 96, 0);
		createStaticPolygon(300, 960, 20, 160, 0);
		createStaticPolygon(448, 1088, 20, 33, 0);
		createStaticPolygon(0, 1248, 128, 192, 0);
		createStaticPolygon(128, 1370, 320, 70, 0);
		createStaticPolygon(640, 968, 224, 24, 0);
		createStaticPolygon(864, 358, 128, 58, 0);
		createStaticPolygon(892, 416, 100, 160, 0);
		createStaticPolygon(992, 512, 18, 64, 0);
		createStaticPolygon(992, 512, 18, 64, 0);
		createStaticPolygon(864, 1234, 352, 8, 0);
		createStaticPolygon(1344, 1234, 144, 8, 0);
		createStaticPolygon(1934, 1032, 100, 12, 0);
		createStaticPolygon(1826, 622, 104, 9, 0);
		createStaticPolygon(1480, 0, 186, 256, 0);
		createStaticPolygon(14, 1440, 244, 161, 0);
		createStaticPolygon(931, 1479, 604, 47, 0);
		createStaticPolygon(13, 230, 152, 124, 0);

		// Start platform
		createStaticPolygon(844, 1888, 203, 108, 0);
		createStaticPolygon(844, 1866, 31, 22, 0);
		createStaticPolygon(1344, 1844, 65, 152, 0);
		createStaticPolygon(1248, 1924, 65, 72, 0);
		createStaticPolygon(1120, 1972, 113, 24, 0);

		createStaticPolygon(930, 1438, 607, 40, 0);
		createStaticPolygon(1440, 1328, 80, 110, 0);
		createStaticPolygon(1440, 1234, 32, 94, 0);
		createStaticPolygon(1696, 622, 130, 408, 0);
		createStaticPolygon(1696, 542, 32, 80, 0);

		// walls
		createStaticPolygon(0, 0, 14, 2048, 0);
		createStaticPolygon(2034, 0, 14, 2048, 0);
		createStaticPolygon(0, 0, 2048, 14, 0);
		createStaticPolygon(0, 1996, 2048, 52, 0);

		// Platforms
		createStaticPolygon(529, 1863, 159, 23, 0);
		createStaticPolygon(1472, 1869, 158, 23, 0);
	}

	private void createWorldPlatforms() {
		// Hq
		createPlatform(0, 1748, 574, 160, 48, PlatformType.Warehouse);

		// Farm Platforms
		createPlatform(1, 852, 318, 160, 48, PlatformType.Farm);
		createPlatform(2, 2, 192, 160, 48, PlatformType.Farm);
		createPlatform(3, 142, 1332, 160, 48, PlatformType.Farm);
		createPlatform(4, 1343, 888, 160, 48, PlatformType.Farm);

		// Water Platforms
		createPlatform(5, 529, 1768, 160, 96, PlatformType.Water);
		createPlatform(6, 1470, 1774, 160, 96, PlatformType.Water);
	}

	private void createStaticPolygon(float x, float y, float w, float h, float r) {
		final float worldXOffset = -1024;
		final float worldYOffset = -1024;

		final float lPxToUts = ConstantsPhysics.PixelsToUnits();

		final float staticFriction = 0.8f;
		final float dynamicFriction = 0.5f;

		final var lPolygon = new RigidBody(true);
		lPolygon.addShape(PolygonShape.createBoxShape(w * lPxToUts, h * lPxToUts, 0.f, 1.f, .3f, staticFriction, dynamicFriction));

		lPolygon.categoryBits(ConstantsGame.PHYSICS_WORLD_MASK_WALL);
		lPolygon.maskBits(ConstantsGame.PHYSICS_WORLD_MASK_SHIP | ConstantsGame.PHYSICS_WORLD_MASK_GHOST);

		lPolygon.moveTo((worldXOffset + x + w / 2) * lPxToUts, (worldYOffset + y + h / 2) * lPxToUts);

		mPhysicsWorld.addBody(lPolygon);
	}

	private void createPlatform(int platformUid, float x, float y, float w, float h, PlatformType type) {
		final float worldXOffset = -1024;
		final float worldYOffset = -1024;

		final var lNewPlatform = new Platform(platformUid);
		lNewPlatform.set(worldXOffset + x, worldYOffset + y, w, h);
		lNewPlatform.platformType = type;

		mPlatformManager.addPlatform(lNewPlatform);

		switch (type) {
		case Farm:
			lNewPlatform.stockValueI = 0;
			lNewPlatform.stockValueF = 0.f;
			break;
		case Water:
			lNewPlatform.stockValueF = 1.f;
			break;
		default: // warehouse
			break;
		}
	}

}
