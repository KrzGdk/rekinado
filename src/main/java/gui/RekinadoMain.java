
package main.java.gui;

import java.io.FileNotFoundException;
import java.util.Random;
import main.java.trees.TreeGUI;

public class RekinadoMain {

	private static TreeGUI[] forest;
	private static int nforest = 10;
	
	
	private static void getNewForest(){
		Random rand = new Random();
		int hvar = 20; //amplituda losowania koloru
		int var = hvar*2;
		
		forest = new TreeGUI[nforest];
		
		for(int i=0; i<nforest;i++){
			forest[i] = new TreeGUI(rand.nextInt(var)-hvar,rand.nextInt(var)-hvar,0,rand.nextInt(10)+10);
			forest[i].changeWind(rand.nextDouble(), 0);
		}
		forest[5].fall();
		forest[6].crack();
		
	}
	
	public static class UIListener {
		public void actualizeUI(GUInterface newui){
			gui = newui;
		}
		
		public void simulate(){
			getNewForest();//!!
			gui.printFrame(forest, nforest);
			
		}
		public void reset(){
			getNewForest();
		}

	}
	private static GUInterface gui = new GUInterface();
	private static final UIListener uilistener = new UIListener();
	

	public static void main(String[] args) {
		gui.addListener(uilistener);
	}
	
}
