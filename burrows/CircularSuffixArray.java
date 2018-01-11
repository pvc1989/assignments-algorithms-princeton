import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private final int N;
    private final char[] chars;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new java.lang.IllegalArgumentException();
        N = s.length();
        index = new int[N];
        chars = new char[2*N];
        for (int i = 0, j = N; i != N; ++i, ++j) {
            index[i] = i;
            chars[i] = s.charAt(i);
            chars[j] = s.charAt(i);
        }
        sort(0, N, 0);
    }
    private void sort(int lo, int hi, int d) {
        // 3-way string quicksort
        if (hi - 1 <= lo) return;
        int lt = lo;      // exclusive upper bound of <
        int gt = hi - 1;  // exclusive lower bound of >
        char pivot = chars[index(lo) + d];  // pivot
        int i = lo + 1;
        while (i <= gt) {
            char x = chars[index(i) + d];
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
        sort(lt, gt, d+1);
        sort(gt, hi, d);
    }
    private void swap(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }
    // length of s
    public int length() {
        return N;
    }
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= N) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }
    // unit testing (required)
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
        for (int i = 0; i != N; ++i) {
            StdOut.printf("%2d    ", i);
            for (int j = 0; j != N; ++j) {
                StdOut.printf("%c ", chars[i+j]);
            }
            StdOut.print("   ");
            for (int j = 0; j != N; ++j) {
                StdOut.printf("%c ", chars[index(i)+j]);
            }
            StdOut.print("   ");
            StdOut.print(index(i));
            StdOut.println();
        }
    }
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
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