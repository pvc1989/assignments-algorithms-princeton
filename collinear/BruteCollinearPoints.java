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

// standard libraries developed for use in algs4
// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.Out;

// system libraries
// import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {
    private int nSegments;
    private LinkedList<LineSegment> lineSegList;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException(
            "the argument to the constructor is null");
        // check points
        for (Point pt : points) {
            if (pt == null) throw new NullPointerException(
                "some point in the array is null");
        }
        nSegments = 0;
        lineSegList = new LinkedList<LineSegment>();
        
        // look for collinear points
        double pqSlope = 0.0;
        double prSlope = 0.0;
        double psSlope = 0.0;
        for (Point p : points) {
            
            for (Point q : points) {
                // p and q refer to the same object
                if (p == q) continue; // goto next q
                // make sure p < q
                if (p.compareTo(q) > 0) continue; // goto next q
                // p and q are 2 objects with the same value
                // i.e. repeated points
                if (p.compareTo(q) == 0) findRepeatedPoints();
                pqSlope = p.slopeTo(q);
                
                for (Point r : points) {
                    // q and r refer to the same object
                    if (q == r) continue; // go to next r-loop
                    // make sure p < q < r
                    if (q.compareTo(r) > 0) continue; // go to next r-loop
                    // p and q are 2 objects with the same value
                    // i.e. repeated points
                    if (q.compareTo(r) == 0) findRepeatedPoints();
                    prSlope = p.slopeTo(r);
                    // make sure p,q,r are collinear
                    if (pqSlope != prSlope) continue; // go to next r-loop
                    
                    for (Point s : points) {
                        // r and s refer to the same object
                        if (r == s) continue; // go to next s-loop
                        // make sure p < q < r < s
                        if (r.compareTo(s) > 0) continue; // go to next s-loop
                        // r and s are 2 objects with the same value
                        // i.e. repeated points
                        if (r.compareTo(s) == 0) findRepeatedPoints();
                        psSlope = p.slopeTo(s);
                        if (pqSlope == psSlope) {
                            // at this point:
                            // pqSlope == prSlope == psSlope
                            // p < q < r < s
                            ++nSegments;
                            LineSegment lineSeg = new LineSegment(p, s);
                            lineSegList.addLast(lineSeg);
                        }
                    } // s-loop
                } // r-loop
            } // q-loop
        } // p-loop
    }
    
    // check repeated points
    private static void findRepeatedPoints() {
        throw new IllegalArgumentException("input contains a repeated point");
    }
    
    
    // the number of line segments
    public int numberOfSegments() {
        return nSegments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
