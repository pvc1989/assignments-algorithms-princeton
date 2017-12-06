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

    /**
     * Create a seam carver object based on the given picture.
     */
    public SeamCarver(Picture pic) {
        int h = pic.height();
        int w = pic.width();
        itsRGB = new int[h][w];
        for (int r = 0; r != h; ++r) {
            for (int c = 0; c != w; ++c) {
                itsRGB[r][c] = pic.getRGB(c, r);
            }
        }
    }
    
    /**
     * @return current picture
     */
    public Picture picture() {
        int h = height();
        int w = width();
        Picture pic = new Picture(w, h);
        for (int r = 0; r != h; ++r) {
            for (int c = 0; c != w; ++c) {
                pic.setRGB(c, r, itsRGB[r][c]);
            }
        }
        return pic;
    }

    /**
     * @return width of current picture
     */
    public int width() {
        return itsRGB[0].length;
    }

    /**
     * @return height of current picture
     */
    public int height() {
        return itsRGB.length;
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
        if (c == 0 || c == width() - 1 || r == 0 || r == height() - 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @return squared central difference of rgb values
     */
    private static double rgbGradSq(int rgb1, int rgb2) {
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
        return null;
    }

    /**
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        return null;
    }

    /**
     * remove horizontal seam from current picture
     */
    public void removeHorizontalSeam(int[] seam) {

    }

    /**
     * remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam) {

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