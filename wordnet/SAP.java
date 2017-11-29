/******************************************************************************
 *  Compilation:  javac-algs4 SAP.java
 *  Execution:    java-algs4 SAP digraph.txt
 *  Dependencies: In.java StdIn.java StdOut.java
 *  Data files:   http://coursera.cs.princeton.edu/algs4/testing/wordnet-testing.zip
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
    private final Digraph graph_;
    // cache for query on vertex pair
    private class QueryOnVertexPair {
        private int v_;
        private int w_;
        private int ancestor_;
        private int length_;
        public QueryOnVertexPair() {
            v_ = -1;
            w_ = -1;
            ancestor_ = -1;
            length_ = -1;
        }
        public void update(int v, int w, int ancestor, int length) {
            v_ = v;
            w_ = w;
            ancestor_ = ancestor;
            length_ = length;
        }
        public boolean sameQuery(int v, int w) {
            if (v == v_ && w == w_ || v == w_ && w == v_) {
                StdOut.print("/* Same as the last query. */ ");                
                return true;
            } else {
                return false;
            }
        }
        public int length() { return length_; }
        public int ancestor() { return ancestor_; }
    }
    private QueryOnVertexPair last_query_on_vertex_pair_;
    // cache for query on two sets of vertices
    private class QueryOnVertexSetPair {
        private HashSet<Integer> v_;
        private HashSet<Integer> w_;
        private int ancestor_;
        private int length_;
        public QueryOnVertexSetPair() {
            v_ = new HashSet<Integer>();
            w_ = new HashSet<Integer>();
            ancestor_ = -1;
            length_ = -1;
        }
        public void update(Iterable<Integer> v, Iterable<Integer> w, 
                           int ancestor, int length) {
            v_.clear();
            for (int x : v) v_.add(x);
            w_.clear();
            for (int x : w) w_.add(x);
            ancestor_ = ancestor;
            length_ = length;
        }
        public boolean sameQuery(Iterable<Integer> v, Iterable<Integer> w) {
            if (v_.equals(v) && w_.equals(w) || w_.equals(v) && v_.equals(w)) {
                StdOut.print("/* Same as the last query. */ ");                
                return true;
            } else {
                return false;
            }
        }
        public int length() { return length_; }
        public int ancestor() { return ancestor_; }
    }
    private QueryOnVertexSetPair last_query_on_vertex_set_pair_;    
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph graph) {
        graph_ = new Digraph(graph);  // a deep copy of the given digraph
        last_query_on_vertex_pair_ = new QueryOnVertexPair();
        last_query_on_vertex_set_pair_ = new QueryOnVertexSetPair();
    }

    // length of a shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!last_query_on_vertex_pair_.sameQuery(v, w)) {
            search_common_ancestor_(v, w);
        }
        return last_query_on_vertex_pair_.length();
    }

    // length of shortest ancestral path between any vertex in v and any vertex 
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!last_query_on_vertex_set_pair_.sameQuery(v, w)) {
            search_common_ancestor_(v, w);
        }
        return last_query_on_vertex_set_pair_.length();
    }

    // a common ancestor of v and w that participates in a shortest ancestral 
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!last_query_on_vertex_pair_.sameQuery(v, w)) {
            search_common_ancestor_(v, w);
        }
        return last_query_on_vertex_pair_.ancestor();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no 
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!last_query_on_vertex_set_pair_.sameQuery(v, w)) {
            search_common_ancestor_(v, w);
        }
        return last_query_on_vertex_set_pair_.ancestor();
    }

    /* Run BFS on both sources, one after another, level by level.
     */
    private int search_common_ancestor_(int v, int w) {
        Queue<Integer> v_queue = new Queue<Integer>();  
        v_queue.enqueue(v);
        HashMap<Integer, Integer> dist_to_v = new HashMap<Integer, Integer>();
        dist_to_v.put(v, 0);

        Queue<Integer> w_queue = new Queue<Integer>();  
        w_queue.enqueue(w);
        HashMap<Integer, Integer> dist_to_w = new HashMap<Integer, Integer>();
        dist_to_w.put(w, 0);

        int path_length = -1;
        int common_ancestor = -1;
        while (true) {
            if (v_queue.isEmpty() && w_queue.isEmpty()) {
                // Both BFS's have finished and no SAP has been found.
                break;
            }
            if (!v_queue.isEmpty()) {
                common_ancestor = bfs_(v_queue, dist_to_v, dist_to_w);
                if (common_ancestor != -1) {
                    path_length = dist_to_v.get(common_ancestor) +
                                  dist_to_w.get(common_ancestor);
                    break;
                }
            }
            if (!w_queue.isEmpty()) {
                common_ancestor = bfs_(w_queue, dist_to_w, dist_to_v);
                if (common_ancestor != -1) {
                    path_length = dist_to_v.get(common_ancestor) +
                                  dist_to_w.get(common_ancestor);
                    break;
                }
            }
        }
        last_query_on_vertex_pair_.update(v, w, common_ancestor, path_length);
        return common_ancestor;
    }

    /* Run BFS on both sources, one after another, level by level.
     */
    private int search_common_ancestor_(
            Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> v_queue = new Queue<Integer>();  
        HashMap<Integer, Integer> dist_to_v = new HashMap<Integer, Integer>();
        for (int x : v) {
            v_queue.enqueue(x);
            dist_to_v.put(x, 0);
        }

        Queue<Integer> w_queue = new Queue<Integer>();
        HashMap<Integer, Integer> dist_to_w = new HashMap<Integer, Integer>();
        for (int x : w) {
            w_queue.enqueue(x);
            dist_to_w.put(x, 0);
        }

        int path_length = -1;
        int common_ancestor = -1;
        while (true) {
            if (v_queue.isEmpty() && w_queue.isEmpty()) {
                // Both BFS's have finished and no SAP has been found.
                break;
            }
            if (!v_queue.isEmpty()) {
                common_ancestor = bfs_(v_queue, dist_to_v, dist_to_w);
                if (common_ancestor != -1) {
                    path_length = dist_to_v.get(common_ancestor) +
                                  dist_to_w.get(common_ancestor);
                    break;
                }
            }
            if (!w_queue.isEmpty()) {
                common_ancestor = bfs_(w_queue, dist_to_w, dist_to_v);
                if (common_ancestor != -1) {
                    path_length = dist_to_v.get(common_ancestor) +
                                  dist_to_w.get(common_ancestor);
                    break;
                }
            }
        }
        last_query_on_vertex_set_pair_.update(
            v, w, common_ancestor, path_length);
        return common_ancestor;
    }

    /* Grow the breadth-first tree rooted at x.
     * Check whether a vertex is in another tree rooted at y; 
     * if so, return it as a common ancestor.
    */
    private int bfs_(
            Queue<Integer> x_queue,
            HashMap<Integer, Integer> dist_to_x,
            HashMap<Integer, Integer> dist_to_y) {
        int parent = x_queue.dequeue();
        if (dist_to_y.containsKey(parent)) {
            return parent;
        }
        int dist = dist_to_x.get(parent) + 1;
        for (int kid : graph_.adj(parent)) {
            if (!dist_to_x.containsKey(kid)) {
                x_queue.enqueue(kid);
                dist_to_x.put(kid, dist);
            }
            if (dist_to_y.containsKey(kid)) {
                return kid;
            }
        }
        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // test constructor
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);
        StdOut.println("The Digraph:");
        StdOut.println(sap.graph_);

        while (true) {
            StdOut.println(
                "\nChoose which to test:\n" + 
                "\t[1] query on a pair of vertices;\n" +
                "\t[2] query on a pair of sets of vertices.");
            int choice = StdIn.readInt();
            if (choice == 1) {
                StdOut.print("Give a pair of vertices: ");
                int v = StdIn.readInt();
                int w = StdIn.readInt();
                // test search_common_ancestor_(int, int)
                StdOut.printf("Common Ancestor of %d and %d: ", v, w);
                StdOut.println(sap.search_common_ancestor_(v, w));
                // test ancestor(int, int)
                StdOut.printf("Common Ancestor of %d and %d: ", v, w);
                StdOut.println(sap.ancestor(v, w));
                // test length(int, int)
                StdOut.printf("Length of SAP between %d and %d: ", v, w);
                StdOut.println(sap.length(v, w));
            } else if (choice == 2) {
                StdOut.print("Give 2 vertices for the 1st set: ");
                Queue<Integer> v = new Queue<Integer>();
                v.enqueue(StdIn.readInt());
                v.enqueue(StdIn.readInt());
                StdOut.print("Give 2 vertices for the 2nd set: ");
                Queue<Integer> w = new Queue<Integer>();
                w.enqueue(StdIn.readInt());
                w.enqueue(StdIn.readInt());
                /* test search_common_ancestor_(Iterable<Integer>, 
                                                Iterable<Integer>) 
                */
                StdOut.print("Common Ancestor of " + v + "and " + w + ": ");
                StdOut.println(sap.search_common_ancestor_(v, w));
                // test ancestor(Iterable<Integer>, Iterable<Integer>)
                StdOut.printf("Common Ancestor of " + v + "and " + w + ": ");
                StdOut.println(sap.ancestor(v, w));
                // test length(Iterable<Integer>, Iterable<Integer>)
                StdOut.printf("Length of SAP between " + v + "and " + w + ": ");
                StdOut.println(sap.length(v, w));
            } else {
                break;
            }
        }
    }
}