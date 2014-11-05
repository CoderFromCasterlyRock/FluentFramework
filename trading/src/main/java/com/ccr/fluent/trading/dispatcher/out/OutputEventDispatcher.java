package com.ccr.fluent.trading.dispatcher.out;

import org.slf4j.*;
import java.util.*;

import org.cliffc.high_scale_lib.*;

import com.ccr.fluent.trading.persistence.*;
import com.ccr.fluent.trading.events.core.*;
import com.ccr.fluent.trading.dispatcher.core.*;
import com.ccr.fluent.trading.events.out.order.*;
import com.ccr.fluent.trading.events.out.response.*;

import static com.ccr.fluent.trading.utility.ContainerUtil.*;


public abstract class OutputEventDispatcher implements OrderEventProvider, ResponseEventProvider{

	private final boolean persistEnabled;
    private final BackoffStrategy backoff;
    private final FluentPersister<FluentOutputEvent> persister;
    private final Set<FluentOutputEventType> recoverables;

    private final static Set<FluentOutputEventListener> LISTENERS;
    private final static String NAME        = OutputEventDispatcher.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    static{
        LISTENERS   = new NonBlockingHashSet<FluentOutputEventListener>( );
    }

    public OutputEventDispatcher( BackoffStrategy backoff ){
    	this( backoff, null, new HashSet<FluentOutputEventType>() );
    }
    
    public OutputEventDispatcher( BackoffStrategy backoff, FluentPersister<FluentOutputEvent> persister, Set<FluentOutputEventType> recoverables ){
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

    protected final FluentPersister<FluentOutputEvent> getPersister( ){
        return persister;
    }

    protected final void performRecovery( ){

    	if( !persistEnabled ) return;
    	
    	List<FluentOutputEvent> all 	= persister.retrieveAll( );
        if( all.isEmpty() ){
            LOGGER.info("Recovery finished as there are no output events to be recovered." );
            return;
        }

        long newEventId				= ZERO;
        
        for( FluentOutputEvent event : all ){
        	newEventId				= event.getEventId();
        	
        	boolean recoverable 	= recoverables.contains( event.getType() );
        	if( !recoverable ) continue;
        	
            for( FluentOutputEventListener listener : getListeners() ){
                boolean interested = listener.isSupported( event.getType() );
                if( !interested ) continue;

                listener.update( event );
            }

        }
        
        FluentOutputEventId.set( newEventId );
        LOGGER.info("OUTPUT EventId SET to [{}].", newEventId );
        

    }


    public final static int count(  ){
        return LISTENERS.size();
    }

    
    protected final static Set<FluentOutputEventListener> getListeners(  ){
        return LISTENERS;
    }
    

    public final static boolean add( FluentOutputEventListener listener ){
        boolean added = LISTENERS.add( listener );
        LOGGER.debug( "ADDED [#{} {}] as an Output event listener.", LISTENERS.size(), listener.name() );

        return added;
    }


    public final static boolean remove( FluentOutputEventListener listener ){
        boolean removed = LISTENERS.remove( listener );
        LOGGER.debug( "REMOVED [#{} {}] as an Output event listener.", LISTENERS.size(), listener.name() );

        return removed;

    }


}
