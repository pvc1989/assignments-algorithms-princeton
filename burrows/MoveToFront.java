/******************************************************************************
 *  Compilation:    javac-algs4 MoveToFront.java
 *  Execution:      java-algs4 MoveToFront - < input
 *                  java-algs4 MoveToFront + < input
 * 
 *  Demo:
 *  > cat abra.txt 
 *  ABRACADABRA!
 *  > java-algs4 MoveToFront - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
 *  41 42 52 02 44 01 45 01 04 04 02 26
 *  96 bits
 *  > java-algs4 MoveToFront - < abra.txt | java-algs4 MoveToFront +
 *  ABRACADABRA!
 ******************************************************************************/
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

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