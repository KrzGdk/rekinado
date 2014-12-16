package main.java.gui;

import main.java.simulation.Simulation;
import main.java.trees.Forest;
import main.java.trees.TreeGUI;

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
		
		public void print(){
			gui.printFrame();
		}
		
		/**
		 * Wywołanie symulacji
		 */
		public void start(){
			guiLoop = new GUILoop();
			thread = new Thread(guiLoop);
			thread.start();			
			Simulation.start();
		}

		public void stop(){
			thread.stop();
			Simulation.stop();
		}	
		public void reset(){
			if(run){
				gui.btnSimAction();
			}
			if(Forest.getList() != null) {
				for (TreeGUI tree : Forest.getList()) {
					if (tree != null){
						tree.terminate();
					}
				}
			}
				
			//
			Forest.fillRegular(12);
//			Forest.fillRandom(4, 100);
//			Forest.fillPatch(8, 200, 50);

			Simulation.resetRankine();

			gui.printFrame();
		}
	}
	public static class GUILoop implements Runnable {
		@Override
		public void run() {
			while(true){
				gui.printFrame();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private static GUInterface gui = null;
	private static UIListener uilistener;
	
	private static Runnable guiLoop;
	private static Thread thread;

	/**
	 * Główna funkcja
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Forest.setSize(300, 300);
		gui = new GUInterface();
		uilistener = new UIListener();
		gui.addListener(uilistener);
		uilistener.reset();
	}
	
}
