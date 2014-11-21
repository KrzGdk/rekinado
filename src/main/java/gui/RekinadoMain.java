package main.java.gui;

import main.java.simulation.Simulation;

/**
 * Plik główny projektu
 * 
 * @author Jacek Pietras
 */
public class RekinadoMain {
	
	/**
	 * Klasa do której są kierowane eventy programu
	 */
	public static class UIListener {

		/**
		 *
		 * @param newui
		 */
		public void actualizeUI(GUInterface newui){
			gui = newui;
		}
		
		/**
		 *
		 */
		public void simulate(){
			Simulation.simMain(gui);
			//gui.printFrame(forest, nforest);
			
		}

		/**
		 *
		 */
		public void reset(){
			Simulation.onlyFrame(gui);
		}

	}
	private static GUInterface gui = new GUInterface();
	private static final UIListener uilistener = new UIListener();

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		gui.addListener(uilistener);
	}
	
}
