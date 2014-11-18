package main.java.trees;

public class TreeGUI {
	public double x;
	public double y;
	public double z;
	public int height;
	public double crownHeight;
	public double crownWidth;
	
	public double windRotation;
	public double windPower;
	
	public boolean fallen = false;
	public boolean cracked = false;
	
	public TreeGUI(double x, double y, double z, int height){
		this.x = x;
		this.y = y;
		this.z = z;
		this.height = height;
		this.crownHeight = height*.7;//HwindData.crownHeight; // To dac z konstruktora;
		this.crownWidth = height*.5;//HwindData.crownWidth; // To dac z konstruktora;
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
