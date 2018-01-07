import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;

/**
 * A Graph representation of BoggleBoard.
 */
public class BoardGraph {
    private final int m;
    private final int n;
    private final int mn;
    private final char[] itsChars;
    private final int[][] id;
    private final ArrayList<Integer>[] itsAdj;

    public BoardGraph(BoggleBoard board) {
        m = board.rows();
        n = board.cols();
        mn = m * n;
        itsChars = new char[mn];
        itsAdj = (ArrayList<Integer>[]) new ArrayList[mn]; 
        id = new int[m][n];
        for (int i = 0, k = 0; i != m; ++i) {
            for (int j = 0; j != n; ++j) {
                itsChars[k] = board.getLetter(i, j);
                id[i][j] = k;
                itsAdj[k] = new ArrayList<Integer>(8);
                ++k;
            }
        }
        // build the adj-list
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                ArrayList<Integer> adjList = itsAdj[id[i][j]];
                if (isValid(i  , j+1)) adjList.add(id[i  ][j+1]);
                if (isValid(i-1, j+1)) adjList.add(id[i-1][j+1]);
                if (isValid(i-1, j  )) adjList.add(id[i-1][j  ]);
                if (isValid(i-1, j-1)) adjList.add(id[i-1][j-1]);
                if (isValid(i  , j-1)) adjList.add(id[i  ][j-1]);
                if (isValid(i+1, j-1)) adjList.add(id[i+1][j-1]);
                if (isValid(i+1, j  )) adjList.add(id[i+1][j  ]);
                if (isValid(i+1, j+1)) adjList.add(id[i+1][j+1]);
            }
        }
    }
    public int V() {
        return mn;
    }
    private boolean isValid(int i, int j) {
        return 0 <= i && i < m && 0 <= j && j < n;
    }
    private boolean isValid(int v) {
        return 0 <= v && v < mn;
    }
    public char getChar(int v) {
        assert isValid(v);
        return itsChars[v];
    }
    public Iterable<Integer> adj(int v) {
        assert isValid(v);
        return itsAdj[v];
    }
    public String toString() {
        StringBuilder str = new StringBuilder(V() + "\n");
        for (int v = 0; v < mn; v++) {
            str.append(getChar(v));
            str.append("  ");
            for (int w : adj(v)) {
                str.append(itsChars[w]);
                str.append("  ");
            }
            str.append("\n");
        }
        return str.toString().trim();
    }
    public static void main(String[] args) {
        BoggleBoard board = new BoggleBoard(args[0]);
        StdOut.println(board);
        BoardGraph graph = new BoardGraph(board);
        StdOut.println(graph);
    }
}