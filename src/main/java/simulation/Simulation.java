package simulation;

import rankine.Rankine;
import trees.Hwind;
import trees.HwindData;
import trees.Tree;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Krzysiek on 17/11/2014.
 */
public class Simulation {

    private Rankine vortex;
    private Hwind hWindModel;
    private final int forestLength;
    private final int forestWidth;
    private Tree[][] forest;

    public Simulation(Rankine vortex, int forestLength, int forestWidth) {
        this.vortex = vortex;
        this.forestLength = forestLength;
        this.forestWidth = forestWidth;
        this.forest = new Tree[forestLength][forestWidth];
    }

    public void fillForest() {
        for(int i = 0; i < forestLength; ++i){
            for(int j = 0; j < forestWidth; ++j) {
                forest[i][j] = new Tree(new Point(i, j), (int) (Math.random() * 10 + 10));
            }
        }
    }

    public void setDefaultHWindModel() {
        this.hWindModel = new Hwind(forest, new HwindData());
    }

    public void printForest() {
        for(int i = 0; i < forestLength; ++i){
            for(int j = 0; j < forestWidth; ++j) {
                if(vortex.getOrigin().equals(new Point(i,j))) {
                    System.out.print("O");
                }
                else {
                    System.out.print(forest[i][j].isFallen ? "X" : "I");
                }
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

    public Tree[][] getForest() {
        return forest;
    }

    public void setForest(Tree[][] forest) {
        this.forest = forest;
    }

    public void simulate() throws Exception{
        vortex.calculateNewCenter(1);
        if(vortex.getOrigin().x >= forestLength || vortex.getOrigin().y >= forestWidth) {
            throw new Exception("Vortex origin out of bounds: (" + vortex.getOrigin().x + "," + vortex.getOrigin().y + ")");
        }
        for(int i = 0; i < forestLength; ++i){
            for(int j = 0; j < forestWidth; ++j) {
                hWindModel.calcTreeForce(forest[i][j], vortex);
            }
        }
    }

    public static void main(String[] args) {
        Rankine wir = new Rankine(0,0,Math.PI/4,3,8,7,2);
        int maxTime = 30;
        Simulation simulation = new Simulation(wir,20,20);
        simulation.fillForest();
        simulation.setDefaultHWindModel();
        simulation.printForest();
        for(int i = 0; i < maxTime; ++i) {
            try {
                simulation.simulate();
                if (i % 3 == 0) {
                    System.out.println("Iteration " + (i+1) + ":");
                    simulation.printForest();
                }
            } catch (Exception e) {
                System.out.println(e);
                break;
            }
        }
        System.out.println("Simulation ended");
    }
}
