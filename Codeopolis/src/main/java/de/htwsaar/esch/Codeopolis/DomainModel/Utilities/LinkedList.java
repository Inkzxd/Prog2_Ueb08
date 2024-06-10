package de.htwsaar.esch.Codeopolis.DomainModel.Utilities;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A generic singly linked list implementation.
 *
 * @param <T> the type of elements in this list
 */
public class LinkedList<T extends Comparable<T>> {
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
    public static class Node<T> {
        T data; // The data stored in the node
        Node<T> next; // The reference to the next node in the list

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public interface Iterator<T> {
        boolean hasNext();
        T next();
        void remove();
    }

    public class LinkedIterator<T> implements Iterator<T>{
        private Node<T> current;
        private Node<T> previous;
        private Node<T> beforePrevious; // Keeps track of the node before 'previous' to update the link when removing

        public LinkedIterator(Node<T> head) {
            this.current = head;
            this.previous = null;
            this.beforePrevious = null;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Move the pointers forward
            T content = current.data;
            beforePrevious = previous;
            previous = current;
            current = current.next;
            return content;
        }

        @Override
        public void remove() {
            if (previous == null) {
                throw new IllegalStateException("next() has not been called or remove() already called after the last next()");
            }

            if (beforePrevious == null) {
                // If `previous` was the first node
                // Update the head of the list to skip the removed node
                previous = null; // Reset previous to avoid illegal state on subsequent calls
            } else {
                // Update the link to skip the removed node
                beforePrevious.next = current;
                previous = null; // Reset previous to avoid illegal state on subsequent calls
            }
        }
    }

    /**
     * Adds an element to the end of the list.
     *
     * @param data the element to be added
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (this.head == null) {
            this.head = newNode;
        } else {
            Node<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
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
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp.data;
        }
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
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                T result = head.data;
                head.data = newData;
                return result;
            }
            Node<T> temp = this.head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            T result = temp.data;
            temp.data = newData;
            return result;
        }
    }

    /**
     * Removes all elements from the list.
     */
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Removes and returns the element at the specified position in the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                T result = head.data;
                size--;
                head = head.next;
                return result;
            }
            Node<T> temp = this.head;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.next;
            }
            T result = temp.next.data;
            temp.next = temp.next.next;
            this.size--;
            return result;
        }
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an iterator over the elements in this list
     */
    public LinkedIterator<T> iterator() {
        return new LinkedIterator<T>(this.head);
    }

    public LinkedList<T> sort() {
        LinkedList<T> sortedList = new LinkedList<>();
    
        // Copy the elements from the original list to the sortedList
        Node<T> current = head;
        while (current != null) {
            sortedList.addLast(current.data);
            current = current.next;
        }
    
        if (sortedList.size() > 1) {
            boolean wasChanged;
    
            do {
                Node<T> currentNode = sortedList.head;
                Node<T> previousNode = null;
                Node<T> nextNode = currentNode.next;
                wasChanged = false;
    
                while (nextNode != null) {
                    if (currentNode.data.compareTo(nextNode.data) > 0) {
                        wasChanged = true;
    
                        // Swap nodes
                        if (previousNode != null) {
                            Node<T> sig = nextNode.next;
    
                            previousNode.next = nextNode;
                            nextNode.next = currentNode;
                            currentNode.next = sig;
                        } else {
                            Node<T> sig = nextNode.next;
    
                            sortedList.head = nextNode;
                            nextNode.next = currentNode;
                            currentNode.next = sig;
                        }
    
                        previousNode = nextNode;
                        nextNode = currentNode.next;
                    } else {
                        previousNode = currentNode;
                        currentNode = nextNode;
                        nextNode = nextNode.next;
                    }
                }
            } while (wasChanged);
        }
    
        return sortedList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    public LinkedList<T> filter (Predicate<T> filterPredicate) {
        LinkedList<T> filteredList = new LinkedList<>();
        Node<T> current = head;
        while (current != null) {
            if (filterPredicate.test(current.data)) {
                filteredList.addLast(current.data);
            }
            current = current.next;
        }
        return filteredList;
    }

    public void forEach (Consumer<T> consumer) {
        Node<T> current = head;
        while (current != null) {
            consumer.accept(current.data);
            current = current.next;
        }
    }

    public void removeIf (Predicate<T> removePredicate) {
        Node<T> current = head;
        Node<T> previous = null;
        while (current != null) {
            if (removePredicate.test(current.data)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
            } else {
                previous = current;
            }
            current = current.next;
        }
    }

    public void addIf (Predicate<T> addPredicate, T data) {
        if (addPredicate.test(data)) {
            addLast(data);
        }
    }

    public void sort (Comparator<T> comparator) {
        Node<T> current = head;
        Node<T> previous = null;
        Node<T> next = current.next;
        while (next != null) {
            if (comparator.compare(current.data, next.data) > 0) {
                if (previous == null) {
                    head = next;
                    current.next = next.next;
                    next.next = current;
                    previous = next;
                    next = current.next;
                } else {
                    previous.next = next;
                    current.next = next.next;
                    next.next = current;
                    previous = next;
                    next = current.next;
                }
            } else {
                previous = current;
                current = next;
                next = next.next;
            }
        }
    }
}