package coleen;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import robocode.AdvancedRobot;
import robocode.RadarTurnCompleteCondition;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Poing extends AdvancedRobot{
	private EnemyList enemies = new EnemyList(this);
	private RadarManager radar = new RadarManager(this);
	Movement movement = new Movement(this);
	
	final static int RADIAL_BINS = 5;
	final static int VELOCITY_BINS = 5;
	final static int INCIDENCE_BINS = 1;
	final static int TANGENTIAL_BINS = 25;
	
	final static double POWER_THRESHOLD = 5.0;
	
	ArrayList<Wave> waves = new ArrayList<Wave>();
	Enemy target;
	
	int direction = 1;
	
	public void run() {
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		setTurnRadarLeftRadians(Math.PI*5/2);
		waitFor(new RadarTurnCompleteCondition(this));
		radar.reset();
		while(true) {
			//scan full field
			setTurnRadarLeftRadians(Math.PI/64.0);
			waitFor(new RadarTurnCompleteCondition(this));
			setTurnRadarRightRadians(getRadarTurnRemainingRadians()+Math.PI*5/2);
			waitFor(radar);
			setTurnRadarRightRadians(Math.PI/64.0);
			waitFor(new RadarTurnCompleteCondition(this));	
			setTurnRadarLeftRadians(getRadarTurnRemainingRadians()+Math.PI*5/2);
			waitFor(radar);
			
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		if(!enemies.containsRobot(e.getName())) enemies.add(e);
		radar.addEnemy(e);
		
		double dBearing = getHeadingRadians() + e.getBearingRadians();
		double eX = getX() + Math.sin(dBearing) * e.getDistance();
		double eY = getY() + Math.cos(dBearing) * e.getDistance();
		
		for(int i = 0; i < waves.size(); i++) {
			Wave w = waves.get(i);
			if(w.detectHit(eX, eY, getTime())) {
				waves.remove(w);
				i--;
			}
		}
		
		if(e.getVelocity() != 0) {
			direction = Math.asin(e.getHeadingRadians()-dBearing)*e.getVelocity() < 0 ? -1 : 1;
		}
		
		int[] currentRegisters = enemies.get(e).getRegisters(e.getDistance(), e.getVelocity(), Utils.normalRelativeAngle(Math.PI - e.getHeadingRadians() + getHeadingRadians()), e.getEnergy(), e.getBearingRadians()); 
		
		int bestRegister = (TANGENTIAL_BINS-1)/2;
		for(int i = 0; i < TANGENTIAL_BINS; i++)
				if(currentRegisters[bestRegister] < currentRegisters[i]) bestRegister = i;
		enemies.get(e).setBestRegister(currentRegisters[bestRegister]);
		
		
		double hitTotal = 0;
		for(int i : currentRegisters) hitTotal+=i;
		double power = 1.0 + 2.0*(double)currentRegisters[bestRegister]/Math.max(hitTotal, POWER_THRESHOLD);
				
		Wave newWave = new Wave(getX(), getY(), dBearing, power, direction, getTime(), currentRegisters);
		
		if(target == null) target = enemies.get(e);
		for(Enemy enemy : enemies.getEnemyList()) {
			if((enemy.bestRegister() >= target.bestRegister() && enemy.getEnergy() < target.getEnergy()) || target.getDistance() > 3/2*enemy.getDistance()) target=enemy;
		}
		out.println("targeting " + target.getName());

		
		if(enemies.get(e).equals(target)) {
			double gF = (double)(bestRegister - (currentRegisters.length-1)/2) / ((currentRegisters.length-1)/2);
			double theta = direction * gF * newWave.maxEscapeAngle();
			double gunAdjust = Utils.normalRelativeAngle(dBearing-getGunHeadingRadians()+theta);
			setTurnGunRightRadians(gunAdjust);
			
			waves.add(newWave);
			if(getGunHeat()==0 && gunAdjust < Math.atan2(9,  e.getDistance())) 
				setFireBullet(power);
		}
		movement.onScannedRobot(e);
	}
	
	
	@Override
    public void onRobotDeath(RobotDeathEvent event) {
        super.onRobotDeath(event);
        enemies.remove(event);
        radar.removeEnemy(event);
        
        target = enemies.getEnemyList().get(0);
        for(Enemy enemy : enemies.getEnemyList()) {
			if((enemy.bestRegister() >= target.bestRegister() && enemy.getEnergy() < target.getEnergy()) || target.getDistance() > 2*enemy.getDistance()) target=enemy;
		}
    }
	
}

//EVERYTHING BELOW IS TEMPORARY

class Movement {
	private static final double BATTLE_FIELD_WIDTH = 800;
	private static final double BATTLE_FIELD_HEIGHT = 600;
	private static final double WALL_MARGIN = 18;
	private static final double MAX_TRIES = 125;
	private static final double REVERSE_TUNER = 0.421075;
	private static final double DEFAULT_EVASION = 1.2;
	private static final double WALL_BOUNCE_TUNER = 0.699484;
 
	private AdvancedRobot robot;
	private Rectangle2D fieldRectangle = new Rectangle2D.Double(WALL_MARGIN, WALL_MARGIN,
		BATTLE_FIELD_WIDTH - WALL_MARGIN * 2, BATTLE_FIELD_HEIGHT - WALL_MARGIN * 2);
	private double enemyFirePower = 3;
	private double direction = 0.4;
 
	Movement(AdvancedRobot _robot) {
		this.robot = _robot;
	}
 
	public void onScannedRobot(ScannedRobotEvent e) {
		double enemyAbsoluteBearing = robot.getHeadingRadians() + e.getBearingRadians();
		double enemyDistance = e.getDistance();
		Point2D robotLocation = new Point2D.Double(robot.getX(), robot.getY());
		Point2D enemyLocation = Methods.project(robotLocation, enemyAbsoluteBearing, enemyDistance);
		Point2D robotDestination;
		double tries = 0;
		while (!fieldRectangle.contains(robotDestination = Methods.project(enemyLocation, enemyAbsoluteBearing + Math.PI + direction,
				enemyDistance * (DEFAULT_EVASION - tries / 100.0))) && tries < MAX_TRIES) {
			tries++;
		}
		if ((Math.random() < (Methods.bulletVelocity(enemyFirePower) / REVERSE_TUNER) / enemyDistance ||
				tries > (enemyDistance / Methods.bulletVelocity(enemyFirePower) / WALL_BOUNCE_TUNER))) {
			direction = -direction;
		}
		// Jamougha's cool way
		double angle = Methods.absoluteBearing(robotLocation, robotDestination) - robot.getHeadingRadians();
		robot.setAhead(Math.cos(angle) * 100);
		robot.setTurnRightRadians(Math.tan(angle));
	}
}

class Methods {
	static double bulletVelocity(double power) {
		return 20 - 3 * power;
	}
 
	static Point2D project(Point2D sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length,
				sourceLocation.getY() + Math.cos(angle) * length);
	}
 
	static double absoluteBearing(Point2D source, Point2D target) {
		return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
	}
 
	static int sign(double v) {
		return v < 0 ? -1 : 1;
	}
 
	static int minMax(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}
}
