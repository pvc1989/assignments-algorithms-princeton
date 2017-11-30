/******************************************************************************
 *  Compilation:  javac-algs4 SAP.java
 *  Execution:    java-algs4 SAP digraph.txt
 * 
 *  % java BinarySearch tinyW.txt < tinyT.txt
 *  50
 *  99
 *  13
 *
 *  % java BinarySearch largeW.txt < largeT.txt | more
 *  499569
 *  984875
 *  295754
 *  207807
 *  140925
 *  161828
 *  [367,966 total values]
 *  
 ******************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The {@code SAP} class provides methods for querying shortest ancestral path
 * on Digraphs.
 * An ancestral path between two vertices v and w in a digraph is a directed 
 * path from v to a common ancestor x, together with a directed path from w to 
 * the same ancestor x.
 * A shortest ancestral path is an ancestral path of minimum total length.
 *
 *  @author Vladimir C. Petrov
 */

public class SAP {
    private final Digraph itsGraph;
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
            return v == itsV && w == itsW || v == itsW && w == itsV;
        }
        public int length() { return itsLength; }
        public int ancestor() { return itsAncestor; }
    }
    private QueryOnVertexPair lastQueryOnVertexPair;
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
            itsV.clear();
            for (int x : v) itsV.add(x);
            itsW.clear();
            for (int x : w) itsW.add(x);
            itsAncestor = ancestor;
            itsLength = length;
        }
        public boolean sameQuery(Iterable<Integer> v, Iterable<Integer> w) {
            return itsV.equals(v) && itsW.equals(w) || 
                   itsW.equals(v) && itsV.equals(w);
        }
        public int length() { return itsLength; }
        public int ancestor() { return itsAncestor; }
    }
    private QueryOnVertexSetPair lastQueryOnVertexSetPair;    
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph graph) {
        this.itsGraph = new Digraph(graph);  // a deep copy of the given digraph
        lastQueryOnVertexPair = new QueryOnVertexPair();
        lastQueryOnVertexSetPair = new QueryOnVertexSetPair();
    }

    // length of a shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!lastQueryOnVertexPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexPair.length();
    }

    // length of shortest ancestral path between any vertex in v and any vertex 
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!lastQueryOnVertexSetPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexSetPair.length();
    }

    // a common ancestor of v and w that participates in a shortest ancestral 
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!lastQueryOnVertexPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexPair.ancestor();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no 
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!lastQueryOnVertexSetPair.sameQuery(v, w)) {
            searchAncestor(v, w);
        }
        return lastQueryOnVertexSetPair.ancestor();
    }

    /* Run BFS on both sources, one after another, level by level.
     */
    private int searchAncestor(int v, int w) {
        Queue<Integer> vQueue = new Queue<Integer>();  
        vQueue.enqueue(v);
        HashMap<Integer, Integer> distToV = new HashMap<Integer, Integer>();
        distToV.put(v, 0);

        Queue<Integer> wQueue = new Queue<Integer>();  
        wQueue.enqueue(w);
        HashMap<Integer, Integer> distToW = new HashMap<Integer, Integer>();
        distToW.put(w, 0);

        int pathLength = -1;
        int commonAncestor = -1;
        while (true) {
            if (vQueue.isEmpty() && wQueue.isEmpty()) {
                // Both BFS's have finished and no SAP has been found.
                break;
            }
            if (!vQueue.isEmpty()) {
                commonAncestor = bfs(vQueue, distToV, distToW);
                if (commonAncestor != -1) {
                    pathLength = distToV.get(commonAncestor) +
                                 distToW.get(commonAncestor);
                    break;
                }
            }
            if (!wQueue.isEmpty()) {
                commonAncestor = bfs(wQueue, distToW, distToV);
                if (commonAncestor != -1) {
                    pathLength = distToV.get(commonAncestor) +
                                  distToW.get(commonAncestor);
                    break;
                }
            }
        }
        lastQueryOnVertexPair.update(v, w, commonAncestor, pathLength);
        return commonAncestor;
    }

    /* Run BFS on both sources, one after another, level by level.
     */
    private int searchAncestor(
            Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> vQueue = new Queue<Integer>();  
        HashMap<Integer, Integer> distToV = new HashMap<Integer, Integer>();
        for (int x : v) {
            vQueue.enqueue(x);
            distToV.put(x, 0);
        }

        Queue<Integer> wQueue = new Queue<Integer>();
        HashMap<Integer, Integer> distToW = new HashMap<Integer, Integer>();
        for (int x : w) {
            wQueue.enqueue(x);
            distToW.put(x, 0);
        }

        int pathLength = -1;
        int commonAncestor = -1;
        while (true) {
            if (vQueue.isEmpty() && wQueue.isEmpty()) {
                // Both BFS's have finished and no SAP has been found.
                break;
            }
            if (!vQueue.isEmpty()) {
                commonAncestor = bfs(vQueue, distToV, distToW);
                if (commonAncestor != -1) {
                    pathLength = distToV.get(commonAncestor) +
                                  distToW.get(commonAncestor);
                    break;
                }
            }
            if (!wQueue.isEmpty()) {
                commonAncestor = bfs(wQueue, distToW, distToV);
                if (commonAncestor != -1) {
                    pathLength = distToV.get(commonAncestor) +
                                  distToW.get(commonAncestor);
                    break;
                }
            }
        }
        lastQueryOnVertexSetPair.update(
            v, w, commonAncestor, pathLength);
        return commonAncestor;
    }

    /* Grow the breadth-first tree rooted at x.
     * Check whether a vertex is in another tree rooted at y; 
     * if so, return it as a common ancestor.
    */
    private int bfs(
            Queue<Integer> xQueue,
            HashMap<Integer, Integer> distToX,
            HashMap<Integer, Integer> distToY) {
        int parent = xQueue.dequeue();
        if (distToY.containsKey(parent)) {
            return parent;
        }
        int dist = distToX.get(parent) + 1;
        for (int kid : itsGraph.adj(parent)) {
            if (!distToX.containsKey(kid)) {
                xQueue.enqueue(kid);
                distToX.put(kid, dist);
            }
            if (distToY.containsKey(kid)) {
                return kid;
            }
        }
        return -1;
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
                // test searchAncestor(int, int)
                StdOut.printf("Common Ancestor of %d and %d: ", v, w);
                StdOut.println(sap.searchAncestor(v, w));
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
                /* test searchAncestor(Iterable<Integer>, 
                                                Iterable<Integer>) 
                */
                StdOut.print("Common Ancestor of " + v + "and " + w + ": ");
                StdOut.println(sap.searchAncestor(v, w));
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