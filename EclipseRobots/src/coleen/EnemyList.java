package coleen;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.util.ArrayList;

class EnemyList {
	Poing player;
	public EnemyList(Poing player) {
		this.player = player;
	}
	
    private ArrayList<Enemy> enemyList = new ArrayList<>();
    ArrayList<Enemy> getEnemyList(){return enemyList;}

	int enemyIDIndex = 0;
    void add(ScannedRobotEvent e){
        enemyList.add(new Enemy(e, enemyIDIndex++, player));
    }
    void remove(RobotDeathEvent d){
        enemyList.remove(getRobotByName(d.getName()));
    }
    Enemy get(ScannedRobotEvent s){
        return getRobotByName(s.getName());
    }

    private Enemy getRobotByName(String n){
        int i = 0;
        for(Enemy e : enemyList)
            if(enemyList.get(i++).getName().equals(n)) return e;
        return null;
    }

    boolean containsRobot(String id){
        for(Enemy e : enemyList) if(e.getName().equals(id)) return true;
        return false;
    }
    
    public int getEnemyID(ScannedRobotEvent event) {
    	int i = 0;
        for(Enemy e : enemyList)
            if(enemyList.get(i++).getName().equals(event.getName())) return e.getID();
        return -1;
    }
}
