
package main.java.gui;

import java.io.FileNotFoundException;
import java.util.Random;
import main.java.simulation.Simulation;
import main.java.trees.TreeGUI;

public class RekinadoMain {
	
	public static class UIListener {
		public void actualizeUI(GUInterface newui){
			gui = newui;
		}
		
		public void simulate(){
			Simulation.simMain(gui);
			//gui.printFrame(forest, nforest);
			
		}
		public void reset(){
			//getNewForest();
		}

	}
	private static GUInterface gui = new GUInterface();
	private static final UIListener uilistener = new UIListener();
	

	public static void main(String[] args) {
		gui.addListener(uilistener);
	}
	
}
