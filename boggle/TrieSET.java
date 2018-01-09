/******************************************************************************
 *  Compilation:  javac TrieSet.java
 *  Execution:    java TrieSet < words.txt
 *
 *  An set for [A-Z] strings, implemented  using a 26-way trie.
 ******************************************************************************/
import java.util.Iterator;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class TrieSet implements Iterable<String> {
    private static final char[] CHAR = 
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int R = CHAR.length;

    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    public static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
        public Node next(char c) {
            return next[c - 'A'];
        }
        public boolean isString() {
            return isString;
        }
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieSet() {
        assert R == 26;
    }
    public Node root() {
        return root;
    }
    /**
     * Does the set contain the given key?
     * @param key the key
     * @return {@code true} if the set contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException(
            "argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d+1);
    }
    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null) throw new IllegalArgumentException(
            "argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
        }
        else {
            int c = key.charAt(d) - 'A';
            x.next[c] = add(x.next[c], key, d+1);
        }
        return x;
    }

    /**
     * Returns the number of strings in the set.
     * @return the number of strings in the set
     */
    public int size() {
        return n;
    }

    /**
     * Is the set empty?
     * @return {@code true} if the set is empty, and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns all of the keys in the set, as an iterator.
     * To iterate over all of the keys in a set named {@code set}, use the
     * foreach notation: {@code for (Key key : set)}.
     * @return an iterator to all of the keys in the set
     */
    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(CHAR[c]);
            // StdOut.println(prefix);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the set that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the set that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */  
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<String>();
        StringBuilder prefix = new StringBuilder();
        collect(root, prefix, pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, 
                         String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.isString)
            results.enqueue(prefix.toString());
        if (d == pattern.length())
            return;
        int c = pattern.charAt(d) - 'A';
        if (pattern.charAt(d) == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(CHAR[ch]);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(CHAR[c]);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the set that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the set that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) throw new IllegalArgumentException(
            "argument to longestPrefixOf() is null");
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of length length
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.isString) length = d;
        if (d == query.length()) return length;
        int c = query.charAt(d) - 'A';
        return longestPrefixOf(x.next[c], query, d+1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException(
            "argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.isString) n--;
            x.isString = false;
        }
        else {
            int c = key.charAt(d) - 'A';
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.isString) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }


    /**
     * Unit tests the {@code TrieSet} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        TrieSet set = new TrieSet();
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            set.add(key);
        }

        // print results
        if (set.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : set) {
                StdOut.println(key);
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"SHELLSORT\"):");
        StdOut.println(set.longestPrefixOf("SHELLSORT"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"XSHELLSORT\"):");
        StdOut.println(set.longestPrefixOf("XSHELLSORT"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"SHOR\"):");
        for (String s : set.keysWithPrefix("SHOR"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysWithPrefix(\"SHORTENING\"):");
        for (String s : set.keysWithPrefix("SHORTENING"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".HE.L.\"):");
        for (String s : set.keysThatMatch(".HE.L."))
            StdOut.println(s);
    }
}