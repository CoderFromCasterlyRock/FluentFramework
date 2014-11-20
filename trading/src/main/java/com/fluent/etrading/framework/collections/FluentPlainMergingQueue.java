package com.fluent.etrading.framework.collections;

import java.util.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;

/**
 * A merging queue for values with keys. Null key/values are not welcome.
 * Backed by ArrayDeque and HashMap.
 *
 * @param <KEY>
 * @param <VAL>
 */

public final class FluentPlainMergingQueue<KEY, VAL>{
	
	private final int capacity; 
    private final ArrayDeque<KEY> keyQueue;
    private final Map<KEY, VAL> lastValMap;

    
    public FluentPlainMergingQueue( int givenCapacity ){
    	this.capacity	= findNextPositivePowerOfTwo( givenCapacity );
        this.keyQueue   = new ArrayDeque<KEY>( capacity );
        this.lastValMap = new HashMap<KEY, VAL>( capacity );
    }

    
    
    public final int capacity() {
        return capacity;
    }
    
    
    
    public final boolean isEmpty( ){
        return keyQueue.isEmpty();
    }
    

    
    public final boolean offer( KEY key, VAL val ){
        VAL lastVal = lastValMap.put(key, val);
        if (lastVal == null) {
            keyQueue.add(key);
            return true;
        }
        
        return false;
    }


    
    public final VAL poll( ){
        // Optimization: put null to avoid recycling the entry in the map:
        // + no garbage overhead, keep the entries and as the use case is for a limited set
        //   of keys this means you'll hit max capacity required in a short while and be done.
        // - keys are kept for the lifetime of the map, if you were hoping for them to get
        //   picked up by the GC then you'll need to go another way
        return lastValMap.put(keyQueue.pollFirst(),null);
    }
    
    
    
    public final boolean isFull() {
        return size() == capacity;
    }
    
    
    public final int size( ){
    	return keyQueue.size();
    }
       	
    
    
    public final void clear( ){
        lastValMap.clear();
        keyQueue.clear();
    }
    
    
}