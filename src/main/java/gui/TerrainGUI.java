package main.java.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import static main.java.gui.GUInterface.normToCenter;
import static main.java.gui.GUInterface.toIzoX;
import static main.java.gui.GUInterface.toIzoY;
import main.java.simulation.Simulation;

/**
 * Klasa statycznego obiektu terenu
 * 
 * @author Jacek Pietras
 */
public class TerrainGUI {
	/**
	* Wyświetla teren na obiekcie Graphics
	* 
	* @param g obiekt Graphics na który zostanie wstawiony
	* @see Graphics
	*/
	
	private static int widthD2;
	private static int heightD2;
	public static int width;
	public static int height;
	
	public static void setSize(int width, int height){
		widthD2 = width/2;
		heightD2 = height/2;
		TerrainGUI.width = width;
		TerrainGUI.height = height;
	}
	
	public static void draw(Graphics g){
		int terrainBold = 10;
		
		int[] xUp = new int[4];
		int[] yUp = new int[4];
		
		double[] p1 = {-widthD2,-heightD2,0};
		double[] p2 = { widthD2,-heightD2,0};
		double[] p3 = { widthD2, heightD2,0};
		double[] p4 = {-widthD2, heightD2,0};
		
		double[] d1 = {-widthD2,-heightD2,-terrainBold};
		double[] d2 = { widthD2,-heightD2,-terrainBold};
		double[] d3 = { widthD2,-heightD2,0};
		double[] d4 = {-widthD2,-heightD2,0};
		
		double[] l1 = { widthD2,-heightD2,-terrainBold};
		double[] l2 = { widthD2, heightD2,-terrainBold};
		double[] l3 = { widthD2, heightD2,0};
		double[] l4 = { widthD2,-heightD2,0};
		
		xUp[0] = toIzoX(p1);
		xUp[1] = toIzoX(p2);
		xUp[2] = toIzoX(p3);
		xUp[3] = toIzoX(p4);
		
		yUp[0] = toIzoY(p1);
		yUp[1] = toIzoY(p2);
		yUp[2] = toIzoY(p3);
		yUp[3] = toIzoY(p4);
		
		g.setColor(new Color(35, 92, 39));
		g.fillPolygon(normToCenter(new Polygon(xUp, yUp, 4)));
		
		
		xUp[0] = toIzoX(d1);
		xUp[1] = toIzoX(d2);
		xUp[2] = toIzoX(d3);
		xUp[3] = toIzoX(d4);
		
		yUp[0] = toIzoY(d1);
		yUp[1] = toIzoY(d2);
		yUp[2] = toIzoY(d3);
		yUp[3] = toIzoY(d4);
		
		g.setColor(new Color(150, 100, 55));
		g.fillPolygon(normToCenter(new Polygon(xUp, yUp, 4)));
		
		
		xUp[0] = toIzoX(l1);
		xUp[1] = toIzoX(l2);
		xUp[2] = toIzoX(l3);
		xUp[3] = toIzoX(l4);
		
		yUp[0] = toIzoY(l1);
		yUp[1] = toIzoY(l2);
		yUp[2] = toIzoY(l3);
		yUp[3] = toIzoY(l4);
		
		g.setColor(new Color(100, 50, 5));
		g.fillPolygon(normToCenter(new Polygon(xUp, yUp, 4)));
	}
}
