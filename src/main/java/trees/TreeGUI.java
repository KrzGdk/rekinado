package main.java.trees;

import java.awt.Color;
import java.awt.Polygon;
import java.util.Random;
import static main.java.gui.GUInterface.toIzoX;
import static main.java.gui.GUInterface.toIzoY;

/**
 * Klasa drzewa przystosowana do rysowania
 * 
 * @author Jacek Pietras
 */
public class TreeGUI {

	/**
	 * współrzędne drzewa
	 */
	public double x, y, z;

	/**
	 * wysokość drzewa w metrach
	 */
	public int height;

	/**
	 * wysokość korony drzewa w metrach
	 */
	public double crownHeight;

	/**
	 * szerokość korony drzewa w metrach
	 */
	public double crownWidth;
	
	/**
	 * 0..1, kąt pod jakim drzewo się przechyla
	 * 0-up, .25-right, .5-down, .75-left
	 */
	public double windRotation = 0;

	/** 
	 * 0..1, kąt o jaki drzewo się przechyla
	 * 0-pion, 1-poziom
	 */
	public double windPower = 0; //0..1
	
	/**
	 * czy drzewo jest wyrwane
	 */
	public boolean fallen = false;

	/**
	 * czy drzewo jest złamane
	 */
	public boolean cracked = false;
	
	private Color trunkColor;
	private Color crownColor;
	private Color fallenColor;
	private Color crackedColor;
	
	/**
	 * Konstruktor drzewa
	 * 
	 * @param x współrzędna
	 * @param y współrzędna
	 * @param z współrzędna
	 * @param height wysokość
	 */
	public TreeGUI(double x, double y, double z, int height){
		this.x = x;
		this.y = y;
		this.z = z;
		this.height = height;
		
		// To dac z konstruktora;
		this.crownHeight = height*.7;//HwindData.crownHeight; 
		this.crownWidth = height*.5;//HwindData.crownWidth;
		
		Random rand = new Random();
		int hvar = 30; //amplituda losowania koloru
		int var = hvar*2;
		this.trunkColor = new Color(
				150+rand.nextInt(var)-hvar,
				100+rand.nextInt(var)-hvar,
				55 +rand.nextInt(var)-hvar);
		this.crownColor = new Color(
				88 +rand.nextInt(var)-hvar,
				188+rand.nextInt(var)-hvar,
				63 +rand.nextInt(var)-hvar);
		this.fallenColor = new Color(0,0,255-rand.nextInt(var*2));
		this.crackedColor = new Color(255-rand.nextInt(var*2),0,0);
	}

	/**
	 * wyrwanie drzewa
	 */
	public void fall(){
		this.fallen = true;
	}

	/**
	 * złamanie drzewa
	 */
	public void crack(){
		this.cracked = true;
	}

	/**
	 * Zmiana przechylenia drzewa
	 * 
	 * @param windPower 0..1, pochylenie w poziomie
	 * @param windRotation 0..1, kąt pod jakim się pochyla
	 */
	public void changeWind(double windPower, double windRotation){
		this.windPower = windPower;
		this.windRotation = windRotation;
	}
	
	// Do grafiki

	/**
	 * Zwraca kolor pnia drzewa
	 * 
	 * @return kolor
	 */
	public Color getTrunkColor(){
		if(fallen ) return fallenColor;
		if(cracked) return crackedColor;
		return trunkColor;
	}

	/**
	 * Zwraca kolor korony drzewa
	 * 
	 * @return koloe
	 */
	public Color getCrownColor(){
		if(fallen ) return fallenColor;
		if(cracked) return crackedColor;
		return crownColor;		
	}
	
	/**
	 * Przelicza współrzędne i uwaktualnia je w p[]
	 * 
	 * @param p współrzędne [x][y][z]
	 */
	private void moveTree(double p[]){
		rotX(p,windPower);
		rotZ(p,windRotation);
		addTreeCord(p, x, y, z);
	}

	/**
	 * Zwraca Polygon rzutowanych na 2d konturów pnia drzewa
	 * 
	 * @return współrzędne 2d
	 */
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
		
		xpoints[0] = toIzoX(p1);
		xpoints[1] = toIzoX(p2);
		xpoints[2] = toIzoX(p3);
		xpoints[3] = toIzoX(p4);
		
		ypoints[0] = toIzoY(p1);
		ypoints[1] = toIzoY(p2);
		ypoints[2] = toIzoY(p3);
		ypoints[3] = toIzoY(p4);
		
		return new Polygon(xpoints, ypoints, 4);
	}	

	/**
	 * Zwraca Polygon rzutowanych na 2d konturów korony drzewa
	 * 
	 * @return współrzędne 2d
	 */
	public Polygon getCrown(){
		int[] xpoints = new int[3];
		int[] ypoints = new int[3];
		
		double[] p1 = {0,0,height};
		double[] p2 = { crownWidth/2,0,height-crownHeight};
		double[] p3 = {-crownWidth/2,0,height-crownHeight};
		
		moveTree(p1);
		moveTree(p2);
		moveTree(p3);
		
		xpoints[0] = toIzoX(p1);
		xpoints[1] = toIzoX(p2);
		xpoints[2] = toIzoX(p3);
		
		ypoints[0] = toIzoY(p1);
		ypoints[1] = toIzoY(p2);
		ypoints[2] = toIzoY(p3);
		
		return new Polygon(xpoints, ypoints, 3);
	}
	
	// Przesuń na współrzędne
	private void addTreeCord(double tab[], double x, double y, double z){
		tab[0] += x;
		tab[1] += y;
		tab[2] += z;
	}
	
	// Pion
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
				c[i][j] = 
						p[i][0]*rot[0][j] + 
						p[i][1]*rot[1][j] + 
						p[i][2]*rot[2][j] + 
						p[i][3]*rot[3][j];
			}
		}
		
		for(int i=0; i<3; i++){
			tab[i] = c[0][i]+c[1][i]+c[2][i]+c[3][i];
		}
	}
	
	// Obrót
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
				c[i][j] = 
						p[i][0]*rot[0][j] + 
						p[i][1]*rot[1][j] +
						p[i][2]*rot[2][j] + 
						p[i][3]*rot[3][j];
			}
		}
		
		for(int i=0; i<3; i++){
			tab[i] = c[0][i]+c[1][i]+c[2][i]+c[3][i];
		}
	}
}
