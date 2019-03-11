package robots;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import robocode.Rules;

import java.awt.Color;

public class RandomBot extends Robot {
	
	static final double RAND_DIST = 400.0;
	
	public void run() {
		setColors(Color.BLACK, Color.GRAY, Color.GREEN, Color.GREEN, Color.GREEN);
		
		setAdjustRadarForGunTurn(false);
		setAdjustGunForRobotTurn(false);
		
		while (true) {
			turnGunRight(360.0);
			switch ((int) (Math.random() * 3.0)) {
			case 0:
				ahead(Math.random() * RAND_DIST);
				break;
			case 1:
				back(Math.random() * RAND_DIST);
				break;
			case 2:
				turnRight((Math.random() - 0.5) * 360.0);
				break;
			}
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(1);
	}
	
	public void onHitWall(HitWallEvent e) {
		ahead(0);
	}
	
	public void onWin(WinEvent e) {
		ahead(0);
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
		fire(Rules.MAX_BULLET_POWER);
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
	}
}
