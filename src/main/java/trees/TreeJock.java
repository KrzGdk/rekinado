package rekinado;

public class Tree {
	public double x;
	public double y;
	public double z;
	public int height;
	
	public double windRotation;
	public double windPower;
	
	public boolean fallen = false;
	public boolean cracked = false;
	
	public Tree(double x, double y, double z, int height){
		this.x = x;
		this.y = y;
		this.z = z;
		this.height = height;
	}
	public void fall(){
		this.fallen = true;
	}
	public void crack(){
		this.cracked = true;
	}
	public void changeWind(double windRotation, double windPower){
		this.windPower = windPower;
		this.windRotation = windRotation;
	}
}
