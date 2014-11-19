package rankine;


import main.java.trees.*;
import java.util.LinkedList;

import java.awt.Point;
import javax.vecmath.Vector2d;

/**
 * Created by Krzysiek on 13/11/2014.
 */
public class Rankine {

    /**
     * Punkt srodka tornada
     */
    private Point origin = new Point(0, 0);
    /**
     * Kierunek rozchodzenia sie tornada
     */
    private double angle = 0.0;
    /**
     * Promien maksymalny
     */
    private double R_max = 1.0;
    /**
     * Predkosc trawersalna maksymalna
     */
    private double Vfi_max = 0.0;
    /**
     * Predkosc radialna maksymalna
     */
    private double Vr_max = 0.0;
    /**
     * Predkosc translacji
     */
    private double V_tr = 0.0;

    public Rankine() {
    }

    public Rankine(int x, int y, double angle, double R_max, double Vfi_max, double Vr_max, double V_tr) {
        this.angle = angle;
        this.R_max = R_max;
        this.Vfi_max = Vfi_max;
        this.Vr_max = Vr_max;
        this.origin.x = x;
        this.origin.y = y;
        this.V_tr = V_tr;
    }

    public void setOrigin(Point o) {
        origin = o;
    }

    public void setOrigin(int x, int y) {
        origin.x = x;
        origin.y = y;
    }

    public Point getOrigin() {
        return origin;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void calculateNewCenter(int deltaT) {
        double s = V_tr * deltaT;
        double deltaY = s * Math.cos(angle);
        double deltaX = s * Math.sin(angle);
        origin.x += deltaX;
        origin.y += deltaY; // mozna zamienic na - w razie czego
    }

    public Vector2d calculateWind(int x, int y) {
        double r = origin.distance(x, y);
        double Vfi, Vr;
        if (r <= R_max) {
            Vfi = Vfi_max * r / R_max;
            Vr = Vr_max * Math.pow(r / R_max, 0.6);
        } else {
            Vfi = Vfi_max * R_max / r;
            Vr = Vr_max * Math.pow(R_max / r, 0.6);
        }
        return new Vector2d(Vfi + V_tr, Vr);
    }

}
