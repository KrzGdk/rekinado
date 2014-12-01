package main.java.simulation;

import main.java.rankine.Rankine;
import main.java.trees.Hwind;
import main.java.trees.HwindData;
import main.java.trees.Tree;

import java.awt.*;
import java.util.Random;
import main.java.gui.GUInterface;
import main.java.trees.TreeGUI;

/**
 * Created by Krzysiek on 17/11/2014.
 */
public class Simulation {

	/**
	 * Wymiary Rozważanego terenu
	 */
	public static int forestLength = 1000, forestWidth = 1000;
    public static int iDist = 20, jDist = 20;

    private static Rankine vortex = new Rankine(0,0,Math.PI/8,0.1,0.1,0.1,0.1);
    private static Hwind hWindModel;
    private static TreeGUI[][] forest = new TreeGUI[forestLength][forestWidth]; // TODO ogarnąć tą tablicę i listę


	public Simulation(){//Rankine vortex, int forestLength, int forestWidth) {
        //this.vortex = vortex;
        //this.forestLength = forestLength;
        //this.forestWidth = forestWidth;
        //this.forest = new Tree[forestLength][forestWidth];
    }

	public static void fillForest() {
        int k = 0;
        forestListLen = forestLength * forestWidth / (iDist*jDist);
        forestList = new TreeGUI[forestListLen];
        for(int i = 0; i < forestLength; i+= iDist){
            for(int j = 0; j < forestWidth; j+= jDist) {
                forestList[k] = new TreeGUI(i-forestLength/2, j-forestWidth/2, 0, (int) (Math.random() * 10 + 10));
                k++;
            }
        }
    }

	//DO GUI
	private static TreeGUI[] forestList;
	private static int forestListLen = 0;
	
	private static void getForestRandom(){
		Random rand = new Random();
		int hvar = forestLength/2;
		int var = forestLength;
		forestListLen = 100;
		
		forestList = new TreeGUI[forestListLen];
		
		for(int i=0; i<forestListLen;i++){
			forestList[i] = new TreeGUI(
					rand.nextInt(var)-hvar,
					rand.nextInt(var)-hvar,
					0,rand.nextInt(10)+10);
			forestList[i].changeWind(rand.nextDouble(), 0);
		}
		//forestList[5].fall();
		//forestList[6].crack();
		
	}
	private static void getForestList(){
		int k = 0;
		forestListLen = forestLength*forestWidth;
		forestList = new TreeGUI[forestListLen];
		
		for(int i = 0; i < forestLength; ++i){
            for(int j = 0; j < forestWidth; ++j) {
                forestList[k] = forest[i][j];
				if(forest[i][j].fallen){
                    forestList[k].fall();
                    forestList[k].changeWind(
							1, -forest[i][j].windRotation / Math.PI);
                }else{
                    forestList[k].changeWind(
							0, -forest[i][j].windRotation/Math.PI);
                }
				k++;
            }
        }
	}

	public static void setDefaultHWindModel() {
        hWindModel = new Hwind(15. /*z dupy*/, new HwindData());
    }

	public static void printForest() {
        int k = 0;
        for(int i = 0; i < forestLength; i += iDist){
            for(int j = 0; j < forestWidth; j+= jDist) {
                if(vortex.getOrigin().equals(new Point(i-forestLength/2,j-forestWidth/2))) {
                   System.out.print("O");
                }
                else {
                   System.out.print(forestList[k].fallen ? "X" : "I");
                }
                k++;
            }
            System.out.println();
        }
    }

	public Rankine getVortex() {
        return vortex;
    }

	public int size() {
        return forestLength * forestWidth;
    }

	public void setVortex(Rankine vortex) {
        this.vortex = vortex;
    }

	public TreeGUI[][] getForest() {
        return forest;
    }
	
	public void setForest(TreeGUI[][] forest) {
        this.forest = forest;
    }

	public static void simulate() throws Exception{
        vortex.calculateNewCenter(1);
        if(vortex.getOrigin().x >= forestLength || vortex.getOrigin().y >= forestWidth) {
            throw new Exception("Vortex origin out of bounds: (" + vortex.getOrigin().x + "," + vortex.getOrigin().y + ")");
        }

        for(int i = 0; i < forestListLen; ++i)
            if( !forestList[i].fallen )
                hWindModel.calcTreeForce(forestList[i], vortex);
    }

	public static void simMain(GUInterface gui) {
//        Rankine wir = new Rankine(0,0,Math.PI/4,3,8,7,2);
        int maxTime = 1;
        //Simulation simulation = new Simulation(wir,20,20);
        fillForest();
        setDefaultHWindModel();
//        printForest();
        for(int i = 0; i < maxTime; ++i) {
            try {
                long startTime = System.nanoTime();
                simulate();
                long elapsedTime = System.nanoTime() - startTime;

                System.out.println("Total execution time of single sim on forest [ms]: "
                        + elapsedTime/1000000);


//                if (i % 3 == 0) {
//                    System.out.println("Iteration " + (i+1) + ":");
//                   printForest();
//                }
            } catch (Exception e) {
                System.out.println(e);
//                break;
            }
        }
        printForest();
//		getForestList(); //JACEK DAL
//		getForestRandom(); //JACEK DAL
		gui.printFrame(forestList, forestListLen);  //JACEK DAL
		
        System.out.println("Simulation ended");
    }

	// SYF DO TESTOW
	public static void onlyFrame(GUInterface gui) {
		gui.printFrame(forestList, forestListLen); 
	}
}
