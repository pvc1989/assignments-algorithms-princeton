/******************************************************************************
 *  Compilation:    javac-algs4 SeamCarver.java
 *  Execution:      java-algs4 SeamCarver.java filename.png
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
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
     * current picture
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
     * width of current picture
     */
    public int width() {
        return itsRGB[0].length;
    }

    /**
     * height of current picture
     */
    public int height() {
        return itsRGB.length;
    }

    /**
     * energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        return 0.0;
    }

    /**
     * sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        return null;
    }

    /**
     * sequence of indices for vertical seam
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