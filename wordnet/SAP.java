import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;

public class SAP {

    private final Digraph g_;
    private class Node {
        private final int parent_;
        private final int depth_;
        public Node(int parent, int depth) {
            parent_ = parent;
            depth_ = depth;
        }
        public int Parent() { return parent_; }
        public int Depth() { return depth_; }
    }
    // breadth-first trees rooted at v and w, built in the last query
    private HashMap<Integer, Node> v_tree_, w_tree_;
    private int v_, w_;  // vertices specified in the last query
    private int ca_;  // common ancestor found in the last query

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph g) {
        g_ = new Digraph(g);  // a deep copy of the given digraph
        v_ = -1;
        w_ = -1;
        ca_ = -1;
    }

    // Run BFS on both sources, one after another, level by level,
    // to find the common ancestor with minimum path length.
    private void find_common_ancestor_(int v, int w) {
        if (v == v_ && w_ == w) {
            StdOut.print("(Same as the last query.) ");
            return;
        }

        v_ = v;
        v_tree_ = new HashMap<Integer, Node>();
        v_tree_.put(v, new Node(v, 0));
        Queue<Integer> v_queue = new Queue<Integer>();
        v_queue.enqueue(v);

        w_ = w;
        w_tree_ = new HashMap<Integer, Node>();        
        w_tree_.put(w, new Node(w, 0));
        Queue<Integer> w_queue = new Queue<Integer>();
        w_queue.enqueue(w);

        while (true) {
            // TODO: merge similar blocks
            // Run BFS on v
            if (!v_queue.isEmpty()) {
                int parent = v_queue.dequeue();
                if (w_tree_.containsKey(parent)) {
                    ca_ = parent;
                    return;
                }
                int depth = v_tree_.get(parent).Depth() + 1;
                for (int kid : g_.adj(parent)) {
                    if (!v_tree_.containsKey(kid)) {
                        v_tree_.put(kid, new Node(parent, depth));
                        v_queue.enqueue(kid);
                    }
                }
            }
            // Run BFS on w
            if (!w_queue.isEmpty()) {
                int parent = w_queue.dequeue();
                if (v_tree_.containsKey(parent)) {
                    ca_ = parent;
                    return;
                }
                int depth = w_tree_.get(parent).Depth() + 1;
                for (int kid : g_.adj(parent)) {
                    if (!w_tree_.containsKey(kid)) {
                        w_tree_.put(kid, new Node(parent, depth));
                        w_queue.enqueue(kid);
                    }
                }
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        find_common_ancestor_(v, w);
        return v_tree_.get(ca_).Depth() + w_tree_.get(ca_).Depth();
    }

    // a common ancestor of v and w that participates in a shortest ancestral 
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        find_common_ancestor_(v, w);
        return ca_;
    }

    // length of shortest ancestral path between any vertex in v and any vertex 
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no 
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // test constructor
        SAP sap = new SAP(new Digraph(new In(args[0])));
        StdOut.println("The Digraph:");
        StdOut.println(sap.g_);
        // test find_common_ancestor_()
        StdOut.println("Give 2 query vertices:");        
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        StdOut.printf("Common Ancestor of %d and %d: ", v, w);
        sap.find_common_ancestor_(v, w);
        StdOut.println(sap.ca_);
        // test ancestor()
        StdOut.printf("Common Ancestor of %d and %d: ", v, w);
        StdOut.println(sap.ancestor(v, w));
        // test length()
        StdOut.printf(
            "Length of shortest ancestral path between %d and %d: ", v, w);
        StdOut.println(sap.length(v, w));
    }
}