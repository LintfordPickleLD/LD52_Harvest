package lintfordpickle.harvest.renderers.trails;

public class TrailVertex {
	public float x;
	public float y;

	public float r = 1.f;
	public float g = 1.f;
	public float b = 1.f;
	public float a = 1.f;

	public float u;
	public float v;

	public float timeSinceStart;

	public void copyFrom(TrailVertex other) {
		x = other.x;
		y = other.y;

		r = other.r;
		g = other.g;
		b = other.b;
		a = other.a;

		u = other.u;
		v = other.v;

		timeSinceStart = other.timeSinceStart;
	}
}
