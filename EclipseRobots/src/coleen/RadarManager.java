package coleen;
import java.util.ArrayList;
import java.util.HashMap;

import robocode.Condition;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class RadarManager extends Condition {
	Poing player;
	ArrayList<String> enemies = new ArrayList<String>();
	HashMap<String, Boolean> accounts = new HashMap<String, Boolean>();

	public RadarManager(Poing p) {
		player = p;
	}
	public void reset() {
		accounts.clear();
		for(String e : enemies) {
			accounts.put(e, false);
		}
	}
	
	public void addEnemy(ScannedRobotEvent e) {
		enemies.add(e.getName());
		accounts.put(e.getName(), true);
	}
	
	public void removeEnemy(RobotDeathEvent e) {
		player.out.println("removing enemy " + e.getName());
		for(int i = 0; i < enemies.size(); i++) {
			if(enemies.get(i).contentEquals(e.getName())) enemies.remove(i--);
		}
		accounts.remove(e.getName());
	}
	
	@Override
	public boolean test() {
		if(!(player.getRadarTurnRemainingRadians() == 0)) {
			for(String e : enemies) {
				if(!accounts.containsKey(e) || accounts.get(e) == false) {
					//player.out.println("has not found robot " + e);
					return false;
				}
				else {
					//player.out.println("has found robot " + e);
				}
			}
		}
		else {
			for(int i = 0; i < enemies.size(); i++) {
				if(!accounts.containsKey(enemies.get(i)) || accounts.get(enemies.get(i)) == false) {
					enemies.remove(i--);
				}
				
			}
		}
		reset();
		return true;
	}

}
