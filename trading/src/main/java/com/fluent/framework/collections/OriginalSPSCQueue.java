package com.fluent.framework.collections;

import java.util.*;
import java.util.concurrent.atomic.*;

import static com.fluent.framework.utility.ContainerUtil.*;


/**
 * <ul>
 * <li>Lock free, observing single writer principal.
 * <li>Replacing the long fields with AtomicLong and using lazySet instead of
 * volatile assignment.
 * <li>Using the power of 2 mask, forcing the capacity to next power of 2.
 * <li>Adding head and tail cache fields. Avoiding redundant volatile reads.
 * <li>Padding head/tail AtomicLong fields. Avoiding false sharing.
 * <li>Padding head/tail cache fields. Avoiding false sharing.
 * </ul>
 */

public final class OriginalSPSCQueue<E> implements Queue<E>{

    private final int capacity;
    private final int mask;
    private final E[] buffer;

    private final AtomicLong tail = new PaddedAtomicLong( ZERO );
    private final AtomicLong head = new PaddedAtomicLong( ZERO );

    public static class PaddedLong {
        public long value = 0, p1, p2, p3, p4, p5, p6;
    }

    private final PaddedLong tailCache = new PaddedLong();
    private final PaddedLong headCache = new PaddedLong();

    @SuppressWarnings("unchecked")
    public OriginalSPSCQueue( final int capacity ) {
        this.capacity   = findNextPositivePowerOfTwo(capacity);
        this.mask       = this.capacity - ONE;
        this.buffer     = (E[]) new Object[this.capacity];
    }


    public boolean add(final E e) {
        if (offer(e)) {
            return true;
        }

        throw new IllegalStateException("Queue is full");
    }

    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException("Null is not a valid element");
        }

        final long currentTail = tail.get();
        final long wrapPoint = currentTail - capacity;
        if (headCache.value <= wrapPoint) {
            headCache.value = head.get();
            if (headCache.value <= wrapPoint) {
                return false;
            }
        }

        buffer[(int) currentTail & mask] = e;
        tail.lazySet(currentTail + ONE);

        return true;
    }

    public E poll() {
        final long currentHead = head.get();
        if (currentHead >= tailCache.value) {
            tailCache.value = tail.get();
            if (currentHead >= tailCache.value) {
                return null;
            }
        }

        final int index = (int) currentHead & mask;
        final E e = buffer[index];
        buffer[index] = null;
        head.lazySet(currentHead + ONE);

        return e;
    }

    public E remove() {
        final E e = poll();
        if (null == e) {
            throw new NoSuchElementException("Queue is empty");
        }

        return e;
    }

    public E element() {
        final E e = peek();
        if (null == e) {
            throw new NoSuchElementException("Queue is empty");
        }

        return e;
    }

    public E peek() {
        return buffer[(int) head.get() & mask];
    }

    public int size() {
        return (int) (tail.get() - head.get());
    }

    public boolean isEmpty() {
        return tail.get() == head.get();
    }

    public boolean contains(final Object o) {
        if (null == o) {
            return false;
        }

        for (long i = head.get(), limit = tail.get(); i < limit; i++) {
            final E e = buffer[(int) i & mask];
            if (o.equals(e)) {
                return true;
            }
        }

        return false;
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T> T[] toArray(final T[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }

        return true;
    }

    public boolean addAll(final Collection<? extends E> c) {
        for (final E e : c) {
            add(e);
        }

        return true;
    }

    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        Object value;
        do {
            value = poll();
        } while (null != value);
    }

}