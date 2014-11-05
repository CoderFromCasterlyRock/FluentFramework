package com.ccr.fluent.trading.collections;

import java.util.*;

public interface FluentQueue<E>{

    public int size();
    public void clear();
    public boolean isEmpty();
    
	public boolean add( final E e );
	public boolean offer( final E e );
	
	public E poll();
	public E peek();
	public E take() throws InterruptedException;
    public int drainTo( Collection<? super E> collection );

}
