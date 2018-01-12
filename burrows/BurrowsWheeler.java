/******************************************************************************
 *  Compilation:    javac-algs4 BurrowsWheeler.java
 *  Execution:      java-algs4 BurrowsWheeler - < input
 *                  java-algs4 BurrowsWheeler + < input
 * 
 *  Demo:
 *  > cat abra.txt 
 *  ABRACADABRA!
 *  > java-algs4 BurrowsWheeler - < abra.txt | java-algs4 BurrowsWheeler +
 *  ABRACADABRA!
 ******************************************************************************/
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class BurrowsWheeler {
    private static final boolean DEBUG = false;

    // apply Burrows-Wheeler transform, 
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);
        final int N = csa.length();
        for (int i = 0; i != N; ++i) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0, j = 0; i != N; ++i) {
            j = csa.index(i) - 1;
            if (j < 0) j = N - 1;
            BinaryStdOut.write(input.charAt(j));
        }
        // close output stream
        BinaryStdOut.close();
    }
    // apply Burrows-Wheeler inverse transform, 
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        final int first = BinaryStdIn.readInt();
        final String input = BinaryStdIn.readString();
        final int N = input.length();
        final int R = 256;
        Stack<Integer>[] rankStacks = (Stack<Integer>[]) new Object[R];
        for (int i = 0; i != N; ++i) {  // go through the t[] array
            char c = input.charAt(i);
            if (rankStacks[c] == null) {
                rankStacks[c] = new Stack<Integer>();
            }
            rankStacks[c].push(i);  // record the rank of c in t[]
        }
        int[] next = new int[N];
        char[] head = new char[N];
        for (int iCount = R-1, iNext = N-1; iCount >= 0; --iCount) {
            Stack<Integer> stack = rankStacks[iCount];
            while (stack != null && !stack.isEmpty()) {
                next[iNext] = stack.pop();     // build the next[] array
                head[iNext] = (char) iCount;  // sort the t[] array
                --iNext;
            }
        }
        if (DEBUG) {
            for (int i = 0; i != N; ++i) {
                StdOut.print(head[i]);
                StdOut.print(" ");
                StdOut.print(next[i]);
                StdOut.println();
            }
        }
        // output the original string
        for (int i = 0, j = first; i != N; ++i) {
            BinaryStdOut.write(head[j]);
            j = next[j];
        }
        BinaryStdOut.close();
    }
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException(
            "Illegal command line argument");
    }
}