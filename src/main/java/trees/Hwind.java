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

    private HwindData data;
    private Vector2d northVec = new Vector2d(0, 1);

	/**
	 *
	 * @param meanHeight
	 * @param data
	 */
	public Hwind(double meanHeight, HwindData data){
        this.data = data;
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
        double windForceSum = 0;
        Vector2d wind = rankine.calculateWind((int)tree.x, (int)tree.y);
        tree.windRotation = wind.angle(northVec);
//        System.out.println(wind.length() + " " + tree.windRotation);

        for(int i = 0; i < tree.height; ++i) {
            double windForce = calcWindForce(tree, wind.length(), i);
            bendingMoment += calcMaxBendingMoment(tree, windForce, i);
            windForceSum += windForce;
        }
        double treeResistance = calcTreeResist();
        double rootResistance = calcRootResist();

        tree.changeWind(Math.min(bendingMoment/rootResistance, 1), wind.angle(northVec)/Math.PI);

       System.out.println("["+tree.x +" " + tree.y + "] : " + bendingMoment + " " + treeResistance + " " + rootResistance);
        if(bendingMoment > treeResistance) {
            tree.crack();
            tree.height = tree.height / 2 + 1;  // w celu uproszczenia lamie sie na pol
        }
        if(bendingMoment > rootResistance)
            tree.fall();
    }

    private double calcWindForce(TreeGUI tree, double windVecLength, int seg) {
        int stemHeight = tree.height - data.crownHeight;

        double windFlowFactor;
        if(windVecLength <= 11.0) windFlowFactor = 0.2;
        else if(windVecLength > 20.0) windFlowFactor = 0.6;
        else windFlowFactor = 0.044444 * windVecLength - 0.28889;

        double res;
        if(seg >= stemHeight)
            res = 0.5 * data.friction * data.airDensity * Math.pow(windVecLength, 2) * triangleSectorArea(seg - stemHeight) * windFlowFactor;
        else
            res = 0.5 * data.friction * data.airDensity * Math.pow(windVecLength, 2) * data.diameter * windFlowFactor; // steam area as rectangle 1 x diameter
        return res;
    }

    private double calcDistFromForestEdge(TreeGUI tree){
        double minX = Math.min( Math.abs(tree.x), Math.abs(Simulation.forestLength - tree.x) );
        double minY = Math.min( Math.abs(tree.y), Math.abs(Simulation.forestWidth - tree.y) );
        return Math.min(minX, minY);
    }

    private double calcTreeResist() {
        return Math.PI/32 * data.mor * Math.pow(data.diameter, 3);
    }

    private double calcRootResist() {
        return (g * data.rootMass * data.rootDepth) / data.f_RW;
    }

    private double calcMaxBendingMoment(TreeGUI tree, double windForce, int seg) {
        double maxMeanDist = maxMeanDistProportion();
        double maxBendMom = maxMeanBendMomentPropotion(tree);
//        double wind = calcWindForce(tree, rankine, seg);
        double crownDev = crownDeviation(tree, windForce, seg);

        return maxMeanDist * maxBendMom *
                (windForce + gravityForce() * crownDev);
    }

    private double gravityForce() {
        return data.crownMass * g;
    }

    private double maxMeanBendMomentPropotion(TreeGUI tree) {
        double div = data.spacing / meanHeight;
        double dist = calcDistFromForestEdge(tree);


        double Gust_mean = (0.68*div - 0.0385) + (-0.68*div + 0.4875) *
                Math.pow(1.7239*div + 0.0316, dist/meanHeight);
        double Gust_max = (2.7193*div - 0.061) + (-1.273*div + 9.9701) *
                Math.pow(1.1127*div + 0.0311, dist/meanHeight);

        return Gust_max/Gust_mean;
    }

    private double maxMeanDistProportion() {
        double Gap_mean = (0.001 + 0.001*Math.pow(data.freeFrontSpace, 0.562)) / 0.00465;
        double Gap_max = (0.0072 + 0.0064*Math.pow(data.freeFrontSpace, 0.3467)) / 0.0214;

        return Gap_max / Gap_mean;
    }

    private double crownDeviation(TreeGUI tree, double windForce, int seg) {
        double crownMidH = tree.height - data.crownHeight/2;
        double seg2topDist = tree.height - seg;
        double top2midDist = data.crownHeight/2;

        double I = Math.PI * Math.pow(data.diameter, 4) / 64;
        if( seg <= crownMidH) {
            double val1 = windForce * Math.pow(crownMidH, 2) * tree.height;
            double val2 = (3 - crownMidH / tree.height - 3 * seg2topDist / tree.height);
            return (val1 *  val2) / (6 * data.moe * I);
        }
        else
            return (windForce * Math.pow(crownMidH, 3) * (2 - 3*(seg2topDist - top2midDist)/crownMidH +
                    Math.pow((seg2topDist - top2midDist), 3)/Math.pow(crownMidH, 3))) / (6*data.moe*I);
    }

    private double triangleSectorArea(int seg){
        double dx = 0.5 * data.crownWidth / data.crownHeight;
        double xLeft = 0.;
        double xRight = data.crownWidth;

        double xLeftBase = xLeft + seg * dx;
        double xLeftTop = xLeft + (seg+1) * dx;
        double xRightBase = xRight - seg * dx;
        double xRightTop = xRight - (seg+1) * dx;

        double a = xRightBase - xLeftBase;
        double b = xRightTop - xLeftTop;
        return (a+b)*0.5;
    }
}