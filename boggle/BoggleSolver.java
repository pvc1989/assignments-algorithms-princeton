import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {

    private final TrieSET itsDict = new TrieSET();
    private final TrieSET itsWords = new TrieSET();
    /**
     * Initializes the data structure using the given dictionary.
     * Each word in the dictionary contains only the uppercase letters [A-Z].
     */
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            itsDict.add(word);
        }
    }
    /**
     * Enumerate all strings in the given Boggle board started from [i][j].
     */
    private void getWords(BoardGraph graph, TrieSET set, boolean[] marked,
                          int v, StringBuilder prefix) {
        if (marked[v] == false) {  // prefix can be 1-char longer
            marked[v] = true;
            char c = graph.getChar(v);
            prefix.append(c);
            if (c == 'Q') {
                prefix.append('U');
            }
            String str = prefix.toString();
            if (itsDict.contains(str) && !set.contains(str)) {
                // StdOut.println(str);
                set.add(prefix.toString());
            }
            // recurse on its 8 neighbors
            for (int w : graph.adj(v)) {
                getWords(graph, set, marked, w, prefix);
            }
            prefix.deleteCharAt(prefix.length() - 1);
            if (c == 'Q') {
                prefix.deleteCharAt(prefix.length() - 1);
            }
            marked[v] = false;
        }
    }
    /**
     *  Returns the set of all valid words in the given Boggle board.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TrieSET set = new TrieSET();
        BoardGraph graph = new BoardGraph(board);
        int mn = graph.V();
        boolean[] marked = new boolean[mn];
        StringBuilder prefix = new StringBuilder();
        for (int v = 0; v != mn; ++v) {
            getWords(graph, set, marked, v, prefix);
        }
        return set;
    }
    /**
     * Returns the score of the given word if it is in the dictionary,
     * 0 otherwise.
     */
    public int scoreOf(String word) {
        int score;
        switch (word.length()) {
            case 1:
            case 2:
                score = 0;
                break;
            case 3:
            case 4:
                score = 1;
                break;
            case 5:
                score = 2;
                break;
            case 6:
                score = 3;
                break;
            case 7:
                score = 5;
                break;
            default:
                score = 11;
        }
        return score;
    }
    /**
     * Test client
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }   
}