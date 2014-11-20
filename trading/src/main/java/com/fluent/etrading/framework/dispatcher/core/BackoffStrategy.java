package com.fluent.etrading.framework.dispatcher.core;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class BackoffStrategy{

    private final long time;
    private final String description;


    public BackoffStrategy(  ){
        this( ONE, TimeUnit.NANOSECONDS );
    }

    public BackoffStrategy( long time, TimeUnit unit ){
        this.time          = TimeUnit.NANOSECONDS.convert( time, unit );
        this.description   = new StringBuilder("Park for ").append( time ).append( SPACE ).append( unit ).toString();
    }


    public final long getBackoffTime(){
        return time;
    }


    public final String description( ){
        return description;
    }

    
    public void apply( ) throws InterruptedException{
    	if( IS_WINDOWS ){
    		Thread.sleep( time);
    	}else{
    		LockSupport.parkNanos( time );
    	}
    }

}
