/******************************************************************************
 *  Compilation:    javac-algs4 FastCollinearPoints.java
 *  Execution:      java-algs4 FastCollinearPoints input_file
 *
 *  Description:
 ******************************************************************************/

// standard libraries developed for use in algs4
// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.Out;

// system libraries
import java.util.Arrays;
import java.util.LinkedList;


public class FastCollinearPoints {
    private final boolean isDebugging = false;
    private int nPoints;
    private int nSegments;
    private LinkedList<LineSegment> lineSegList;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // check input
        if (points == null) throw new NullPointerException(
            "the argument to the constructor is null");
        
        // check points
        nPoints = 0;
        for (Point p : points) {
            if (p == null) throw new NullPointerException(
                "some point in the array is null");
            ++nPoints;
        }
        
        // check repeated points
        for (int i = 1; i < nPoints; ++i) {
            if (points[0].compareTo(points[i]) == 0)
                throw new IllegalArgumentException(
                    "input contains a repeated point");
        }
        
        // make sure points[0] <= ... <= points[nPoints - 1]
        Arrays.sort(points);
        if (isDebugging) {
            for (Point pt : points) {
                StdOut.println(pt);
            }
        }
        
        nSegments = 0;
        lineSegList = new LinkedList<LineSegment>();
        
        // Think of p as the origin.
        for (int i = 0; i < nPoints - 1; ++i) {
            if (isDebugging) StdOut.println("i: " + i);
            // For each other point q, determine the slope it makes with p.
            // Sort the points according to the slopes they makes with p.
            Point p = points[i];
            Arrays.sort(points, p.slopeOrder());
            
            // Check if any 3 (or more) adjacent points in the sorted order have
            // equal slopes with respect to p. If so, these points, together
            // with p, are collinear.
            LinkedList<Integer> idStack = new LinkedList<Integer>();
            
            int first = 1;
            double slope1 = p.slopeTo(points[first]);
            idStack.addLast(first);
            if (isDebugging) StdOut.println(first + " in");

            int j = 2;
            for (; j < nPoints; ++j) {
                double slope2 = p.slopeTo(points[j]);
                if (slope2 != slope1) {
                    // when find different slope
                    // check # of collinear points
                    if (idStack.size() >= 3) {
                        if (isDebugging)
                            StdOut.println("Stack.size(): " + idStack.size());
                        first = idStack.getFirst();
                        if (p.compareTo(points[first]) < 0) {
                            LineSegment lineSeg = new LineSegment(
                                p, points[j - 1]);
                            lineSegList.addLast(lineSeg);
                            ++nSegments;
                        }
                    }
                    slope1 = slope2;
                    if (isDebugging) StdOut.println((j - 1) + " out");
                    idStack.clear();
                }
                idStack.addLast(j);
                if (isDebugging) StdOut.println(j + " in");
            } // p-loop
            
            // when j == nPoints
            // check # of collinear points
            if (idStack.size() >= 3) {
                if (isDebugging)
                    StdOut.println("Stack.size(): " + idStack.size());
                first = idStack.getFirst();
                if (p.compareTo(points[first]) < 0) {
                    LineSegment lineSeg = new LineSegment(p, points[j - 1]);
                    lineSegList.addLast(lineSeg);
                    ++nSegments;
                }
            }
            
            // recover natural order of points[]
            Arrays.sort(points);
            
        } // p-loop
        
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return nSegments;
    }
    
    // the line segments
    /**
     *  The method segments() should include each maximal line segment 
     *  containing 4 (or more) points exactly once. For example, if 5 points 
     *  appear on a line segment in the order p→q→r→s→t, then do not include the
     *  subsegments p→s or q→t.
     */
    public LineSegment[] segments() {
        return lineSegList.toArray(new LineSegment[nSegments]);
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
