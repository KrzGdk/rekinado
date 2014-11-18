package main.java.trees;

import java.awt.Point;

/**
 * Created by Mariusz on 2014-11-16.
 */
public class Tree {
    public Point position;
    public double lastWindAngle = -1; /** -1 at start, angle in radians*/
    public boolean isFallen = false;
    public int height;

    public Tree(Point pos, int height){
        this.position = pos;
        this.height = height;
    }

    //public SomeTreeType treeType;
}
