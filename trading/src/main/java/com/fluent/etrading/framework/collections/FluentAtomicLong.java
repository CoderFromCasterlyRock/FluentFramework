package com.fluent.etrading.framework.collections;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


/**
 * Improves upon plain AtomicLong by padding it so the storage of it falls in one cache-line (prevents false sharing).
 * Also, applies backoff when the CAS fails, helps when getNext() is highly contended between multiple threads.
 */

public final class FluentAtomicLong{

    private final AtomicLong id;

    public FluentAtomicLong( ){
        this( ZERO );
    }

    public FluentAtomicLong( long startingId ){
        this.id = new PaddedAtomicLong( startingId );
    }


    public final long getCurrent( ){
        return id.get( );
    }


    public final long getNext( ){
        for( ;; ){
            long current    = getCurrent();
            long next       = current + ONE;
            if( compareAndSet(current, next) )
                return next;
        }

    }

    
    public final long set( final long newValue ){
        
    	while( true ) {
            long current = id.get();
            if (compareAndSet(current, newValue))
                return current;
        }
    }
    
    

    protected final boolean compareAndSet( final long current, final long next ){
        if( id.compareAndSet(current, next) ){
            return true;
        }else{
            LockSupport.parkNanos( ONE );
            return false;
        }

    }

}
