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

public class BruteCollinearPoints {
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
    }

    // the number of line segments
    public int numberOfSegments() {
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
    }
    
    /**
     *  This client program takes the name of an input file as a command-line
     *  argument; read the input file (in the format specified below); prints to
     *  standard output the line segments that your program discovers, one per
     *  line; and draws to standard draw the line segments.
     */
    public static void main(String[] args) {
    }
}
