import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.TreeSet;

public class SAP {

    private final Digraph g_;
    // cache for single source query
    private int v_, w_;
    private HashMap<Integer, Integer> dist_to_v_, dist_to_w_;
    private int ca_;  // common ancestor of 2 vertices 
    // cache for multiple sources query
    private TreeSet<Integer> v_set_, w_set_;
    private HashMap<Integer, Integer> dist_to_v_set_, dist_to_w_set_;
    private int ca_of_2_sets_;  // common ancestor of 2 sets of vertices

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph g) {
        g_ = new Digraph(g);  // a deep copy of the given digraph
        v_ = -1;
        w_ = -1;
        ca_ = -1;
    }

    // Run BFS on both sources, one after another, level by level,
    // to find the common ancestor with minimum path length.
    private void bfs_(int v, int w) {
        if (v == v_ && w_ == w) {
            StdOut.print("(Same as the last query.) ");
            return;
        }

        v_ = v;
        dist_to_v_ = new HashMap<Integer, Integer>();
        dist_to_v_.put(v, 0);
        Queue<Integer> v_queue = new Queue<Integer>();
        v_queue.enqueue(v);

        w_ = w;
        dist_to_w_ = new HashMap<Integer, Integer>();        
        dist_to_w_.put(w, 0);
        Queue<Integer> w_queue = new Queue<Integer>();
        w_queue.enqueue(w);

        while (true) {
            // TODO: merge similar blocks
            // Run BFS on v
            if (!v_queue.isEmpty()) {
                int parent = v_queue.dequeue();
                if (dist_to_w_.containsKey(parent)) {
                    ca_ = parent;
                    return;
                }
                int depth = dist_to_v_.get(parent) + 1;
                for (int kid : g_.adj(parent)) {
                    if (!dist_to_v_.containsKey(kid)) {
                        dist_to_v_.put(kid, depth);
                        v_queue.enqueue(kid);
                    }
                }
            }
            // Run BFS on w
            if (!w_queue.isEmpty()) {
                int parent = w_queue.dequeue();
                if (dist_to_v_.containsKey(parent)) {
                    ca_ = parent;
                    return;
                }
                int depth = dist_to_w_.get(parent) + 1;
                for (int kid : g_.adj(parent)) {
                    if (!dist_to_w_.containsKey(kid)) {
                        dist_to_w_.put(kid, depth);
                        w_queue.enqueue(kid);
                    }
                }
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        bfs_(v, w);
        return dist_to_v_.get(ca_) + dist_to_w_.get(ca_);
    }

    // a common ancestor of v and w that participates in a shortest ancestral 
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        bfs_(v, w);
        return ca_;
    }

    // Run BFS on both sources, one after another, level by level,
    // to find the common ancestor with minimum path length.
    private void bfs_(Iterable<Integer> v, Iterable<Integer> w) {
        TreeSet<Integer> v_set = new TreeSet<Integer>();
        for (int x : v) v_set.add(x);
        TreeSet<Integer> w_set = new TreeSet<Integer>();
        for (int x : w) w_set.add(x);
        if (v_set.equals(v_set_) && w_set.equals(w_set_)) {
            StdOut.print("(Same as the last query.) ");
            return;
        }

        v_set_ = v_set;
        dist_to_v_set_ = new HashMap<Integer, Integer>();
        Queue<Integer> v_queue = new Queue<Integer>();
        for (int x : v_set) {
            dist_to_v_set_.put(x, 0);
            v_queue.enqueue(x);
        }

        w_set_ = w_set;
        dist_to_w_set_ = new HashMap<Integer, Integer>();  
        Queue<Integer> w_queue = new Queue<Integer>();  
        for (int x : w_set) {
            dist_to_w_set_.put(x, 0);
            w_queue.enqueue(x);
        }

        while (true) {
            // TODO: merge similar blocks
            // Run BFS on v_set
            if (!v_queue.isEmpty()) {
                int parent = v_queue.dequeue();
                if (dist_to_w_set_.containsKey(parent)) {
                    ca_of_2_sets_ = parent;
                    return;
                }
                int depth = dist_to_v_set_.get(parent) + 1;
                for (int kid : g_.adj(parent)) {
                    if (!dist_to_v_set_.containsKey(kid)) {
                        dist_to_v_set_.put(kid, depth);
                        v_queue.enqueue(kid);
                    }
                }
            }
            // Run BFS on w_set
            if (!w_queue.isEmpty()) {
                int parent = w_queue.dequeue();
                if (dist_to_v_set_.containsKey(parent)) {
                    ca_of_2_sets_ = parent;
                    return;
                }
                int depth = dist_to_w_set_.get(parent) + 1;
                for (int kid : g_.adj(parent)) {
                    if (!dist_to_w_set_.containsKey(kid)) {
                        dist_to_w_set_.put(kid, depth);
                        w_queue.enqueue(kid);
                    }
                }
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex 
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfs_(v, w);
        return dist_to_v_set_.get(ca_of_2_sets_) + 
               dist_to_w_set_.get(ca_of_2_sets_);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no 
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfs_(v, w);
        return ca_of_2_sets_;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // test constructor
        SAP sap = new SAP(new Digraph(new In(args[0])));
        StdOut.println("The Digraph:");
        StdOut.println(sap.g_);
        // test bfs_(int, int)
        StdOut.println("Give 2 query vertices:");        
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        StdOut.printf("Common Ancestor of %d and %d: ", v, w);
        sap.bfs_(v, w);
        StdOut.println(sap.ca_);
        // test ancestor(int, int)
        StdOut.printf("Common Ancestor of %d and %d: ", v, w);
        StdOut.println(sap.ancestor(v, w));
        // test length(int, int)
        StdOut.printf(
            "Length of shortest ancestral path between %d and %d: ", v, w);
        StdOut.println(sap.length(v, w));
        // test bfs_(Iterable<Integer>, Iterable<Integer>)
        StdOut.println("Give 2 query vertices in the 1st set:");        
        TreeSet<Integer> v_set = new TreeSet<Integer>();
        v_set.add(StdIn.readInt());
        v_set.add(StdIn.readInt());
        StdOut.println("Give 2 query vertices in the 2nd set:");        
        TreeSet<Integer> w_set = new TreeSet<Integer>();
        w_set.add(StdIn.readInt());
        w_set.add(StdIn.readInt());
        StdOut.println("Common Ancestor of " + v_set + " and " + w_set);
        sap.bfs_(v_set, w_set);
        StdOut.println(sap.ca_of_2_sets_);
        // test ancestor(Iterable<Integer>, Iterable<Integer>)
        StdOut.println("Common Ancestor of " + v_set + " and " + w_set);
        StdOut.println(sap.ancestor(v_set, w_set));
        // test length(Iterable<Integer>, Iterable<Integer>)
        StdOut.println(
            "Length of shortest ancestral path between " + 
            v_set + " and " + w_set);
        StdOut.println(sap.length(v_set, w_set));
    }
}