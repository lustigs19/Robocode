import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import java.awt.Color;

public class StuBot extends Robot {
	
	public void run() {
		setColors(Color.BLUE, Color.RED, Color.GREEN);
		
		while (true) {
			
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		
	}
	
	public void onHitWall(HitWallEvent e) {
		
	}
}
