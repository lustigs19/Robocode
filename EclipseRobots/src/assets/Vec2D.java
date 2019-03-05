package assets;

public class Vec2D {
	double r;
	double theta;
	
//	public static Vec2D makeCart(double x, double y) {
//		//return new Vec2D); TODO
//	}
	
	public static Vec2D makePolar(double r, double theta) {
		return new Vec2D(r, theta);
	}
	
	private Vec2D(double s, double angle) {
		r = s;
		theta = angle;
	}
}
