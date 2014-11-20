package com.fluent.etrading.framework.dispatcher.in;

import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

import com.fluent.etrading.framework.core.*;
import com.fluent.etrading.framework.dispatcher.core.*;
import com.fluent.etrading.framework.events.core.*;
import com.fluent.etrading.framework.events.in.*;
import com.fluent.etrading.framework.persistence.*;

import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class BlockingInputEventDispatcher extends InputEventDispatcher implements Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final BlockingQueue<FluentInputEvent> queue;

    private final static String NAME        = FluentInputEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
    public BlockingInputEventDispatcher( BackoffStrategy backoff ){
    	this( backoff, null );
    }

    public BlockingInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister ){
    	this( backoff, persister, new HashSet<FluentInputEventType>( Arrays.asList(FluentInputEventType.EXECUTION_REPORT_UPDATE) ) );
    }
        
    public BlockingInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables ){
        this( backoff, persister, recoverables, new ArrayBlockingQueue<FluentInputEvent>( FOUR * SIXTY_FOUR * SIXTY_FOUR ) );
    }

    public BlockingInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables, ArrayBlockingQueue<FluentInputEvent> queue ){
        super( backoff, persister, recoverables );

        this.queue      = queue;
        this.executor   = Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );
    }

   

    @Override
    public final void startDispatch( ){
        keepDispatching = true;
        executor.execute( this );

        LOGGER.info("Started [{}], using BlockingQueue with [{}] as backoff strategy.", NAME, getBackoff().description() );
    }
    

    @Override
    public final boolean addLoopbackEvent( final LoopbackEvent event ){
        return queue.offer( event );
    }


    @Override
    public final boolean addAdminEvent( final AdminEvent event ){
        return queue.offer( event );
    }


    @Override
    public final boolean addRequestEvent( final TraderDataEvent event ){
        return queue.offer( event );
    }


    @Override
    public final boolean addExecutionEvent( final ExecutionReportEvent event ){
        return queue.offer( event );
    }


    @Override
    public final boolean addReferenceDataEvent( final ReferenceDataEvent event ){
        return queue.offer( event );
    }


    @Override
    public final boolean addMarketDataEvent( final MarketDataEvent event ){
        return queue.offer( event );
    }


    @Override
    public final void run( ){

        performRecovery();
        Collection<FluentInputEvent> bucket = new ArrayList<FluentInputEvent>( SIXTY_FOUR );

        while( keepDispatching ){
           
        	try{

        		int countPolled         = queue.drainTo( bucket );
        		boolean nothingPolled   = ( countPolled == ZERO );
        		if( nothingPolled ){
        			getBackoff().apply( );
        			continue;
        		}

        		for( FluentInputEvent event : bucket ){

        			for( FluentInputEventListener listener : getListeners() ){
        				if( listener.isSupported(event.getType()) ){
        					listener.update(  event );
        				}
        			}
                
        			//  getPersister().persist( event );

        		}

        		if( countPolled > ZERO ){
        			bucket.clear();	
        		}
        		
        	}catch( Exception e ){
        		LOGGER.error("FAILED to dispatch input events.");
        		LOGGER.error("Exception:", e);
        	}
        	
        }

    }


    @Override
    public final void stopDispatch( ){
        keepDispatching = false;
        executor.shutdown();

        LOGGER.info("Successfully stopped {}.", NAME );
    }


}
