package com.fluent.framework.events.out;

import org.slf4j.*;

import uk.co.real_logic.agrona.concurrent.*;

import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.collection.*;
import com.fluent.framework.core.*;

import static com.fluent.framework.util.FluentToolkit.*;
import static com.fluent.framework.util.FluentUtil.*;



public final class FluentOutEventDispatcher implements FluentLifecycle, Runnable{

    private volatile boolean                                    keepDispatching;

    private final int                                           queueCapacity;
    private final FluentSingleThreadExecutor                    executor;

    private final List<FluentOutListener>                       eventListener;
    private final ManyToOneConcurrentArrayQueue<FluentOutEvent> eventQueue;

    private final static int                                    QUEUE_CAPACITY = nextPowerOfTwo( MILLION );
    private final static String                                 NAME           = FluentOutEventDispatcher.class.getSimpleName( );
    private final static Logger                                 LOGGER         = LoggerFactory.getLogger( NAME );


    public FluentOutEventDispatcher( FluentConfigManager cfgManager ){
        this( QUEUE_CAPACITY, cfgManager );
    }


    public FluentOutEventDispatcher( int queueCapacity, FluentConfigManager cfgManager ){

        this.queueCapacity = notNegative( queueCapacity, "Queue Capacity must be positive." );
        this.eventListener = new CopyOnWriteArrayList<FluentOutListener>( );
        this.eventQueue = new ManyToOneConcurrentArrayQueue<>( queueCapacity );
        this.executor = new FluentSingleThreadExecutor( new FluentThreadFactory( "OutDispatcher" ) );

    }

    @Override
    public final String name( ) {
        return NAME;
    }

    public final int getQueueSize( ) {
        return eventQueue.size( );
    }


    public final int getQueueCapacity( ) {
        return queueCapacity;
    }


    private final void prime( ) {

        for( int i = ZERO; i < queueCapacity; i++ ){
            enqueue( OUT_WARMUP_EVENT );
            process( );
        }

        eventQueue.clear( );
        eventListener.clear( );
        
    }


    @Override
    public final void start( ) {

        if( keepDispatching ){
            LOGGER.warn( "Attempted to start {} while it is already running.", NAME );
            return;
        }

        prime( );

        executor.start( );
        keepDispatching = true;
        executor.execute( this );

        LOGGER.info( "Started and primed Out-Dispatcher with Q Size [{}].{}", queueCapacity, NEWLINE );
    }


    public final boolean register( FluentOutListener listener ) {
        boolean added = eventListener.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Outbound event listener.", eventListener.size( ), listener.name( ) );

        return added;
    }


    public final boolean deregister( FluentOutListener listener ) {
        boolean removed = eventListener.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Outbound event listener.", eventListener.size( ), listener.name( ) );
        return removed;
    }



    public final boolean enqueue( final FluentOutEvent event ) {
        boolean result = eventQueue.offer( event );
        if( !result ){
            LOGGER.warn( "Failed to enqueue Size[{}], Event:[{}]", eventQueue.size( ), event );
        }

        return result;
    }


    @Override
    public final void run( ) {

        while( keepDispatching ){
            process( );
        }

    }


    protected final void process( ) {

        try{

            if( eventQueue.peek( ) == null ){
                FluentBackoffStrategy.apply( HUNDRED );
                return;
            }

            FluentOutEvent event = eventQueue.poll( );
            for( FluentOutListener listener : eventListener ){
                if( listener.isSupported( event.getType( ) ) ){
                    listener.outUpdate( event );
                }
            }

        }catch( Exception e ){
            LOGGER.error( "FAILED to dispatch Outbound events.", e );
        }

    }


    @Override
    public final void stop( ) {
        keepDispatching = false;

        eventListener.clear( );
        eventQueue.clear( );
        executor.shutdown( );

        LOGGER.info( "Successfully stopped Outbound dispatcher." );
    }


}
