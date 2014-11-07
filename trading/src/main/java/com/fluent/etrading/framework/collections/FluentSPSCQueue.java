package com.fluent.etrading.framework.collections;

import java.util.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class FluentSPSCQueue<E> implements FluentQueue<E>{
	
	private final OriginalSPSCQueue<E> queue;

    public FluentSPSCQueue( ){
		this( SIXTY_FOUR );
	}

	public FluentSPSCQueue( int capacity ){
		this.queue = new OriginalSPSCQueue<E>( capacity );
	}

	
	@Override
	public int size( ){
		return queue.size();
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
	public E take( ) throws InterruptedException{
		return queue.poll();
	}


    @Override
    public int drainTo( final Collection<? super E> collection ){
        throw new UnsupportedOperationException( );
    }


    @Override
    public void clear( ){
        queue.clear();
    }
	
}
