package de.htwsaar.esch.codeopolis.tests;

import de.htwsaar.esch.Codeopolis.Util.LinkedList;
import de.htwsaar.esch.Codeopolis.Util.Iterator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedListTest {

    private LinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new LinkedList<>();
    }

    @Test
    void testAddAndSize() {
        assertTrue(list.isEmpty(), "List should be empty initially");
        list.addLast(10);
        assertEquals(1, list.size(), "List size should be 1 after adding an element");
        list.addLast(20);
        assertEquals(2, list.size(), "List size should be 2 after adding another element");
    }

    @Test
    void testRemoveFirst() {
        list.addLast(10);
        list.addLast(20);
        assertEquals(10, list.removeFirst(), "First removed element should be 10");
        assertEquals(20, list.removeFirst(), "Second removed element should be 20");
        assertTrue(list.isEmpty(), "List should be empty after removing all elements");
    }

    @Test
    void testGetAndSet() {
        list.addLast(10);
        list.addLast(20);
        assertEquals(10, list.get(0), "First element should be 10");
        assertEquals(20, list.get(1), "Second element should be 20");

        list.set(0, 15);
        assertEquals(15, list.get(0), "First element should be updated to 15");
    }

    @Test
    void testClear() {
        list.addLast(10);
        list.addLast(20);
        list.clear();
        assertTrue(list.isEmpty(), "List should be empty after clear");
        assertEquals(0, list.size(), "Size should be 0 after clear");
    }

    @Test
    void testRemoveAtIndex() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        assertEquals(20, list.remove(1), "Element at index 1 should be removed");
        assertEquals(2, list.size(), "Size should be 2 after removing an element");
        assertEquals(30, list.get(1), "Element at index 1 should now be 30");
    }

    @Test
    void testIterator() {
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        Iterator<Integer> iterator = list.iterator();
        assertTrue(iterator.hasNext(), "Iterator should have next element");
        assertEquals(10, iterator.next(), "First element should be 10");
        iterator.next(); // Skip 20
        assertEquals(30, iterator.next(), "Third element should be 30");
    }
}
