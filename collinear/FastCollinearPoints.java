/******************************************************************************
 *  Compilation:    javac-algs4 FastCollinearPoints.java
 *  Execution:      java-algs4 FastCollinearPoints input_file
 *
 *  Description:
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {
    private final Point[] itsPoints;
    private final LinkedList<LineSegment> itsLineSegments;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)  // detect null input
            throw new IllegalArgumentException("The input is null.");
        final int nPoints = points.length;
        itsPoints = new Point[nPoints];
        for (int i = 0; i != nPoints; ++i) {
            if (points[i] == null)  // detect null point
                throw new IllegalArgumentException(
                    "Some point in the array is null.");
            itsPoints[i] = points[i];
        }
        Arrays.sort(itsPoints);
        for (int i = 1; i != nPoints; ++i) {
            if (itsPoints[i-1].compareTo(itsPoints[i]) == 0)
                throw new IllegalArgumentException(
                    "The input contains a repeated point.");
        }
        itsLineSegments = new LinkedList<LineSegment>();
        findSegments();
    }
    private void findSegments() {
        final int nPoints = itsPoints.length;
        final Point[] points = itsPoints.clone();
        for (Point p : itsPoints) {
            // Sort the points according to the slopes they makes with p.
            Arrays.sort(points, p.slopeOrder());
            // According to the specification, the slope of a degenerate line 
            // segment (between a point and itself) is negative infinity,
            Point q = points[0];
            assert (p == q);  // p must be the min
            // If any 3 (or more) adjacent points in the sorted order have equal
            // slopes with respect to p, then these points, together with p, are
            // collinear.
            // initial state {p}:
            int count = 1;
            Point qMin = p;
            Point qMax = p;
            double lastSlope = p.slopeTo(p);
            for (int i = 1; i != nPoints; ++i) {
                q = points[i];
                double nextSlope = p.slopeTo(q);
                if (lastSlope == nextSlope) {
                    ++count;
                    if (q.compareTo(qMin) < 0) qMin = q;
                    if (q.compareTo(qMax) > 0) qMax = q;
                }
                else {  // encounter a different slope
                    updateLineSegments(p, qMin, qMax, count);
                    // set to {p, q}:
                    count = 2;
                    if (p.compareTo(q) < 0) {
                        qMin = p;
                        qMax = q;
                    }
                    else {
                        qMin = q;
                        qMax = p;
                    }
                    lastSlope = nextSlope;
                }
            }  // q-loop
            updateLineSegments(p, qMin, qMax, count);     
        }  // p-loop
    }
    private void updateLineSegments(Point p, Point qMin, Point qMax, int count) {
        if (count >= 4 && p == qMin) {
            itsLineSegments.add(new LineSegment(p, qMax));
        } 
    }
    /**
     * the number of line segments
     */
    public int numberOfSegments() {
        return itsLineSegments.size();
    }
    /**
     *  The method segments() should include each maximal line segment 
     *  containing 4 (or more) points exactly once. For example, if 5 points 
     *  appear on a line segment in the order p→q→r→s→t, then do not include the
     *  subsegments p→s or q→t.
     */
    public LineSegment[] segments() {
        return itsLineSegments.toArray(new LineSegment[numberOfSegments()]);
    }
    /**
     *  This client program takes the name of an input file as a command-line
     *  argument; read the input file (in the format specified below); prints to
     *  standard output the line segments that your program discovers, one per 
     *  line; and draws to standard draw the line segments.
     */
    public static void main(String[] args) {
        
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.RED);
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        StdDraw.setPenRadius(0.003);
        StdDraw.setPenColor(StdDraw.BLACK);
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("# Segments: " + collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
