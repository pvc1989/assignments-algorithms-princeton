/******************************************************************************
 *  Compilation:    javac-algs4 Percolation.java
 *  Execution:      java-algs4 Percolation n
 *
 *  This program takes an integer 'n' as a command-line argument.
 *  From that integer, it
 *    - Reads the grid size n of the percolation system.
 *    - Creates an n-by-n grid of sites (intially all blocked)
 *  Then, during run time, it
 *    - Reads in a sequence of sites (row i, column j) to open.
 *
 *  See http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  for detailed specification of this assignment.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // instance variables
    private final int nRow;  // number of rows
    private final int nCol;  // number of columns
    private final int iTop;  // id of the top sentinel
    private final int iBottom;  // id of the bottom sentinel
    private int nOpen;  // number of open sites
    private final boolean[] alreadyOpen;
    private final WeightedQuickUnionUF weakConnection;
    private final WeightedQuickUnionUF strongConnection;
    
    /** create n-by-n grid, with all sites blocked
     * 
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive integer");
        // initialize private variables
        nRow = n;
        nCol = n;
        final int nSquare = n * n;
        iBottom = nSquare + 1;
        iTop = 0;
        nOpen = 0;
        alreadyOpen = new boolean[nSquare + 2];
        weakConnection = new WeightedQuickUnionUF(nSquare + 2);
        strongConnection = new WeightedQuickUnionUF(nSquare + 1);
    }
    /** Make sure that row and column indices are integers between 1 and n.
     * 
     */
    private void checkIndices(int row, int col) {
        if (row < 1 || row > nRow)
            throw new IllegalArgumentException("row index i out of bounds");
        if (col < 1 || col > nCol)
            throw new IllegalArgumentException("col index j out of bounds");
    }
    /** Map from (row, col) to a 1-dimensional index.
     * 
     */
    private int xyCheckedTo1D(int row, int col) {
        checkIndices(row, col);
        // 0 for the top sentinel
        // 1 for (1, 1), 2 for (1, 2), ..., n for (1, n)
        // ...
        // ((n-1)*n + 1) for (n, 1), ..., ((n-1)*n + n) for (n, n)
        // (n*n + 1) for the bottom sentinel
        return (row - 1) * nCol + col;
    }
    /** open site (row, col) if it is not open already
     * 
     */
    public void open(int row, int col) {
        int id = xyCheckedTo1D(row, col);
        if (alreadyOpen[id]) return;
        // open this site
        alreadyOpen[id] = true;
        ++nOpen;
        // connect to the top sentinel site
        if (row == 1) {
            weakConnection.union(id, iTop);
            strongConnection.union(id, iTop);
        }
        // connect to the bottom sentinel site
        if (row == nRow) {
            weakConnection.union(id, iBottom);
        }
        // connect to neighboring open sites
        if (row > 1) {  // 1. connect to the UPPER site
            connectToNeighbors(id, row - 1, col);
        }
        if (row < nRow) {  // 2. connect to the LOWER site           
            connectToNeighbors(id, row + 1, col);
        }
        if (col > 1) {  // 3. connect to the LEFT site
            connectToNeighbors(id, row, col - 1);
        }
        if (col < nCol) {  // 4. connect to the RIGHT site
            connectToNeighbors(id, row, col + 1);
        }
    }
    /**
     * 
     */
    private void connectToNeighbors(int id, int row, int col) {
        int idNeighbor = xyCheckedTo1D(row, col);
        if (alreadyOpen[idNeighbor]) {
            weakConnection.union(id, idNeighbor);
            strongConnection.union(id, idNeighbor);
        }
    }
    /** is site (row, col) open?
     * 
     */
    public boolean isOpen(int row, int col) {
        return alreadyOpen[xyCheckedTo1D(row, col)];
    }
    /** is site (row, col) full?
     * 
     */
    public boolean isFull(int row, int col) {
        return strongConnection.connected(iTop, xyCheckedTo1D(row, col));
    }
    /** number of open sites
     * 
     */
    public int numberOfOpenSites() {
        return nOpen;
    }
    /** does the system percolate?
     * 
     */
    public boolean percolates() {
        return weakConnection.connected(iTop, iBottom);
    }
    /** tests
     * 
     */
    public static void main(String[] args) {
        int total = Integer.parseInt(args[0]);
        Percolation perc = new Percolation(total);
        int i, j;
        while (!perc.percolates()) {
            StdOut.print("Enter (row,col) to open a site: ");
            i = StdIn.readInt();
            j = StdIn.readInt();
            perc.open(i, j);
            StdOut.printf("Is (%d,%d) open?: " + perc.isOpen(i, j) + "\n",
                          i, j);
            StdOut.printf("Is (%d,%d) full?: " + perc.isFull(i, j) + "\n",
                          i, j);
            StdOut.println("nOpen: " + perc.numberOfOpenSites());
            StdOut.println("percaolates?: " + perc.percolates());
        }
    }
}