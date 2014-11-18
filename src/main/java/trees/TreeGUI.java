package main.java.trees;

import java.awt.Polygon;
import main.java.gui.GUInterface;

public class TreeGUI {
	public double x;
	public double y;
	public double z;
	public int height;
	public double crownHeight;
	public double crownWidth;
		
	public double windRotation = 0; //0..1 (0-up, .25-right, .5-down, .75-left)
	public double windPower = 0; //0..1
	
	public boolean fallen = false;
	public boolean cracked = false;
	
	
	public TreeGUI(double x, double y, double z, int height){
		this.x = x;
		this.y = y;
		this.z = z;
		this.height = height;
		this.crownHeight = height*.7;//HwindData.crownHeight; // To dac z konstruktora;
		this.crownWidth = height*.5;//HwindData.crownWidth; // To dac z konstruktora;
		
		//Losować kolor
	}
	public void fall(){
		this.fallen = true;
	}
	public void crack(){
		this.cracked = true;
	}
	public void changeWind(double windPower, double windRotation){
		this.windPower = windPower;
		this.windRotation = windRotation;
	}
	
	// Do grafiki

	
	private void moveTree(double p[]){
		rotX(p,windPower);
		rotZ(p,windRotation);
		addTreeCord(p, x, y, z);
	}
	public Polygon getTrunk(){
		int[] xpoints = new int[4];
		int[] ypoints = new int[4];
		int trunkWidth = 1;
		
		double[] p1 = {-trunkWidth,0,0};
		double[] p2 = {-trunkWidth,0,+height-crownHeight};
		double[] p3 = {+trunkWidth,0,+height-crownHeight};
		double[] p4 = {+trunkWidth,0,0};
		
		moveTree(p1);
		moveTree(p2);
		moveTree(p3);
		moveTree(p4);
		
		xpoints[0] = GUInterface.toIzoX(p1);
		xpoints[1] = GUInterface.toIzoX(p2);
		xpoints[2] = GUInterface.toIzoX(p3);
		xpoints[3] = GUInterface.toIzoX(p4);
		
		ypoints[0] = GUInterface.toIzoY(p1);
		ypoints[1] = GUInterface.toIzoY(p2);
		ypoints[2] = GUInterface.toIzoY(p3);
		ypoints[3] = GUInterface.toIzoY(p4);
		
		return new Polygon(xpoints, ypoints, 4);
	}	
	public Polygon getCrown(){
		int[] xpoints = new int[3];
		int[] ypoints = new int[3];
		
		double[] p1 = {0,0,height};
		double[] p2 = { crownWidth/2,0,height-crownHeight};
		double[] p3 = {-crownWidth/2,0,height-crownHeight};
		
		moveTree(p1);
		moveTree(p2);
		moveTree(p3);
		
		xpoints[0] = GUInterface.toIzoX(p1);
		xpoints[1] = GUInterface.toIzoX(p2);
		xpoints[2] = GUInterface.toIzoX(p3);
		
		ypoints[0] = GUInterface.toIzoY(p1);
		ypoints[1] = GUInterface.toIzoY(p2);
		ypoints[2] = GUInterface.toIzoY(p3);
		
		return new Polygon(xpoints, ypoints, 3);
	}
	private void addTreeCord(double tab[], double x, double y, double z){
		tab[0] += x;
		tab[1] += y;
		tab[2] += z;
	}
	//Pion
	private void rotX(double[] tab,double r){
		r = -r*Math.PI/2;
		double p[][]={
			{tab[0],0,0,0},
			{0,tab[1],0,0},
			{0,0,tab[2],0},
			{0,0,0,1}};
		double rot[][]={
			{1,0,0,0},
			{0,Math.cos(r),Math.sin(r),0},
			{0,-Math.sin(r),Math.cos(r),0},
			{0,0,0,1}};
		double c[][]={
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}};
		
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				c[i][j] = p[i][0]*rot[0][j] + p[i][1]*rot[1][j] + p[i][2]*rot[2][j] + p[i][3]*rot[3][j];
			}
		}
		
		for(int i=0; i<3; i++){
			tab[i] = c[0][i]+c[1][i]+c[2][i]+c[3][i];
		}
	}
	
	//Obrót
	private void rotZ(double[] tab,double r){
		r = -r*Math.PI*2;
		double p[][]={
			{tab[0],0,0,0},
			{0,tab[1],0,0},
			{0,0,tab[2],0},
			{0,0,0,1}};
		double rot[][]={
			{Math.cos(r),Math.sin(r),0,0},
			{-Math.sin(r),Math.cos(r),0,0},
			{0,0,1,0},
			{0,0,0,1}};
		double c[][]={
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0},
			{0,0,0,0}};
		
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				c[i][j] = p[i][0]*rot[0][j] + p[i][1]*rot[1][j] + p[i][2]*rot[2][j] + p[i][3]*rot[3][j];
			}
		}
		
		for(int i=0; i<3; i++){
			tab[i] = c[0][i]+c[1][i]+c[2][i]+c[3][i];
		}
	}
}
