import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.TreeSet;

public class WordNet {
    
    private HashMap<String, Integer> noun_to_id_;
    private HashMap<Integer, String> id_to_nouns_;
    private Digraph g_;
    private SAP sap_;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // parse synsets
        In in = new In(synsets);
        noun_to_id_ = new HashMap<String, Integer>();
        id_to_nouns_ = new HashMap<Integer, String>();
        int id = 0;
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            id = Integer.parseInt(line[0]);
            id_to_nouns_.put(id, line[1]);
            String[] nouns = line[1].split(" ");
            for (String s : nouns) {
                noun_to_id_.put(s, id);
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
        return noun_to_id_.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return noun_to_id_.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        return sap_.length(noun_to_id_.get(nounA), noun_to_id_.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of 
    // nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int id = sap_.ancestor(noun_to_id_.get(nounA), noun_to_id_.get(nounB));
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
        // test isNoun(String)
        int n = 2;
        StdOut.printf("Given %d nouns:\n", n);
        for (int i = 0; i < n; ++i) {
            StdOut.println(wordnet.isNoun(StdIn.readString()));
        }
        // test distance() and sap()
        StdOut.println("Given 2 nouns:");
        String v = StdIn.readString();
        String w = StdIn.readString();
        StdOut.println("Distance: " + wordnet.distance(v, w));
        StdOut.println("Common Ancestor: " + wordnet.sap(v, w));
    }
}
