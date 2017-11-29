/******************************************************************************
 *  Compilation:    javac-algs4 Outcast.java
 *  Execution:      java-algs4 Outcast
 *  Dependencies:   WordNet.java
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet itsWordnet;

    // constructor takes a itsWordnet object
    public Outcast(WordNet wordnet) {
        itsWordnet = wordnet;
    }   

    // given an array of itsWordnet nouns, return an outcast
    public String outcast(String[] nouns) {
        int n = nouns.length;
        int maxDist = 0;
        String str = "";
        for (int i = 0; i < n; ++i) {
            int dist = 0;
            for (int j = 0; j < n; ++j) {
                dist += itsWordnet.distance(nouns[i], nouns[j]);
            }
            if (maxDist < dist) {
                maxDist = dist;
                str = nouns[i];
            }
        }
        return str;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet itsWordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(itsWordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}