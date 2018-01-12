/******************************************************************************
 *  Compilation:    javac-algs4 CircularSuffixArray.java
 *  Execution:      java-algs4 CircularSuffixArray 
 * 
 *  Demo:
 *  > java-algs4 CircularSuffixArray ABRACADABRA!
 *   i       Original Suffixes           Sorted Suffixes         index[i]
 *  --    -----------------------     -----------------------    --------
 *   0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 *   1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 *   2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
 *   3    A C A D A B R A ! A B R     A B R A C A D A B R A !    0
 *   4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 *   5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 *   6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 *   7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 *   8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 *   9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
 *  10    A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
 *  11    ! A B R A C A D A B R A     R A C A D A B R A ! A B    2
 ******************************************************************************/
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private final int n;
    private final char[] chars;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new java.lang.IllegalArgumentException();
        n = s.length();
        index = new int[n];
        chars = new char[2*n];
        for (int i = 0, j = n; i != n; ++i, ++j) {
            index[i] = i;
            chars[i] = s.charAt(i);
            chars[j] = s.charAt(i);
        }
        sort(0, n, 0);
    }
    private int charAt(int d) {
        if (d < chars.length) return chars[d];
        else                  return -1;
    }
    private void sort(int lo, int hi, int d) {
        // 3-way string quicksort
        if (hi - 1 <= lo) return;
        int lt = lo;      // exclusive upper bound of <
        int gt = hi - 1;  // exclusive lower bound of >
        int pivot = charAt(index(lo) + d);  // pivot
        int i = lo + 1;
        while (i <= gt) {
            int x = charAt(index(i) + d);
            if      (x < pivot) swap(lt++, i++);
            else if (x > pivot) swap(gt--, i);
            else                ++i;
        }       // loop invariant:
                //      \forall x \in [lo, lt) : x < pivot
                //      \forall x \in [lt, gt] : x = pivot
                //      \forall x \in (gt, hi) : x > pivot
        ++gt;   // after this point:
                //      \forall x \in [lo, lt) : x < pivot
                //      \forall x \in [lt, gt) : x = pivot
                //      \forall x \in [gt, hi) : x > pivot
        sort(lo, lt, d);
        if (pivot >= 0) sort(lt, gt, d+1);
        sort(gt, hi, d);
    }
    private void swap(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }
    // length of s
    public int length() {
        return n;
    }
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }
    private void print() {
        StdOut.print(" i    ");
        StdOut.print("Original Suffixes          ");
        StdOut.print("Sorted Suffixes       t    ");
        StdOut.print("index[i]");
        StdOut.println();
        StdOut.print("--    ");
        StdOut.print("-----------------------    ");
        StdOut.print("-----------------------    ");
        StdOut.print("--------");
        StdOut.println();
        for (int i = 0; i != n; ++i) {
            StdOut.printf("%2d    ", i);
            for (int j = 0; j != n; ++j) {
                StdOut.printf("%c ", chars[i+j]);
            }
            StdOut.print("   ");
            for (int j = 0; j != n; ++j) {
                StdOut.printf("%c ", chars[index(i)+j]);
            }
            StdOut.print("   ");
            StdOut.print(index(i));
            StdOut.println();
        }
    }
    // unit testing (required)
    public static void main(String[] args) {
        String s = args[0];
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StdOut.print("Index: ");
        for (int i = 0; i != csa.length(); ++i) {
            StdOut.printf("%d ", csa.index(i));
        }
        StdOut.println();
        StdOut.println("Length: " + csa.length());
        csa.print();
    }
}