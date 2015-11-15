package com.fluent.framework.events.out;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import uk.co.real_logic.agrona.concurrent.*;

import com.fluent.framework.core.*;
import com.fluent.framework.collection.*;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;



public final class OutEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final int queueCapacity;
    private final FluentSingleThreadExecutor executor;
        
    private final List<OutListener> eventListener;
    private final ManyToOneConcurrentArrayQueue<OutEvent> eventQueue;
    
    private final static int QUEUE_CAPACITY   	= nextPowerOfTwo(MILLION); 
    private final static String NAME        	= OutEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      	= LoggerFactory.getLogger( NAME );
    
        
    public OutEventDispatcher( ){
    	this( QUEUE_CAPACITY );
    }
    
  
    public OutEventDispatcher( int queueCapacity ){
    
    	this.queueCapacity	= notNegative(queueCapacity, "Queue Capacity must be positive.");
    	this.eventListener 	= new CopyOnWriteArrayList<OutListener>( );
    	this.eventQueue		= new ManyToOneConcurrentArrayQueue<>( queueCapacity );
    	this.executor		= new FluentSingleThreadExecutor( new FluentThreadFactory("in-Dispatcher")); 
    			
    }

    
    @Override
	public final FluentServiceType getServiceType( ){
		return FluentServiceType.OUT_DISPATCHER;
	}
    
    
    public final int getQueueSize( ){
    	return eventQueue.size();
    }

    public final int getQueueCapacity( ){
    	return queueCapacity;
    }

    
    private final void prime(  ){
    	
    	for( int i =ZERO; i < queueCapacity; i++ ){
    		enqueue( OUT_WARMUP_EVENT );
    		process( );
    	
    	}

    	eventQueue.clear();
    	eventListener.clear();
    	LOGGER.info("Finished warming Outbound dispatch queue." );
    	
    }
    
    
    @Override
    public final void start( ){
      
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    	prime();
    	
    	executor.start();
    	keepDispatching = true;
        executor.execute( this );

        LOGGER.debug("Successfully started In-Dispatcher with Q capacity [{}].", queueCapacity );
    }
    
    
    public final boolean register( OutListener listener ){
        boolean added = eventListener.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Outbound event listener.", eventListener.size(), listener.name() );

        return added;
    }


    public final boolean deregister( OutListener listener ){
        boolean removed = eventListener.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Outbound event listener.", eventListener.size(), listener.name() );
        return removed;
    }
  
    

    public final boolean enqueue( final OutEvent event ){
        boolean result = eventQueue.offer( event );
        if( !result ){
        	LOGGER.warn( "Failed to enqueue Size[{}], Event:[{}]", eventQueue.size(), event);
        }
        
        return result;
    }

    
    @Override
    public final void run( ){

    	while( keepDispatching ){
           process( );
        }

    }

    
    protected final void process( ){

    	try{

        	if( eventQueue.peek() == null ){
        		FluentBackoffStrategy.apply( HUNDRED );
        		return;
        	}

        	OutEvent event = eventQueue.poll();
        	for( OutListener listener : eventListener ){
        		if( listener.isSupported(event.getType()) ){
        			listener.outUpdate(  event );
        		}
        	}
        	                        		
        }catch( Exception e ){
        	LOGGER.error("FAILED to dispatch Outbound events.", e );
        }
        
    }
    

    @Override
    public final void stop( ){
        keepDispatching = false;
        
        eventListener.clear();
        eventQueue.clear();
        executor.shutdown();
        
        LOGGER.info("Successfully stopped Outbound dispatcher." );
    }


}
