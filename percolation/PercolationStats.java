/******************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java-algs4 PercolationStats n trials
 *  Dependencies: Percolation.java
 *
 *  This program takes integer 'n' and 'trials' as command-line arguments.
 *  From those integers, it
 *
 *    - Reads the grid size n of the percolation system.
 *    - Creates an n-by-n grid of sites (intially all blocked)
 *    - Run Percolation trials times.
 *    - Do statistics.
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


/******************************************************************************/



public class PercolationStats {
//	private instance variables
	private Percolation perc;// Percolation obj
	private final int nSq;// n^2, i.e. number of sites
	private final int T;// times of trials
	
	// number of sites that are opened when the system percolates
	private int[] x;
	
	// statistic values
	private double fracMean;// sample mean
	private double fracStdDev;// sample standard deviation
	private double fracLo;// lower bound of the 95% confidence interval
	private double fracHi;// higher bound of the 95% confidence interval

//	public instance mathods
    public PercolationStats(int n, int trials)
	{// perform trials independent experiments on an n-by-n grid
		nSq = n * n;
		T = trials;
		x = new int[T];
		for (int k = 0, i, j; k < T; ++k)
		{	// Initialize all sites to be blocked.
			Percolation perc = new Percolation(n);
			// Repeat the following until the system percolates:
			while (!perc.percolates())
			{
				// Choose a site uniformly at random among all blocked sites.
				i = StdRandom.uniform(1, n + 1);
				j = StdRandom.uniform(1, n + 1);
				// Open the site.
				perc.open(i,j);
			}
			x[k] = perc.numberOfOpenSites();
		}
		
    }
    public double mean()
	{// sample mean of percolation threshold
        return StdStats.mean(x) / nSq;
    }
    public double stddev()
	{// sample standard deviation of percolation threshold
		return StdStats.stddev(x) / nSq;
    }
    public double confidenceLo()
	{// low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }
    public double confidenceHi()
	{// high endpoint of 95% confidence interval
		return mean() + 1.96 * stddev() / Math.sqrt(T);
    }
//	public test client
    public static void main(String[] args)
    {// test client (described below)
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats percStats = new PercolationStats(n, trials);
		
//		% java PercolationStats 200 100
//		mean                    = 0.5929934999999997
		StdOut.println("mean:                      " + percStats.mean());
//		stddev                  = 0.00876990421552567
		StdOut.println("stddev:                    " + percStats.stddev());
//		95% confidence interval = [0.5912745987737567, 0.5947124012262428]
		StdOut.println("95% confidence interval = [" + percStats.confidenceLo()
					   + ", " + percStats.confidenceHi() + "]");
		
    }
}
