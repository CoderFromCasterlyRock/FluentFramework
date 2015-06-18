package com.fluent.framework.events.in;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import org.jctools.queues.MpscArrayQueue;

import com.fluent.framework.collection.FluentBackoffStrategy;
import com.fluent.framework.core.*;
import com.fluent.framework.events.core.FluentInboundEvent;
import com.fluent.framework.events.core.FluentInboundListener;
import com.fluent.framework.events.persister.FluentPersister;

import static com.fluent.framework.util.FluentUtil.*;


public final class InboundEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final FluentPersister persister;
    private final AbstractQueue<FluentInboundEvent> queue;
    private final static List<FluentInboundListener> LISTENERS;
    
    private final static String NAME        = InboundEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );
    
    static{
    	LISTENERS = new CopyOnWriteArrayList<FluentInboundListener>( );
    }
        
    
    public InboundEventDispatcher( int capacity, FluentPersister persister  ){
    	this.persister		= persister;
        this.queue      	= new MpscArrayQueue<>(capacity);
        this.executor   	= Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
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
    	
    	persister.init();
    	keepDispatching = true;
        executor.execute( this );

    }
    
    
    public final static boolean register( FluentInboundListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Inbound event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final static boolean deregister( FluentInboundListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Inbound event listener.", LISTENERS.size(), listener.name() );
        return removed;
    }
  
    

    public final boolean enqueue( final FluentInboundEvent event ){
        return queue.offer( event );
    }

    
    protected final int getQueueSize( ){
    	return queue.size();
    }


    @Override
    public final void run( ){

        while( keepDispatching ){
           
        	try{

        		FluentInboundEvent event  = queue.poll( );
        		if( event == null  ){
        			FluentBackoffStrategy.apply( ONE );
        			continue;
        		}

        		for( FluentInboundListener listener : LISTENERS ){
        			if( listener.isSupported(event.getType()) ){
        				listener.update(  event );
        			}
        		}
                
        		persister.persist( event );
        		
        	}catch( Exception e ){
        		LOGGER.error("FAILED to dispatch Inbound events.");
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
