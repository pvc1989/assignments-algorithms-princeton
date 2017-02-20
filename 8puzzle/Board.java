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
import edu.princeton.cs.algs4.StdRandom;

import java.util.Stack;

public class Board {
    private static final boolean IF_DEBUG = false;

    private final int dim; // dimension of the board
    private int[][] board; // board
    
    /*
     construct a board from an n-by-n array of blocks
     (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks) {
        dim = blocks.length;
        board = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }
    
    // board dimension n
    public int dimension() {
        return dim;
    }
    
    /*
     Hamming priority function.
     The number of blocks in the wrong position, a search node with a small
     number of blocks in the wrong position is close to the goal, and we prefer 
     a search node that have been reached using a small number of moves.
     */
    public int hamming() {
        int count = 0;
        int goal = 0; // goal value in this position
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                goal = i * dim + j + 1;
                if (board[i][j] != goal) {
                    if (IF_DEBUG) StdOut.println(goal);
                    ++count;
                }
            }
        }
        if (board[dim - 1][dim - 1] == 0)
            --count;
        return count;
    }
    
    /*
     Manhattan priority function. 
     The sum of the Manhattan distances (sum of the vertical and horizontal 
     distance) from the blocks to their goal positions, plus the number of moves
     made so far to get to the search node.
     */
    public int manhattan() {
        int iGoal = 0; // row-index of its goal position
        int jGoal = 0; // column-index of ist goal position
        int dist = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != 0) {
                    iGoal = (board[i][j] - 1) / dim;
                    jGoal = (board[i][j] - 1) % dim;
                    dist += (Math.abs(i - iGoal) + Math.abs(j - jGoal));
                }
            }
        }
        return dist;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twinObj = new Board(board);
        // get a random integer uniformly in [0, dim * dim).
        int k1 = StdRandom.uniform(dim * dim);
        int i1 = k1 / dim;
        int j1 = k1 % dim;
        // its value is not 0
        while (board[i1][j1] == 0) {
            k1 = StdRandom.uniform(dim * dim);
            i1 = k1 / dim;
            j1 = k1 % dim;
        }
        // get another random integer uniformly in [0, dim * dim).
        int k2 = StdRandom.uniform(dim * dim);
        int i2 = k2 / dim;
        int j2 = k2 % dim;
        // which is different from the 1st one
        // and its value is not 0
        while (k1 == k2 || board[i2][j2] == 0) {
            k2 = StdRandom.uniform(dim * dim);
            i2 = k2 / dim;
            j2 = k2 % dim;
        }
        twinObj.swap(i1, j1, i2, j2);
        return twinObj;
    }
    // swap a pair of blocks
    private void swap(int i1, int j1, int i2, int j2) {
        if (IF_DEBUG)
            StdOut.println("swap [" + i1 + "][" + j1 + "] with ["
                           + i2 + "][" + j2 + "]");
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }
    

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // check whether (i,j) is valid
    private boolean checkID(int i, int j) {
        if (0 <= i && i < dim && 0 <= j && j < dim)
            return true;
        return false;
    }
    
    // get all neighboring boards
    public Iterable<Board> neighbors() {
        // find (i,j) where board[i][j] == 0
        int i0 = 0;
        int j0 = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }
        // create a stack to hold the neighbors
        Stack<Board> neighbStack = new Stack<Board>();
        // left neighbor
        if (checkID(i0 - 1, j0)) {
            // create a new object
            Board that = new Board(board);
            // make a one-step movement
            that.swap(i0, j0, i0 - 1, j0);
            // push it into the stack
            neighbStack.push(that);
        }
        // right neighbor
        if (checkID(i0 + 1, j0)) {
            Board that = new Board(board);
            that.swap(i0, j0, i0 + 1, j0);
            neighbStack.push(that);
        }
        // upside neighbor
        if (checkID(i0, j0 - 1)) {
            Board that = new Board(board);
            that.swap(i0, j0, i0, j0 - 1);
            neighbStack.push(that);
        }
        // downside neighbor
        if (checkID(i0, j0 + 1)) {
            Board that = new Board(board);
            that.swap(i0, j0, i0, j0 + 1);
            neighbStack.push(that);
        }
        return neighbStack;
    }
    
    
    /*
     string representation of this board
     The input and output format for a board is the board dimension n followed 
     by the n-by-n initial board, using 0 to represent the blank square. 
     
     As an example,
     % more puzzle04.txt
     3
      0  1  3
      4  2  5
      7  8  6
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
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

            // test constructor
            Board initial = new Board(tiles);
            // test toString()
            StdOut.println("initial:\n" + initial);
            // test dimension()
            StdOut.println("initial.dimention() = " + initial.dimension());
            // test hamming()
            StdOut.println("initial.hamming() = " + initial.hamming());
            // test manhattan()
            StdOut.println("initial.manhattan() = " + initial.manhattan());
            // test isGoal()
            StdOut.println("initial.isGoal() = " + initial.isGoal());
            // test twin()
            Board twinObj = initial.twin();
            StdOut.println("initial.twin():\n" + twinObj);
            // test equals()
            StdOut.println("initial.equals(twinObj) = "
                           + initial.equals(twinObj));
            // test neighbors()
            Iterable<Board> neighbSet = initial.neighbors();
            StdOut.println("initial:\n" + initial);
            for (Board item : neighbSet) {
                StdOut.println("possible neighbor:\n" + item);
            }
            
        }
    }
}
