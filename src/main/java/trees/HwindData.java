package main.java.trees;

import main.java.trees.TreeDataSet;

import java.util.Random;

/**
 * Created by Mariusz on 2014-11-16.
 */

/**
 * todo remove hardcoded values
 */
public class HwindData {
    public static TreeDataSet treeData [] = {
        new TreeDataSet(12, 0.100,  13, 5, 3.1,  345., 0.44), // h | d | crownM | crownH | crownW | rootM | rootD
        new TreeDataSet(12, 0.120,  18, 5, 3.5,  485., 0.48),
        new TreeDataSet(12, 0.150,  28, 5, 4.0,  719., 0.55),
        new TreeDataSet(16, 0.133,  28, 7, 3.7,  573., 0.51),
        new TreeDataSet(16, 0.160,  40, 7, 4.2,  817., 0.57),
        new TreeDataSet(16, 0.200,  63, 7, 5.0, 1261., 0.64),
        new TreeDataSet(20, 0.167,  52, 8, 4.3,  887., 0.58),
        new TreeDataSet(20, 0.200,  74, 8, 5.0, 1252., 0.64),
        new TreeDataSet(20, 0.250, 116, 8, 5.9, 1988., 0.70)
    };

    public static double f_RW = 0.3;         /** stosunek wagi gleby wokol korzeni do masy calego drzewa*/
    public static double mor = 39.1 * 1000. * 1000.;          /** wspolczynnik pekania drewna */
    public static double moe = 7000. * 1000. * 1000.;          /** wspolczynnik elastycznosci */
    public static double friction = 0.29;     /** wspolczynnik tarcia */

//    public double stemMass;
	public static double freeFrontSpace = 15; /** wolna przestrzen przed sciana lasu */
    public static double airDensity = 1.226;
    public static double spacing = 6;

    private static int sumHeight = 0;
    private static int generatedTreeCount = 0;

    public static TreeDataSet randData(){
        Random rand = new Random();
        int id = rand.nextInt(treeData.length);
        TreeDataSet temp = treeData[id];

        sumHeight += temp.height;
        generatedTreeCount++;   // do sredniej wysokosci drzewa w lesie

        return temp;
    }
    public static void registerFallenTree(TreeGUI tree) {
        if(tree.fallen) {
            sumHeight -= tree.height;
            generatedTreeCount--;
        }
    }

    public static double getMeanHeight() {
        return sumHeight / generatedTreeCount;
    }
}
