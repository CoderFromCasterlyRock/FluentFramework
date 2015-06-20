package com.fluent.framework.core;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class FluentThreadFactory implements ThreadFactory{

    private final String appName;
    private final String threadName;
    
    private final static AtomicLong COUNTER   = new AtomicLong( ZERO );
    

    public FluentThreadFactory( String threadName ){
        this( EMPTY, threadName );
    }


    public FluentThreadFactory( String appName, String threadName ){
    	this.appName 	= appName;
    	this.threadName = threadName;
    }

    
    @Override
    public final Thread newThread( final Runnable runnable ){
    	
    	long threadCount 	= COUNTER.incrementAndGet();
    	String fullName		= appName + DASH + threadCount + DASH + threadName;
    	
        return new Thread( runnable, fullName );
    }

}