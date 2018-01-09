/******************************************************************************
 *  Compilation:  javac-algs4 BoardGraph.java
 *  Execution:    java-algs4 BoardGraph board4x4.txt
 ******************************************************************************/
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
    private final int[][] itsAdj;

    public BoardGraph(BoggleBoard board) {
        m = board.rows();
        n = board.cols();
        mn = m * n;
        itsChars = new char[mn];
        itsAdj = new int[mn][8]; 
        int[][] id = new int[m][n];
        for (int i = 0, k = 0; i != m; ++i) {
            for (int j = 0; j != n; ++j) {
                itsChars[k] = board.getLetter(i, j);
                id[i][j] = k;
                ++k;
            }
        }
        // build the adj-list
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                int[] adjList = itsAdj[id[i][j]];
                int k = 0;
                if (isValid(i  , j+1)) adjList[k++] = id[i  ][j+1];
                if (isValid(i-1, j+1)) adjList[k++] = id[i-1][j+1];
                if (isValid(i-1, j  )) adjList[k++] = id[i-1][j  ];
                if (isValid(i-1, j-1)) adjList[k++] = id[i-1][j-1];
                if (isValid(i  , j-1)) adjList[k++] = id[i  ][j-1];
                if (isValid(i+1, j-1)) adjList[k++] = id[i+1][j-1];
                if (isValid(i+1, j  )) adjList[k++] = id[i+1][j  ];
                if (isValid(i+1, j+1)) adjList[k++] = id[i+1][j+1];
                if (k != 8) adjList[k] = -1;
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
        ArrayList<Integer> arraylist = new ArrayList<Integer>(8);
        for (int k = 0; k != 8; ++k) {
            int w = itsAdj[v][k];
            if (w < 0) break;
            else       arraylist.add(w);
        }
        return arraylist;
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