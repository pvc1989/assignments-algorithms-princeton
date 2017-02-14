/******************************************************************************
 *  Compilation:  javac-algs4 Point.java
 *  Execution:    java-algs4 Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

// standard libraries developed for use in algs4
// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.Out;

// system libraries
// import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {
    
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point
    
    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }
    
    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }
    
    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    
    /**
     * Returns the slope between this point and the specified point. Formally, 
     * if the two points are (x0, y0) and (x1, y1), then the slope is 
     * (y1 - y0) / (x1 - x0). 
     * For completeness, the slope is defined to be 
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical; and 
     * Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        int dx = x - that.x;
        int dy = y - that.y;
        
        // two points are equal
        if (dy == 0 && dx == 0) return Double.NEGATIVE_INFINITY;
        
        // the line segment connecting the two points is horizontal
        // but the two points are NOT equal
        if (dy == 0 && dx != 0) return +0.0;

        // the line segment connecting the two points is vertical
        // but the two points are NOT equal
        if (dy != 0 && dx == 0) return Double.POSITIVE_INFINITY;
        
        return (double) dy / dx;
    }
    
    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        int dx = x - that.x;
        int dy = y - that.y;
        if (dy < 0) return -1;
        else if (dy > 0) return +1;
        else if (dx < 0) return -1;
        else if (dx > 0) return +1;
        else return 0;
    }
    
    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }
    
    /**
     * a private nested (non-static) class SlopeOrder that implements the 
     * Comparator<Point> interface. This class has a single method 
     * compare(q1, q2) that compares the slopes that q1 and q2 make with the 
     * invoking object p. 
     * the slopeOrder() method should create an instance of this nested class 
     * and return it.
     */
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point q1, Point q2) {
            double slope1 = slopeTo(q1);
            double slope2 = slopeTo(q2);
            if (slope1 < slope2) return -1;
            else if (slope1 > slope2) return +1;
            else return 0;
        }
    }
    
    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
    
    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // test constructor
        Point p0 = new Point(0, 0);
        Point p1 = new Point(1, 0);
        Point p2 = new Point(1, 1);
        Point p3 = new Point(0, 1);
        Point p4 = new Point(-1, 0);
        Point p5 = new Point(0, -1);
        // test slopeTo()
        StdOut.println(p0.slopeTo(p0));
        StdOut.println(p0.slopeTo(p1));
        StdOut.println(p0.slopeTo(p2));
        StdOut.println(p0.slopeTo(p3));
        StdOut.println(p0.slopeTo(p4));
        StdOut.println(p0.slopeTo(p5));
        // test compareTo()
        StdOut.println(p0.compareTo(p0));
        StdOut.println(p0.compareTo(p1));
        StdOut.println(p0.compareTo(p2));
        StdOut.println(p0.compareTo(p3));
        StdOut.println(p0.compareTo(p4));
        StdOut.println(p0.compareTo(p5));
    }
}
