/******************************************************************************
 *  Compilation:  javac-algs4 PointSET.java
 *  Execution:    java-algs4 PointSET
 *  Dependencies: 
 * 
 *  $ java-algs4 PointSET < circle4.txt
 *  Size: 4
 *  $ java-algs4 KdTreeGenerator 5 | java-algs4 PointSET
 *  Size: 5
 * 
 ******************************************************************************/

import java.util.TreeSet;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private TreeSet<Point2D> pointSet;
    
    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }
    // is the set empty?
    public boolean isEmpty() {
        return (size() == 0);
    }
    // number of points in the set
    public int size() {
        return pointSet.size();
    }
    private void check(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        check(p);
        pointSet.add(p);
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
        PointSET points = new PointSET();
        while (!StdIn.isEmpty()) {
            points.insert(new Point2D(/* x= */StdIn.readDouble(),
                                      /* y= */StdIn.readDouble()));
        }
        StdOut.printf("Size: %d\n", points.size());
    }
}