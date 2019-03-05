package assets;

public class Line {
	double m;
	double b;
	
	/** slope-intercept form */
	public Line(double slope, double yInt) {
		m = slope;
		b = yInt;
	}
	
	/** 2 Points 
	 * @throws Exception
	 * (if slope is undefined) */
	public Line(Point a, Point b) throws Exception{
		if (b.getX() - a.getX() == 0) {
			throw(new Exception());
		}
		m = (b.getY() - a.getY()) / (b.getX() - a.getX());
	}
	
	/** point-slope form */
	public Line(double slope, Point a) {
		m = slope;
		
		b = a.getY() - m * a.getX();
	}
	
	public double get(double x1) {
		return m * x1 + b;
	}
	
	public double getInverse(double y1) throws Exception {
		if (m == 0) {
			throw(new Exception());
		}
		return (y1 - b) / m;
	}

	public double getSlope() {
		return m;
	}

	public void setSlope(double m) {
		this.m = m;
	}

	public double getYInt() {
		return b;
	}

	public void setYInt(double b) {
		this.b = b;
	}
}
