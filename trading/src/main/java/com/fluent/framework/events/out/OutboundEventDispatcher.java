package com.fluent.framework.events.out;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import org.jctools.queues.MpscArrayQueue;

import com.fluent.framework.collection.FluentBackoffStrategy;
import com.fluent.framework.core.*;
import com.fluent.framework.events.core.FluentInboundListener;
import com.fluent.framework.events.core.FluentOutboundEvent;
import com.fluent.framework.events.core.FluentOutboundListener;
import com.fluent.framework.events.persister.FluentEventFstPersister;

import static com.fluent.framework.util.FluentUtil.*;


public final class OutboundEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final FluentEventFstPersister persister;
    private final AbstractQueue<FluentOutboundEvent> queue;
    private final static List<FluentOutboundListener> LISTENERS;
    
    private final static String NAME        = OutboundEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );
    
    static{
    	LISTENERS = new CopyOnWriteArrayList<FluentOutboundListener>( );
    }
    
    
    public OutboundEventDispatcher( int capacity, FluentEventFstPersister persister  ){
        
    	this.persister	= persister;
        this.queue      = new MpscArrayQueue<>(capacity);
        this.executor   = Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }

   
    @Override
    public final String name( ){
    	return NAME;
    }
    
    
    @Override
    public final void init( ){
      
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    	keepDispatching = true;
        executor.execute( this );

    }
    
    
    public final static boolean register( FluentOutboundListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Outbound event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final static boolean deregister( FluentInboundListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Outbound event listener.", LISTENERS.size(), listener.name() );
        return removed;
    }
  
    

    public final boolean enqueue( final FluentOutboundEvent event ){
        return queue.offer( event );
    }

    
    protected final int getQueueSize( ){
    	return queue.size();
    }


    @Override
    public final void run( ){

        while( keepDispatching ){
           
        	try{

        		FluentOutboundEvent event  = queue.poll( );
        		if( event == null  ){
        			FluentBackoffStrategy.apply( ONE );
        			continue;
        		}

        		for( FluentOutboundListener listener : LISTENERS ){
        			if( listener.isSupported(event.getType()) ){
        				listener.update(  event );
        			}
        		}
                
        		persister.persist( event );
        		
        	}catch( Exception e ){
        		LOGGER.error("FAILED to dispatch outbound events.");
        		LOGGER.error("Exception:", e);
        	}
        	
        }

    }


    @Override
    public final void stop( ){
        keepDispatching = false;
        executor.shutdown();

        LOGGER.info("Successfully stopped {}.", NAME );
    }


}
