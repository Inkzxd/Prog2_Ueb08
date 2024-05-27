package de.htwsaar.esch.Codeopolis.DomainModel;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Klasse LinkedList mit generischem Typ T
 * Beinhält Methoden zum Hinzufügen, Entfernen, Setzen, Leeren, Größe und Iterieren
 * Beinhalet eine innere Klasse Node
 * @author Christian Petry
 * @version 1.0
 */
public class LinkedList<T> {

    // Attribute
    private Node head;
    private int size;

    // Innere Klasse Node
    public class Node {
        T data;
        Node next;
        
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Standardkonstruktor, initialisiert head mit null und size mit 0
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    /**
     * Methode zum Hinzufügen eines Elements am Ende der Liste
     * @param data Element, das hinzugefügt werden soll
     */
    public void addLast(T data) {
        Node newNode = new Node(data);
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
     * Methode zum Entfernen des Elements am Anfang der Liste
     * @return data Element, das entfernt wurde
     */
    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        Node current = head;
        head = head.next;
        size--;
        return current.data;
    }

    /**
     * Methode zum Überprüfen, ob die Liste leer ist
     * @return true, wenn die Liste leer ist, sonst false
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Methode zur Ausgabe der Größe der Liste
     * @return size Größe der Liste
     */
    public int size() {
        return size;
    }

    /**
     * Methode zur Ausgabe eines Elements an einem bestimmten Index
     * @param index Index des Elements
     * @return data Element an dem Index
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Methode zum ersetzten eines Elements an einem bestimmten Index
     * @param index Index des Elements
     * @param data neues Element
     * @return oldData altes Element
     */ 
    public T set (int index, T data) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T oldData = current.data;
        current.data = data;
        return oldData;
    }

    /**
     * Methode zum Leeren der Liste
     */
    public void clear () {
        head = null;
        size = 0;
    }

    /**
     * Methode zum Entfernen eines Elements an einem bestimmten Index
     * @param index Index des Elements
     * @return removeNode Element, das entfernt wurde
     */
    public T remove (int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return removeFirst();
        }
        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        Node removeNode = current.next;
        current.next = removeNode.next;
        size--;
        return removeNode.data;
    }

    /**
     * Methode zum Iterieren über die Liste
     * @param consumer Consumer, der das Element akzeptiert
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    /**
     * Methode zum Sortieren der Liste mit BubbleSort
     * @return sortedList sortierte Liste
     */
    public LinkedList<T> bubbleSort() {
        LinkedList<T> sortedList = new LinkedList<>();
        Node current = head;
        while (current != null) {
            sortedList.addLast(current.data);
            current = current.next;
        }
        for (int i = 1; i < sortedList.size(); i++) {
            for (int j = 0; j < sortedList.size() - i; j++) {
                if (sortedList.get(j).hashCode() > sortedList.get(j + 1).hashCode()){
                    T temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                }
            }
        }
        return sortedList;
    }


    /**
     * Methode zur Ausgabe der Liste als String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node current = head;
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
}
