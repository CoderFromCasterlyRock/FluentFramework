package com.fluent.framework.dispatcher.core;

import java.util.concurrent.*;

import static com.fluent.framework.utility.ContainerUtil.*;

public final class SleepingBackoffStrategy implements BackoffStrategy{

    private final long time;
    private final String description;

    public SleepingBackoffStrategy( long time, TimeUnit unit ){
        this.time           = TimeUnit.NANOSECONDS.convert( time, unit );
        this.description    = new StringBuilder("Sleep for ").append( time ).append( SPACE ).append( unit ).toString();
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
    public final void apply( ) throws InterruptedException{
        Thread.sleep( time );
    }

}
