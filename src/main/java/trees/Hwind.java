package main.java.trees;

import main.java.rankine.*;
import main.java.trees.TreeGUI;
import main.java.simulation.*;

import javax.vecmath.Vector2d;
import java.util.LinkedList;

/**
 * Created by Mariusz on 2014-11-15.
 */
public class Hwind {

    private final double g = 9.81;
    private double meanHeight;

    private Vector2d northVec = new Vector2d(0, 1);

	/**
	 *
	 * @param meanHeight
	 * @param data
	 */
	public Hwind(double meanHeight){
        this.meanHeight = meanHeight;
    }

	/**
	 *
	 * @param tree
	 * @param rankine
	 * @return
	 */
	public void calcTreeForce(TreeGUI tree, Rankine rankine){
        double bendingMoment = 0;
        Vector2d wind = rankine.calculateWind((int)tree.x, (int)tree.y);
        tree.windRotation = northVec.angle(wind);
        System.out.println(wind.length() + "\t" + tree.windRotation + "\t" + rankine.getAngle());

        for(int i = 0; i < tree.height; ++i) {
            double windForce = calcWindForce(tree, wind.length(), i);
            bendingMoment += calcMaxBendingMoment(tree, windForce, i);

            if(tree.cracked && i > 4) break; // todo - uwzglednienie zlamania drzewa przy liczeniu sily
        }
        double treeResistance = calcTreeResist(tree);
        double rootResistance = calcRootResist(tree);

//        tree.changeWind(Math.min(bendingMoment/rootResistance, 1), wind.angle(northVec)/Math.PI);

//       System.out.println("["+tree.x +" " + tree.y + "] : " + bendingMoment + " " + treeResistance + " " + rootResistance);
        if(bendingMoment > treeResistance) {
            tree.crack();
//            tree.height = tree.height / 2 + 1;  // w celu uproszczenia lamie sie na pol
        }
        if(bendingMoment > rootResistance)
            tree.fall();
    }

    private double calcWindForce(TreeGUI tree, double windVecLength, int seg) {
        int stemHeight = tree.height - tree.crownHeight;

        double windFlowFactor;
        if(windVecLength <= 11.0) windFlowFactor = 0.2;
        else if(windVecLength > 20.0) windFlowFactor = 0.6;
        else windFlowFactor = 0.044444 * windVecLength - 0.28889;

        double res;
        if(seg >= stemHeight)
            res = 0.5 * HwindData.friction * HwindData.airDensity * Math.pow(windVecLength, 2) * triangleSectorArea(tree, seg - stemHeight) * windFlowFactor;
        else
            res = 0.5 * HwindData.friction * HwindData.airDensity * Math.pow(windVecLength, 2) *  tree.diameter * windFlowFactor; // steam area as rectangle 1 x diameter
        return res;
    }

    private double calcDistFromForestEdge(TreeGUI tree){
        double minX = Math.min( Math.abs(tree.x), Math.abs(Forest.width - tree.x) );
        double minY = Math.min( Math.abs(tree.y), Math.abs(Forest.height - tree.y) );
        return Math.min(minX, minY);
    }

    private double calcTreeResist(TreeGUI tree) {
        return Math.PI/32 * HwindData.mor * Math.pow( tree.diameter, 3);
    }

    private double calcRootResist(TreeGUI tree) {
        return (g *  tree.rootMass *  tree.rootDepth) / HwindData.f_RW;
    }

    private double calcMaxBendingMoment(TreeGUI tree, double windForce, int seg) {
        double maxMeanDist = maxMeanDistProportion();
        double maxBendMom = maxMeanBendMomentPropotion(tree);
//        double wind = calcWindForce(tree, rankine, seg);
        double crownDev = crownDeviation(tree, windForce, seg);

        return maxMeanDist * maxBendMom *
                (windForce + gravityForce(tree) * crownDev);
    }

    private double gravityForce(TreeGUI tree) {
        return tree.crownMass * g;
    }

    private double maxMeanBendMomentPropotion(TreeGUI tree) {
        double div = HwindData.spacing / meanHeight;
        double dist = calcDistFromForestEdge(tree);


        double Gust_mean = (0.68*div - 0.0385) + (-0.68*div + 0.4875) *
                Math.pow(1.7239*div + 0.0316, dist/meanHeight);
        double Gust_max = (2.7193*div - 0.061) + (-1.273*div + 9.9701) *
                Math.pow(1.1127*div + 0.0311, dist/meanHeight);

        return Gust_max/Gust_mean;
    }

    private double maxMeanDistProportion() {
        double Gap_mean = (0.001 + 0.001*Math.pow(HwindData.freeFrontSpace, 0.562)) / 0.00465;
        double Gap_max = (0.0072 + 0.0064*Math.pow(HwindData.freeFrontSpace, 0.3467)) / 0.0214;

        return Gap_max / Gap_mean;
    }

    private double crownDeviation(TreeGUI tree, double windForce, int seg) {
        double crownMidH = tree.height - tree.crownHeight/2;
        double seg2topDist = tree.height - seg;
        double top2midDist = tree.crownHeight/2;

        double I = Math.PI * Math.pow(tree.diameter, 4) / 64;
        if( seg <= crownMidH) {
            double val1 = windForce * Math.pow(crownMidH, 2) * tree.height;
            double val2 = (3 - crownMidH / tree.height - 3 * seg2topDist / tree.height);
            return (val1 *  val2) / (6 * HwindData.moe * I);
        }
        else
            return (windForce * Math.pow(crownMidH, 3) * (2 - 3*(seg2topDist - top2midDist)/crownMidH +
                    Math.pow((seg2topDist - top2midDist), 3)/Math.pow(crownMidH, 3))) / (6*HwindData.moe*I);
    }

    private double triangleSectorArea(TreeGUI tree, int seg){
        double dx = 0.5 * tree.crownWidth / tree.crownHeight;
        double xLeft = 0.;
        double xRight = tree.crownWidth;

        double xLeftBase = xLeft + seg * dx;
        double xLeftTop = xLeft + (seg+1) * dx;
        double xRightBase = xRight - seg * dx;
        double xRightTop = xRight - (seg+1) * dx;

        double a = xRightBase - xLeftBase;
        double b = xRightTop - xLeftTop;
        return (a+b)*0.5;
    }
}