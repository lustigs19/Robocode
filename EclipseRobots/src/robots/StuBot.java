package robots;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import robocode.WinEvent;
import robocode.Rules;

import java.awt.Color;

import assets.*;

public class StuBot extends Robot {
	
	static final int QUADRANT_ERROR = 0;
	static final double SCAN_FACTOR = 1.9;
	
	static final int TIME_THRESHOLD = 25;
	
	static final double LOCKED_THRESHOLD = 12.0;
	double lastRadarTurn = 180.0;
	
	public void run() {
		setColors(Color.BLACK, Color.GRAY, Color.GREEN, Color.GREEN, Color.GREEN);
		
		setAdjustRadarForGunTurn(false);
		setAdjustGunForRobotTurn(true);
		
		while (true) {
			int quadrant;
			if (getX() >= getBattleFieldWidth() / 2.0 && getY() >= getBattleFieldHeight() / 2.0) {
				quadrant = 1;
			} else if (getX() <= getBattleFieldWidth() / 2.0 && getY() >= getBattleFieldHeight() / 2.0) {
				quadrant = 2;
			} else if (getX() <= getBattleFieldWidth() / 2.0 && getY() <= getBattleFieldHeight() / 2.0) {
				quadrant = 3;
			} else if (getX() >= getBattleFieldWidth() / 2.0 && getY() <= getBattleFieldHeight() / 2.0) {
				quadrant = 4;
			} else {
				quadrant = QUADRANT_ERROR;
			}
			
			Double angle;
			
			switch (quadrant) {
			case 1:
				angle = 45.0;
				break;
			case 2:
				angle = 315.0;
				break;
			case 3:
				angle = 225.0;
				break;
			case 4:
				angle = 135.0;
				break;
			default:
			case QUADRANT_ERROR:
				// do nothing
				angle = null;
				break;
			}
			
			out.println("quadrant: " + quadrant + "\nheading: " + getRadarHeading() + "\nangle: " + angle.toString());
			
			
			if (angle != null) {
				if (getRadarHeading() >= angle && getRadarHeading() < (angle + 180.0) % 360.0) {
					out.println("Turning right");
					turnRadarRight(Double.POSITIVE_INFINITY);
				} else {
					out.println("Turning left");
					turnRadarLeft(Double.POSITIVE_INFINITY);
				}
			}
			
			lastRadarTurn = 180.0;
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		out.println("Locked? " + isLockedOn());
		double gunAngle = 0;
		
		if (isLockedOn()) {
			e.getVelocity();
			Rules.getBulletSpeed(1);
			turnGunRight(gunAngle);
			e.getHeading();
			
			Line l;
			
			try {
				l = new Line()
			} catch (Exception e) {
				
			}
		}
		
		lastRadarTurn = SCAN_FACTOR * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading() - gunAngle);
		turnRadarRight(lastRadarTurn);
	}
	
	public void onHitWall(HitWallEvent e) {
		
	}
	
	public void onWin(WinEvent e) {
		for (int i = 0; i < 10; i++) {
			turnRight(45);
			turnRadarLeft(180);
			turnLeft(45);
			turnRadarRight(180);
		}
	}
	
	public boolean isLockedOn() {
		return Math.abs(lastRadarTurn) < LOCKED_THRESHOLD;
	}
}
