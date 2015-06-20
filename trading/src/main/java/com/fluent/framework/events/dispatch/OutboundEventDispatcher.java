package com.fluent.framework.events.dispatch;

import org.slf4j.*;

import java.util.*;

import org.jctools.queues.*;

import java.util.concurrent.*;

import com.fluent.framework.core.*;
import com.fluent.framework.collection.*;
import com.fluent.framework.events.core.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class OutboundEventDispatcher implements FluentService, Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final static AbstractQueue<FluentOutboundEvent> queue;
    private final static List<FluentOutboundListener> LISTENERS;
    
    private final static int DEFAULT_SIZE   = SIXTY_FOUR * SIXTY_FOUR; 
    private final static String NAME        = OutboundEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );
    
    
    static{
    	queue      	= new MpscArrayQueue<>( DEFAULT_SIZE );
    	LISTENERS	= new CopyOnWriteArrayList<FluentOutboundListener>( );
    }
    
        
    public OutboundEventDispatcher( ){
    	this.executor   = Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }

   
    @Override
    public String name( ){
    	return NAME;
    }
    

    protected final void warmUp( FluentOutboundEvent event ){
    	
    	for( int i =ZERO; i <( TWO * DEFAULT_SIZE); i++ ){
    		queue.offer( event );
    		queue.poll( );
    	}

    	queue.clear();
    	
    }
    
    
    @Override
    public void init( ){
      
    	if( keepDispatching ){
    		LOGGER.warn("Attempted to start {} while it is already running.", NAME );
    		return;
    	}
    	
    	//warmUp( );
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
  
    

    public final static boolean enqueue( final FluentOutboundEvent event ){
        return queue.offer( event );
    }

    
    protected final int getQueueSize( ){
    	return queue.size();
    }


    protected void performPostOperation( FluentOutboundEvent event ){}
    

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
        		
        		performPostOperation( event );
        		
                
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
