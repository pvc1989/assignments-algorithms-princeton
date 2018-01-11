import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256;
    
    // apply move-to-front encoding, 
    // reading from standard input and writing to standard output
    public static void encode() {
        String input = BinaryStdIn.readString();
        final int N = input.length();
        char[] alphabet = new char[R];
        for (char c = 0; c != R; ++c) {
            alphabet[c] = c;
        }
        for (int i = 0; i != N; ++i) {
            char c = input.charAt(i);
            char rank = 0;
            while (c != alphabet[rank]) {
                ++rank;
            }
            BinaryStdOut.write(rank);
            while (rank > 0) {
                alphabet[rank] = alphabet[rank - 1];
                --rank;
            }
            alphabet[0] = c;  // move-to-front
        }
        BinaryStdOut.close();  // close output stream
    }
    // apply move-to-front decoding, 
    // reading from standard input and writing to standard output
    public static void decode() {
        String input = BinaryStdIn.readString();
        final int N = input.length();
        char[] alphabet = new char[R];
        for (char c = 0; c != R; ++c) {
            alphabet[c] = c;
        }
        for (int i = 0; i != N; ++i) {
            char rank = input.charAt(i);
            char c = alphabet[rank];
            BinaryStdOut.write(c);
            while (rank > 0) {
                alphabet[rank] = alphabet[rank - 1];
                --rank;
            }
            alphabet[0] = c;  // move-to-front
        }
        BinaryStdOut.close();  // close output stream
    }
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException(
            "Illegal command line argument");
    }
}
