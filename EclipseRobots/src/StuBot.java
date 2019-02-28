import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

import java.awt.Color;

public class StuBot extends Robot {
	
	boolean spinning = false;
	boolean enemyDir = false; // right true left false
	int spinCount = 0;
	
	static final int SPIN_COUNT_THRESHOLD = 3;
	static final double ROBOT_DEG_PER_TURN = 10.0;
	static final double GUN_DEG_PER_TURN = 20.0;
	
	public void run() {
		setColors(Color.BLACK, Color.GRAY, Color.GREEN);
		
		setAdjustRadarForGunTurn(false);
		
		spinning = true;
		while (true) {
			if (spinning) {
				turnGunRight(GUN_DEG_PER_TURN);
				out.println("turning right spinning");
			} else {
				turnGunLeft(GUN_DEG_PER_TURN);
				turnGunRight(GUN_DEG_PER_TURN);
				spinCount++;
				out.println("turning left/right");
			}
			
			if (spinCount > SPIN_COUNT_THRESHOLD) {
				spinning = true;
				spinCount = 0;
			}
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(2);
		spinning = false;
		spinCount = 0;
	}
	
	public void onHitWall(HitWallEvent e) {
		turnLeft(180);
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
