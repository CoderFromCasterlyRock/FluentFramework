package com.fluent.framework.events.in;

import org.slf4j.*;
import java.util.*;
import java.util.concurrent.*;
import org.jctools.queues.*;

import com.fluent.framework.core.*;
import com.fluent.framework.util.*;
import com.fluent.framework.collection.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class InboundEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    
    private final static AbstractQueue<FluentInboundEvent> QUEUE;
    private final static List<FluentInboundListener> LISTENERS;
    
    private final static int DEFAULT_SIZE   = SIXTY_FOUR * SIXTY_FOUR; 
    private final static String NAME        = InboundEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );
    
    
    static{
    	QUEUE		= new MpscArrayQueue<>( DEFAULT_SIZE );
    	LISTENERS 	= new CopyOnWriteArrayList<FluentInboundListener>( );
    }
    
    
    public InboundEventDispatcher( ){
    	this.executor	= Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }

   
    @Override
    public final String name( ){
    	return NAME;
    }
    
    
    @Override
    public final void prime(  ){
    	
    	int warmupSize				= SIXTY_FOUR * DEFAULT_SIZE;
    	FluentInboundEvent event 	= new InboundWarmupEvent( );
    	
    	for( int i =ZERO; i < warmupSize; i++ ){
    		QUEUE.offer( event );
    		QUEUE.poll( );
    	}

    	QUEUE.clear();
    	LOGGER.info("Finished warming Inbound dispatch queue, fed [{}] events.", warmupSize );
    	
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
        return QUEUE.offer( event );
    }

    
    protected final int getQueueSize( ){
    	return QUEUE.size();
    }


    @Override
    public final void run( ){

        while( keepDispatching ){
           
        	try{

        		FluentInboundEvent event  = QUEUE.poll( );
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
