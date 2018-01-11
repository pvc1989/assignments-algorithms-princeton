import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class BurrowsWheeler {
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
        Stack<Integer>[] rank_stacks = new Stack[R];
        for (int i = 0; i != N; ++i) {  // go through the t[] array
            char c = input.charAt(i);
            if (rank_stacks[c] == null) {
                rank_stacks[c] = new Stack<Integer>();
            }
            rank_stacks[c].push(i);  // record the rank of c in t[]
        }
        int[] next = new int[N];
        char[] head = new char[N];
        for (int i_count = R-1, i_next = N-1; i_count >= 0; --i_count) {
            Stack<Integer> stack = rank_stacks[i_count];
            while (stack != null && !stack.isEmpty()) {
                next[i_next] = stack.pop();     // build the next[] array
                head[i_next] = (char) i_count;  // sort the t[] array
                --i_next;
            }
        }
        // for (int i = 0; i != N; ++i) {
        //     StdOut.print(head[i]);
        //     StdOut.print(" ");
        //     StdOut.print(next[i]);
        //     StdOut.println();
        // }
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