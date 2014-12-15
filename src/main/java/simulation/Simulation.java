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


    private static double angle = Math.PI/4;
    private static double R_max = 5.0;              /** Promien maksymalny */
    private static double V_traversal_max = 5.0;    /** Predkosc trawersalna maksymalna */
    private static double V_radial_max = 15.;       /** Predkosc radialna maksymalna */
    private static double V_translation = 15.0;      /** Predkosc translacji */

    public static Rankine vortex = new Rankine(0, 0, angle, R_max, V_traversal_max, V_radial_max, V_translation);

    public static Hwind hWindModel;


	public static void setDefaultHWindModel() {
        hWindModel = new Hwind();
    }

    public static void resetRankine() {
        vortex = new Rankine(0, 0, angle, R_max, V_traversal_max, V_radial_max, V_translation);
    }

	/*public static void printForest() {
        int k = 0;
        for(int i = 0; i < forestLength; i += iDist){
            for(int j = 0; j < forestWidth; j+= jDist) {
                if(vortex.getOrigin().equals(new Point(i-forestLength/2,j-forestWidth/2))) {
                   System.out.print("O");
                }
                else {
                   System.out.print(forestList[k].fallen ? "X" : (forestList[k].cracked ? "/" : "I"));
                }
                k++;
            }
            System.out.println();
        }
    }*/

	public Rankine getVortex() {
        return vortex;
    }


	public void setVortex(Rankine vortex) {
        this.vortex = vortex;
    }

	

	public static void simulate() throws Exception{
		TreeGUI [] forestList = Forest.getList();
		
        vortex.calculateNewCenter(1);
        if(vortex.getOrigin().x >= Forest.width/2 || vortex.getOrigin().y >=  Forest.height/2) {
            throw new Exception("Vortex origin out of bounds: (" + vortex.getOrigin().x + "," + vortex.getOrigin().y + ")");
        }

        for(int i = 0; i <  Forest.getLength() ; ++i)
            if( !forestList[i].fallen )
                hWindModel.calcTreeForce(forestList[i], vortex);
    }

	public static void simMain() {
        setDefaultHWindModel();
        TreeGUI [] forest = Forest.getList();

        new Thread(vortex).start();
        for(TreeGUI tree : forest) {
            new Thread(tree).start();
        }
//        int maxTime = 20000;
//
//        setDefaultHWindModel();
////        printForest();
//        for(int i = 0; i < maxTime; ++i) {
//            try {
////                long startTime = System.nanoTime();
//                simulate();
////                long elapsedTime = System.nanoTime() - startTime;
//
////                System.out.println("Total execution time of single sim on forest [ms]: "
////                        + elapsedTime/1000000);
//
//
////                if (i % 3 == 0) {
////                    System.out.println("Iteration " + (i+1) + ":");
////                   printForest();
////                }
//            } catch (Exception e) {
//                System.out.println(e);
//                break;
//            }
//        }
		
        System.out.println("Simulation ended");
    }

	
	
	// SIM THREAD
	
	
	public static void start(){
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
			//while(true){
				simMain();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {e.printStackTrace();}
			//}
		}
	}
	
	private static Runnable simLoop;
	private static Thread thread;
	
	

}
