package de.htwsaar.esch.Codeopolis.DomainModel;

import java.util.NoSuchElementException;

/**
 * A generic singly linked list implementation.
 *
 * @param <T> the type of elements in this list
 */
public class LinkedList<T> {
    private Node<T> head; // The head (first node) of the list
    private int size; // The number of elements in the list

    /**
     * Constructs an empty list.
     */
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Node class representing each element in the linked list.
     *
     * @param <T> the type of data stored in the node
     */
    private static class Node<T> {
        T data; // The data stored in the node
        Node<T> next; // The reference to the next node in the list

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param data the element to be added
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the first element of the list.
     *
     * @return the removed element
     * @throws NoSuchElementException if the list is empty
     */
    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("The list is empty.");
        }
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified position in the list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Replaces the element at the specified position in the list with the specified element.
     *
     * @param index   the index of the element to replace
     * @param newData the element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T set(int index, T newData) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T oldData = current.data;
        current.data = newData;
        return oldData;
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Removes and returns the element at the specified position in the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T remove(int index) {
        checkIndex(index);
        if (index == 0) {
            return removeFirst();
        }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        T data = current.next.data;
        current.next = current.next.next;
        size--;
        return data;
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an iterator over the elements in this list
     */
    public LinkedListIterator iterator() {
        return new LinkedListIterator();
    }

    /**
     * Checks if the index is in range.
     *
     * @param index the index to check
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * An iterator over the elements in the list.
     */
    public class LinkedListIterator {
        private Node<T> current = head; // The current node in the iteration

        /**
         * Checks if the iteration has more elements.
         *
         * @return true if the iteration has more elements, false otherwise
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

}