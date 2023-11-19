package lintfordpickle.harvest.data.editor.physics;

import java.util.ArrayList;
import java.util.List;

import lintfordpickle.harvest.data.scene.collisions.GridEntityType;
import net.lintfordlib.core.geometry.Rectangle;
import net.lintfordlib.core.geometry.partitioning.GridEntity;
import net.lintfordlib.core.geometry.partitioning.SpatialHashGrid;
import net.lintfordlib.core.maths.Vector2f;

public class EditorPhysicsObjectInstance extends GridEntity {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public transient final EditorPolygonPoint a = new EditorPolygonPoint(this);
	public transient final EditorPolygonPoint b = new EditorPolygonPoint(this);
	public transient final EditorPolygonPoint c = new EditorPolygonPoint(this);
	public transient final EditorPolygonPoint d = new EditorPolygonPoint(this);

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final List<Vector2f> worldPoints = new ArrayList<>();
	public final Rectangle aabb = new Rectangle();
	public float bcx;
	public float bcy;
	public float wcx;
	public float wcy;
	public float angle;
	public float radius;

	public boolean hasPhysicsBody;
	public boolean body_isStatic;
	public float body_restitution;
	public float body_density;
	public float body_staticFriction;
	public float body_dynamicFriction;

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorPhysicsObjectInstance(int regionUid) {
		super(regionUid, GridEntityType.GRID_ENTITY_TYPE_PHYSICS_OBJECTS);

		worldPoints.add(a.worldPosition);
		worldPoints.add(b.worldPosition);
		worldPoints.add(c.worldPosition);
		worldPoints.add(d.worldPosition);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialize(float worldX, float worldY, float angleInRadians) {
		wcx = worldX;
		wcy = worldY;

		a.worldPosition.set(wcx, wcy);
		b.worldPosition.set(wcx, wcy);
		c.worldPosition.set(wcx, wcy);
		d.worldPosition.set(wcx, wcy);

		angle = angleInRadians;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void setWorldVertices(Vector2f newA, Vector2f newB, Vector2f newC, Vector2f newD) {
		a.worldPosition.set(newA);
		b.worldPosition.set(newB);
		c.worldPosition.set(newC);
		d.worldPosition.set(newD);

		recalculateBounds();
	}

	public boolean intersectAndGetHeight(float x, float y) {

		final var p_a = a.worldPosition;
		final var p_b = b.worldPosition;
		final var p_c = c.worldPosition;
		final var p_d = d.worldPosition;

		{ // tri a
			float fAB = (y - p_a.y) * (p_b.x - p_a.x) - (x - p_a.x) * (p_b.y - p_a.y);
			float fBC = (y - p_b.y) * (p_c.x - p_b.x) - (x - p_b.x) * (p_c.y - p_b.y);
			float fCA = (y - p_c.y) * (p_a.x - p_c.x) - (x - p_c.x) * (p_a.y - p_c.y);

			if (fAB * fBC > 0 && fBC * fCA > 0) {
				return true;
			}
		}

		{ // tri b
			float fAC = (y - p_a.y) * (p_c.x - p_a.x) - (x - p_a.x) * (p_c.y - p_a.y);
			float fCD = (y - p_c.y) * (p_d.x - p_c.x) - (x - p_c.x) * (p_d.y - p_c.y);
			float fDA = (y - p_d.y) * (p_a.x - p_d.x) - (x - p_d.x) * (p_a.y - p_d.y);

			if (fAC * fCD > 0 && fCD * fDA > 0) {
				return true;
			}
		}

		return false;
	}

	public void recalculateBounds() {
		var minX = Float.MAX_VALUE;
		var minY = Float.MAX_VALUE;
		var maxX = -Float.MAX_VALUE;
		var maxY = -Float.MAX_VALUE;

		final var lNumVertices = worldPoints.size();

		if (lNumVertices == 0)
			return;

		bcx = 0;
		bcy = 0;

		for (int i = 0; i < lNumVertices; i++) {
			final var p = worldPoints.get(i);

			if (p.x < minX)
				minX = p.x;
			if (p.x > maxX)
				maxX = p.x;
			if (p.y < minY)
				minY = p.y;
			if (p.y > maxY)
				maxY = p.y;

			bcx += p.x;
			bcy += p.y;

		}

		aabb.set(minX, minY, maxX - minX, maxY - minY);

		bcx /= (float) lNumVertices;
		bcy /= (float) lNumVertices;

		for (int i = 0; i < lNumVertices; i++) {
			final var p = worldPoints.get(i);

			final var tx = p.x - bcx;
			final var ty = p.y - bcy;

			float dst = Vector2f.dst2(tx, ty);
			if (dst > radius)
				radius = dst;

		}

		radius = (float) Math.sqrt(radius);
	}

	@Override
	public void fillEntityBounds(SpatialHashGrid<?> grid) {
		minX = grid.getCellIndexX((int) aabb.left());
		minY = grid.getCellIndexY((int) aabb.top());

		maxX = grid.getCellIndexX((int) aabb.right());
		maxY = grid.getCellIndexY((int) aabb.bottom());
	}

	@Override
	public boolean isGridCacheOld(SpatialHashGrid<?> grid) {
		final var newMinX = grid.getCellIndexX((int) aabb.left());
		final var newMinY = grid.getCellIndexY((int) aabb.top());

		final var newMaxX = grid.getCellIndexX((int) aabb.right());
		final var newMaxY = grid.getCellIndexY((int) aabb.bottom());

		if (newMinX == minX && newMinY == minY && newMaxX == maxX && newMaxY == maxY)
			return false; // early out

		return true;
	}
}
