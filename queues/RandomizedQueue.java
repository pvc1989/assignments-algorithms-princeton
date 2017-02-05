/******************************************************************************
 *  Compilation:    javac-algs4 RandomizedQueue.java
 *  Execution:      java-algs4 RandomizedQueue
 *
 *  Description:    A randomized queue is similar to a stack or queue, except 
 *      that the item removed is chosen uniformly at random from items in the 
 *      data structure.
 ******************************************************************************/

// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a; // array of items
    private int n; // number of elements on stack
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
    
    // return the number of items on the queue
    public int size() {
        return n;
    }
    
    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;
        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) temp[i] = a[i];
        a = temp;
        // alternative implementation
        // a = java.util.Arrays.copyOf(a, capacity);
    }
    
    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException(
            "cannot add a null item");
        // double size of array if necessary
        if (n == a.length) resize(2*a.length);
        a[n++] = item; // add item
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("underflow");
        // choose a pseudo-random integer between 0 and n − 1
        int j = StdRandom.uniform(n);
        Item item = a[j]; // back up a[j], then assign it with a[n - 1]
        a[j] = a[n - 1];
        a[n - 1] = null; // to avoid loitering
        --n;
        // shrink size of array if necessary
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }
    
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("underflow");
        // choose a pseudo-random integer between 0 and n − 1
        // then return the corresponding item
        return a[StdRandom.uniform(n)];
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }
    private class RandomArrayIterator implements Iterator<Item> {
        private int i;
        private int[] iter;
        
        public RandomArrayIterator() {
            i = n - 1;
            iter = new int[n];
            for (int j = 0; j < n; ++j) iter[j] = j;
            StdRandom.shuffle(iter);
        }
        
        public boolean hasNext() {
            return i >= 0;
        }
        
        public void remove() {
            throw new UnsupportedOperationException(
                "cannot call the remove() method in the iterator");
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException(
                "there are no more items to return");
            return a[i--];
        }
    }
    
    // unit testing (optional)
    public static void main(String[] args) {
        StdOut.println("test constructor():");
        RandomizedQueue<Integer> rdQueue = new RandomizedQueue<Integer>();
        StdOut.println(rdQueue.size());
        StdOut.println(rdQueue.isEmpty());
        
        StdOut.println("test enqueue():");
        for (int i = 0; i < 10; ++i) {
            rdQueue.enqueue(i);
        }
        StdOut.println(rdQueue.size());
        StdOut.println(rdQueue.isEmpty());
        
        StdOut.println("test sample():");
        for (int i = 0; i < 5; ++i)
            StdOut.print(rdQueue.size() + " " + rdQueue.sample() + " " +
                         rdQueue.size() + "\n");

        StdOut.println("test dequeue():");
        for (int i = 0; i < 5; ++i)
            StdOut.print(rdQueue.size() + " " + rdQueue.dequeue() + " " +
                         rdQueue.size() + "\n");
        
        StdOut.println("test iterator():");
        for (Integer t : rdQueue)
            StdOut.print(t + " ");
        StdOut.println();
        
    }
}
