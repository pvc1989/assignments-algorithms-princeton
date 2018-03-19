/******************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java-algs4 PercolationStats n trials
 *  Dependencies: Percolation.java
 *
 *  This program takes integer 'n' and 'trials' as command-line arguments.
 *  From those integers, it
 *    - Reads the grid size n of the percolation system.
 *    - Creates an n-by-n grid of sites (intially all blocked)
 *    - Run Percolation trials times.
 *    - Do statistics.
 *
 *  See http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *  for detailed specification of this assignment.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {    
    private final double itsMean;
    private final double itsStddev;
    private final double itsHalf;

    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be >= 0.");
        }
        if (trials < 1) {
            throw new IllegalArgumentException("trials must be >= 1.");
        }
        // perform trials independent experiments on an n-by-n grid
        final int[] nOpen = new int[trials];
        for (int k = 0, i, j; k < trials; ++k) {
            // Initialize all sites to be blocked.
            Percolation perc = new Percolation(n);
            // Repeat the following until the system percolates:
            while (!perc.percolates()) {
                // uniform(int n) returns a random integer uniformly in [0, n).
                i = 1 + StdRandom.uniform(n);
                j = 1 + StdRandom.uniform(n);
                perc.open(i, j);
            }
            nOpen[k] = perc.numberOfOpenSites();
        }
        final int nSquare = n * n;
        itsMean = StdStats.mean(nOpen) / nSquare;
        itsStddev = StdStats.stddev(nOpen) / nSquare;
        itsHalf = 1.96 * itsStddev / Math.sqrt(trials);
    }
    public double mean() {
        // sample mean of percolation threshold
        return itsMean;
    }
    public double stddev() {
        // sample standard deviation of percolation threshold
        return itsStddev;
    }
    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return itsMean - itsHalf;
    }
    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return itsMean + itsHalf;
    }
    
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        // % java PercolationStats 200 100
        // mean                    = 0.5929934999999997
        StdOut.println("mean:                      " + percStats.mean());
        // stddev                  = 0.00876990421552567
        StdOut.println("stddev:                    " + percStats.stddev());
        // 95% confidence interval = [0.5912745987737567, 0.5947124012262428]
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo()
                       + ", " + percStats.confidenceHi() + "]");
    }
}
