package com.fluent.etrading.framework.events.core;

import com.fluent.etrading.framework.collections.*;


public final class FluentInputEventId{


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
