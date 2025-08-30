package edu.eci.arsw.generics.collections;

import java.util.*;

/**
 * Implementación propia de una lista doblemente enlazada genérica 
 * Características:
 *   -Doble enlace: nodos con code prev y code next
 *   -Accesos por índice en O(n) con búsqueda desde cabeza/cola según el índice
 *   Iterador fail-fast mediante code modCount
 *
 * @param <E> tipo de elemento.
 */
public class SimpleLinkedList<E> implements List<E> {

    /**
     * Nodo interno de la lista.
     */
    static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(E item, Node<E> prev, Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;
    /** Cuenta modificaciones estructurales para iterador fail-fast */
    protected transient int modCount;


    private void linkFirst(E e) {
        Node<E> h = head;
        Node<E> newNode = new Node<>(e, null, h);
        head = newNode;
        if (h == null) {
            tail = newNode;
        } else {
            h.prev = newNode;
        }
        size++;
        modCount++;
    }

    private void linkLast(E e) {
        Node<E> t = tail;
        Node<E> newNode = new Node<>(e, t, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        size++;
        modCount++;
    }

    private void linkBefore(E e, Node<E> succ) {
        Node<E> pred = succ.prev;
        Node<E> newNode = new Node<>(e, pred, succ);
        succ.prev = newNode;
        if (pred == null) {
            head = newNode;
        } else {
            pred.next = newNode;
        }
        size++;
        modCount++;
    }

    private E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        x.item = null;
        x.next = null;
        x.prev = null;

        size--;
        modCount++;
        return element;
    }

    private Node<E> node(int index) {
        // index validado por caller
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
    }

    // ---------- Métodos List ----------

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /** Iterador fail-fast básico (next/remove). */
    private class Itr implements Iterator<E> {
        Node<E> next = head;
        Node<E> lastReturned;
        int nextIndex = 0;
        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            checkForComodification();
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            checkForComodification();

            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned) {
                next = lastNext;
            } else {
                nextIndex--;
            }
            lastReturned = null;
            expectedModCount = modCount;
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            arr[i++] = x.item;
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        checkPositionIndex(index);
        if (index == size) linkLast(element);
        else linkBefore(element, node(index));
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (Objects.equals(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    @Override
    public void clear() {
        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        head = tail = null;
        size = 0;
        modCount++;
    }

    @Override
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    @Override
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E old = x.item;
        x.item = element;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            if (Objects.equals(o, x.item)) return i;
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = size - 1;
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (Objects.equals(o, x.item)) return i;
            i--;
        }
        return -1;
    }

    // ----- Operaciones de colección utilitarias -----

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c, "La colección no puede ser nula");
        for (Object e : c) if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Objects.requireNonNull(c, "La colección no puede ser nula");
        boolean modified = false;
        for (E e : c) {
            add(e);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Objects.requireNonNull(c, "La colección no puede ser nula");
        checkPositionIndex(index);
        if (c.isEmpty()) return false;

        // Insertar manteniendo el orden de c
        int i = index;
        for (E e : c) {
            add(i++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c, "La colección no puede ser nula");
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c, "La colección no puede ser nula");
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("listIterator() no soportado en esta implementación.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("listIterator(int) no soportado en esta implementación.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList no soportado en esta implementación.");
    }
}
