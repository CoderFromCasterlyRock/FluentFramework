package com.ccr.fluent.trading.dispatcher.core;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;


public final class ParkingBackoffStrategy implements BackoffStrategy{

    private final long time;
    private final String description;


    public ParkingBackoffStrategy(  ){
        this( ONE, TimeUnit.NANOSECONDS );
    }

    public ParkingBackoffStrategy( long time, TimeUnit unit ){
        this.time          = TimeUnit.NANOSECONDS.convert( time, unit );
        this.description   = new StringBuilder("Park for ").append( time ).append( SPACE ).append( unit ).toString();
    }


    @Override
    public final long getBackoffTime(){
        return time;
    }


    @Override
    public final String description( ){
        return description;
    }

    @Override
    public void apply( ){
        LockSupport.parkNanos( time );
    }

}
