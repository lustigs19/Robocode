package coleen;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

class Enemy {
	Poing player;
	int ID;
	String name;
	double energy;
	double lastVelocity;
	double lastIncidence;
	double bearing;
	double distance;
	int bestRegister = 0;
	
	static int[][][][][][] registers = new int[Poing.RADIAL_BINS][Poing.VELOCITY_BINS][Poing.VELOCITY_BINS][Poing.INCIDENCE_BINS][Poing.INCIDENCE_BINS][Poing.TANGENTIAL_BINS];

	public Enemy(ScannedRobotEvent e, int ID, Poing player) {
		this.ID = ID;
		this.player = player;
		name = e.getName();
		energy = e.getEnergy();
		bearing = e.getBearingRadians();
		distance = e.getDistance();
		lastVelocity = e.getVelocity();
		lastIncidence = Utils.normalRelativeAngle(Math.PI - e.getHeadingRadians() + player.getHeadingRadians());
	}
	
	
	public int[] getRegisters(double distance, double vel, double inc, double energy, double bearing) {
		int radialIndex = (int)(distance/(1200.0/(double)Poing.RADIAL_BINS));
		int velIndex = (int)((Math.abs(vel)+1)/(16/(Poing.VELOCITY_BINS-1))*sign(vel)+(Poing.VELOCITY_BINS-1)/2);
		int oldVelIndex = (int)((Math.abs(lastVelocity)+1)/(16/(Poing.VELOCITY_BINS-1))*sign(lastVelocity)+(Poing.VELOCITY_BINS-1)/2);
		int incIndex = (int)Math.floor((inc+Math.PI)*(double)(Poing.INCIDENCE_BINS)/(Math.PI*2.0));
		int oldIncIndex = (int)Math.floor((lastIncidence+Math.PI)*(double)(Poing.INCIDENCE_BINS)/(Math.PI*2.0));
		
		/*
		player.out.println("Distance is " + distance + ", so radial bin is " + radialIndex);
		player.out.println("Velocity is " + vel + ", so velocity bin is " + velIndex);
		player.out.println("Last velocity was " + lastVelocity + ", so velocity bin is " + oldVelIndex);
		player.out.println("Incidence is " + inc + ", so incidence bin is " + incIndex);
		player.out.println("Last incidence was " + lastIncidence + ", so incidence bin is " + oldIncIndex);
		player.out.println("\n");
		*/
		
		lastVelocity = vel;
		lastIncidence = inc;
		this.energy = energy;
		this.bearing = bearing;
		this.distance = distance;
		
		return registers[radialIndex][velIndex][oldVelIndex][incIndex][oldIncIndex];
	}
	
	public int bestRegister() {
		return bestRegister;
	}
	
	public void setBestRegister(int register) {
		if (register > bestRegister) bestRegister = register;
	}
	
	private double sign(double x) {
		return (x/Math.abs(x));
	}
	
	public String getName() {
		return name;
	}
	
	public double getBearing() {
		return bearing;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public double getLastVelocity() {return lastVelocity;}
	public double setLastVelocity(double lV) {
		double temp = lastVelocity;
		lastVelocity = lV;
		return temp;
	}
	
	public int getID() {
		return ID;
	}
	
	public double getEnergy() {
		return energy;
	}
}
