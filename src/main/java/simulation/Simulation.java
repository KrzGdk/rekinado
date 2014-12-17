package main.java.simulation;

import main.java.rankine.Rankine;
import main.java.trees.Hwind;
import main.java.trees.Forest;
import main.java.trees.TreeGUI;
import java.lang.Thread;

/**
 * Created by Krzysiek on 17/11/2014.
 */
public class Simulation {


    public static int x = -80;
    public static int y = -80;
    public static double angle = Math.PI/4;
    public static double R_max = 7.0;              /** Promien maksymalny */
    public static double V_traversal_max = 12.0;    /** Predkosc trawersalna maksymalna */
    public static double V_radial_max = 13.;       /** Predkosc radialna maksymalna */
    public static double V_translation = 14.0;      /** Predkosc translacji */

    public static Rankine vortex;

    public static Hwind hWindModel;


	public static void setDefaultHWindModel() {
        hWindModel = new Hwind();
    }

    public static void resetRankine() {
        vortex = new Rankine(x, y, angle, R_max, V_traversal_max, V_radial_max, V_translation);
    }

	public Rankine getVortex() {
        return vortex;
    }

	public void setVortex(Rankine vortex) {
        this.vortex = vortex;
    }

	public static void simMain() {
        setDefaultHWindModel();
        TreeGUI [] forest = Forest.getList();

        new Thread(vortex).start();

        for(TreeGUI tree : forest) {
            if(tree != null && tree.thread != null)
                tree.thread.start();
        }

        System.out.println("Simulation ended");
    }

	// SIM THREAD

	public static void start() {
        simLoop = new SIMLoop();
        thread = new Thread(simLoop);
        thread.start();
    }

    public static void stop(){
        thread.stop();
	}
	
	public static class SIMLoop implements Runnable {
		@Override
		public void run() {
				simMain();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	private static Runnable simLoop;
	private static Thread thread;
	
	

}
