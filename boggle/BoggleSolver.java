/******************************************************************************
 *  Compilation:  javac-algs4 BoggleSolver.java
 *  Execution:    java-algs4 BoggleSolver dictionary-yawl.txt board-16q.txt
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private final TrieSet itsDict = new TrieSet();
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
     * Enumerate all strings in the given BoggleBoard started from [i][j].
     */
    private void getWords(BoggleGraph graph, TrieSet set, boolean[] marked,
                          int v, StringBuilder prefix, TrieSet.Node node) {
        if (marked[v]) return;  // found an ancestor, just ignore it
        // otherwise, marked this vertex
        marked[v] = true;
        // entend the prefix, at least 1-char longer
        final char c = graph.getChar(v);
        prefix.append(c);
        node = node.next(c);
        if (c == 'Q') {
            prefix.append('U');
            if (node != null) node = node.next('U');
        }
        if (node == null) {  // no subtrie rooted at current node
            cleanPrefix(prefix, c);
            marked[v] = false;
            return;
        }
        if (node.isString()) {
            if (prefix.length() > 2) {
                String str = prefix.toString();
                if (!set.contains(str)) set.add(str);
            }
        }
        // recurse on its neighbors
        for (int w : graph.adj(v)) {
            getWords(graph, set, marked, w, prefix, node);
        }
        cleanPrefix(prefix, c);
        marked[v] = false;
    }
    private void cleanPrefix(StringBuilder prefix, char c) {
        prefix.deleteCharAt(prefix.length() - 1);
        if (c == 'Q') {
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
    /**
     *  Returns the set of all valid words in the given Boggle board.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TrieSet set = new TrieSet();
        BoggleGraph graph = new BoggleGraph(board);
        int mn = graph.V();
        boolean[] marked = new boolean[mn];
        StringBuilder prefix = new StringBuilder();
        for (int v = 0; v != mn; ++v) {
            TrieSet.Node node = itsDict.root();
            getWords(graph, set, marked, v, prefix, node);
        }
        return set;
    }
    /**
     * Returns the score of the given word if it is in the dictionary,
     * 0 otherwise.
     */
    public int scoreOf(String word) {
        if (!itsDict.contains(word)) return 0;
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
                break;
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