package com.fluent.etrading.framework.events.core;

import com.fluent.etrading.framework.collections.*;


public final class FluentOutputEventId{


	private final static FluentAtomicLongCounter ID = new FluentAtomicLongCounter();

    
    public final static void set( long newId ){
        ID.getAndSet( newId );
    }
    
    
    public final static long current( ){
        return ID.get( );
    }


    public final static long nextId( ){
        return ID.getAndIncrement();
    }

    
}
