/* Permutation client. 
 Write a client program Permutation.java that takes a command-line integer k; 
 reads in a sequence of strings from standard input using StdIn.readString(); 
 and prints exactly k of them, uniformly at random. Print each item from the 
 sequence at most once. You may assume that 0 ≤ k ≤ n, where n is the number of 
 string on standard input.

% more distinct.txt                        % more duplicates.txt
A B C D E F G H I                          AA BB BB BB BB BB CC CC

% java Permutation 3 < distinct.txt       % java Permutation 8 < duplicates.txt
C                                          BB
G                                          AA
A                                          BB
CC
% java Permutation 3 < distinct.txt        BB
E                                          BB
F                                          CC
G                                          BB
The running time of Permutation must be linear in the size of the input. You may
 use only a constant amount of memory plus either one Deque or RandomizedQueue 
 object of maximum size at most n. (For an extra challenge, use only one Deque 
 or RandomizedQueue object of maximum size at most k.) 
 */
public class Permutation {
    public static void main(String[] args)
}
