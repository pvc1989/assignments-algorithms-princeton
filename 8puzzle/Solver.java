/******************************************************************************
 *  Compilation:  javac-algs4 Solver.java
 *  Execution:    java-algs4 Solver filename1.txt filename2.txt ...
 *  Dependencies: Board.java
 *
 *  This program finds the minimum number of moves to reach the goal state.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.Comparator;

public class Solver {
    private SearchNode lastSN; // last SearhcNode, whos board is goal
    
    private class SearchNode {
        private int nMove; // number of moves made to reach the board
        private Board board; // reference to the current board
        private SearchNode prevSN; // reference to the previous search-node
        // constructor
        public SearchNode(int n, Board c, SearchNode p) {
            nMove = n;
            board = c;
            prevSN = p;
        }
    }
    
    private class PriorityOrder implements Comparator<SearchNode> {
        public int compare(SearchNode v, SearchNode w) {
            int vPri = v.board.manhattan() + v.nMove;
            int wPri = w.board.manhattan() + w.nMove;
            if (vPri < wPri) return -1;
            if (vPri > wPri) return +1;
            return 0;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException(
            "null argument");
        lastSN = null;
            // create 2 empty priority-queues for SearchNode
            // one for initial, another for initial.twin()
        PriorityOrder po = new PriorityOrder();
        MinPQ<SearchNode> pq1SN = new MinPQ<SearchNode>(po);
        MinPQ<SearchNode> pq2SN = new MinPQ<SearchNode>(po);
            // create the first SN obj
        SearchNode obj1SN = new SearchNode(0, initial, null);
        SearchNode obj2SN = new SearchNode(0, initial.twin(), null);
            // enqueue it to the pq
        pq1SN.insert(obj1SN);
        pq2SN.insert(obj2SN);
            // until one of them is solved
        while (!obj1SN.board.isGoal() && !obj2SN.board.isGoal()) {
                // delete from the priority queue the search node with the
                // minimum priority
            obj1SN = pq1SN.delMin();
                // insert onto the priority queue all neighboring search nodes
            Iterable<Board> stack1Board = obj1SN.board.neighbors();
            for (Board neighbBoard : stack1Board) {
                    // don't enqueue a neighbor if its board is the same as the
                    // board of the previous search node.
                if (obj1SN.prevSN == null ||
                    !neighbBoard.equals(obj1SN.prevSN.board)) {
                    pq1SN.insert(new SearchNode(obj1SN.nMove + 1,
                                                neighbBoard, obj1SN));
                }
            }
                // parallel solution for its twin
            obj2SN = pq2SN.delMin();
            Iterable<Board> stack2Board = obj2SN.board.neighbors();
            for (Board neighbBoard : stack2Board) {
                    // don't enqueue a neighbor if its board is the same as the
                    // board of the previous search node.
                if (obj2SN.prevSN == null ||
                    !neighbBoard.equals(obj2SN.prevSN.board)) {
                    pq2SN.insert(new SearchNode(obj2SN.nMove + 1,
                                                neighbBoard, obj2SN));
                }
            }
        }
        if (obj1SN.board.isGoal())
            lastSN = obj1SN;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        if (lastSN != null) return true;
        return false;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (lastSN != null) return lastSN.nMove;
        return -1;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
            // create an empty stack for Board
        LinkedList<Board> dequeBoard = new LinkedList<Board>();
            // recursively add each board into the deque
        SearchNode objSN = lastSN;
        do {
            dequeBoard.addFirst(objSN.board);
            objSN = objSN.prevSN;
        } while (objSN != null);
        return dequeBoard;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
