package net.arwix.astronomy.math;

/**
 * PointD holds two double coordinates
 */
public class PointD {
    public double x;
    public double y;

    public PointD() {
    }

    public PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public final void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the point's x and y coordinates to the coordinates of p
     */
    public final void set(PointD p) {
        this.x = p.x;
        this.y = p.y;
    }

    public final void negate() {
        x = -x;
        y = -y;
    }

    public final void offset(double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(double x, double y) {
        return this.x == x && this.y == y;
    }

    /**
     * Return the euclidean distance from (0,0) to the point
     */
    public final double length() {
        return length(x, y);
    }

    /**
     * Returns the euclidean distance from (0,0) to (x,y)
     */
    public static double length(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}