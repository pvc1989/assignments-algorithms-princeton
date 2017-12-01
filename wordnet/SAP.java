/******************************************************************************
 *  Compilation:  javac-algs4 SAP.java
 *  Execution:    java-algs4 SAP digraph.txt
 ******************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

/**
 * The {@code SAP} class provides methods for querying shortest ancestral path
 * on Digraphs.
 * 
 * An ancestral path between two vertices v and w in a digraph is a directed 
 * path from v to a common ancestor x, together with a directed path from w to 
 * the same ancestor x.
 * A shortest ancestral path is an ancestral path of minimum total length.
 *
 * @author Vladimir C. Petrov
 */

public class SAP {

    private static final boolean DEBUG = false;
    private static final int INFINITY = Integer.MAX_VALUE;

    private final Digraph itsGraph;
    private final QueryOnVertexPair lastQueryOnVertexPair;    
    private final QueryOnVertexSetPair lastQueryOnVertexSetPair;    
    
    // cache for query on vertex pair
    private class QueryOnVertexPair {
        private int itsV;
        private int itsW;
        private int itsAncestor;
        private int itsLength;
        public QueryOnVertexPair() {
            itsV = -1;
            itsW = -1;
            itsAncestor = -1;
            itsLength = -1;
        }
        public void update(int v, int w, int ancestor, int length) {
            itsV = v;
            itsW = w;
            itsAncestor = ancestor;
            itsLength = length;
        }
        public boolean sameQuery(int v, int w) {
            if (v == itsV && w == itsW || v == itsW && w == itsV) {
                if (DEBUG) {
                    StdOut.print("/* Same as the last query. */");                    
                }
                return true;
            }
            return false;
        }
        public int length() { return itsLength; }
        public int ancestor() { return itsAncestor; }
    }
    // cache for query on two sets of vertices
    private class QueryOnVertexSetPair {
        private HashSet<Integer> itsV;
        private HashSet<Integer> itsW;
        private int itsAncestor;
        private int itsLength;
        public QueryOnVertexSetPair() {
            itsV = new HashSet<Integer>();
            itsW = new HashSet<Integer>();
            itsAncestor = -1;
            itsLength = -1;
        }
        public void update(Iterable<Integer> v, Iterable<Integer> w, 
                           int ancestor, int length) {
            itsV = new HashSet<Integer>();
            for (int x : v) itsV.add(x);
            itsW = new HashSet<Integer>();
            for (int x : w) itsW.add(x);
            itsAncestor = ancestor;
            itsLength = length;
        }
        public boolean sameQuery(Iterable<Integer> v, Iterable<Integer> w) {
            HashSet<Integer> vSet = new HashSet<Integer>();
            for (int x : v) vSet.add(x);
            HashSet<Integer> wSet = new HashSet<Integer>();
            for (int x : w) wSet.add(x);
            if (DEBUG) {
                StdOut.print("\nitsV: " + itsV);
                StdOut.print("\nvSet: " + vSet);
                StdOut.print("\nitsW: " + itsW);
                StdOut.print("\nwSet: " + wSet);
            }
            if (itsV.equals(vSet) && itsW.equals(wSet) ||
                itsW.equals(vSet) && itsV.equals(wSet)) {
                if (DEBUG) {
                    StdOut.print("/* Same as the last query. */");                    
                }
                return true;
            }
            return false;
        }
        public int length() { return itsLength; }
        public int ancestor() { return itsAncestor; }
    }

    /**
     * @param graph is a digraph, not necessarily a DAG.
     */
    public SAP(Digraph graph) {
        this.itsGraph = new Digraph(graph);  // a deep copy of the given digraph
        lastQueryOnVertexPair = new QueryOnVertexPair();
        lastQueryOnVertexSetPair = new QueryOnVertexSetPair();
    }

    /**
     * @param v,w a pair of vertices
     * @return length of a shortest ancestral path between v and w; 
     *         -1 if no such path.
     */
    public int length(int v, int w) {
        if (!lastQueryOnVertexPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexPair.length();
    }

    /**
     * @param v,w a pair of sets of vertices
     * @return length of a shortest ancestral path between v and w; 
     *         -1 if no such path.
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!lastQueryOnVertexSetPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexSetPair.length();
    }

    /**
     * @param v,w a pair of vertices
     * @return id of the common ancestor, -1 if not found.
     */
    public int ancestor(int v, int w) {
        if (!lastQueryOnVertexPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexPair.ancestor();
    }

    /**
     * @param v,w a pair of sets of vertices
     * @return id of the common ancestor, -1 if not found.
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!lastQueryOnVertexSetPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexSetPair.ancestor();
    }

    /**
     * Search the common ancestor of 2 vertices.
     * @return id of the common ancestor, -1 if not found.
     */
    private int searchAncestor(int v, int w) {
        DeluxeBFS vTree = new DeluxeBFS(itsGraph, v);
        DeluxeBFS wTree = new DeluxeBFS(itsGraph, w);
        int ancestor = bfs(vTree, wTree);
        int minLength = vTree.distTo(ancestor) + wTree.distTo(ancestor);
        if (ancestor == -1) {
            overflowInfo();
            minLength = -1;
        }
        lastQueryOnVertexPair.update(v, w, ancestor, minLength);
        return ancestor;
    }

    /**
     * Search the common ancestor of 2 sets of vertices.
     * @return id of the common ancestor, -1 if not found.
     */
    private int searchAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        DeluxeBFS vTree = new DeluxeBFS(itsGraph, v);
        DeluxeBFS wTree = new DeluxeBFS(itsGraph, w);
        int ancestor = bfs(vTree, wTree);
        int minLength = vTree.distTo(ancestor) + wTree.distTo(ancestor);
        if (ancestor == -1) {
            overflowInfo();
            minLength = -1;
        }
        lastQueryOnVertexSetPair.update(v, w, ancestor, minLength);
        return ancestor;
    }

    private void overflowInfo() {
        if (DEBUG) {
            StdOut.printf("/* Integer.MAX_VALUE == %d, ", INFINITY);
            StdOut.printf(
                "but %d + %d == %d */", 
                INFINITY, INFINITY, INFINITY + INFINITY);
        }
    }

    /**
     * Run BFS on both sources, one after another, level by level.
     * @param vTree the breadth-first tree rooted at v
     * @param wTree the breadth-first tree rooted at w
     * @return the id of the common ancestor, -1 if not found.
     */ 
    private int bfs(DeluxeBFS vTree, DeluxeBFS wTree) {
        int minLength = INFINITY;
        int ancestor = -1;
        while (true) {
            if (vTree.done() && wTree.done()) {
                // Both BFS's have finished and no SAP has been found.
                break;
            }
            if (!vTree.done()) {
                if (vTree.depth() >= minLength) {
                    // minLength cannot decrease
                    break;
                }
                for (int x : vTree.newVertices()) {
                    // whether these vertices are in another tree
                    if (wTree.hasPathTo(x)) {
                        int pathLength = vTree.distTo(x) + wTree.distTo(x);
                        // update, if such path length < current min length
                        if (pathLength < minLength) {
                            minLength = pathLength;
                            ancestor = x;
                        }
                    }
                }
                vTree.growOneLevel();  // continue growing the tree
            }
            if (!wTree.done()) {
                if (wTree.depth() >= minLength) {
                    // minLength cannot decrease
                    break;
                }
                for (int x : wTree.newVertices()) {
                    // whether these vertices are in another tree
                    if (vTree.hasPathTo(x)) {
                        int pathLength = vTree.distTo(x) + wTree.distTo(x);
                        // update, if such path length < current min length
                        if (pathLength < minLength) {
                            minLength = pathLength;
                            ancestor = x;
                        }
                    }
                }
                wTree.growOneLevel();  // continue growing the tree
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // test constructor
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        SAP sap = new SAP(graph);
        StdOut.println("The Digraph:");
        StdOut.println(sap.itsGraph);

        while (true) {
            StdOut.println(
                "\nChoose which to test:\n" + 
                "    [1] query on a pair of vertices;\n" +
                "    [2] query on a pair of sets of vertices;\n" +
                "    [else] quit.");
            int choice = StdIn.readInt();
            if (choice == 1) {
                StdOut.print("Give a pair of vertices: ");
                int v = StdIn.readInt();
                int w = StdIn.readInt();
                // test ancestor(int, int)
                StdOut.printf("Common Ancestor of %d and %d: ", v, w);
                StdOut.println(sap.ancestor(v, w));
                // test length(int, int)
                StdOut.printf("Length of SAP between %d and %d: ", v, w);
                StdOut.println(sap.length(v, w));
            }
            else if (choice == 2) {
                StdOut.print("Give 2 vertices for the 1st set: ");
                Queue<Integer> v = new Queue<Integer>();
                v.enqueue(StdIn.readInt());
                v.enqueue(StdIn.readInt());
                StdOut.print("Give 2 vertices for the 2nd set: ");
                Queue<Integer> w = new Queue<Integer>();
                w.enqueue(StdIn.readInt());
                w.enqueue(StdIn.readInt());
                // test ancestor(Iterable<Integer>, Iterable<Integer>)
                StdOut.printf("Common Ancestor of " + v + "and " + w + ": ");
                StdOut.println(sap.ancestor(v, w));
                // test length(Iterable<Integer>, Iterable<Integer>)
                StdOut.printf("Length of SAP between " + v + "and " + w + ": ");
                StdOut.println(sap.length(v, w));
            }
            else {
                break;
            }
        }
    }
}