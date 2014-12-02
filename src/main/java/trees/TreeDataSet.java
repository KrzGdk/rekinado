package main.java.trees;

/**
 * Created by Mariusz on 2014-12-02.
 */
public class TreeDataSet {
    public int height;
    public double diameter;
    public double crownMass;

    public double rootMass;
    public double rootDepth;
    public int crownHeight;
    public double crownWidth;

    public TreeDataSet(int height, double diameter, double crownMass, int crownHeight, double crownWidth,
                double rootMass, double rootDepth){
        this.height = height;
        this.diameter = diameter;
        this.crownHeight = crownHeight;
        this.crownWidth = crownWidth;
        this.rootDepth = rootDepth;
        this.crownMass = crownMass;
        this.rootMass = rootMass;
    }
}
