package com.fluent.framework.events.in;

import org.slf4j.*;
import java.util.*;
import java.util.concurrent.*;

import com.fluent.framework.core.*;
import com.fluent.framework.collection.*;
import uk.co.real_logic.agrona.concurrent.*;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;


public final class InEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final int bucketCapacity;
    private final int queueCapacity;
    private final FluentSingleThreadExecutor executor;
        
    private final List<InListener> eventListeners;
    private final ManyToOneConcurrentArrayQueue<InEvent> eventQueue;
    
    private final static int BUCKET_CAPACITY   	= SIXTY_FOUR; 
    private final static int QUEUE_CAPACITY   	= nextPowerOfTwo(MILLION); 
    private final static String NAME        	= InEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      	= LoggerFactory.getLogger( NAME );
    
    
    //TODO: Use a better backoff mechanism
    
    public InEventDispatcher( ){
    	this( BUCKET_CAPACITY, QUEUE_CAPACITY );
    }
        
    
    public InEventDispatcher( int bucketCapacity, int queueCapacity ){
  
    	this.bucketCapacity	= notNegative(bucketCapacity, "Bucket Capacity must be positive.");
    	this.queueCapacity	= notNegative(queueCapacity, "Queue Capacity must be positive.");
    	
    	this.eventListeners = new CopyOnWriteArrayList<InListener>( );
    	this.eventQueue		= new ManyToOneConcurrentArrayQueue<InEvent>(queueCapacity);
    	this.executor		= new FluentSingleThreadExecutor( new FluentThreadFactory("in-Dispatcher")); 
    			
    }

   
    @Override
    public final String name( ){
    	return NAME;
    }
   
    
    public final int getBucketCapacity( ){
    	return bucketCapacity;
    }
       
    
    public final int getQueueCapacity( ){
    	return queueCapacity;
    }
    
    
    protected final int getQueueSize( ){
    	return eventQueue.size();
    }
    
    
    private final void prime(  ){
    	
    	ArrayDeque<InEvent> bucket 	= new ArrayDeque<>( bucketCapacity) ;
    	    	
    	for( int i =ZERO; i < queueCapacity; i++ ){
    		enqueue( IN_WARMUP_EVENT );
    		process( bucket );
    	
    		bucket.clear();
    	}

    	eventQueue.clear();
    	eventListeners.clear();
    	    	
    }
    
    
    @Override
    public final void start( ){
      
    	LOGGER.info("Attempted to start Inbound dispatcher with Q Size [{}] and B Size[{}].", queueCapacity, bucketCapacity );
    	
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    	prime();
    	
    	executor.start();
    	keepDispatching = true;
        executor.execute( this );

        LOGGER.info("Successfully started In-Dispatcher." );
    }
    
    
    public final boolean register( InListener listener ){
        boolean added = eventListeners.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Inbound event listener.", eventListeners.size(), listener.name() );

        return added;
    }


    public final boolean deregister( InListener listener ){
        boolean removed = eventListeners.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Inbound event listener.", eventListeners.size(), listener.name() );
        return removed;
    }
  
    

    public final boolean enqueue( final InEvent event ){
        boolean result = eventQueue.offer( event );
        if( !result ){
        	LOGGER.warn( "Failed to enqueue Size[{}], Event:[{}]", eventQueue.size(), event);
        }
        
        return result;
    }



    @Override
    public final void run( ){

    	final ArrayDeque<InEvent> bucket 	= new ArrayDeque<>( bucketCapacity) ;
    	    	
        while( keepDispatching ){
           process( bucket );
        
           bucket.clear();
        }

    }

    
    protected final void process( ArrayDeque<InEvent> bucket ){

    	try{

    		int itemsPolled = eventQueue.drainTo(bucket, bucketCapacity);
        	if( itemsPolled == ZERO  ){
        		FluentBackoffStrategy.apply( HUNDRED );
        		return;
        	}

        	for( InEvent event : bucket ){
        	
        		for( InListener listener : eventListeners ){
        			if( listener.isSupported(event.getType()) ){
        				listener.inUpdate(  event );
        			}
        		}

        	}
                        		
        }catch( Exception e ){
        	LOGGER.error("FAILED to dispatch Inbound events.", e );
        }
        
    }
        	   

    @Override
    public final void stop( ){
        keepDispatching = false;
        
        eventListeners.clear();
        eventQueue.clear();
        
        executor.shutdown();
        
        LOGGER.info("Successfully stopped Inbound dispatcher." );
    }


}
