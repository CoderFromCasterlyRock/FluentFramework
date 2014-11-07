package com.fluent.framework.dispatcher.in;

import java.util.*;

import org.slf4j.*;
import org.cliffc.high_scale_lib.*;

import com.fluent.framework.dispatcher.core.*;
import com.fluent.framework.events.core.*;
import com.fluent.framework.events.in.*;
import com.fluent.framework.persistence.*;

import static com.fluent.framework.utility.ContainerUtil.*;


public abstract class InputEventDispatcher implements   AdminEventProvider,
                                                        LoopbackEventProvider,
														RequestEventProvider,
														ExecutionEventProvider,
														ReferenceDataEventProvider,
														MarketDataEventProvider{

	private final boolean persistEnabled;
	private final BackoffStrategy backoff;
	private final FluentPersister<FluentInputEvent> persister;
	private final Set<FluentInputEventType> recoverables;

    private final static Set<FluentInputEventListener> LISTENERS;

    private final static String NAME        = InputEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    static{
        LISTENERS = new NonBlockingHashSet<FluentInputEventListener>( );
    }

    
    public InputEventDispatcher( BackoffStrategy backoff ){
    	this( backoff, null, new HashSet<FluentInputEventType>() );
    }
    
   
    public InputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentInputEvent> persister, Set<FluentInputEventType> recoverables ){
        this.backoff        = backoff;
        this.persister      = persister;
        this.recoverables	= recoverables;
        this.persistEnabled = ( persister !=null );
    }

    
    public abstract void startDispatch( );
	public abstract void stopDispatch( );
        
	
	protected final boolean isPersistenceEnabled( ){
        return persistEnabled;
    }
	
	protected final BackoffStrategy getBackoff( ){
        return backoff;
    }


    protected final FluentPersister<FluentInputEvent> getPersister( ){
        return persister;
    }


    protected final void performRecovery( ){

    	if( !persistEnabled ) return;
    	
        List<FluentInputEvent> all 	= persister.retrieveAll( );
        if( all.isEmpty() ){
            LOGGER.info("Recovery finished as there are [0] input events to be recovered." );
            return;
        }

        long newEventId				= ZERO;
        
        for( FluentInputEvent event : all ){
        	newEventId				= event.getEventId();
        	
        	boolean recoverable 	= recoverables.contains( event.getType() );
        	if( !recoverable ) continue;
        	            
        	for( FluentInputEventListener listener : getListeners() ){
                boolean interested = listener.isSupported( event.getType() );
                if( !interested ) continue;

                listener.update( event );
            }

        }
        
        
        FluentInputEventId.set( newEventId );
        LOGGER.info("INPUT EventId SET to [{}].", newEventId );        

  
    }


    public final static int count( ){
        return LISTENERS.size();
    }

    
    protected final static Set<FluentInputEventListener> getListeners( ){
        return LISTENERS;
    }


    public final static boolean add( FluentInputEventListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "[#{} {}] ADDED as an Input event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final static boolean remove( FluentInputEventListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "[#{} {}] REMOVED as an Input event listener.", LISTENERS.size(), listener.name() );
        return removed;
    }
  
    
}
