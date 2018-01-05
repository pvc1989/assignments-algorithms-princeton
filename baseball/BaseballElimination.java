/******************************************************************************
 *  Compilation:    javac-algs4 BaseballElimination.java
 *  Execution:      java-algs4 BaseballElimination.java filename.txt
 * 
 ******************************************************************************/
import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private final HashMap<String, Integer> itsNameToId;
    private final String[] itsIdToName;
    private final int[] itsWins;
    private final int[] itsLosses;
    private final int[] itsLefts;
    private final int[][] itsGameCount;
    private final int[][] itsGameId;
    private final HashMap<Integer, Iterable<String>> itsTrivialCache;
    private final HashMap<Integer, Iterable<String>> itsMincutCache;

    // create a baseball division from given filename
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        itsNameToId = new HashMap<String, Integer>();
        itsIdToName = new String[n];
        itsWins = new int[n];
        itsLosses = new int[n];
        itsLefts = new int[n];
        itsGameCount = new int[n][n];
        itsGameId = new int[n][n];
        int k = n;
        for (int i = 0; i != n; ++i) {
            String name = in.readString();
            itsNameToId.put(name, i);
            itsIdToName[i] = name;
            itsWins[i] = in.readInt();
            itsLosses[i] = in.readInt();
            itsLefts[i] = in.readInt();
            for (int j = 0; j != n; ++j) {
                itsGameCount[i][j] = in.readInt();
                if (j > i) itsGameId[i][j] = k++;
            }
        }
        itsTrivialCache = new HashMap<Integer, Iterable<String>>();
        itsMincutCache = new HashMap<Integer, Iterable<String>>();
    }
    // number of teams
    public int numberOfTeams() {
        return itsWins.length;
    }
    // all teams
    public Iterable<String> teams() {
        return itsNameToId.keySet();
    }
    private int nameToId(String team) {
        if (itsNameToId.get(team) == null)
            throw new IllegalArgumentException("No such team!");
        return itsNameToId.get(team);
    }
    // number of wins for given team
    public int wins(String team) {
        return itsWins[nameToId(team)];
    }
    // number of losses for given team
    public int losses(String team) {
        return itsLosses[nameToId(team)];
    }
    // number of remaining games for given team
    public int remaining(String team) {
        return itsLefts[nameToId(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return itsGameCount[nameToId(team1)][nameToId(team2)];
    }
    // trivial elimination
    private Iterable<String> trivialEliminate(int x) {
        if (itsTrivialCache.containsKey(x))
            return itsTrivialCache.get(x);
        final int n = numberOfTeams();
        final int most = itsWins[x] + itsLefts[x];
        // if w[x] + r[x] < w[i], then x is mathematically eliminated by i.
        for (int i = 0; i != n; ++i) {
            if (most < itsWins[i]) {
                LinkedList<String> list = new LinkedList<String>();
                list.add(itsIdToName[i]);
                itsTrivialCache.put(x, list);
                return list;
            }
        }
        itsTrivialCache.put(x, null);
        return null;
    }
    // non-trivial elimination
    private Iterable<String> mincutEliminate(int x) {
        if (itsMincutCache.containsKey(x))
            return itsMincutCache.get(x);
        final int most = itsWins[x] + itsLefts[x];
        final int n = numberOfTeams();
        final int target = n * (n + 1) / 2;  // n + n*(n-1)/2
        final int source = target + 1;
        int capacity = 0;
        FlowNetwork network = new FlowNetwork(source + 1);
        int g = 0;  // id of the next game vertex
        for (int i = 0; i != n; ++i) {
            if (i == x)  // ignore query team
                continue;
            // team -> target
            network.addEdge(new FlowEdge(i, target, most - itsWins[i]));
            for (int j = i+1; j != n; ++j) {
                if (j == x)  // ignore query team
                    continue;
                // game -> team
                g = itsGameId[i][j];
                network.addEdge(new FlowEdge(g, i, INFINITY));
                network.addEdge(new FlowEdge(g, j, INFINITY));
                // source -> game
                network.addEdge(new FlowEdge(source, g, itsGameCount[i][j]));
                capacity += itsGameCount[i][j];
            }
        }
        // mincut
        FordFulkerson mincut = new FordFulkerson(network, source, target);
        if (mincut.value() == capacity) {
            // It is possible to assign winners to all of the remaining games 
            // in such a way that no team wins more games than x.
            itsMincutCache.put(x, null);
            return null;
        }
        else {
            LinkedList<String> list = new LinkedList<String>();
            for (int i = 0; i != n; ++i)
                if (mincut.inCut(i))
                    list.add(itsIdToName[i]);
            itsMincutCache.put(x, list);
            return list;
        }
    }
    // is given team eliminated?
    public boolean isEliminated(String team) {
        final int x = itsNameToId.get(team);
        Iterable<String> list = trivialEliminate(x);
        if (list != null)
            return true;
        list = mincutEliminate(x);
        return list != null;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        final int x = itsNameToId.get(team);
        Iterable<String> list = trivialEliminate(x);
        if (list != null)
            return list;
        list = mincutEliminate(x);
        return list;
    }
    // unittest
    public static void main(String[] args) {
        // reads in a sports division from an input file
        // prints whether each team is mathematically eliminated and 
        // a certificate of elimination for each team that is eliminated:
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}