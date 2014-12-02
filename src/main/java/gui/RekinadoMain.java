package main.java.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.simulation.Simulation;
import main.java.trees.Forest;

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
		public static boolean run = false;
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
		public void start(){
			Simulation.simMain(gui);
			guiLoop = new GUILoop();
			thread = new Thread(guiLoop);
			thread.start();
			//gui.printFrame(forest, nforest);
			
		}

		public void stop(){
			thread.stop();
		}
		/**
		 * Zresetowanie do stanu początkowego
		 */
		public void reset(){
			
		}

		
	}
	public static class GUILoop implements Runnable {
		@Override
		public void run() {
			while(true){
				//System.out.println("Watek");
				Simulation.onlyFrame(gui);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static GUInterface gui;
	private static UIListener uilistener;
	
	private static Runnable guiLoop;
	private static Thread thread;

	/**
	 * Główna funkcja
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		gui = new GUInterface();
		uilistener = new UIListener();
		
		Forest.setSize(100, 100);
		//Forest.fillRegular(12);
		Forest.fillRandom(8, 100);
		
		gui.addListener(uilistener);
	}
	
}
