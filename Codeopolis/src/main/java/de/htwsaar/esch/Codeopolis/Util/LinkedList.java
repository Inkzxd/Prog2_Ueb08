package de.htwsaar.esch.Codeopolis.Util;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Iterator;

/**
 * A generic class for a singly linked list.
 * This class provides basic methods for adding, removing, and accessing elements.
 *
 * @param <T> The type of elements in the list.
 */
public class LinkedList<T extends Comparable<T>> implements Iterable<T> {
    /**
     * Internal node class to hold data and link to the next node.
     */
    private class Node {
        T data;
        Node next;

        /**
         * Node constructor.
         *
         * @param data The data item to be held by this node.
         */
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;  // Head of the list
    private int size;   // Number of elements in the list

    /**
     * Constructor for MyLinkedList.
     * Initializes an empty list.
     */
    public LinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param item The element to be added.
     */
    public void addLast(T item) {
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Adds all elements from another LinkedList to this LinkedList.
     *
     * @param other The LinkedList containing elements to be added.
     */
    public void addAll(LinkedList<T> other) {
        if (other == null || other.isEmpty()) {
            return;
        }
        Iterator<T> iterator = other.iterator();
        while (iterator.hasNext()) {
            this.addLast(iterator.next());
        }
    }

    /**
     * Removes the first element of the list and returns it.
     * If the list is empty, returns null.
     *
     * @return The removed element or null if the list is empty.
     */
    public T removeFirst() {
        if (head == null) return null;
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the size of the list.
     *
     * @return The number of elements in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves the element at a specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds (index < 0 || index >= size).
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Replaces the element at a specified index with a new element.
     * Returns the element previously at the index.
     *
     * @param index The index of the element to replace.
     * @param element The new element to be set at the index.
     * @return The element previously at the index.
     * @throws IndexOutOfBoundsException If the index is out of bounds (index < 0 || index >= size).
     */
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T oldData = current.data;
        current.data = element;
        return oldData;
    }

    /**
     * Clears all elements from the list.
     * After this method is called, the list will be empty.
     */
    public void clear() {
        head = null;  // Remove the reference to the head node, allowing garbage collection
        size = 0;     // Reset the size of the list
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        }
        Node prev = null;
        Node current = head;

        if (index == 0) {
            head = head.next; // Move head
        } else {
            // Traverse to the element before the one to be removed
            for (int i = 0; i < index; i++) {
                prev = current;
                current = current.next;
            }
            // Remove the element
            prev.next = current.next;
        }
        size--;
        return current.data;
    }

    /**
     * Returns an iterator that provides sequential access to the elements in this list.
     *
     * @return an Iterator instance capable of iterating over the elements of the list in sequence.
     */
    public Iterator<T> iterator() {
        return this.new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException("No more elements in the list");
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    /**
     * Sorts the elements of the list using the Bubble Sort algorithm.
     */
    public void sort() {
        if (size <= 1) {
            return;
        }

        for (int n = size; n > 1; n--) {
            Node current = head;
            for (int i = 0; i < n - 1; i++) {
                Node next = current.next;
                if (current.data.compareTo(next.data) > 0) {
                    // Swap current and next data
                    T temp = current.data;
                    current.data = next.data;
                    next.data = temp;
                }
                current = current.next;
            }
        }
    }
    /////////////////////////
    // ---- Uebung 06 ---- //
    /////////////////////////

    // Hilfsmethode 
    
    /**
     * Finds the index of the first occurrence of a specified element in the list.
     *
     * @param  data  the element to search for
     * @return        the index of the first occurrence of the element, or -1 if not found
     */
    public int getIndexOf (T data) {
        for (int i = 0; i < size; i++) {
            if (this.get(i).equals(data)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Filters the elements of this LinkedList based on the given predicate.
     *
     * @param predicate A function that takes an element and returns a boolean indicating whether the element should be included in the result.
     * @return A new LinkedList containing only the elements that satisfy the predicate.
     */
    public LinkedList<T> filter (Predicate<T> predicate) {
        LinkedList<T> result = new LinkedList<>();
       for (T element : this) {
           if (predicate.test(element)) {
               result.addLast(element);
           }
       }
        return result;
    }

    /**
     * Iterates over each element in the LinkedList and applies the given Consumer to each element.
     *
     * @param  consumer  a function that takes an element of type T and performs some operation on it
     */

    public void forEach (Consumer<? super T> consumer) {
        for (T element : this) {
            consumer.accept(element);
        }
    }

    /**
     * Removes all elements from the LinkedList that satisfy the given predicate.
     *
     * @param  predicate  a function that takes an element of type T and returns a boolean indicating whether the element should be removed
     */
    public void removeIf (Predicate<T> predicate) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(this.get(i))) {
                this.remove(i);
            }
        }
    }

    /**
     * Adds the given data to the end of the list if it satisfies the given predicate.
     *
     * @param  data      the data to be added
     * @param  predicate the predicate that determines whether the data should be added
     */
    public void addIf (T data, Predicate<T> predicate) {
        if (predicate.test(data)) {
            this.addLast(data);
        }
    }
    
    /**
     * Sorts the elements in the LinkedList using the given Comparator.
     *
     * @param  comparator  the Comparator used to compare elements in the list
     */
    public void sort (Comparator<T> comparator) {
        for (int i = this.size(); i > 1; i--) {
            for (int j = 0; j < i - 1; j++) {
                if (comparator.compare(this.get(j), this.get(j+1)) == 1) {
                    T temp = this.get(j);
                    this.set(j, this.get(j+1));
                    this.set(j+1, temp);
                }
            }
        }
    }

    public double sum (Function<T, Double> function) {
        double sum = 0;
        for (T element : this) {
            sum += function.apply(element);
        }
        return sum;
    }
}
