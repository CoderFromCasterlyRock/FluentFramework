package com.ccr.fluent.trading.collections;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;


public final class FluentEventIdGenerator{

    private final FluentAtomicLong id;

    public FluentEventIdGenerator( ){
        this( ZERO );
    }

    public FluentEventIdGenerator( long startingId ){
        this.id = new FluentAtomicLong( startingId );
    }


    public final long getCurrent( ){
        return id.getCurrent( );
    }


    public final long getNext( ){
        return id.getNext();
    }

}
