/******************************************************************************
 *  Compilation:    javac-algs4 BruteCollinearPoints.java
 *  Execution:      java-algs4 BruteCollinearPoints input_file
 *
 *  Description:    examines 4 points at a time and checks whether they all lie 
 *      on the same line segment, returning all such line segments. 
 *      To check whether the 4 points p, q, r, and s are collinear, check 
 *      whether the three slopes between p and q, between p and r, and between p
 *      and s are all equal.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;

public class BruteCollinearPoints {
    private final Point[] itsPoints;
    private final LinkedList<LineSegment> itsLineSegments;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        java.util.Arrays.sort(itsPoints);
        for (int i = 1; i != nPoints; ++i) {
            if (itsPoints[i-1].compareTo(itsPoints[i]) == 0)
                throw new IllegalArgumentException(
                    "The input contains a repeated point.");
        }
        itsLineSegments = new LinkedList<LineSegment>();
        findSegments();
    }
    private void findSegments() {
        if (itsPoints.length < 4)
            return;
        final int nPoints = itsPoints.length;
        double pqSlope, prSlope, psSlope;
        for (int p = 0; p != nPoints; ++p) {
            for (int q = p + 1; q != nPoints; ++q) {
                pqSlope = itsPoints[p].slopeTo(itsPoints[q]);
                for (int r = q + 1; r != nPoints; ++r) {
                    prSlope = itsPoints[p].slopeTo(itsPoints[r]);
                    if (pqSlope != prSlope) {
                        continue;
                    }
                    for (int s = r + 1; s != nPoints; ++s) {
                        psSlope = itsPoints[p].slopeTo(itsPoints[s]);
                        if (pqSlope == psSlope) {
                            itsLineSegments.addLast(
                                new LineSegment(itsPoints[p], itsPoints[s]));
                        }
                    }  // s-loop
                }  // r-loop
            }  // q-loop
        }  // p-loop
    }
    /**
     * number of line segments
     */
    public int numberOfSegments() {
        return itsLineSegments.size();
    }
    
    /**
     *  the line segments
     *  The method segments() should include each line segment containing 4 
     *  points exactly once. If 4 points appear on a line segment in the order 
     *  p→q→r→s, then you should include either the line segment p→s or s→p (but
     *  not both) and you should not include subsegments such as p→r or q→r. For
     *  simplicity, we will not supply any input to BruteCollinearPoints that 
     *  has 5 or more collinear points.
     */
    public LineSegment[] segments() {
        return itsLineSegments.toArray(new LineSegment[1]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
