package com.fluent.etrading.framework.collections;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class FluentEventIdGenerator{

    private final FluentAtomicLongCounter id;

    public FluentEventIdGenerator( ){
        this( ZERO );
    }

    public FluentEventIdGenerator( long startingId ){
        this.id = new FluentAtomicLongCounter( startingId );
    }


    public final long getCurrent( ){
        return id.get();
    }


    public final long getNext( ){
        return id.getAndIncrement();
    }

}
