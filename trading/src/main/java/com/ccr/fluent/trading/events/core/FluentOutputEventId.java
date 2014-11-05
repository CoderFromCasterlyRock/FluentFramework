package com.ccr.fluent.trading.events.core;

import com.ccr.fluent.trading.collections.*;


public final class FluentOutputEventId{


	private final static FluentAtomicLong ID = new FluentAtomicLong();

    
    public final static void set( long newId ){
        ID.set( newId );
    }
    
    
    public final static long current( ){
        return ID.getCurrent( );
    }


    public final static long nextId( ){
        return ID.getNext();
    }

    
}
