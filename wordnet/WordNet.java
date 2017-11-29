import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    
    private HashMap<String, HashSet<Integer>> noun_to_ids_;
    private HashMap<Integer, String> id_to_nouns_;
    private Digraph g_;
    private SAP sap_;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // parse synsets
        In in = new In(synsets);
        noun_to_ids_ = new HashMap<String, HashSet<Integer>>();
        id_to_nouns_ = new HashMap<Integer, String>();
        int id = 0;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            id = Integer.parseInt(line[0]);
            id_to_nouns_.put(id, line[1]);
            String[] nouns = line[1].split(" ");
            for (String s : nouns) {
                if (noun_to_ids_.containsKey(s)) {
                    noun_to_ids_.get(s).add(id);
                } else {
                    HashSet<Integer> ids = new HashSet<Integer>();
                    ids.add(id);
                    noun_to_ids_.put(s, ids);
                }
            }
        }
        // build the Digraph
        in = new In(hypernyms);
        g_ = new Digraph(id + 1);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int synset = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; ++i) {
                g_.addEdge(synset, Integer.parseInt(line[i]));
            }
        }
        // build the Shortest Ancestral Path object
        sap_ = new SAP(g_);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun_to_ids_.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return noun_to_ids_.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        return sap_.length(noun_to_ids_.get(nounA), noun_to_ids_.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of 
    // nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int id = sap_.ancestor(
            noun_to_ids_.get(nounA), noun_to_ids_.get(nounB));
        return id_to_nouns_.get(id);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wordnet = new WordNet(synsets, hypernyms);        
        // test parsing synsets and nouns()
        StdOut.println("Nouns:");
        for (String s : wordnet.nouns()) {
            StdOut.println(s);
        }
        // test parsing hypernyms
        StdOut.println("Digraph:");
        StdOut.println(
            wordnet.g_.V() + " vertices and " + 
            wordnet.g_.E() + " Edges.");
        while (true) {
            StdOut.println(
                "\nChoose which to test:\n" + 
                "\t[1] whether a noun is in the WordNet;\n" +
                "\t[2] distance between 2 nouns.");
            int choice = StdIn.readInt();
            if (choice == 1) {
                // test isNoun(String)
                StdOut.print("Give a noun: ");
                StdOut.println(wordnet.isNoun(StdIn.readString()));
            } else if (choice == 2) {
                // test distance() and sap()
                StdOut.println("Given 2 nouns:");
                String v = StdIn.readString();
                String w = StdIn.readString();
                StdOut.println("Distance: " + wordnet.distance(v, w));
                StdOut.println("Common Ancestor: " + wordnet.sap(v, w));
            } else {
                break;
            }
        }
    }
}