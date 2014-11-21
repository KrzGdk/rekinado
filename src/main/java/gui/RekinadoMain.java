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
		 * Podpina gui
		 * 
		 * @param newui nowy wskaźnik na ui
		 */
		public void actualizeUI(GUInterface newui){
			gui = newui;
		}
		
		/**
		 * Wywołanie symulacji
		 */
		public void simulate(){
			Simulation.simMain(gui);
			//gui.printFrame(forest, nforest);
			
		}

		/**
		 * Zresetowanie do stanu początkowego
		 */
		public void reset(){
			Simulation.onlyFrame(gui);
		}

	}
	private static GUInterface gui = new GUInterface();
	private static final UIListener uilistener = new UIListener();

	/**
	 * Główna funkcja
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		gui.addListener(uilistener);
	}
	
}
