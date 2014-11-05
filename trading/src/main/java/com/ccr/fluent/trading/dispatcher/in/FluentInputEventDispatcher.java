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


/**
 *
 * SmartInputEventDispatcher uses a "smart" design to distribute events that arrive from multiple sources (threads) to listeners.
 * It provides higher throughput and lower latency by relaxing mutual-exclusion provided by locks in <link>BlockingQueue</link> and by introducing "single-writer" principal.
 *
 * Currently, the input events expected by SmartInputEventDispatcher are:

 * AdminEvent       = Events generated by a JMX page or any other administrative tool.
 * LoopbackEvent    = Events send by the output dispatcher
 * RequestEvent     = Events send by the traders using the GUI.
 * ExecutionEvent   = Events containing execution report from the market.
 * ReferenceEvent   = Events containing reference data for instruments.
 * MarketDataEvent  = Events containing Market data.
 *
 * NOTE:
 * The correctness of this class relies on the "single-writer" assumption.
 * Each of the aforementioned events are delivered to this class using ONE single thread.
 * If this assumption doesn't hold true, then switch to the <link>BlockingInputEventDispatcher</link>
 *
 */

public final class FluentInputEventDispatcher extends InputEventDispatcher implements Runnable{

    private volatile boolean keepDispatching;
    
    private final ExecutorService executor;
    private final FluentQueue<FluentInputEvent> adminQueue;
    private final FluentQueue<FluentInputEvent> loopbackQueue;
    private final FluentQueue<FluentInputEvent> requestQueue;
    private final FluentQueue<FluentInputEvent> executionQueue;
    private final FluentQueue<FluentInputEvent> referenceQueue;
    private final FluentQueue<FluentInputEvent> marketDataQueue;

    private final static int DEFAULT_SIZE	= FOUR * SIXTY_FOUR * SIXTY_FOUR;
    private final static String NAME        = FluentInputEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    public FluentInputEventDispatcher( BackoffStrategy backoff ){
    	this( backoff, null );
    }
    
    public FluentInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister ){
    	this( backoff, persister, new HashSet<FluentInputEventType>( Arrays.asList(FluentInputEventType.EXECUTION_REPORT_UPDATE) ) );
    }
    
    public FluentInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables ){
        this( backoff, persister, recoverables, new int[]{DEFAULT_SIZE,DEFAULT_SIZE,DEFAULT_SIZE,DEFAULT_SIZE,DEFAULT_SIZE,DEFAULT_SIZE} );
    }
    
    public FluentInputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables, int[] capacities ){
        super( backoff, persister, recoverables );

        this.adminQueue         = new FluentSPSCQueue<FluentInputEvent>( capacities[0] );
        this.executionQueue     = new FluentSPSCQueue<FluentInputEvent>( capacities[1] );
        this.loopbackQueue      = new FluentSPSCQueue<FluentInputEvent>( capacities[2] );
        this.marketDataQueue    = new FluentSPSCQueue<FluentInputEvent>( capacities[3] );
        this.referenceQueue     = new FluentSPSCQueue<FluentInputEvent>( capacities[4] );
        this.requestQueue       = new FluentSPSCQueue<FluentInputEvent>( capacities[5] );
        this.executor           = Executors.newSingleThreadExecutor( new FluentThreadFactory(NAME) );

    }

    
    @Override
    public final void startDispatch( ){
        keepDispatching = true;
        executor.execute( this );

        LOGGER.info("[{}] started, using SPCQueues with [{}] as backoff strategy.", NAME, getBackoff().description() );
    }


    @Override
    public final boolean addAdminEvent( final AdminEvent event ){
        return adminQueue.offer( event );
    }


    @Override
    public final boolean addLoopbackEvent( final LoopbackEvent event ){
        return loopbackQueue.offer( event );
    }


    @Override
    public final boolean addRequestEvent( final TraderDataEvent event ){
        return requestQueue.offer( event );
    }


    @Override
    public final boolean addExecutionEvent( final ExecutionReportEvent event ){
        return executionQueue.offer( event );
    }


    @Override
    public final boolean addReferenceDataEvent( final ReferenceDataEvent event ){
        return referenceQueue.offer( event );
    }


    @Override
    public final boolean addMarketDataEvent( final MarketDataEvent event ){
        return marketDataQueue.offer( event );
    }


    @Override
    public final void run( ){

    	performRecovery( );

        while( keepDispatching ){
            polledDispatch();
        }

    }


    /**
     * NOTE: Do we need to prioritize listeners?
     * Currently, the first listener will gets all events before the second one gets any.
     */
     protected final int polledDispatch( ){

        int dispatchedCount         = ZERO;

        try{

            FluentInputEvent adEvent = adminQueue.poll( );
            FluentInputEvent loEvent = loopbackQueue.poll( );
            FluentInputEvent rfEvent = referenceQueue.poll( );
            FluentInputEvent exEvent = executionQueue.poll( );
            FluentInputEvent rqEvent = requestQueue.poll( );
            FluentInputEvent mdEvent = marketDataQueue.poll( );

            boolean nothingPolled   = (adEvent == null && loEvent == null && rfEvent == null && exEvent == null && rqEvent == null && mdEvent == null);
            if( nothingPolled ){
                getBackoff().apply();
                return NEGATIVE_ONE;
            }


            for( FluentInputEventListener listener : getListeners() ){

                if( adEvent != null && listener.isSupported(adEvent.getType()) ){
                    listener.update( adEvent );
                    ++dispatchedCount;
                }

                if( loEvent != null && listener.isSupported(loEvent.getType()) ){
                    listener.update( loEvent );
                    ++dispatchedCount;
                }

                if( rfEvent != null && listener.isSupported(rfEvent.getType()) ){
                    listener.update( rfEvent );
                    ++dispatchedCount;
                }

                if( exEvent != null && listener.isSupported(exEvent.getType()) ){
                    listener.update( exEvent );
                    ++dispatchedCount;
                }

                if( rqEvent != null && listener.isSupported(rqEvent.getType()) ){
                    listener.update( rqEvent );
                    ++dispatchedCount;
                }

                if( mdEvent != null && listener.isSupported(mdEvent.getType()) ){
                    listener.update( mdEvent );
                    ++dispatchedCount;
                }

                if( dispatchedCount == ZERO ){
                    LOGGER.warn( "DEAD EVENT! Valid input events arrived but no listeners are interested!" );
                }


                //getPersister().persistAll( adEvent, loEvent, rfEvent, exEvent, rqEvent, mdEvent );

            }

        }catch( Exception e ){
            LOGGER.error("FAILED to dispatch input events.");
            LOGGER.error("Exception:", e);
        }

        return dispatchedCount;

    }


    @Override
    public final void stopDispatch( ){
        keepDispatching = false;
        executor.shutdown();

        LOGGER.info("[{}], sucessfully STOPPED", NAME );
    }


}
