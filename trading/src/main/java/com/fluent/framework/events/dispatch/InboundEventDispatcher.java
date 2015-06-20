package com.fluent.framework.events.dispatch;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;
import org.jctools.queues.*;

import com.fluent.framework.core.*;
import com.fluent.framework.collection.*;
import com.fluent.framework.events.core.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class InboundEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    
    private final static AbstractQueue<FluentInboundEvent> eventQueue;
    private final static List<FluentInboundListener> LISTENERS;
    
    private final static int DEFAULT_SIZE   = SIXTY_FOUR * SIXTY_FOUR; 
    private final static String NAME        = InboundEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );
    
    
    static{
    	eventQueue	= new MpscArrayQueue<>( DEFAULT_SIZE );
    	LISTENERS 	= new CopyOnWriteArrayList<FluentInboundListener>( );
    }
    
    
    public InboundEventDispatcher( ){
    	this.executor	= Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }

   
    @Override
    public final String name( ){
    	return NAME;
    }
    
    
    protected final void warmUp( FluentInboundEvent event ){
    	
    	for( int i =ZERO; i <( TWO * DEFAULT_SIZE); i++ ){
    		eventQueue.offer( event );
    		eventQueue.poll( );
    	}

    	eventQueue.clear();
    	LOGGER.info("[{}] Finished warming, queue size [{}].", NAME, eventQueue.size() );
    	
    }
    
    
    @Override
    public final void init( ){
      
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    //	warmUp( );
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
  
    

    public final static boolean enqueue( final FluentInboundEvent event ){
        return eventQueue.offer( event );
    }

    
    protected final int getQueueSize( ){
    	return eventQueue.size();
    }


    @Override
    public final void run( ){

        while( keepDispatching ){
           
        	try{

        		FluentInboundEvent event  = eventQueue.poll( );
        		if( event == null  ){
        			FluentBackoffStrategy.apply( ONE );
        			continue;
        		}

        		for( FluentInboundListener listener : LISTENERS ){
        			if( listener.isSupported(event.getType()) ){
        				listener.update(  event );
        			}
        		}
                        		
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
