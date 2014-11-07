package com.fluent.etrading.framework.dispatcher.out;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import com.fluent.etrading.framework.collections.*;
import com.fluent.etrading.framework.core.*;
import com.fluent.etrading.framework.dispatcher.core.*;
import com.fluent.etrading.framework.events.core.*;
import com.fluent.etrading.framework.events.out.order.*;
import com.fluent.etrading.framework.events.out.response.*;
import com.fluent.etrading.framework.persistence.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class BlockingOutputEventDispatcher extends OutputEventDispatcher implements Runnable{
	
    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final FluentQueue<FluentOutputEvent> queue;

    private final static String NAME        = BlockingOutputEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    public BlockingOutputEventDispatcher( BackoffStrategy backoff ){
    	this( backoff, null );
    }
    
    public BlockingOutputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentOutputEvent> persister ){
    	this( backoff, persister, new HashSet<FluentOutputEventType>( Arrays.asList(FluentOutputEventType.EVENT_TO_TRADER) ) );
    }
    
    public BlockingOutputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentOutputEvent> persister, Set<FluentOutputEventType> recoverables ){
        this( backoff, persister, recoverables, new FluentBlockingQueue<FluentOutputEvent>( SIXTY_FOUR * SIXTY_FOUR ) );
    }
    
    
    public BlockingOutputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentOutputEvent> persister, Set<FluentOutputEventType> recoverables, FluentBlockingQueue<FluentOutputEvent> queue ){
        super( backoff, persister, recoverables );

        this.queue      = queue;
        this.executor   = Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }


    @Override
    public final void startDispatch( ){
        keepDispatching = true;
        executor.execute( this );

        LOGGER.info("Started [{}], using BlockingQueue with [{}] as backoff strategy.", NAME, getBackoff().description() );
    }
    

    @Override
    public final boolean addOrderEvent( final OrderEvent event ){
        return queue.offer( event );
    }


    public final boolean addResponseEvent( final long eventId, final FluentEvent event, final String outputMessage ){
        return addResponseEvent( new ResponseEvent(eventId, event, outputMessage) );
    }


    @Override
    public final boolean addResponseEvent( final ResponseEvent event ){
        return queue.offer( event );
    }


    @Override
    public final void run( ){

    	performRecovery( );
    	
        while( keepDispatching ){
            dispatch( );
        }

    }


    protected final int dispatch(  ){

        int dispatchedCount         = ZERO;

        try{

            FluentOutputEvent event  = queue.poll( );
            boolean nothingPolled   = (event == null);
            if( nothingPolled ){
                getBackoff().apply();
                return NEGATIVE_ONE;
            }

            
            for( FluentOutputEventListener listener : getListeners() ){
                if( listener.isSupported(event.getType()) ){
                    listener.update( event );
                    ++dispatchedCount;
                }
            }

            
            if( dispatchedCount == ZERO ){
                LOGGER.warn( "DEAD EVENT! Valid output events arrived but no listeners are registered!" );
                LOGGER.warn( "[{}]", event.toJSON() );
            }
            
            
            getPersister().persist( event );

       
        }catch( Exception e ){
            LOGGER.error("FAILED to dispatch output events.");
            LOGGER.error("Exception:", e);

        }

        return dispatchedCount;

    }


    @Override
    public final void stopDispatch( ){
        keepDispatching = false;
        executor.shutdown();

        LOGGER.info("Successfully stopped {}.", NAME );
    }


}
