/******************************************************************************
 *  Compilation:    javac-algs4 DeluxeBFS.java
 *  Execution:      java-algs4 DeluxeBFS
 ******************************************************************************/
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The {@code DeluxeBFS} class mimic {@code BreadthFirstDirectedPaths}.
 *
 *  @author Vladimir C. Petrov
 */
public class DeluxeBFS {

    private static final boolean DEBUG = false;
    private static final int INFINITY = Integer.MAX_VALUE;

    private final Digraph itsGraph;  
    private final HashSet<Integer> itsRoot;  // root of the breadth-first tree
    private final HashMap<Integer, Node> itsTree;  // the breadth-first tree
    private LinkedList<Integer> itsNextLevel;
    private LinkedList<Integer> itsCurrLevel;
    private int itsDepth;  // maximum depth of leaves  

    private class Node {
        private final int itsDepth;
        private final int itsParent;  // parent's id
        public Node(int depth, int parent) {
            itsDepth = depth;
            itsParent = parent;
        }
    }

    /**
     * Initialize the breadth-first tree rooted at the source vertices in 
     * {@code sources}.
     * @param g the digraph
     * @param sources the source vertices
     */
    public DeluxeBFS(Digraph graph, Iterable<Integer> sources) {
        itsGraph = graph;
        itsRoot = new HashSet<Integer>();
        itsTree = new HashMap<Integer, Node>();
        itsNextLevel = new LinkedList<Integer>();
        itsCurrLevel = new LinkedList<Integer>();
        itsDepth = 0;
        for (int s : sources) { 
            itsRoot.add(s);
            itsTree.put(s, new Node(0, s));
            itsNextLevel.add(s);
        }
    }

    /**
     * Initialize the breadth-first tree rooted at the source vertex {@code s}.
     * @param g the digraph
     * @param s the source vertex
     */
    public DeluxeBFS(Digraph graph, int s) {
        itsGraph = graph;
        itsRoot = new HashSet<Integer>();
        itsTree = new HashMap<Integer, Node>();
        itsNextLevel = new LinkedList<Integer>();
        itsCurrLevel = new LinkedList<Integer>();
        itsDepth = 0;
        itsRoot.add(s);
        itsTree.put(s, new Node(0, s));
        itsNextLevel.add(s);
    }

    /**
     * Grow the breadth-first tree, one level per call.
     */
    public void growOneLevel() {
        if (itsNextLevel.isEmpty()) return;
        assert itsCurrLevel.isEmpty();
        LinkedList<Integer> temp = itsCurrLevel;
        itsCurrLevel = itsNextLevel;
        itsNextLevel = temp;
        ++itsDepth;
        while (!itsCurrLevel.isEmpty()) {
            int v = itsCurrLevel.remove();
            for (int w : itsGraph.adj(v)) {
                if (!itsTree.containsKey(w)) {
                    itsNextLevel.add(w);
                    itsTree.put(w, new Node(itsDepth, v));
                }
            }
        }
    }
    
    /**
     * Grow the breadth-first tree, one call for all the levels.
     */
    public void growAll() {
        while (!itsNextLevel.isEmpty()) {
            if (DEBUG) {
                StdOut.print("Vertices to be finished: ");
                StdOut.println(newVertices());
            }
            growOneLevel();
        }
    }

    /**
     * @return whether the BFS has finished.
     */
    public boolean done() {
        return itsNextLevel.isEmpty();
    }
    
    /**
     * @return current depth of the breadth-first tree.
     */
    public int depth() {
        return itsDepth;
    }
    
    /**
     * Returns the vertices discovered but unfinished.
     */
    public Iterable<Integer> newVertices() {
        // make a deep copy
        return new LinkedList<Integer>(itsNextLevel);
    }
    /**
     * Is there a directed path from the source {@code s} (or sources) to vertex
     * {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a directed path, {@code false} otherwise
     */
    public boolean hasPathTo(int v) {
        return itsTree.containsKey(v);
    }

    /**
     * Returns the number of edges in a shortest path from the source {@code s}
     * (or sources) to vertex {@code v}?
     * @param v the vertex
     * @return the number of edges in a shortest path
     */
    public int distTo(int v) {
        Node node = itsTree.get(v);
        if (node == null) return INFINITY;
        else              return node.itsDepth;
    }

    /**
     * Returns a shortest path from {@code s} (or sources) to {@code v}, or
     * {@code null} if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        Node node = itsTree.get(v);
        if (node == null) return null;
        Stack<Integer> path = new Stack<Integer>();
        path.push(v);
        while (node.itsDepth != 0) {
            path.push(node.itsParent);
            node = itsTree.get(node.itsParent);
        }
        return path;
    }

   /**
     * Unit tests the {@code DeluxeBFS} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        while (true) {
            StdOut.println(
                "\nChoose which to test:\n" + 
                "    [1] single source;\n" +
                "    [2] multiple sources;\n" +
                "    [else] quit.");
            int choice = StdIn.readInt();
            if (choice == 1) {
                StdOut.print("Give a source: ");
                int s = StdIn.readInt();
                DeluxeBFS bfs = new DeluxeBFS(graph, s);
                bfs.growAll();
                for (int v = 0; v < graph.V(); v++) {
                    if (bfs.hasPathTo(v)) {
                        StdOut.print(s + " to " + v + ": ");
                        Iterator<Integer> x = bfs.pathTo(v).iterator();
                        while (x.hasNext()) {
                            StdOut.print(x.next());
                            if (x.hasNext()) StdOut.print("->");
                        }
                        StdOut.println(", dist == " + bfs.distTo(v));
                    }
                    else {
                        StdOut.println(s + " to " + v + ": not connected");
                    }
                }
            }
            else if (choice == 2) {
                StdOut.print("Give 2 sources: ");
                HashSet<Integer> sources = new HashSet<Integer>();
                sources.add(StdIn.readInt());
                sources.add(StdIn.readInt());
                DeluxeBFS bfs = new DeluxeBFS(graph, sources);
                bfs.growAll();
                for (int v = 0; v < graph.V(); v++) {
                    if (bfs.hasPathTo(v)) {
                        StdOut.print(sources + " to " + v + ": ");
                        Iterator<Integer> x = bfs.pathTo(v).iterator();
                        while (x.hasNext()) {
                            StdOut.print(x.next());
                            if (x.hasNext()) StdOut.print("->");
                        }
                        StdOut.println(", dist == " + bfs.distTo(v));
                    }
                    else {
                        StdOut.println(
                            sources + " to " + v + ": not connected");
                    }
                }
            }
            else {
                break;
            }
        }
    }
}