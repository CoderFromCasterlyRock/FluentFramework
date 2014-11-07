package com.fluent.etrading.framework.core;

import org.slf4j.*;

import java.lang.Thread.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class FluentThreadFactory implements ThreadFactory{

    private final String namePrefix;

    private final static String THREAD      = " T-";
    private final static AtomicLong count   = new AtomicLong( ZERO );
    private final static String NAME        = FluentThreadFactory.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger(  NAME );


    static{
    	Thread.setDefaultUncaughtExceptionHandler(  new SmartThreadExceptionHandler() );
    }

    public FluentThreadFactory( String namePrefix ){
        this.namePrefix = new StringBuilder( ).append(count.incrementAndGet()).append( THREAD ).append(namePrefix).toString();
    }


    @Override
    public final Thread newThread( final Runnable runnable ){
        return new Thread( runnable, namePrefix );
    }


    private final static class SmartThreadExceptionHandler implements UncaughtExceptionHandler{

        @Override
        public void uncaughtException( final Thread t, final Throwable e ){
            LOGGER.warn( "CAUGHT unhandled thread exception!");
            LOGGER.warn( "Exception: ", e );
        }

    }


}