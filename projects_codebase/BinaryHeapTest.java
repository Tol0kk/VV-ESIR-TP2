package fr.istic.vv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.NoSuchElementException;

class BinaryHeapTest {
    @Test
    void testCount1() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());

        assertEquals(0, tree.count());
    }

    @Test
    void testCount2() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());
        tree.push(10);

        assertEquals(1, tree.count());
    }

    @Test
    void testPeek1() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());

        assertThrows(NoSuchElementException.class, () -> tree.peek());
    }

    @Test
    void testPeek2() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());
        tree.push(10);

        assertEquals(10, tree.peek());
    }

    @Test
    void testPush() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());

        tree.push(10);
        tree.push(2);
        tree.push(12);
        tree.push(6);
        tree.push(4);
        tree.push(3);
        tree.push(1);
        // Assert
        assertEquals(1, tree.peek());
        assertEquals(7, tree.count());
    }

    @Test
    void testPop1() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());

        // Assert
        assertThrows(NoSuchElementException.class, () -> tree.pop());
    }

    @Test
    void testPop2() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());

        tree.push(10);
        tree.push(2);
        tree.push(12);
        tree.push(6);
        tree.push(4);
        tree.push(3);
        tree.push(1);

        // Assert
        assertEquals(1, tree.peek());
        assertEquals(7, tree.count());
        assertEquals(1, tree.pop());
        assertEquals(6, tree.count());
        assertEquals(2, tree.peek());
    }

    @Test
    void testPop3() {
        BinaryHeap<Integer> tree = new BinaryHeap<Integer>(Comparator.naturalOrder());

        tree.push(10);
        tree.push(2);
        tree.push(12);
        tree.push(6);
        tree.push(4);
        tree.push(3);
        tree.push(1);

        // Assert
        assertEquals(1, tree.pop());
        assertEquals(2, tree.pop());
        assertEquals(3, tree.pop());
        assertEquals(4, tree.pop());
        assertEquals(6, tree.pop());
        assertEquals(10, tree.pop());
        assertEquals(12, tree.pop());
        assertEquals(0, tree.count());
    }
}