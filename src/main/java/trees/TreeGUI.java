package main.java.trees;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Random;
import static main.java.gui.GUInterface.normToCenter;
import static main.java.gui.GUInterface.toIzoX;
import static main.java.gui.GUInterface.toIzoY;
import main.java.gui.TerrainGUI;

/**
 * Klasa drzewa przystosowana do rysowania
 * 
 * @author Jacek Pietras
 */
public class TreeGUI implements Runnable {

	public double x, y, z; 	/** współrzędne drzewa */
	public int height;
	public double diameter;
	public double crownMass;

	public double rootMass;
	public double rootDepth;
	public int crownHeight;
	public double crownWidth;

	/**
	 * 0..1, kąt pod jakim drzewo się przechyla
	 * 0-up, .25-right, .5-down, .75-left
	 */
	public double windRotation = 0;
	private double currentwindRotation = 0;

	/** 
	 * 0..1, kąt o jaki drzewo się przechyla
	 * 0-pion, 1-poziom
	 */
	public double windPower = 0;
	private double currentwindPower = 0;
	
	public boolean fallen = false;	/** czy drzewo jest wyrwane */
	public boolean cracked = false; /** czy drzewo jest złamane */
	
	private Color trunkColor;
	private Color crownColor;
	private Color fallenColor;
	private Color crackedColor;
	private Color ShadowColor = new Color(0,50,0);
	
	/**
	 * Promień podstawy pnia
	 */
	private int trunkWidth;
	
	private static Thread thread;
	
	//funkcja wywoływana w wątku
	private void lookToEnv(){
		//?collision test
		//?pobierz siłe z wiru
		//?pochyl
		//?sprawdz wyrwanie
		
		
		
	}
	@Override
	public void run() {
		while(true){
			lookToEnv();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	
	
	/**
	 * Konstruktor drzewa
	 * 
	 * @param x współrzędna
	 * @param y współrzędna
	 * @param z współrzędna
	 */
	public TreeGUI(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		TreeDataSet data = HwindData.randData(HwindData.getRandomTreeSetId());
		this.height = data.height;
		this.diameter = data.diameter;
		this.crownMass = data.crownMass;
		this.rootMass = data.rootMass;
		this.rootDepth = data.rootDepth;
		this.crownHeight = data.crownHeight;
		this.crownWidth = data.crownWidth;

		this.trunkWidth = 1;

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
		
		thread.start();
	}

	public double distance(TreeGUI tree){
		double ax = this.x - tree.x;
		double ay = this.y - tree.y;
		double az = this.z - tree.z;
		return Math.sqrt(ax*ax+ay*ay+az*az);
	}
	
	// Wykrywa kolizje pomiędzy tym drzewem a innym
	public Boolean collisionTest(TreeGUI tree){
		if(distance(tree)>height+tree.height) return false;
		if(this == tree) return false;
		
		Boolean solve = false;
		double[][][] crown1 = new double [4][3][2];//[trojkat][punkt][x|y]
		double[][][] crown2 = new double [4][3][2];
		     getCrownHorizontal(crown1);
		tree.getCrownHorizontal(crown2);
		for(int i=0; i<4; ++i){
			//peak
			solve |= pointInPolygon(crown1[0][0][0], crown1[0][0][1], crown2[i]);
			//sides
			solve |= pointInPolygon(crown1[0][1][0], crown1[0][1][1], crown2[i]);
			solve |= pointInPolygon(crown1[1][1][0], crown1[1][1][1], crown2[i]);
			solve |= pointInPolygon(crown1[2][1][0], crown1[2][1][1], crown2[i]);
			solve |= pointInPolygon(crown1[3][1][0], crown1[3][1][1], crown2[i]);
		}
		return solve;
	}
	
	// Wykrywa kolizje punktu z poligonem
	private Boolean pointInPolygon(double x, double y, double[][] poly) { 
		int i, j = 0; 
		Boolean oddNODES = false; 
		for (i = 0; i < 3; i++) { 
			j++; 
			if (j == 3) j = 0; 
			if (	poly[i][1] < y && poly[j][1] >= y ||
					poly[j][1] < y && poly[i][1] >= y) { 
				if (	poly[i][0] 
						+ (y - poly[i][1]) / (poly[j][1] 
						- poly[i][1]) * (poly[j][0] 
						- poly[i][0]) < x) { 
					oddNODES = !oddNODES; 
				} 
			} 
		} 
		return oddNODES; 
	}
	
	
	// Do grafiki


	public void draw(Graphics g){
		currentwindPower = (currentwindPower + windPower)/2;

		if(Math.abs(currentwindRotation - windRotation)> .5){
			double temp = windRotation;
			if(currentwindRotation < temp)
				currentwindRotation += 1;
			else
				temp += 1;
			currentwindRotation = (currentwindRotation + temp)/2;
			if(currentwindRotation >= 1) currentwindRotation -= 1;
		}else{
			currentwindRotation = (currentwindRotation + windRotation)/2;
		}
		   
		
		
		Polygon[] crown = new Polygon [4];
		
		g.setColor(getTrunkColor());
		g.fillPolygon(getTrunkIzo());
		
		getCrownIzo(crown);
		g.setColor(getCrownColor());
		g.fillPolygon(crown[0]);
		g.fillPolygon(crown[1]);
		g.fillPolygon(crown[2]);
		g.fillPolygon(crown[3]);
	}
	
	public void drawShadow(Graphics g){
		
		g.setColor(ShadowColor);
		g.fillPolygon(getShadowIzo());
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
		rotX(p,currentwindPower);
		rotZ(p,currentwindRotation);
		addTreeCord(p, x, y, z);
	}

	/**
	 * Zwraca Polygon rzutowanych na 2d konturów pnia drzewa
	 * 
	 * @return współrzędne 2d
	 */
	private Polygon getTrunkIzo(){
		int[] xpoints = new int[4];
		int[] ypoints = new int[4];
		
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
	 * Wrzuca do tablicy 4 trójkąty drzewa w rzucie z góry
	 * 
	 * @param crown tablica double[4][3][2]
	 */
	public void getCrownHorizontal(double[][][] crown){
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
			crown[i][0][0] = peak[0];
			crown[i][1][0] = p[i][0];
			crown[i][2][0] = p[(i+1)%4][0];
		
			crown[i][0][1] = peak[1];
			crown[i][1][1] = p[i][1];
			crown[i][2][1] = p[(i+1)%4][1];
		}
	}
	
	/**
	 * Wrzuca do tablicy 4 Polygon'y z trójkątami drzewa rzut izomorficzny
	 * 
	 * @param crown tablica Polygon[4]
	 */
	private void getCrownIzo(Polygon[] crown){
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
	
	private Polygon getShadowIzo(){
		double[][][] crown = new double [4][3][2];//[trojkat][peak,punkt1,punkt2][x,y]
		getCrownHorizontal(crown);
		int min = 0;
		int max = 0;
		double shadowAngle = 1-currentwindPower;
		for(int i=1; i<4;++i){
			if(crown[i][1][0]>crown[max][1][0]) max = i;
			else
			if(crown[i][1][0]<crown[min][1][0]) min = i;
		}
		
		
		double[][] p = {{ crown[0][0][0]  , crown[0][0][1]  , 0},
			            { crown[max][1][0], crown[max][1][1], 0},
		                { x+trunkWidth    , crown[max][1][1], 0},
		                { x+trunkWidth    , y               , 0},
		                { x-trunkWidth    , y               , 0},
		                { x-trunkWidth    , crown[min][1][1], 0},
			            { crown[min][1][0], crown[min][1][1], 0},
		                };
		p[0][1]+=Math.abs(shadowAngle*(height));
		p[1][1]+=Math.abs(shadowAngle*(height-crownHeight));
		p[2][1]+=Math.abs(shadowAngle*(height-crownHeight));
		p[5][1]+=Math.abs(shadowAngle*(height-crownHeight));
		p[6][1]+=Math.abs(shadowAngle*(height-crownHeight));
		
		for(int i=0; i<7;++i){
			if(p[i][0]>+TerrainGUI.width/2) 
				p[i][0] =  TerrainGUI.width/2;
			if(p[i][0]<-TerrainGUI.width/2)
				p[i][0] = -TerrainGUI.width/2;
			if(p[i][1]>+TerrainGUI.height/2)
				p[i][1] =  TerrainGUI.height/2;
			if(p[i][1]<-TerrainGUI.height/2)
				p[i][1] = -TerrainGUI.height/2;
		}
		
		int[] xpoints = {
			toIzoX(p[0]),
			toIzoX(p[1]),
			toIzoX(p[2]),
			toIzoX(p[3]),
			toIzoX(p[4]),
			toIzoX(p[5]),
			toIzoX(p[6])
		};
		
		int[] ypoints = {
			toIzoY(p[0]),
			toIzoY(p[1]),
			toIzoY(p[2]),
			toIzoY(p[3]),
			toIzoY(p[4]),
			toIzoY(p[5]),
			toIzoY(p[6])
		};
		return normToCenter(new Polygon(xpoints, ypoints, 7));
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
