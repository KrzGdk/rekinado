package main.java.trees;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;
import static main.java.gui.GUInterface.normToCenter;
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

	// TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// Funkcja w ktorej drzewo pyta rankine jaka siła na nią działa
	// Funkcja sprawdzająca czy suma sił wyrwała drzewo
	
	
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
	
	public double distance(TreeGUI tree){
		double ax = x - tree.x;
		double ay = y - tree.y;
		double az = z - tree.z;
		return Math.sqrt(ax*ax+ay*ay+az*az);
	}
	
	public Boolean collisionTest(TreeGUI tree){
		if(distance(tree)>height+tree.height) return false;
		double[] peak1 = {0,0,height};
		double[] trunk1 = {x,y,z};
		//double[] side1 = {crownWidth/2,0,height-crownHeight};
		calcPoint(peak1);
		//calcPoint(side1);
		
		return true;
	}
	
	// Do grafiki


	public void draw(Graphics g){
		Polygon[] crown = new Polygon [4];
		
		g.setColor(getTrunkColor());
		g.fillPolygon(getTrunk());
		
		getCrown(crown);
		g.setColor(getCrownColor());
		g.fillPolygon(crown[0]);
		g.fillPolygon(crown[1]);
		g.fillPolygon(crown[2]);
		g.fillPolygon(crown[3]);
	}
	
	/**
	 * Zwraca kolor pnia drzewa
	 * 
	 * @return kolor
	 */
	private Color getTrunkColor(){
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
	private void calcPoint(double p[]){
		rotX(p,windPower);
		rotZ(p,windRotation);
		addTreeCord(p, x, y, z);
	}

	/**
	 * Zwraca Polygon rzutowanych na 2d konturów pnia drzewa
	 * 
	 * @return współrzędne 2d
	 */
	private Polygon getTrunk(){
		int[] xpoints = new int[4];
		int[] ypoints = new int[4];
		int trunkWidth = 1;
		
		double[] p1 = {-trunkWidth,0,0};
		double[] p2 = {-trunkWidth,0,+height-crownHeight};
		double[] p3 = {+trunkWidth,0,+height-crownHeight};
		double[] p4 = {+trunkWidth,0,0};
		
		calcPoint(p1);
		calcPoint(p2);
		calcPoint(p3);
		calcPoint(p4);
		
		xpoints[0] = toIzoX(p1);
		xpoints[1] = toIzoX(p2);
		xpoints[2] = toIzoX(p3);
		xpoints[3] = toIzoX(p4);
		
		ypoints[0] = toIzoY(p1);
		ypoints[1] = toIzoY(p2);
		ypoints[2] = toIzoY(p3);
		ypoints[3] = toIzoY(p4);
		
		return normToCenter(new Polygon(xpoints, ypoints, 4));
	}	

	/**
	 * Wrzuca do tablicy 4 Polygon'y z trójkątami drzewa
	 * 
	 * @param crown tablica Polygon[4]
	 */
	private void getCrown(Polygon[] crown){
		int[] xpoints = new int[3];
		int[] ypoints = new int[3];
		
		double[] peak = {0,0,height};
		double[][] p = {{ crownWidth/2, 0           ,height-crownHeight},
			            { 0           , crownWidth/2,height-crownHeight},
		                {-crownWidth/2, 0           ,height-crownHeight},
		                { 0           ,-crownWidth/2,height-crownHeight}
		                };
		
		calcPoint(peak);
		calcPoint(p[0]);
		calcPoint(p[1]);
		calcPoint(p[2]);
		calcPoint(p[3]);
		
		for(int i=0; i<4; ++i){
			xpoints[0] = toIzoX(peak);
			xpoints[1] = toIzoX(p[i]);
			xpoints[2] = toIzoX(p[(i+1)%4]);
		
			ypoints[0] = toIzoY(peak);
			ypoints[1] = toIzoY(p[i]);
			ypoints[2] = toIzoY(p[(i+1)%4]);
			
			crown[i] = normToCenter(new Polygon(xpoints, ypoints, 3));
		}
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
