package main.java.rankine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.java.gui.GUInterface;
import main.java.trees.Forest;

/**
 * Klasa statycznego obiektu tornada
 * 
 * @author Jacek Pietras
 */
public class TornadoGUI {

	/**
	 * współrzędne tornada
	 */
	public static double x = 0,	y = 0, z = 0;
	
	/**
	 * Tablica współrzędnych cząsteczek
	 */
	private static double[] particlesZ = null;
	
	/**
	 * 0..1, Tablica współrzędnych kąta cząsteczek
	 */
	private static double[] angle = null; //0..1
	
	/** 
	 * Ilość cząsteczek
	*/
	private static int density = 2000;
	
	/** 
	 * 0..1, kąt o jaki się przesuwa tornado w jednej klatce
	*/
	private static double windPower = .01;
	
	/** 
	 * wysokość tornada w metrach. Od powierzchni ziemi
	*/
	private static int height = 80;
	
	/** 
	 * Kolor naświetlonych cząsteczek w tornadzie
	*/
	private static int tornadoColor = Color.BLACK.getRGB()+0xbfa186;

	/**
	 * Promien maksymalny tornada (z modelu Rankine)
	 */
	private static double R_max = 1.0;

	public static void setR_max(double r_max) {
		R_max = r_max;
	}


	/**
	 * Wyświetla teren na obiekcie Graphics
	 *
	 * @param _x nowa współrzędna tornada
	 * @param _y nowa współrzędna tornada
	 */
	public static void move(double _x, double _y){
		x = _x;
		y = _y;
	}

	/**
	* Wyświetla teren na obiekcie Graphics
	* 
	* @param _x nowa współrzędna tornada
	* @param _y nowa współrzędna tornada
	* @param _z nowa współrzędna tornada
	*/
	public static void move(double _x, double _y, double _z){
		x = _x;
		y = _y;
		z = _z;
	}
	
	/**
	 * Losuje cząsteczki w tornadzie oraz alokuje pamięć na nie
	 */
	private static void resetTornado(){
		Random rand = new Random();
		particlesZ = new double[density];
		angle = new double[density];
		
		for(int i=0;i<density;i++){
			particlesZ[i] = rand.nextDouble()*height;
			angle[i] = rand.nextDouble();
		}
	}
	
	/**
	 * Porusza cząsteczkami w tornadzie do następnej klatki
	 */
	public static void moveParticles(){
		if(particlesZ == null) resetTornado();
		for(int i=0;i<density;i++){
			angle[i]+=windPower;
			if(angle[i]>1) angle[i]-=1;
		}
	}
	
	
	// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	/**
	 * Funkcja licząca promień tornada na danej wysokości
	 * 
	 * @param z wysokość
	 * @return promień
	 */
	public static double radius(double z){
		return (-0.000000085637 * Math.pow(z,.3) + 0.00018695 * z * z + 0.0078765 * z + 0.94933) * R_max;
	}
	
	/**
	 * Wstawia do argumentów współrzędne oraz kolor cząsteczek potrzebne
	 * do rysowania tornada.
	 * 
	 * @param X tablica współrzędnych cząsteczek
	 * @param Y tablica współrzędnych cząsteczek
	 * @param color tablica koloru cząsteczek
	 */
	private static void setParticles(int X[], int Y[], int color[]){
		if(particlesZ == null) resetTornado();
		double[] p = new double[3];
		double r;
		double shadow;
		
		for(int i=0;i<density;i++){
			r = radius(particlesZ[i]);
			shadow = Math.abs(angle[i]-.5);
					
			p[0] = r*Math.sin(angle[i]*Math.PI*2)+x;
			p[1] = r*Math.cos(angle[i]*Math.PI*2)+y;
			p[2] = particlesZ[i]+z;
			
			X[i] = GUInterface.toIzoX(p);
			Y[i] = GUInterface.toIzoY(p);
			color[i] =  tornadoColor 
					- ((int)(shadow*0xbf)<<16) 
					- ((int)(shadow*0xa1)<<8) 
					- (int)(shadow*0x86);
		}
	}
	
	private static void setShadowParticles(int X[], int Y[]){
		if(particlesZ == null) resetTornado();
		double[] p = new double[3];
		double r;
		double shadow;
		
		for(int i=0;i<density;i++){
			r = radius(particlesZ[i]);
			shadow = Math.abs(angle[i]-.5);
					
			p[0] = r*Math.sin(angle[i]*Math.PI*2)+x;
			p[1] = r*Math.cos(angle[i]*Math.PI*2)+y +Math.abs(particlesZ[i]+z);
			p[2] = 0;
			
			/*if(p[0]>+TerrainGUI.width/2) 
				p[0] =  TerrainGUI.width/2;
			if(p[0]<-TerrainGUI.width/2)
				p[0] = -TerrainGUI.width/2;
			if(p[1]>+TerrainGUI.height/2)
				p[1] =  TerrainGUI.height/2;
			if(p[1]<-TerrainGUI.height/2)
				p[1] = -TerrainGUI.height/2;*/
			
			
			X[i] = GUInterface.toIzoX(p);
			Y[i] = GUInterface.toIzoY(p);
		}
	}
	
	/**
	 * Funkcja rysuje tornado
	 * 
	 * @param canvas obiekt BufferedImage na którym rysowane jest tornado
	 * @param shiftx przesunięcie tornada do środka obrazu
	 * @param shifty przesunięcie tornada do środka obrazu
	 */
	public static void draw(BufferedImage canvas, int shiftx, int shifty){
		int[] X = new int[density];
		int[] Y = new int[density];
		int[] Color = new int[density];
		setParticles(X,Y,Color);
		
		//For particle 1x1
		//for(int i=0; i<TornadoGUI.density;i++){
		//	canvas.setRGB(X[i]+(bgWidth/2), Y[i]+(bgHeight/2), Color[i]);
		//}
		
		//For particle 2x2
		for(int i=0; i<TornadoGUI.density;i++){			
			if(X[i]+shiftx<1 || Y[i]+shifty<1 || 
					X[i]+shiftx+2>canvas.getWidth() || 
					Y[i]+shifty+2>canvas.getHeight()) 
				continue;
			
			System.out.printf((X[i]+shiftx) + " "+ (Y[i]+shifty) +"\n");
			
			canvas.setRGB(X[i]+shiftx, Y[i]+shifty, Color[i]);
			canvas.setRGB(X[i]+shiftx+1, Y[i]+shifty+1, Color[i]);
			canvas.setRGB(X[i]+shiftx+1, Y[i]+shifty, Color[i]);
			canvas.setRGB(X[i]+shiftx, Y[i]+shifty+1, Color[i]);
		}
	}
	public static void drawShadow(BufferedImage canvas, int shiftx, int shifty){
		int[] X = new int[density];
		int[] Y = new int[density];
		
		setShadowParticles(X,Y);
		
		for(int i=0; i<TornadoGUI.density;i++){

			addShadow(canvas,X[i]+shiftx  , Y[i]+shifty  );
			addShadow(canvas,X[i]+shiftx+1, Y[i]+shifty  );
			addShadow(canvas,X[i]+shiftx  , Y[i]+shifty+1);
			addShadow(canvas,X[i]+shiftx+1, Y[i]+shifty+1);
			
			//canvas.setRGB(X[i]+shiftx, Y[i]+shifty, color);
		//	canvas.setRGB(X[i]+shiftx+1, Y[i]+shifty+1, color);
			//canvas.setRGB(X[i]+shiftx+1, Y[i]+shifty, color);
			//canvas.setRGB(X[i]+shiftx, Y[i]+shifty+1, color);
		}
	}
	private static void addShadow(BufferedImage canvas, int x, int y){
		if(x > Forest.width && y > Forest.height) {
			int color = canvas.getRGB(x, y);
			if (color == Color.WHITE.getRGB()) return;
			canvas.setRGB(x, y, (new Color(color)).darker().getRGB());
		}
		
	}
}
