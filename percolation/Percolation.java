/******************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation n
 *
 *  This program takes an integer 'n' as a command-line argument.
 *  From that integer, it
 *
 *    - Reads the grid size n of the percolation system.
 *    - Creates an n-by-n grid of sites (intially all blocked)
 *
 *	Then, during run time, it
 *    - Reads in a sequence of sites (row i, column j) to open.
 *
 *	See http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *	for detailed specification of this assignment.
 ******************************************************************************/

// standard libraries developed for use in algs4
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
//import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
//import edu.princeton.cs.algs4.In;
//import edu.princeton.cs.algs4.Out;

// imported system libraries
//import java.util.Arrays;

// use weighted-quick-union algorithm
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/******************************************************************************/

public class Percolation {
	
// public instance methods
    public Percolation(int n)
    {// create n-by-n grid, with all sites blocked
	}

    public void open(int row, int col) 
    {// open site (row, col) if it is not open already
    }
    public boolean isOpen(int row, int col)  
    {// is site (row, col) open?
    }
    public boolean isFull(int row, int col)  
    {// is site (row, col) full?
    }
    public int numberOfOpenSites() 
    {// number of open sites
    }
    public boolean percolates() 
    {// does the system percolate?
    }


// test client
    public static void main(String[] args)
    {// test client (optional)
	}
    
    
}
