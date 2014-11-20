package com.fluent.etrading.framework.collections;

import java.util.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class FluentSPSCQueue<E> implements Queue<E>{
	
	private final OriginalSPSCQueue<E> queue;

    public FluentSPSCQueue( ){
		this( SIXTY_FOUR );
	}

	public FluentSPSCQueue( int capacity ){
		this.queue = new OriginalSPSCQueue<E>( capacity );
	}

	
	@Override
	public final int size( ){
		return queue.size();
	}

	
	@Override
	public final boolean isEmpty( ){
		return queue.isEmpty();
	}
	

	@Override
	public final boolean add( E e ){
		return queue.add( e );
	}

	
	@Override
	public final boolean offer( E e ){
		return queue.offer(e);
	}
	

	@Override
	public final E poll( ){
		return queue.poll();
	}

	
	@Override
	public final E peek( ){
		return queue.peek();
	}
	

	public final E take( ) throws InterruptedException{
		return queue.poll();
	}

		
    @Override
	public final boolean contains(Object o) {
		return queue.contains(o);
	}

	
	@Override
	public final Iterator<E> iterator() {
		return queue.iterator();
	}

	@Override
	public final Object[] toArray() {
		return queue.toArray();
	}

	@Override
	public final <T> T[] toArray(T[] a) {
		return queue.toArray(a);
	}

	@Override
	public final boolean remove(Object o) {
		return queue.remove(o);
	}

	@Override
	public final boolean containsAll(Collection<?> c) {
		return queue.containsAll(c);
	}

	@Override
	public final boolean addAll(Collection<? extends E> c) {
		return queue.addAll(c);
	}

	@Override
	public final boolean removeAll(Collection<?> c) {
		return queue.removeAll(c);
	}

	@Override
	public final boolean retainAll(Collection<?> c) {
		return queue.retainAll(c);
	}

	@Override
	public final E remove() {
		return queue.remove();
	}

	@Override
	public final E element() {
		return queue.element();
	}
	
	@Override
    public final void clear( ){
        queue.clear();
    }
	
}
