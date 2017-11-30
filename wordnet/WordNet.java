/******************************************************************************
 *  Compilation:    javac-algs4 WordNet.java
 *  Execution:      java-algs4 WordNet synsets.txt hypernyms.txt
 ******************************************************************************/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycleX;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    
    private final HashMap<String, HashSet<Integer>> itsNounToIds;
    private final HashMap<Integer, String> itsIdToNouns;
    private final Digraph itsGraph;
    private final SAP itsSap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // parse synsets.txt
        In in = new In(synsets);
        itsNounToIds = new HashMap<String, HashSet<Integer>>();
        itsIdToNouns = new HashMap<Integer, String>();
        HashSet<Integer> vertices = new HashSet<Integer>();
        int id = 0;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            id = Integer.parseInt(line[0]);
            itsIdToNouns.put(id, line[1]);
            vertices.add(id);
            String[] nouns = line[1].split(" ");
            for (String s : nouns) {
                if (itsNounToIds.containsKey(s)) {
                    itsNounToIds.get(s).add(id);
                }
                else {
                    HashSet<Integer> ids = new HashSet<Integer>();
                    ids.add(id);
                    itsNounToIds.put(s, ids);
                }
            }
        }
        // parse hypernyms.txt, build the Digraph
        in = new In(hypernyms);
        itsGraph = new Digraph(itsIdToNouns.size());
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int synset = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; ++i) {
                int hypernym = Integer.parseInt(line[i]);
                itsGraph.addEdge(synset, hypernym);
                if (synset != hypernym) {
                    vertices.remove(synset);  // such vertex cannot be the root
                }
            }
        }
        // make sure there is only 1 vertex left
        if (vertices.size() != 1) {
            throw new IllegalArgumentException(
                "The input does not have a single root.");
        }
        // else {
        //     int root = vertices.iterator().next();
        //     StdOut.println(
        //         "The root is: " + root + "," + itsIdToNouns.get(root));
        // }
        // make sure the Digraph is acyclic
        DirectedCycleX cycleFinder = new DirectedCycleX(itsGraph);
        if (cycleFinder.hasCycle()) {
            throw new IllegalArgumentException(
                "The input does not correspond to a DAG.");
        }
        // build the Shortest Ancestral Path object
        itsSap = new SAP(itsGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return itsNounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return itsNounToIds.containsKey(word);
    }

    /* Return the ID set corresponding to a noun.
     * A java.lang.IllegalArgumentException will be thrown,
     * if the argument is not a WordNet noun.
     */
    private Iterable<Integer> getIds(String noun) {
        HashSet<Integer> ids = itsNounToIds.get(noun);
        if (ids == null) {
            throw new IllegalArgumentException(
                "The noun argument is not in the WordNet.");
        }
        return ids;
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        return itsSap.length(getIds(nounA), getIds(nounB));
    }

    // a synset that is the common ancestor of nounA and nounB 
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return itsIdToNouns.get(itsSap.ancestor(getIds(nounA), getIds(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        // test the constructor
        WordNet wordnet = new WordNet(synsets, hypernyms);
        StdOut.println("Digraph:");
        StdOut.println(
            wordnet.itsGraph.V() + " vertices and " + 
            wordnet.itsGraph.E() + " Edges.");
        while (true) {
            StdOut.println(
                "\nChoose which to test:\n" + 
                "    [1] whether a noun is in the WordNet;\n" +
                "    [2] distance between 2 nouns;\n" +
                "    [else] quit.");
            int choice = StdIn.readInt();
            if (choice == 1) {
                // test isNoun(String)
                StdOut.print("Give a noun: ");
                StdOut.println(wordnet.isNoun(StdIn.readString()));
            }
            else if (choice == 2) {
                // test distance() and sap()
                StdOut.println("Given 2 nouns:");
                String v = StdIn.readString();
                String w = StdIn.readString();
                StdOut.println("Distance: " + wordnet.distance(v, w));
                StdOut.println("Ancestor: " + wordnet.sap(v, w));
            }
            else {
                break;
            }
        }
    }
}