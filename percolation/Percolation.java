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
	// instance variables
	private final int nRow;// number of rows, i.e. the input n
	private final int nCol;// number of columns, i.e. the input n
	private final int nSite;// number of sites
	private final int iTop;// id of the top sentinel site
	private final int iBottom;// id of the bottom sentinel site
	private int nOpen;// number of open sites
	// WeightedQuickUnionUF object that represent all the sites
	private WeightedQuickUnionUF uf;
	// boolean array that stores the states of all sites
	// true means open, false(default) means closed
	private boolean[] state;
	
	// public instance methods
	public Percolation(int n)
	{// create n-by-n grid, with all sites blocked
		// The constructor should throw a java.lang.IllegalArgumentException
		// if n ≤ 0.
		if (n <= 0)
			throw new java.lang.IllegalArgumentException
			("n must be positive integer");
		// initialize private variables
		nRow = n;
		nCol = n;
		nSite = n * n;
		iBottom = nSite + 1;
		iTop = 0;
		nOpen = 0;
		// n^2 normal sites + 2 sentinel sites
		uf = new WeightedQuickUnionUF(nSite + 2);
		// set the 2 sentinel sites to be open
		state = new boolean[nSite + 2];
		state[iTop] = true;
		state[iBottom] = true;
	}
	
	public void open(int row, int col)
	{// open site (row, col) if it is not open already
		if (!isOpen(row, col))
		{
			// open this site
			int id = xyTo1D(row, col);
			state[id] = true;
			++nOpen;
			// connect to the top sentinel site
			if (row == 1)
				uf.union(id, iTop);
			// connect to the bottom sentinel site
			if (row == nRow)
				uf.union(id, iBottom);
			// connect to neighboring open sites
			// 1. connect to the UPPER site, if exists
			// make sure there is at least 1 UPPER row
			// then make sure that site is already open
			if (row > 1 && isOpen(row - 1, col))
				uf.union(id, xyTo1D(row - 1, col));
			// 2. connect to the LOWER site, if exists
			// make sure there is at least 1 LOWER row
			// then make sure that site is already open
			if (row < nRow && isOpen(row + 1, col))
				uf.union(id, xyTo1D(row + 1, col));
			// 3. connect to the LEFT site, if exists
			// make sure there is at least 1 LEFT row
			// then make sure that site is already open
			if (col > 1 && isOpen(row, col - 1))
				uf.union(id, xyTo1D(row, col - 1));
			// 4. connect to the RIGHT site, if exists
			// make sure there is at least 1 RIGHT row
			// then make sure that site is already open
			if (col < nCol && isOpen(row, col + 1))
				uf.union(id, xyTo1D(row, col + 1));
		}
		
	}
	public boolean isOpen(int row, int col)
	{// is site (row, col) open?
		validateID(row, col);
		return state[xyTo1D(row, col)];
	}
	public boolean isFull(int row, int col)
	{// is site (row, col) full?
		validateID(row, col);
		return uf.connected(iTop, xyTo1D(row, col));
	}
	public int numberOfOpenSites()
	{// number of open sites
		return nOpen;
	}
	public boolean percolates()
	{// does the system percolate?
		return uf.connected(iTop, iBottom);
	}
	
	
	// private instance methods
	private int xyTo1D(int row, int col)
	{// map from a 2-dimensional (row, column) pair to a 1-dimensional index
		return (row - 1) * nCol + col;
	}
	private void validateID(int row, int col)
	{// validate indices
		//		StdOut.println(row);
		//		StdOut.println(col);
		if (row < 1 || row > nRow)
			throw new IndexOutOfBoundsException("row index i out of bounds");
		if (col < 1 || col > nCol)
			throw new IndexOutOfBoundsException("col index j out of bounds");
	}
	
	
	// test client
	public static void main(String[] args)
	{// test client (optional)
		int Total = Integer.parseInt(args[0]);
		Percolation perc = new Percolation(Total);
		
		// test public instance methods
		int i, j;
		while (!perc.percolates())
		{
			StdOut.print("Enter (row,col) to open a site: ");
			i = StdIn.readInt();
			j = StdIn.readInt();
			perc.open(i, j);
			StdOut.printf("Is (%d,%d) open?: " + perc.isOpen(i,j) + "\n", i, j);
			StdOut.printf("Is (%d,%d) full?: " + perc.isFull(i,j) + "\n", i, j);
			StdOut.println("nOpen: " + perc.numberOfOpenSites());
			StdOut.println("percaolates?: " + perc.percolates());
		}
	}
	
	
}
