/******************************************************************************
 *  Compilation:    javac-algs4 SeamCarver.java
 *  Execution:      java-algs4 SeamCarver.java filename.png
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private static final boolean DEBUG = false;
    private int[][] itsRGB;
    private double[][] itsEnergy;
    private int itsHeight;
    private int itsWidth;

    /**
     * Create a seam carver object based on the given picture.
     */
    public SeamCarver(Picture pic) {
        itsHeight = pic.height();
        itsWidth = pic.width();
        itsRGB = new int[itsHeight][itsWidth];
        for (int r = 0; r != itsHeight; ++r) {
            for (int c = 0; c != itsWidth; ++c) {
                itsRGB[r][c] = pic.getRGB(c, r);
            }
        }
        itsEnergy = new double[itsHeight][itsWidth];
        for (int r = 0; r != itsHeight; ++r) {
            for (int c = 0; c != itsWidth; ++c) {
                itsEnergy[r][c] = energy(c, r);
            }
        }
    }
    
    /**
     * @return current picture
     */
    public Picture picture() {
        Picture pic = new Picture(itsWidth, itsHeight);
        for (int r = 0; r != itsHeight; ++r) {
            for (int c = 0; c != itsWidth; ++c) {
                pic.setRGB(c, r, itsRGB[r][c]);
            }
        }
        return pic;
    }

    /**
     * @return width of current picture
     */
    public int width() {
        return itsWidth;
    }

    /**
     * @return height of current picture
     */
    public int height() {
        return itsHeight;
    }

    /**
     * @return energy of pixel at column c and row r
     */
    public double energy(int c, int r) {
        if (DEBUG) {
            StdOut.printf("/* %d, %d */", r, c);
        }
        if (onBorder(c, r)) {
            return 1000;  // trivial case
        }
        else {
            return Math.sqrt(
                rgbGradSq(itsRGB[r-1][c], itsRGB[r+1][c]) +
                rgbGradSq(itsRGB[r][c-1], itsRGB[r][c+1]));
        }
    }

    /**
     * @return whether the pixel is on the border of the picture
     */
    private boolean onBorder(int c, int r) {
        if (c == 0 || c == itsWidth - 1) return true;
        if (r == 0 || r == itsHeight - 1) return true;
        return false;
    }

    /**
     * @return whether the pixel is within the closed domain of the picture
     */
    private boolean inClosedDomain(int c, int r) {
        if (c < 0 || c >= itsWidth) return false;
        if (r < 0 || r >= itsHeight) return false;
        return true;
    }

    /**
     * @return squared central difference of rgb values
     */
    private static int rgbGradSq(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int dr = r1 - r2;
        int g1 = (rgb1 >>  8) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int dg = g1 - g2;
        int b1 = (rgb1 >>  0) & 0xFF;
        int b2 = (rgb2 >>  0) & 0xFF;
        int db = b1 - b2;
        return dr * dr + dg * dg + db * db;
    }

    /**
     * @return sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        double[][] dist = new double[itsWidth][itsHeight];
        int[][] prev = new int[itsWidth][itsHeight];
        for (int c = 1; c != itsWidth; ++c) {
            for (int r = 0; r != itsHeight; ++r) {
                dist[c][r] = Double.MAX_VALUE;
            }
        }
        for (int c = 0; c != itsWidth - 1; ++c) {
            relaxCol(c, dist, prev);
        }
        // find the outlet of the path
        double minDist = Double.MAX_VALUE;
        int minRow = 0;
        double[] distLastCol = dist[itsWidth - 1];
        for (int r = 0; r != itsHeight; ++r) {
            if (distLastCol[r] < minDist) {
                minDist = distLastCol[r];
                minRow = r;
            }
        }
        // build the path
        int[] seam = new int[itsWidth];
        seam[itsWidth - 1] = minRow;
        for (int c = itsWidth - 1; c != 0; --c) {
            seam[c-1] = prev[c][seam[c]];
        }
        return seam;       
    }

    /**
     * relax all the vertices in the c-th column.
     */
    private void relaxCol(int c, double[][] dist, int[][] prev) {
        if (c >= width() - 1) return;
        for (int r = 0; r != itsHeight; ++r) {
            if (dist[c+1][r] > dist[c][r] + itsEnergy[r][c]) {
                // relax the edge point to the East
                dist[c+1][r] = dist[c][r] + itsEnergy[r][c];
                prev[c+1][r] = r;
            }
            if (r != 0 &&
                dist[c+1][r-1] > dist[c][r] + itsEnergy[r][c]) {
                // relax the edge point to the NorthEast
                dist[c+1][r-1] = dist[c][r] + itsEnergy[r][c];
                prev[c+1][r-1] = r;
            }
            if (r != height() - 1 &&
                dist[c+1][r+1] > dist[c][r] + itsEnergy[r][c]) {
                // relax the edge point to the SouthEast
                dist[c+1][r+1] = dist[c][r] + itsEnergy[r][c];
                prev[c+1][r+1] = r;
            }
        }
    }

    /**
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        int h = height();
        int w = width();
        double[][] dist = new double[itsHeight][itsWidth];
        int[][] prev = new int[itsHeight][itsWidth];
        for (int r = 1; r != itsHeight; ++r) {
            for (int c = 0; c != itsWidth; ++c) {
                dist[r][c] = Double.MAX_VALUE;
            }
        }
        for (int r = 0; r != itsHeight - 1; ++r) {
            relaxRow(r, dist, prev);
        }
        // find the outlet of the path
        double minDist = Double.MAX_VALUE;
        int minCol = 0;
        double[] distLastRow = dist[itsHeight - 1];
        for (int c = 0; c != itsWidth; ++c) {
            if (distLastRow[c] < minDist) {
                minDist = distLastRow[c];
                minCol = c;
            }
        }
        // build the path
        int[] seam = new int[itsHeight];
        seam[h-1] = minCol;
        for (int r = itsHeight - 1; r != 0; --r) {
            seam[r-1] = prev[r][seam[r]];
        }
        return seam;
    }
    
    /**
     * relax all the vertices in the r-th row.
     */
    private void relaxRow(int r, double[][] dist, int[][] prev) {
        if (r >= height() - 1) return;
        for (int c = 0; c != itsWidth; ++c) {
            if (dist[r+1][c] > dist[r][c] + itsEnergy[r][c]) {
                // relax the edge point to the bottom
                dist[r+1][c] = dist[r][c] + itsEnergy[r][c];
                prev[r+1][c] = c;
            }
            if (c != 0 &&
                dist[r+1][c-1] > dist[r][c] + itsEnergy[r][c]) {
                // relax the edge point to the bottom left
                dist[r+1][c-1] = dist[r][c] + itsEnergy[r][c];
                prev[r+1][c-1] = c;
            }
            if (c != width() - 1 &&
                dist[r+1][c+1] > dist[r][c] + itsEnergy[r][c]) {
                // relax the edge point to the bottom right
                dist[r+1][c+1] = dist[r][c] + itsEnergy[r][c];
                prev[r+1][c+1] = c;
            }
        }
    }

    /**
     * remove horizontal seam from current picture
     */
    public void removeHorizontalSeam(int[] seam) {
        for (int c = 0; c != itsWidth; ++c) {
            int r = seam[c];
            // shift the rest of this column one step up
            for (int k = r; k != itsHeight - 1; ++k) {
                itsRGB[k][c] = itsRGB[k+1][c];
                itsEnergy[k][c] = itsEnergy[k+1][c];
            }
        }
        --itsHeight;
        // update itsEnergy along the seam
        for (int c = 0; c != itsWidth; ++c) {
            int r = seam[c];
            if (inClosedDomain(c, r)) itsEnergy[r][c] = energy(c, r);
            if (inClosedDomain(c, r-1)) itsEnergy[r-1][c] = energy(c, r-1);
        }
    }

    /**
     * remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam) {
        for (int r = 0; r != itsHeight; ++r) {
            int c = seam[r];
            // shift the rest of this row one step left
            for (int k = c; k != itsWidth - 1; ++k) {
                itsRGB[r][k] = itsRGB[r][k+1];
                itsEnergy[r][k] = itsEnergy[r][k+1];
            }
        }
        --itsWidth;
        // update itsEnergy along the seam
        for (int r = 0; r != itsHeight; ++r) {
            int c = seam[r];
            if (inClosedDomain(c, r)) itsEnergy[r][c] = energy(c, r);
            if (inClosedDomain(c-1, r)) itsEnergy[r][c-1] = energy(c-1, r);
        }
    }

    /**
     * test client
     */
    public static void main(String[] args) {
        // test the constructor and picture()
        Picture pic = new Picture(args[0]);
        pic.show();
        SeamCarver sc = new SeamCarver(pic);
        StdOut.printf("Size of the image: %dx%d\n", sc.width(), sc.height());
        sc.picture().show();
    }
}