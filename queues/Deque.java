/******************************************************************************
 *  Compilation:    javac-algs4 Deque.java
 *  Execution:      java-algs4 Deque
 *
 *  Description:    A double-ended queue or deque (pronounced "deck") is a
 *      generalization of a stack and a queue that supports adding and removing 
 *      items from either the front or the back of the data structure.
 ******************************************************************************/

// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
// import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n; // size of the deque
    private Node first; // head of deque
    private Node last; // tail of deque
    private final Node sentinel; // null node
    
    // helper double linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    // construct an empty deque
    public Deque() {
        sentinel = new Node();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        
        first = new Node();
        first.next = sentinel;
        first.prev = sentinel;
        
        last = new Node();
        last.next = sentinel;
        last.prev = sentinel;
        
        n = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return n;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException(
                                                         "cannot add a null item");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = sentinel;
        if (isEmpty())
            last = first;
        else
            oldfirst.prev = first;
        ++n;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException(
                                                         "cannot add a null item");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = sentinel;
        last.prev = oldlast;
        if (isEmpty())
            first = last;
        else
            oldlast.next = last;
        ++n;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException(
                                                        "cannot remove an item from an empty deque");
        --n;
        Item item = first.item;
        first = first.next;
        if (isEmpty())
            last = sentinel;
        else
            first.prev = sentinel;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException(
                                                        "cannot remove an item from an empty deque");
        --n;
        Item item = last.item;
        last = last.prev;
        if (isEmpty())
            first = sentinel;
        else
            last.next = sentinel;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != sentinel;
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException(
                                                             "there are no more items to return");
            Item item = current.item;
            current = current.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException(
                                                    "cannot call the remove() method in the iterator");
        }
    }
    
    // unit testing (optional)
    public static void main(String[] args) {
        StdOut.println("test constructor():");
        Deque<Integer> deque = new Deque<Integer>();
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
        
        StdOut.println("test addFirst():");
        for (int i = 0; i < 3; ++i) {
            deque.addFirst(i);
        }
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
        
        StdOut.println("test addLast():");
        for (int i = 10; i < 13; ++i) {
            deque.addLast(i);
        }
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
        
        StdOut.println("test iterator():");
        for (Integer t : deque)
            StdOut.print(t + " ");
        StdOut.println();
        
        StdOut.println("test removeFirst():");
        for (int i = 0; i < 3; ++i)
            StdOut.print(deque.size() + " " + deque.removeFirst() + " " +
                         deque.size() + "\n");
        
        StdOut.println("show all elements():");
        for (Integer t : deque)
            StdOut.print(t + " ");
        StdOut.println();
        
        StdOut.println("test removeLast():");
        for (int i = 0; i < 3; ++i)
            StdOut.print(deque.size() + " " + deque.removeLast() + " " +
                         deque.size() + "\n");
        
        StdOut.println("test add a null element():");
            //        deque.addFirst(null);
            //        deque.addLast(null);
        
        
    }
}
