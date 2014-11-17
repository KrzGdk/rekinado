package trees;


import rankine.*;

import javax.vecmath.Vector2d;
import java.util.LinkedList;

/**
 * Created by Mariusz on 2014-11-15.
 */
public class Hwind {

    private final double g = 9.81;
    private double meanHeight;
    private double forestMaxX;
    private double forestMaxY;

    private HwindData data;
    private Vector2d northVec = new Vector2d(0, 1);

    public Hwind(LinkedList<Tree> forest, HwindData data){
        this.data = data;
        double heightSum = 0;

        for(Tree t : forest) {
            heightSum += t.height;
            forestMaxX = Math.max(forestMaxX, t.position.getX());
            forestMaxY = Math.max(forestMaxY, t.position.getY());
        }
        meanHeight = heightSum / forest.size();
    }

    public double calcTreeForce(Tree tree, Rankine rankine){
        double bendingMoment = 0;

        for(int i = 0; i < tree.height; ++i) {
            bendingMoment += calcMaxBendingMoment(tree, rankine, i);
        }
        double treeResitance = calcTreeResist();
        double rootResistance = calcRootResist();

        System.out.println("Result:");
        System.out.println("wind speed: " + rankine.calculateWind(tree.position.x, tree.position.y).length());
        System.out.println("bending moment: " + bendingMoment);
        System.out.println("tree resistance: " + treeResitance);
        System.out.println("root resistance: " + rootResistance);

        if(bendingMoment > treeResitance || bendingMoment > rootResistance)
            tree.isFallen = true;

        return Math.min(bendingMoment - treeResitance, bendingMoment - rootResistance); // for testing
    }

    private double calcWindForce(Tree tree, Rankine rankine, int seg){
        Vector2d wind = rankine.calculateWind(tree.position.x, tree.position.y);
        tree.lastWindAngle = wind.angle(northVec);
        double v = wind.length();
        int stemHeight = tree.height - data.crownHeight;

        double windFlowFactor;
        if( v <= 11.) windFlowFactor = 0.2;
        else if( v > 20.) windFlowFactor = 0.6;
        else windFlowFactor = 0.044444 * v - 0.28889;

        double res;
        if(seg >= stemHeight)
            res = 0.5 * data.friction * data.airDensity * Math.pow(v, 2) * triangleSectorArea(seg - stemHeight) * windFlowFactor;
        else
            res = 0.5 * data.friction * data.airDensity * Math.pow(v, 2) * data.diameter * windFlowFactor; // steam area as rectangle 1 x diameter
        return res;
    }

    private double calcDistFromForestEdge(Tree tree){
        double minX = Math.min(tree.position.getX(), forestMaxX - tree.position.getX());
        double minY = Math.min(tree.position.getY(), forestMaxY - tree.position.getY());
        return Math.min(minX, minY);
    }

    private double calcTreeResist() {
        return Math.PI/32 * data.mor * Math.pow(data.diameter, 3);
    }

    private double calcRootResist() {
        return (g * data.rootMass * data.rootDepth) / data.f_RW;
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

    private double calcMaxBendingMoment(Tree tree,Rankine rankine, int seg) {
        double maxMeanDist = maxMeanDistProportion();
        double maxBendMom = maxMeanBendMomentPropotion(tree);
        double wind = calcWindForce(tree, rankine, seg);
        double crownDev = crownDeviation(tree, rankine, seg);

        return maxMeanDist * maxBendMom *
                (wind + gravityForce() * crownDev);
    }

    private double gravityForce() {
        return data.crownMass * g;
    }

    private double maxMeanBendMomentPropotion(Tree tree) {
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

    private double crownDeviation(Tree tree, Rankine rankine, int seg) {
        double a = tree.height - data.crownHeight/2;
        double l = tree.height - seg;
        double b = data.crownHeight/2;

        double Fw = 0;
        for(int i = 0; i < tree.height; i++) {
            Fw += calcWindForce(tree, rankine, i);
        }

        double I = Math.PI * Math.pow(data.diameter, 4) / 64;
        if( seg <= a)
            return (Fw * a*a*tree.height *( 3- a/tree.height - 3*l/tree.height)) / (6 * data.moe * I);
        else
            return (Fw * Math.pow(a,3) *(2-3*(l-b)/a+Math.pow((l-b),3)/Math.pow(a,3))) / (6 * data.moe * I);
    }
}