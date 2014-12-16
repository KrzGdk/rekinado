package main.java.gui;

import main.java.simulation.Simulation;
import main.java.trees.Forest;
import main.java.trees.TreeGUI;
import main.java.trees.Weakness;
import main.java.trees.HwindData;

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

			if(Simulation.vortex != null) {
				Simulation.vortex.terminate();
			}
			if(Forest.getList() != null) {
				for (TreeGUI tree : Forest.getList()) {
					if (tree != null){
						tree.terminate();
					}
				}
			}

							

				 if(Forest.type == Forest.Type.Jednorodny) Forest.fillRegular(HwindData.spacing);
			else if(Forest.type == Forest.Type.Losowy)     Forest.fillRandom(HwindData.spacing, 100);
			else if(Forest.type == Forest.Type.Plama)      Forest.fillPatch(HwindData.spacing, 200, 50);

			Simulation.resetRankine();
			if(Simulation.vortex != null) {
				System.out.println("Simulation.vortex.running = " + Simulation.vortex.running);
			}
				 
			System.out.printf("RekinadoMain / linia 63 : powyższe wartości są z dupy\n");
			Weakness.calc();
			
		

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
				} catch (InterruptedException e) {}
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
		Forest.setSize(100, 100);
		gui = new GUInterface();
		uilistener = new UIListener();
		gui.addListener(uilistener);
		uilistener.reset();
		
	}
	
}
