package robots;
import robocode.HitWallEvent;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import robocode.WinEvent;
import robocode.Rules;

import java.awt.Color;

public class StuBot extends AdvancedRobot {
	
	static final int QUADRANT_ERROR = 0;
	static final double SCAN_FACTOR = 1.9;
	
	static final double LOCKED_THRESHOLD = 16.0;
	
	static final double ROBOT_WIDTHS = 3.0;
	
	static final double SLOW_TURN_ANGLE = 25.0;
	
	double lastRadarTurn = 180.0;
	
	public void run() {
		setColors(Color.BLACK, Color.GRAY, Color.GREEN, Color.GREEN, Color.GREEN);
		
		setAdjustRadarForGunTurn(false);
		setAdjustGunForRobotTurn(false);
		
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
					turnRadarRight(360);
				} else {
					out.println("Turning left");
					turnRadarLeft(360);
				}
			}
			lastRadarTurn = 180.0;
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		//out.println("Locked? " + isLockedOn());
		
		if (isLockedOn()) {
			
			if (Math.abs(e.getBearing()) > SLOW_TURN_ANGLE) {
				setAhead(0);
			} else {
				setAhead(Double.POSITIVE_INFINITY);
			}
				
			setTurnRight(e.getBearing());
			
			if (e.getDistance() < ROBOT_WIDTHS * getWidth()) {
//				out.println("firing, not touching");
				setFire(Rules.MAX_BULLET_POWER);
			}
		}
		
		lastRadarTurn = SCAN_FACTOR * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading());
		turnRadarRight(lastRadarTurn);
	}
	
	public void onHitWall(HitWallEvent e) {
		setTurnRight(180);
	}
	
	public void onWin(WinEvent e) {
		setAhead(0);
		for (int i = 0; i < 10; i++) {
			turnRight(45);
			turnRadarLeft(180);
			turnLeft(45);
			turnRadarRight(180);
			turnLeft(45);
			turnRadarRight(180);
			turnRight(45);
			turnRadarLeft(180);
		}
	}
	
	public void onHitRobot(HitRobotEvent e) {
//		out.println("firing, touching");
		setFire(Rules.MAX_BULLET_POWER);
	}
	
	public boolean isLockedOn() {
		return Math.abs(lastRadarTurn) < LOCKED_THRESHOLD;
	}
}
