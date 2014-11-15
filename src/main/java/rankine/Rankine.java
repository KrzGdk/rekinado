package main.java.rankine;

import java.awt.Point;
import javax.vecmath.Vector2d;
/**
 * Created by Krzysiek on 13/11/2014.
 */
public class Rankine {

    /**
     * Punkt srodka tornada
     */
    private Point origin = new Point(0,0);
    /**
     * Kierunek rozchodzenia sie tornada
     */
    private double angle;
    /**
     * Promien maksymalny
     */
    private double R_max;
    /**
     * Predkosc trawersalna maksymalna
     */
    private double Vfi_max;
    /**
     * Predkosc radialna maksymalna
     */
    private double Vr_max;

    public Rankine() { }
    public Rankine(int x, int y, double angle, double R_max, double Vfi_max, double Vr_max) {
        this.angle = angle;
        this.R_max = R_max;
        this.Vfi_max = Vfi_max;
        this.Vr_max = Vr_max;
        this.origin.x = x;
        this.origin.y = y;
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

    public void calculateNewCenter(int t) {

    }

    public Vector2d calculateWind(int x, int y) {
        return new Vector2d(1.2, 2.5);
    }

    public static void main(String[] args) {
        Rankine wir = new Rankine();
        System.out.println(wir.calculateWind(0,0).toString());
    }


}
