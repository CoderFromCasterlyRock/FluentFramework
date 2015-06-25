package com.fluent.framework.persistence;

import org.slf4j.*;
import org.jctools.queues.*;

import java.util.concurrent.*;

import com.fluent.framework.core.*;
import com.fluent.framework.collection.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.events.in.FluentInboundEvent;
import com.fluent.framework.events.in.FluentInboundListener;
import com.fluent.framework.events.in.FluentInboundType;
import com.fluent.framework.events.in.InboundEventDispatcher;
import com.fluent.framework.events.in.InboundWarmupEvent;

import static com.fluent.framework.util.FluentUtil.*;


public final class InboundEventPersisterService implements Runnable, FluentService, FluentInboundListener{

	private volatile boolean keepDispatching;
	
	private final int eventCount;
	private final FluentPersister persister;
	private final ThreadPoolExecutor service;
	private final SpscArrayQueue<FluentEvent> eventQueue;
		
	private final static String NAME		= InboundEventPersisterService.class.getSimpleName();
    private final static Logger LOGGER     	= LoggerFactory.getLogger( NAME );


    public InboundEventPersisterService( int eventCount, FluentPersister persister ){
    	
    	this.eventCount 	= eventCount;
    	this.persister 		= persister;
    	this.eventQueue		= new SpscArrayQueue<>( eventCount );
    	this.service		= new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(2) );
        
    }


	@Override
    public final String name( ){
        return NAME;
    }

	
	@Override
	public final boolean isSupported( FluentInboundType type ){
		return true;
	}
	
	
	@Override
	public final void prime( ){
	    	
		FluentInboundEvent event = new InboundWarmupEvent();
		
    	for( int i =ZERO; i <( TWO * eventCount); i++ ){
    		eventQueue.offer( event );
    		eventQueue.poll( );
    	}

    	eventQueue.clear();
    	LOGGER.info("[{}] Finished warming, queue size [{}].", NAME, eventQueue.size() );
    }

    

    @Override
    public final void init( ){
    	
        keepDispatching = true;
        
        persister.init();
        service.prestartCoreThread();
        service.execute( this );
        InboundEventDispatcher.register( this );
        
        LOGGER.info("[{}] initialized, will listen and persit all input events.", NAME );
    }

    
    @Override
    public final boolean update( FluentInboundEvent event ){
		return eventQueue.offer( event );
    }
    
    
    @Override
    public final void run( ){

        while( keepDispatching ){

            try{
            	
            	FluentEvent event  = eventQueue.poll();
                if( event == null ){
                	FluentBackoffStrategy.apply( ONE );
                    continue;
                }

                persister.persist( event );
                
            }catch( Exception e ){
            	LOGGER.warn("Failed to persist event.", e );                  
            }
        }
    
    }

    
    public final void generateStats( ){
    	int retrievedSize = persister.retrieveAllEvents().size();
    	
    	if( retrievedSize != eventCount ){
    		//throw new RuntimeException("Only retriecved " + retrievedSize + "/" + eventCount );
    	}

    	LOGGER.info("Successfully retrieved {} events", eventCount );
    }
    
    
    @Override
    public final void stop( ){
    	
    	try{
    	     
    		keepDispatching = false;
    		persister.stop();
    	    service.shutdown();
    	
    	}catch( Exception e ){
    		LOGGER.warn("FAILED to stop.", e );
    	}
        
    }

}
