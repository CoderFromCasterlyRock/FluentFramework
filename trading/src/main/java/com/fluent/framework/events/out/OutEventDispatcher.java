package com.fluent.framework.events.out;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import org.jctools.queues.*;

import com.fluent.framework.core.*;
import com.fluent.framework.persistence.*;
import com.fluent.framework.util.FluentUtil;
import com.fluent.framework.collection.*;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;



public final class OutEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final int bucketCapacity;
    private final FluentSingleThreadExecutor executor;
    private final PersisterService<OutEvent> pService;
    
    private final static AbstractQueue<OutEvent> QUEUE;
    private final static List<OutListener> LISTENERS;
    
    private final static int DEFAULT_B_SIZE   	= SIXTY_FOUR;
    private final static int DEFAULT_Q_SIZE   	= nextPowerOfTwo(MILLION); 
    private final static String NAME        	= OutEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      	= LoggerFactory.getLogger( NAME );
    
    
    static{
    	QUEUE		= new MpscArrayQueue<>( DEFAULT_Q_SIZE );
    	LISTENERS 	= new CopyOnWriteArrayList<OutListener>( );
    }
    
    
    public OutEventDispatcher( PersisterService<OutEvent> pService ){
    	this( DEFAULT_B_SIZE, pService );
    }
    
    public OutEventDispatcher( int bucketCapacity, PersisterService<OutEvent> pService ){
  
    	this.bucketCapacity	= bucketCapacity;
    	this.pService		= pService;
    	this.executor		= new FluentSingleThreadExecutor( new FluentThreadFactory("in-Dispatcher")); 
    			
    }

   
    @Override
    public final String name( ){
    	return NAME;
    }
    
    
    public final int getQueueSize( ){
    	return QUEUE.size();
    }


    public final int getBucketCapacity( ){
    	return bucketCapacity;
    }

    
    private final void prime(  ){
    	
    	ArrayDeque<OutEvent> bucket 	= new ArrayDeque<>( bucketCapacity) ;
        	
    	for( int i =ZERO; i < DEFAULT_Q_SIZE; i++ ){
    		enqueue( FluentUtil.OUT_WARMUP_EVENT );
    		process( bucket );
    	
    		bucket.clear();
    	}

    	QUEUE.clear();
    	LISTENERS.clear();
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

        LOGGER.debug("Successfully started In-Dispatcher with Q Size [{}] and B Size[{}].", DEFAULT_Q_SIZE, bucketCapacity );
    }
    
    
    public final static boolean register( OutListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Outbound event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final static boolean deregister( OutListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Outbound event listener.", LISTENERS.size(), listener.name() );
        return removed;
    }
  
    

    public final static boolean enqueue( final OutEvent event ){
        boolean result = QUEUE.offer( event );
        if( !result ){
        	LOGGER.warn( "Failed to enqueue Size[{}], Event:[{}]", QUEUE.size(), event);
        }
        
        return result;
    }

    
    @Override
    public final void run( ){

    	final ArrayDeque<OutEvent> bucket 	= new ArrayDeque<>( bucketCapacity) ;
    	
        while( keepDispatching ){
           process( bucket );
        
           bucket.clear();
        }

    }

    
    protected final void process( final ArrayDeque<OutEvent> bucket ){

    	try{

        	int itemsPolled  = batchEvents( bucket );
        	if( itemsPolled == ZERO  ){
        		FluentBackoffStrategy.apply( HUNDRED );
        		return;
        	}

        	for( OutEvent event : bucket ){
        	
        		for( OutListener listener : LISTENERS ){
        			if( listener.isSupported(event.getType()) ){
        				listener.update(  event );
        			}
        		}

        		pService.persistEvent( event );
        	}
                        		
        }catch( Exception e ){
        	LOGGER.error("FAILED to dispatch Outbound events.", e );
        }
        
    }
    
    
    protected final int batchEvents( final ArrayDeque<OutEvent> bucket ){
    	
    	int eventsPolled = ZERO;
    	
    	while( QUEUE.peek() != null && eventsPolled < bucketCapacity ){
    		
    		OutEvent event = QUEUE.poll();
    		bucket.add( event );
    		   	
    		++ eventsPolled;
    	}
    	   	    	
    	return eventsPolled;
    
    }


    @Override
    public final void stop( ){
        keepDispatching = false;
        
        LISTENERS.clear();
        QUEUE.clear();
        
        pService.stop();
        executor.shutdown();
        
        LOGGER.info("Successfully stopped Outbound dispatcher." );
    }


}
