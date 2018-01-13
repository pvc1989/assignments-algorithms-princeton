/******************************************************************************
 *  Compilation:  javac-algs4 KdTree.java
 *  Execution:    java-algs4 KdTree
 *  Dependencies: 
 * 
 *  $ java-algs4 KdTree < circle4.txt
 *  Size: 4
 *  $ java-algs4 KdTreeGenerator 5 | java-algs4 KdTree
 *  Size: 5
 * 
 ******************************************************************************/

import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static final boolean DEBUG = false;
    // indicate which isXinate is compared at the current Node
    private static final boolean X = true;
    private static final boolean Y = false;
    
    private static class Node {
        private Point2D point;
        private RectHV rect;
        private Node left;   // the left/bottom subtree
        private Node right;  // the right/top subtree
        
        public Node(Point2D point, RectHV rect, Node lb, Node rt) {
            this.point = point;
            this.rect = rect;
            this.left = left;
            this.right = right;
        }
    }
    
    private Node root;
    private int size;
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    // is the set empty?
    public boolean isEmpty() {
        return (size == 0);
    }
    // number of points in the set
    public int size() {
        return size;
    }
    // ensure the argument is valid
    private void check(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        check(point);
        if (isEmpty()) {
            // insert the first node at root
            root = new Node(point, new RectHV(0, 0, 1, 1), null, null);
            ++size;
        }
        else {
            insert(root, point, X);
        }
    }
    private void insert(Node node, Point2D point, boolean isX) {
        // the point to be inserted is already in the tree:
        if (point.equals(node.point)) return;
        if (isX) {  // X-coordinate is compared at the current Node
            if (point.x() < node.point.x()) {  // go left
                if (node.left == null) {  // at a leave
                    RectHV rect = new RectHV(
                        node.rect.xmin(), node.rect.ymin(),
                        node.point.x(), node.rect.ymax());
                    // add a new Node to the left child of the current Node
                    node.left = new Node(point, rect, null, null);
                    ++size;
                }
                else {  // not at a leave
                    insert(node.left, point, !isX);
                }
            }
            else {  // point.x() >= node.point.x(), go right
                if (node.right == null) {  // at a leave
                    RectHV rect = new RectHV(
                        node.point.x(), node.rect.ymin(),
                        node.rect.xmax(), node.rect.ymax());
                    // add a new Node to the right child of the current Node
                    node.right = new Node(point, rect, null, null);
                    ++size;
                }
                else {  // not at a leave
                    insert(node.right, point, !isX);
                }
            }
        }
        // similar to X
        if (isX == Y) {  // Y-coordinate is compared at the current Node
            if (point.y() < node.point.y()) {
                if (node.left == null) {
                    RectHV rect = new RectHV(
                        node.rect.xmin(), node.rect.ymin(),
                        node.rect.xmax(), node.point.y());
                    // add a new Node to the left child of the current Node
                    node.left = new Node(point, rect, null, null);
                    ++size;
                }
                else {
                    insert(node.left, point, !isX);
                }
            }
            else {  // point.y() >= node.point.y()
                if (node.right == null) {
                    RectHV rectNew = new RectHV(
                        node.rect.xmin(), node.point.y(),
                        node.rect.xmax(), node.rect.ymax());
                    // add a new Node to the right child of the current Node
                    node.right = new Node(point, rectNew, null, null);
                    ++size;
                }
                else {
                    insert(node.right, point, !isX);
                }
            }
        }
    }
    // does the set contain point p?
    public boolean contains(Point2D point) {
        check(point);
        return contains(root, point, X);
    }
    private boolean contains(Node node, Point2D point, boolean isX) {
        // the tree is empty or arrive at a leave
        if (node == null) return false;
        // find a equivalent point
        if (point.equals(node.point)) return true;
        if (isX == X) {  // X-coordinate is compared at the current Node
            if (point.x() < node.point.x())
                return contains(node.left, point, !isX);
            else
                return contains(node.right, point, !isX);
        }
        else {  // Y-coordinate is compared at the current Node
            if (point.y() < node.point.y())
                return contains(node.left, point, !isX);
            else
                return contains(node.right, point, !isX);
        }
    }
    // draw all of the points to standard draw in black and
    // the subdivisions in red (for vertical splits) and
    // blue (for horizontal splits)
    public void draw() {
        draw(root, X);
    }
    private void draw(Node node, boolean isX) {
        // the tree is empty or arrive at a leave
        if (node == null) return;
        // draw current Node
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        // draw current split line
        if (isX == X) {  // vertical split
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            // line(double x1, double y1, double x2, double y2)
            StdDraw.line(node.point.x(), node.rect.ymin(), 
                         node.point.x(), node.rect.ymax());
        }
        else {  // horizontal split
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            // line(double x1, double y1, double x2, double y2)
            StdDraw.line(node.rect.xmin(), node.point.y(),
                         node.rect.xmax(), node.point.y());
        }
        if (DEBUG) {
            StdDraw.pause(100);
            StdDraw.show();
        }
        draw(node.left, !isX);
        draw(node.right, !isX);
    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        check(rect);
        // initialize container for output
        LinkedList<Point2D> queue = new LinkedList<Point2D>();
        // call recursive version of range-search from root
        if (root != null) range(rect, root, queue);
        return queue;
    }
    private void range(RectHV range, Node node, LinkedList<Point2D> queue) {
        // check if current point in Node lies in the given range
        if (range.contains(node.point))
            queue.addLast(node.point);
        // recursively search left if needed
        if (node.left != null && range.intersects(node.left.rect))
            range(range, node.left, queue);
        // recursively search right if needed
        if (node.right != null && range.intersects(node.right.rect))
            range(range, node.right, queue);
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D pQuery) {
        check(pQuery);
        Point2D pNearest = null;
        if (root != null) {
            pNearest = root.point;
            pNearest = nearest(pQuery, pNearest, root, X);
        }
        if (DEBUG) {
            StdOut.println("pNearest: " + pNearest);
        }
        return pNearest;
    }
    private Point2D nearest(Point2D pQuery, Point2D pNearest,
                            Node node, boolean isX) {
        if (pQuery.distanceSquaredTo(node.point) <
            pQuery.distanceSquaredTo(pNearest)) {
            pNearest = node.point;
        }
        if (DEBUG) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.02);
            node.point.draw();
            StdDraw.show();
            StdOut.println("newest pNearest: " + pNearest);
        }
        // go left/bottom first
        if (isX == X && pQuery.x() < node.point.x() ||
            isX == Y && pQuery.y() < node.point.y()) {
            if (node.left != null &&
                    node.left.rect.distanceSquaredTo(pQuery) <
                    pNearest.distanceSquaredTo(pQuery)) {
                pNearest = nearest(pQuery, pNearest, node.left, !isX);
            }
            if (node.right != null &&
                    node.right.rect.distanceSquaredTo(pQuery) <
                    pNearest.distanceSquaredTo(pQuery)) {
                pNearest = nearest(pQuery, pNearest, node.right, !isX);
            }
        }
        else {
            if (node.right != null &&
                    node.right.rect.distanceSquaredTo(pQuery) <
                    pNearest.distanceSquaredTo(pQuery)) {
                pNearest = nearest(pQuery, pNearest, node.right, !isX);
            }
            if (node.left != null &&
                    node.left.rect.distanceSquaredTo(pQuery) <
                    pNearest.distanceSquaredTo(pQuery)) {
                pNearest = nearest(pQuery, pNearest, node.left, !isX);
            }
        }
        return pNearest;
    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree points = new KdTree();
        while (!StdIn.isEmpty()) {
            points.insert(new Point2D(/* x= */StdIn.readDouble(),
                                      /* y= */StdIn.readDouble()));
        }
        StdOut.printf("Size: %d\n", points.size());
    }
}