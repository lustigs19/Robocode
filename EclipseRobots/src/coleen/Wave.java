package coleen;
import java.awt.geom.*;

import robocode.Rules;
import robocode.util.Utils;

public class Wave {
	private double startX, startY, startBearing, power;
	private long fireTime;
	private int direction;
	private int[] returnSegment;
	
	
	public Wave(double x, double y, double bearing, double power, int direction, long time, int[] segment) {
		this.startX = x;
		this.startY = y;
		this.startBearing = bearing;
		this.power = power;
		this.direction = direction;
		this.fireTime = time;
		this.returnSegment = segment;
	}
	
	
	public boolean detectHit(double eX, double eY, long currentTime) {
		if(Point2D.distance(startX, startY, eX, eY) <= (currentTime-fireTime)*getSpeed()) {
			double desiredDirection = Math.atan2(eX-startX, eY-startY);
			double angleOffset = Utils.normalRelativeAngle(desiredDirection-startBearing);
			double guessFactor = Math.max(-1, Math.min(1, angleOffset/maxEscapeAngle()))*direction;
			int index = (int) Math.round((returnSegment.length-1)/2 * (guessFactor+1));
			returnSegment[index]++;
			return true;
		}
		return false;
	}
	
	
	public double getSpeed() {return Rules.getBulletSpeed(power);}
	
	public double maxEscapeAngle() {return Math.asin(8 / getSpeed());}
}
