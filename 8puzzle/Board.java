/******************************************************************************
 *  Compilation:  javac-algs4 Board.java
 *  Execution:    java-algs4 Board filename1.txt filename2.txt ...
 *  Dependencies:
 *
 *  This program creates an initial board from each filename specified on the 
 *  command line.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    
    /*
     construct a board from an n-by-n array of blocks
     (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks) {
        
    }
    
    // board dimension n
    public int dimension() {
        
    }
    
    // number of blocks out of place
    public int hamming() {
        
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        
    }
    
    /*
     string representation of this board (in the output format specified below)
     */
    public String toString() {
        
    }

    
    // test client
    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
//            Solver solver = new Solver(initial);
//            StdOut.println(filename + ": " + solver.moves());
        }
    }
}
