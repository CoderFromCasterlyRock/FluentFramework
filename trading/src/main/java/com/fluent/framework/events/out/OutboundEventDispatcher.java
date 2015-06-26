package com.fluent.framework.events.out;

import org.slf4j.*;
import java.util.*;
import org.jctools.queues.*;
import java.util.concurrent.*;

import com.fluent.framework.core.*;
import com.fluent.framework.util.*;
import com.fluent.framework.events.in.*;
import com.fluent.framework.collection.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class OutboundEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final static AbstractQueue<OutboundEvent> QUEUE;
    private final static List<OutboundListener> LISTENERS;
    
    private final static int DEFAULT_SIZE   = SIXTY_FOUR * SIXTY_FOUR; 
    private final static String NAME        = OutboundEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );
    
    
    static{
    	QUEUE      	= new MpscArrayQueue<>( DEFAULT_SIZE );
    	LISTENERS	= new CopyOnWriteArrayList<OutboundListener>( );
    }
    
        
    public OutboundEventDispatcher( ){
    	this.executor   = Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }

   
    @Override
    public String name( ){
    	return NAME;
    }
    

    @Override
    public final void prime( ){
    	
    	int warmupSize				= SIXTY_FOUR * DEFAULT_SIZE;
    	OutboundEvent event 	= new OutboundWarmupEvent( );
    	
    	for( int i =ZERO; i <( SIXTY_FOUR * DEFAULT_SIZE); i++ ){
    		QUEUE.offer( event );
    		QUEUE.poll( );
    	}

    	QUEUE.clear();
    	LOGGER.info("Finished warming Outbound dispatch queue, fed [{}] events.", warmupSize );
   
    }
    
    
    @Override
    public void init( ){
      
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    	keepDispatching = true;
        executor.execute( this );

    }
    
    
    public final static boolean register( OutboundListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Outbound event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final static boolean deregister( InboundListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Outbound event listener.", LISTENERS.size(), listener.name() );
        return removed;
    }
  
    

    public final static boolean enqueue( final OutboundEvent event ){
        return QUEUE.offer( event );
    }

    
    protected final int getQueueSize( ){
    	return QUEUE.size();
    }

    

    @Override
    public final void run( ){

        while( keepDispatching ){
           
        	try{

        		OutboundEvent event  = QUEUE.poll( );
        		if( event == null  ){
        			FluentBackoffStrategy.apply( ONE );
        			continue;
        		}

        		for( OutboundListener listener : LISTENERS ){
        			if( listener.isSupported(event.getType()) ){
        				listener.update(  event );
        			}
        		}
      		
        	}catch( Exception e ){
        		LOGGER.error("FAILED to dispatch outbound events.");
        		LOGGER.error("Exception:", e);
        	}
        	
        }

    }


    @Override
    public void stop( ){
    	
    	keepDispatching = false;
        executor.shutdown();

        LOGGER.info("Successfully stopped {}.", NAME );
    }


}
