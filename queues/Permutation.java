/******************************************************************************
 *  Compilation:    javac-algs4 Permutation.java
 *  Execution:      java-algs4 Permutation 3
 *
 *  Description:    This is a client program that takes a command-line integer 
 *      k, reads in a sequence of strings from standard input, and prints 
 *      exactly k of them, uniformly at random. Print each item from the 
 *      sequence at most once. You may assume that 0 ≤ k ≤ n, where n is the 
 *      number of string on standard input.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        /* Standard input can contain any sequence of strings. 
         You may assume that there is one integer command-line argument k and 
         it is between 0 and the number of strings on standard input.
         */
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rdQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rdQueue.enqueue(item);
        }
        for (int i = 0; i < k; ++i) StdOut.println(rdQueue.dequeue());
    }
}
