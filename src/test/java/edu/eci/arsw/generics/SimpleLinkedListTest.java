package edu.eci.arsw.generics;

import edu.eci.arsw.generics.collections.SimpleLinkedList;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class SimpleLinkedListTest {

    @Test
    void add_and_get_shouldWork() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        assertTrue(list.isEmpty());
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void addAtIndex_shouldInsert() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("a");
        list.add("c");
        list.add(1, "b");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void set_shouldReplace() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(10);
        list.add(20);
        int old = list.set(1, 99);
        assertEquals(20, old);
        assertEquals(99, list.get(1));
    }

    @Test
    void removeByIndex_shouldRemove() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        int removed = list.remove(1);
        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(30, list.get(1));
    }

    @Test
    void removeByObject_shouldRemoveFirstMatch() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("x");
        list.add("y");
        list.add("x");
        assertTrue(list.remove("x"));
        assertEquals(2, list.size());
        assertEquals("y", list.get(0));
        assertEquals("x", list.get(1));
    }

    @Test
    void contains_and_indexOf_shouldWork() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(5);
        list.add(7);
        list.add(9);
        assertTrue(list.contains(7));
        assertEquals(1, list.indexOf(7));
        assertEquals(-1, list.indexOf(100));
    }

    @Test
    void iterator_shouldTraverseAll() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> it = list.iterator();
        int sum = 0;
        while (it.hasNext()) sum += it.next();
        assertEquals(6, sum);
    }

    @Test
    void iterator_shouldBeFailFast() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(1);
        list.add(2);
        Iterator<Integer> it = list.iterator();
        assertTrue(it.hasNext());
        list.add(3); // modificación estructural después de crear el iterador
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void clear_shouldEmptyList() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(1);
        list.add(2);
        list.clear();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void invalidIndexes_shouldThrow() {
        SimpleLinkedList<Integer> list = new SimpleLinkedList<>();
        list.add(1);
        list.add(2);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(3, 99));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(2, 0));
    }

    @Test
    void toArray_shouldCopyElements() {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.add("a");
        list.add("b");
        Object[] arr = list.toArray();
        assertArrayEquals(new Object[]{"a", "b"}, arr);

        String[] arr2 = list.toArray(new String[0]);
        assertArrayEquals(new String[]{"a", "b"}, arr2);
    }
}
