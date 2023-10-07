package lintfordpickle.harvest.renderers.trails;

import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.core.maths.MathHelper;

public class TrailRendererComponent {

	// --------------------------------------
	// Constrants
	// --------------------------------------

	public static final int NUM_VERTS_PER_SEGMENT = 4;

	private TrailVertex[] mVertices;

	// --------------------------------------
	// Variable
	// --------------------------------------

	private int mSegmentsUsed;
	private int mNumSegments = 25;
	private int mSegmentLength = 16;
	private float mLastX;
	private float mLastY;
	private float mTrailWidth = 1.f;
	private float mFadeoutTime = 1000;
	private float mR, mG, mB, mA;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public void color(float r, float g, float b, float a) {
		mR = MathHelper.clamp(r, 0, 1);
		mG = MathHelper.clamp(g, 0, 1);
		mB = MathHelper.clamp(b, 0, 1);
		mA = MathHelper.clamp(a, 0, 1);
	}

	public TrailVertex[] vertices() {
		return mVertices;
	}

	public int vertexCount() {
		return mSegmentsUsed;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public TrailRendererComponent() {
		final int lNumVertices = mNumSegments * 4;
		mVertices = new TrailVertex[lNumVertices];
		for (int i = 0; i < lNumVertices; i++) {
			mVertices[i] = new TrailVertex();
		}

		mR = 0.17f;
		mG = 0.13f;
		mB = 0.87f;
		mA = 1.f;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void updateTrail(LintfordCore core, float positionX, float positionY, float heading) {
		if (mSegmentsUsed == 0) {
			final float forwardVectorX = (float) Math.cos(heading);
			final float forwardVectorY = (float) Math.sin(heading);

			// add 2 vertices, left and right of heading
			mVertices[0].x = positionX + forwardVectorY * mTrailWidth;
			mVertices[0].y = positionY + -forwardVectorX * mTrailWidth;

			mVertices[0].u = 0;
			mVertices[0].v = 0;
			mVertices[0].timeSinceStart = 1000; // start faded out

			mVertices[1].x = positionX - forwardVectorY * mTrailWidth;
			mVertices[1].y = positionY - -forwardVectorX * mTrailWidth;

			mVertices[1].u = 0;
			mVertices[1].v = 1;
			mVertices[1].timeSinceStart = 1000; // start faded out

			mSegmentsUsed = 1;
		}

		final float dirX = positionX - mLastX;
		final float dirY = positionY - mLastY;
		final float dirLength = (float) Math.sqrt(dirX * dirX + dirY * dirY);

		// if the distance between our newPosition and the last segment is greater than our asssigned
		// segmentLength, we have to delete the oldest segment and make a new one at the other end
		if (dirLength > mSegmentLength) {
			// if we are already at the maximum segments, then we need to delete the last one
			if (mSegmentsUsed >= mNumSegments) {
				shiftDownSegments();
			} else {
				mSegmentsUsed++;
			}

			final int curSegmentIndex = mSegmentsUsed - 1;
			// Update the latest segment with the new position
			final int vert0x = curSegmentIndex * 4;
			mVertices[vert0x].x = mVertices[vert0x - 2].x;
			mVertices[vert0x].y = mVertices[vert0x - 2].y;
			mVertices[vert0x].u = 0;
			mVertices[vert0x].v = 0;
			mVertices[vert0x].timeSinceStart = 0;

			final int vert1i = curSegmentIndex * 4 + 1;
			mVertices[vert1i].x = mVertices[vert1i - 2].x;
			mVertices[vert1i].y = mVertices[vert1i - 2].y;
			mVertices[vert1i].u = 0;
			mVertices[vert1i].v = 1;
			mVertices[vert1i].timeSinceStart = 0;

			mLastX = positionX;
			mLastY = positionY;
		}

		// If we are not further than a segment's length, but further than the minimum needed to change something,
		// (We don't want to recalculate everything when our target didn't move from the last segment)

		final float normalizedX = dirX / dirLength;
		final float normalizedY = dirY / dirLength;

		final float normalX = normalizedY;
		final float normalY = -normalizedX;

		final int curSegmentIndex = mSegmentsUsed - 1;
		final int currentSegmentOffset = curSegmentIndex * 4;
		final int vert0i = currentSegmentOffset + 2;
		mVertices[vert0i].x = positionX + normalX * mTrailWidth;
		mVertices[vert0i].y = positionY + normalY * mTrailWidth;
		mVertices[vert0i].u = 1;
		mVertices[vert0i].v = 0;
		mVertices[vert0i].timeSinceStart = 0;

		final int vert1i = currentSegmentOffset + 3;
		mVertices[vert1i].x = positionX - normalX * mTrailWidth;
		mVertices[vert1i].y = positionY - normalY * mTrailWidth;
		mVertices[vert1i].u = 1;
		mVertices[vert1i].v = 1;
		mVertices[vert1i].timeSinceStart = 0;

		updateVertices(core);
	}

	private void shiftDownSegments() {
		final int lNumVertices = mNumSegments * NUM_VERTS_PER_SEGMENT;
		for (int i = 0; i < lNumVertices - NUM_VERTS_PER_SEGMENT; i += NUM_VERTS_PER_SEGMENT) {
			final int lSegmentTo = i;
			final int lSegmentFrom = i + NUM_VERTS_PER_SEGMENT;

			mVertices[lSegmentTo + 0].copyFrom(mVertices[lSegmentFrom + 0]);
			mVertices[lSegmentTo + 1].copyFrom(mVertices[lSegmentFrom + 1]);
			mVertices[lSegmentTo + 2].copyFrom(mVertices[lSegmentFrom + 2]);
			mVertices[lSegmentTo + 3].copyFrom(mVertices[lSegmentFrom + 3]);
		}
	}

	private void updateVertices(LintfordCore core) {
		final float lElapsedMs = (float) core.gameTime().elapsedTimeMilli();

		final float lLifeTime = mFadeoutTime;
		for (int i = 0; i < mSegmentsUsed * NUM_VERTS_PER_SEGMENT; i++) {
			final var vert = mVertices[i];

			vert.r = mR;
			vert.g = mG;
			vert.b = mB;

			if (mFadeoutTime == 0)
				continue;

			if (vert.timeSinceStart < lLifeTime)
				vert.timeSinceStart += lElapsedMs;

			final float lNormalized = (vert.timeSinceStart / lLifeTime);
			vert.a = (1.f - lNormalized) * mA;
		}
	}
}
