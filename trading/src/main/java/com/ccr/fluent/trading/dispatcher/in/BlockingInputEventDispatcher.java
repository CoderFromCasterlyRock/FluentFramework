package com.ccr.fluent.trading.dispatcher.in;

import org.slf4j.*;
import java.util.*;
import java.util.concurrent.*;

import com.ccr.fluent.trading.core.*;
import com.ccr.fluent.trading.events.in.*;
import com.ccr.fluent.trading.events.core.*;
import com.ccr.fluent.trading.persistence.*;
import com.ccr.fluent.trading.collections.*;
import com.ccr.fluent.trading.dispatcher.core.*;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;


public final class BlockingInputEventDispatcher extends InputEventDispatcher implements Runnable{

    private volatile boolean keepDispatching;

    private final ExecutorService executor;
    private final FluentQueue<FluentInputEvent> queue;

    private final static String NAME        = FluentInputEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
    public BlockingInputEventDispatcher( BackoffStrategy backoff ){
    	this( backoff, null );
    }

    public BlockingInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister ){
    	this( backoff, persister, new HashSet<FluentInputEventType>( Arrays.asList(FluentInputEventType.EXECUTION_REPORT_UPDATE) ) );
    }
        
    public BlockingInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables ){
        this( backoff, persister, recoverables, new FluentBlockingQueue<FluentInputEvent>( FOUR * SIXTY_FOUR * SIXTY_FOUR ) );
    }

    public BlockingInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables, FluentBlockingQueue<FluentInputEvent> queue ){
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
            dispatch( bucket );
        }

    }


    protected final int dispatch( final Collection<FluentInputEvent> bucket ){

        int dispatchedCount         = ZERO;

        try{

            int countPolled         = queue.drainTo( bucket );
            boolean nothingPolled   = ( countPolled == ZERO );
            if( nothingPolled ){
                getBackoff().apply( );
                return NEGATIVE_ONE;
            }

            
            for( FluentInputEvent event : bucket ){

                for( FluentInputEventListener listener : getListeners() ){
                    if( listener.isSupported(event.getType()) ){
                        listener.update(  event );
                        ++dispatchedCount;
                    }
                }
                
              //  getPersister().persist( event );

            }

            if( dispatchedCount == ZERO ){
                LOGGER.warn( "DEAD EVENT! Valid input events arrived but no listeners are registered!" );
            }
            

        }catch( Exception e ){
            LOGGER.error("FAILED to dispatch input events.");
            LOGGER.error("Exception:", e);

        }finally{
            bucket.clear();
        }

        return dispatchedCount;

    }


    @Override
    public final void stopDispatch( ){
        keepDispatching = false;
        executor.shutdown();

        LOGGER.info("Successfully stopped {}.", NAME );
    }


}
