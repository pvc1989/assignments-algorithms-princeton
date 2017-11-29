import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;

public class Outcast {

    private WordNet wordnet_;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordnet_ = wordnet;
    }   

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int n = nouns.length;
        int max_dist = 0;
        String str = "";
        for (int i = 0; i < n; ++i) {
            int dist = 0;
            for (int j = 0; j < n; ++j) {
                dist += wordnet_.distance(nouns[i], nouns[j]);
            }
            if (max_dist < dist) {
                max_dist = dist;
                str = nouns[i];
            }
        }
        return str;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
 }