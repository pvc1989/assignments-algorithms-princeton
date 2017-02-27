/******************************************************************************
 *  Compilation:  javac-algs4 PointSET.java
 *  Execution:    javac-algs4 PointSET filename
 *  Dependencies: 
 *
 *  a mutable data type that represents a set of points in the unit square using
 *  a red-black BST.
 *
 *  Corner cases:
 *      Throw a java.lang.NullPointerException if any argument is null.
 *
 *  Performance requirements:
 *      Your implementation should support insert() and contains() in time 
 *      proportional to the logarithm of the number of points in the set in the 
 *      worst case; it should support nearest() and range() in time proportional
 *      to the number of points in the set.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;
import java.util.LinkedList;

public class PointSET {
    private TreeSet<Point2D> pointSet;
    private int nPoint;
    
        // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<Point2D>();
        nPoint = 0;
    }
        // is the set empty?
    public boolean isEmpty() {
        return (nPoint == 0);
    }
        // number of points in the set
    public int size() {
        return nPoint;
    }
    
    private void check(Object obj) {
        if (obj == null) throw new java.lang.NullPointerException(
            "argument is null");
    }
        // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        check(p);
        pointSet.add(p);
        ++nPoint;
    }
        // does the set contain point p?
    public boolean contains(Point2D p) {
        check(p);
        return pointSet.contains(p);
    }
        // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : pointSet) {
            StdDraw.point(p.x(), p.y());
        }
    }
        // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        check(rect);
            // create an empty queue for Point2D
        LinkedList<Point2D> queuePoint2D = new LinkedList<Point2D>();
        for (Point2D p : pointSet) {
            if (rect.contains(p))
                queuePoint2D.addLast(p);
        }
        return queuePoint2D;
    }
        // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        check(p);
        if (pointSet.isEmpty()) return null;
        Point2D nearestPoint = pointSet.first();
        double minDistSq = p.distanceSquaredTo(nearestPoint);
        for (Point2D q : pointSet) {
            if (p.distanceSquaredTo(q) < minDistSq) {
                nearestPoint = q;
                minDistSq = p.distanceSquaredTo(q);
            }
        }
        return nearestPoint;
    }
        // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        
        StdDraw.enableDoubleBuffering();
        
            // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
//        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
            brute.insert(p);
        }
        
        while (true) {
                // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);
            
                // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();
            
                // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);
            
                // draw in blue the nearest neighbor (using kd-tree algorithm)
//            StdDraw.setPenColor(StdDraw.BLUE);
//            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
