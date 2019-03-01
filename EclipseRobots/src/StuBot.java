import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import robocode.WinEvent;

import java.awt.Color;

public class StuBot extends Robot {
	
	static final int SPIN_COUNT_THRESHOLD = 3;
	static final double ROBOT_DEG_PER_TURN = 10.0;
	static final double GUN_DEG_PER_TURN = 20.0;
	static final int QUADRANT_ERROR = 0;
	static final double SCAN_FACTOR = 1.9;
	
	public void run() {
		setColors(Color.BLACK, Color.GRAY, Color.GREEN, Color.GREEN, Color.GREEN);
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		
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
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		turnRadarRight(SCAN_FACTOR * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading()));
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
}
