/******************************************************************************
 *  Compilation:  javac-algs4 KdTree.java
 *  Execution:    java-algs4 KdTree
 *  Dependencies: 
 *
 *  a mutable data type that uses a 2d-tree to implement the same API. 
 *  A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is 
 *  to build a BST with points in the nodes, using the x- and y-coordinates of 
 *  the points as keys in strictly alternating sequence.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree {
    private static final boolean DEBUG = false;
        // indicate which coordinate is compared at the current Node
    private static final boolean X = true;
    private static final boolean Y = false;
    
    private static class Node {
            // the point
        private Point2D p;
            // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
            // the left/bottom subtree
        private Node lb;
            // the right/top subtree
        private Node rt;
        
            // constructor
        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }
    
    private Node root;
    private int nPoint;
    
        // construct an empty set of points
    public KdTree() {
        root = null;
        nPoint = 0;
    }
        // is the set empty?
    public boolean isEmpty() {
        return (nPoint == 0);
    }
        // number of points in the set
    public int size() {
        return nPoint;
    }
    
        // ensure the argument is valid
    private void check(Object obj) {
        if (obj == null) throw new java.lang.NullPointerException(
            "argument is null");
    }
    
        // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        check(p);
            // insert the first node
        if (nPoint == 0) {
            RectHV r = new RectHV(0, 0, 1, 1);
            root = new Node(p, r, null, null);
        }
            // insert other nodes
        else {
                // recursively call insert(Node nd, Point2D pt, boolean ori)
                // until arrive at a leave or
                // find that the point to be inserted is already in the tree
            recursiveInsert(root, p, X);
        }
        ++nPoint;
    }
    
    private void recursiveInsert(Node nd, Point2D pt, boolean ori) {
            // find that the point to be inserted is already in the tree
        if (pt.equals(nd.p))
                // do nothing
            return;
        
            // X-coordinate is compared at the current Node:
        if (ori == X) {
                // go left
            if (pt.x() < nd.p.x()) {
                    // arrive at a leave:
                if (nd.lb == null) {
                        // create a new RectHV
                    RectHV rectNew = new RectHV(nd.rect.xmin(),
                                                nd.rect.ymin(),
                                                nd.p.x(),
                                                nd.rect.ymax());
                        // create a new Node
                        // add it to the left child of the current Node
                    nd.lb = new Node(pt, rectNew, null, null);
                }
                    // not arrive at a leave:
                else {
                        // goto the next level
                    recursiveInsert(nd.lb, pt, !ori);
                }
            }
                // pt.x() >= nd.p.x()
                // go right
            else {
                    // arrive at a leave:
                if (nd.rt == null) {
                        // create a new RectHV
                    RectHV rectNew = new RectHV(nd.p.x(),
                                                nd.rect.ymin(),
                                                nd.rect.xmax(),
                                                nd.rect.ymax());
                        // create a new Node
                        // add it to the right child of the current Node
                    nd.rt = new Node(pt, rectNew, null, null);
                }
                    // not arrive at a leave:
                else {
                        // goto the next level
                    recursiveInsert(nd.rt, pt, !ori);
                }
            }
        }
        
            // Y-coordinate is compared at the current Node:
        if (ori == Y) {
                // go bottom
            if (pt.y() < nd.p.y()) {
                    // arrive at a leave:
                if (nd.lb == null) {
                        // create a new RectHV
                    RectHV rectNew = new RectHV(nd.rect.xmin(),
                                                nd.rect.ymin(),
                                                nd.rect.xmax(),
                                                nd.p.y());
                        // create a new Node
                        // add it to the bottom child of the current Node
                    nd.lb = new Node(pt, rectNew, null, null);
                }
                    // not arrive at a leave:
                else {
                        // goto the next level
                    recursiveInsert(nd.lb, pt, !ori);
                }
            }
                // pt.y() >= nd.p.y()
                // go top
            else {
                    // arrive at a leave:
                if (nd.rt == null) {
                        // create a new RectHV
                    RectHV rectNew = new RectHV(nd.rect.xmin(),
                                                nd.p.y(),
                                                nd.rect.xmax(),
                                                nd.rect.ymax());
                        // create a new Node
                        // add it to the top child of the current Node
                    nd.rt = new Node(pt, rectNew, null, null);
                }
                    // not arrive at a leave:
                else {
                        // goto the next level
                    recursiveInsert(nd.rt, pt, !ori);
                }
            }
        }
    }
    
        // does the set contain point p?
    public boolean contains(Point2D p) {
        check(p);
        return recursiveContains(root, p, X);
    }
    
    private boolean recursiveContains(Node nd, Point2D pt, boolean ori) {
            // the tree is empty or arrive at a leave
        if (nd == null)
            return false;
        
            // find a equivalent point
        if (pt.equals(nd.p))
            return true;
        
            // X-coordinate is compared at the current Node:
        if (ori == X) {
            if (pt.x() < nd.p.x())
                return recursiveContains(nd.lb, pt, !ori);
            else
                return recursiveContains(nd.rt, pt, !ori);
        }
            // Y-coordinate is compared at the current Node:
        else {
            if (pt.y() < nd.p.y())
                return recursiveContains(nd.lb, pt, !ori);
            else
                return recursiveContains(nd.rt, pt, !ori);
        }
    }
        // draw all of the points to standard draw in black and
        // the subdivisions in red (for vertical splits) and
        // blue (for horizontal splits)
    public void draw() {
            // recursively draw each level
        recursiveDraw(root, X);
    }
    
    private void recursiveDraw(Node nd, boolean ori) {
            // the tree is empty or arrive at a leave
        if (nd == null)
            return;
            // draw current Node
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(nd.p.x(), nd.p.y());
            // draw current split line
        if (ori == X) {
                // vertical split
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
                // line(double x1, double y1, double x2, double y2)
            StdDraw.line(nd.p.x(), nd.rect.ymin(), nd.p.x(), nd.rect.ymax());
        }
        else {
                // horizontal split
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
                // line(double x1, double y1, double x2, double y2)
            StdDraw.line(nd.rect.xmin(), nd.p.y(), nd.rect.xmax(), nd.p.y());
        }
        if (DEBUG) {
            StdDraw.pause(100);
            StdDraw.show();
        }
            // draw the left/bottom child
        recursiveDraw(nd.lb, !ori);
            // draw the right/top child
        recursiveDraw(nd.rt, !ori);
    }
    

        // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
            // initialize container for output
        LinkedList<Point2D> queue = new LinkedList<Point2D>();
            // call recursive version of range-search from root
        if (root != null)
            recursiveRange(rect, root, queue);
            // return container
        return queue;
    }
    
    private void recursiveRange(RectHV rc, Node nd, LinkedList<Point2D> queue) {
            // check if current point in Node lies in given RectHV
        if (rc.contains(nd.p))
            queue.addLast(nd.p);
            // recursively search left/bottom if needed
        if (nd.lb != null && rc.intersects(nd.lb.rect))
            recursiveRange(rc, nd.lb, queue);
            // recursively search right/top if needed
        if (nd.rt != null && rc.intersects(nd.rt.rect))
            recursiveRange(rc, nd.rt, queue);
    }
    
        // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D pQuery) {
        Point2D pNearest = null;
        if (root != null) {
            pNearest = root.p;
            pNearest = recursiveNearest(pQuery, root, X, pNearest);
        }
        if (DEBUG) {
            StdOut.println("pNearest: " + pNearest);
        }
        return pNearest;
    }
    
    private Point2D recursiveNearest(Point2D pQuery, Node nd, boolean ori,
                                  Point2D pNearest) {
            // ckeck distance from current point in Node to query point
        if (pQuery.distanceSquaredTo(nd.p) <
            pQuery.distanceSquaredTo(pNearest)) {
            pNearest = nd.p;
        }
        if (DEBUG) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.02);
            nd.p.draw();
            StdDraw.show();
            StdOut.println("newest pNearest: " + pNearest);
        }
            // go left/bottom first
        if (ori == X && pQuery.x() < nd.p.x() ||
            ori == Y && pQuery.y() < nd.p.y()) {
                // recursively search left/bottom if needed
            if (nd.lb != null &&
                nd.lb.rect.distanceSquaredTo(pQuery) <
                pNearest.distanceSquaredTo(pQuery)) {
                pNearest = recursiveNearest(pQuery, nd.lb, !ori, pNearest);
            }
                // recursively search right/top if needed
            if (nd.rt != null &&
                nd.rt.rect.distanceSquaredTo(pQuery) <
                pNearest.distanceSquaredTo(pQuery)) {
                pNearest = recursiveNearest(pQuery, nd.rt, !ori, pNearest);
            }
        }
            // go right/top first
        else {
            if (nd.rt != null &&
                nd.rt.rect.distanceSquaredTo(pQuery) <
                pNearest.distanceSquaredTo(pQuery)) {
                pNearest = recursiveNearest(pQuery, nd.rt, !ori, pNearest);
            }
            if (nd.lb != null &&
                             nd.lb.rect.distanceSquaredTo(pQuery) <
                             pNearest.distanceSquaredTo(pQuery)) {
                pNearest = recursiveNearest(pQuery, nd.lb, !ori, pNearest);
            }
        }
        return pNearest;
    }
    
        // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        
            // initialize the two data structures with point from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

            // draw kdtree
        kdtree.draw();
        StdDraw.show();

            // test contains()
        Point2D p0 = new Point2D(0, 0);
        Point2D p1 = new Point2D(0.5, 1);
        StdOut.println(kdtree.contains(p0));
        StdOut.println(kdtree.contains(p1));
        
            // draw query rectangle
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        RectHV rect = new RectHV(0.5, 0, 1, 0.5);
        rect.draw();
        StdDraw.show();
        
            // draw points inside query rectangle
        for (Point2D p : kdtree.range(rect))
            p.draw();
        StdDraw.show();

            // draw point at root
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.BLUE);
        kdtree.root.p.draw();
        StdDraw.show();

            // draw query point
        Point2D query = new Point2D(0.1, 0.5);
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.BLACK);
        query.draw();
        StdDraw.show();

            // draw nearest point
        Point2D pNearest = kdtree.nearest(query);
        StdOut.println(pNearest);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.03);
        pNearest.draw();
        StdDraw.show();

        /*
        while (true) {
            
                // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);
            
                // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            kdtree.draw();
            
                // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            
            StdDraw.show();
            StdDraw.pause(40);
         }       
         */

    }

}
