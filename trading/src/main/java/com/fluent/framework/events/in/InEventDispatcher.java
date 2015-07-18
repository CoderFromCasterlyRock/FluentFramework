package com.fluent.framework.events.in;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import org.jctools.queues.*;

import com.fluent.framework.core.*;
import com.fluent.framework.market.MarketDataEvent;
import com.fluent.framework.persistence.PersisterService;
import com.fluent.framework.util.FluentUtil;
import com.fluent.framework.collection.*;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;


public final class InEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final int bucketCapacity;
    private final FluentSingleThreadExecutor executor;
    private final PersisterService<InEvent> pService;
    
    private final AbstractQueue<InEvent> QUEUE;
    private final List<InListener> LISTENERS;
    
    private final static int DEFAULT_B_SIZE   	= SIXTY_FOUR; 
    private final static int DEFAULT_Q_SIZE   	= nextPowerOfTwo(MILLION); 
    private final static String NAME        	= InEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      	= LoggerFactory.getLogger( NAME );
    
    
    public InEventDispatcher( PersisterService<InEvent> pService ){
    	this( DEFAULT_B_SIZE, new MpscArrayQueue<InEvent>(DEFAULT_Q_SIZE), pService );
    }
    
    public InEventDispatcher( int bucketSize, int queueSize, PersisterService<InEvent> pService ){
    	this( bucketSize, new MpscArrayQueue<InEvent>(queueSize), pService );
    }
    
    
    public InEventDispatcher( int bucketCapacity, AbstractQueue<InEvent> QUEUE, PersisterService<InEvent> pService ){
  
    	this.bucketCapacity	= bucketCapacity;
    	this.QUEUE			= QUEUE;
    	this.pService		= pService;
    	this.LISTENERS 		= new CopyOnWriteArrayList<InListener>( );
    	this.executor		= new FluentSingleThreadExecutor( new FluentThreadFactory("in-Dispatcher")); 
    			
    }

   
    @Override
    public final String name( ){
    	return NAME;
    }
    
    
    protected final int getQueueSize( ){
    	return QUEUE.size();
    }


    public final int getBucketCapacity( ){
    	return bucketCapacity;
    }
    
    private final void prime(  ){
    	
    	ArrayDeque<InEvent> bucket 	= new ArrayDeque<>( bucketCapacity) ;
    	Map<String, InEvent> map 	= new HashMap<String, InEvent>(bucketCapacity);
    	
    	for( int i =ZERO; i < DEFAULT_Q_SIZE; i++ ){
    		enqueue( FluentUtil.IN_WARMUP_EVENT );
    		process( bucket, map );
    	
    		bucket.clear();
    		map.clear();
    	}

    	QUEUE.clear();
    	LISTENERS.clear();
    	    	
    }
    
    
    @Override
    public final void start( ){
      
    	LOGGER.info("Attempted to start Inbound dispatcher with Q Size [{}] and B Size[{}].", DEFAULT_Q_SIZE, bucketCapacity );
    	
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    	prime();
    	
    	pService.start();
    	executor.start();
    	keepDispatching = true;
        executor.execute( this );

        LOGGER.info("Successfully started In-Dispatcher." );
    }
    
    
    public final boolean register( InListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Inbound event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final boolean deregister( InListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Inbound event listener.", LISTENERS.size(), listener.name() );
        return removed;
    }
  
    

    public final boolean enqueue( final InEvent event ){
        boolean result = QUEUE.offer( event );
        if( !result ){
        	LOGGER.warn( "Failed to enqueue Size[{}], Event:[{}]", QUEUE.size(), event);
        }
        
        return result;
    }



    @Override
    public final void run( ){

    	final ArrayDeque<InEvent> bucket 	= new ArrayDeque<>( bucketCapacity) ;
    	final Map<String, InEvent> map 	= new HashMap<String, InEvent>(bucketCapacity);
    	
        while( keepDispatching ){
           process( bucket, map );
        
           bucket.clear();
           map.clear();
        }

    }

    
    protected final void process( ArrayDeque<InEvent> bucket, Map<String, InEvent> map ){

    	try{

        	int itemsPolled  = batchEvents(bucket, map);
        	if( itemsPolled == ZERO  ){
        		FluentBackoffStrategy.apply( HUNDRED );
        		return;
        	}

        	for( InEvent event : bucket ){
        	
        		for( InListener listener : LISTENERS ){
        			if( listener.isSupported(event.getType()) ){
        				listener.update(  event );
        			}
        		}

        		pService.persistEvent( event );
        	}
                        		
        }catch( Exception e ){
        	LOGGER.error("FAILED to dispatch Inbound events.", e );
        }
        
    }
    
    
    protected final int batchEvents( ArrayDeque<InEvent> bucket, Map<String, InEvent> map ){
    	
    	int eventsPolled = ZERO;
    	
    	while( QUEUE.peek() != null && eventsPolled < bucketCapacity ){
    		
    		InEvent event = QUEUE.poll();
    		
    		if( InType.MARKET_DATA == event.getType() ){
    			MarketDataEvent mdEvent = (MarketDataEvent) event;
    			map.put( mdEvent.getSymbol(), event );
    		}else{
    			bucket.add( event );
    		}
    	
    		++ eventsPolled;
    	}
    	
    	for( InEvent compactedEvent : map.values() ){
    		bucket.addFirst( compactedEvent );
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
        
        LOGGER.info("Successfully stopped Inbound dispatcher." );
    }


}
