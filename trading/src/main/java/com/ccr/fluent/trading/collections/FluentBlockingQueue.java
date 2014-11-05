package com.ccr.fluent.trading.collections;

import java.util.Collection;
import java.util.concurrent.*;


public final class FluentBlockingQueue<E> implements FluentQueue<E>{
	
	private final BlockingQueue<E> queue;
	
	
	public FluentBlockingQueue( int capacity ){
		this( new ArrayBlockingQueue<E>(capacity) );
	}
	
	
	public FluentBlockingQueue( BlockingQueue<E> queue ){
		this.queue = queue;
	}
	

	@Override
	public int size( ){
		return queue.size();
	}

    @Override
    public void clear( ){
        queue.clear();
    }


	@Override
	public boolean isEmpty( ){
		return queue.isEmpty();
	}
	

	@Override
	public boolean add( E e ){
		return queue.add( e );
	}

	
	@Override
	public boolean offer( E e ){
		return queue.offer(e);
	}
	

	@Override
	public E poll( ){
		return queue.poll();
	}

	
	@Override
	public E peek( ){
		return queue.peek();
	}


    @Override
    public int drainTo( Collection<? super E> collection ){
        return queue.drainTo( collection );
    }


	@Override
	public E take( ) throws InterruptedException{
		return queue.take();
	}
	

}
