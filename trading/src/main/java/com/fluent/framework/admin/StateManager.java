package com.fluent.framework.admin;

import org.slf4j.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.fluent.framework.collection.*;
import com.fluent.framework.config.*;
import com.fluent.framework.core.*;
import com.fluent.framework.market.core.Exchange;
import com.fluent.framework.market.core.ExchangeDetails;
import com.fluent.framework.util.*;
import com.fluent.framework.core.FluentContext.FluentState;
import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.in.InEventDispatcher;

import static java.util.concurrent.TimeUnit.*;
import static com.fluent.framework.core.FluentContext.FluentState.*;


public final class StateManager implements Runnable, FluentLifecycle{

	private volatile boolean keepRunning;
	
	private final int delay;
	private final TimeUnit unit;
	private final ConfigManager config;
	
	private final InEventDispatcher inDispatcher;
	private final ScheduledExecutorService service;
	private final Map<Exchange, Set<TimedTask>> eTaskStatus;
		
	private final static AtomicReference<FluentState> APP_STATE;
	
	private final static int DEFAULT_DELAY		= 5;
	private final static TimeUnit DEFAULT_UNIT	= SECONDS;
	private final static String NAME			= StateManager.class.getSimpleName();
    private final static Logger LOGGER     		= LoggerFactory.getLogger( NAME );

    
    static{
    	APP_STATE = new AtomicReference<>( STOPPED );
    }

    
    public StateManager( ConfigManager config, InEventDispatcher inDispatcher ){
    	this( DEFAULT_DELAY, DEFAULT_UNIT, config, inDispatcher );
    }
    
    
    public StateManager( int delay, TimeUnit unit, ConfigManager config, InEventDispatcher inDispatcher ){
    	
    	this.delay	 		= delay;
    	this.unit			= unit;
    	this.config			= config;
    	this.inDispatcher	= inDispatcher;
    	this.eTaskStatus	= new HashMap<>( );
    	this.service		= Executors.newSingleThreadScheduledExecutor( new FluentThreadFactory(NAME) );
        
    }

    
	
	@Override
	public final String name( ){
		return NAME;
	}

	
	public static final boolean isRunning( ){
		return ( FluentState.RUNNING  == getState() );
	}
	

	public static final FluentState getState( ){
		return APP_STATE.get();
	}


	public static final FluentState setState( FluentState newState ){
		return APP_STATE.getAndSet( newState );
	}
	
	
	@Override
	public final void start( ){
		
		for( ExchangeDetails details : config.getExchangeDetailsMap().values() ){
			eTaskStatus.put(details.getExchange(), new HashSet<TimedTask>() );
		}
		
		keepRunning = true;
		service.scheduleAtFixedRate( this, delay, delay, unit );
		LOGGER.info("Monitoring Exchanges {}.", config.getExchangeDetailsMap().values()  );
		LOGGER.info("STARTED will publish Metronome events every {} {}.", delay, unit );
	
	}
	
	
	@Override
	public final void run( ){
		
		if( keepRunning ){
			sendMetronomeEvent( );
			checkExchangeClosingTasks();
		}
	
	}

	
	protected final void sendMetronomeEvent( ){
		
		long nowMillis 		= TimeUtil.currentMillis();
		long openMillis 	= config.getAppOpenTime();
		long closeMillis 	= config.getAppCloseTime();
		TimedTask timeTask	= TimedTask.getTask(nowMillis, openMillis, closeMillis );
		boolean isAppClose	= timeTask.isClosing();
		
		InEvent event 		= null;
		if( isAppClose ){
			event 	= new AdminClosingEvent(Exchange.ALL, TimedTask.CLOSING_TIME);
		}else{
			event 	= new MetronomeEvent();
		}
		
		LOGGER.info("{}", event );
		inDispatcher.enqueue( event );
		
	}
	
	
	protected final void checkExchangeClosingTasks( ){
		
		for( ExchangeDetails details : config.getExchangeDetailsMap().values() ){
			
			//Check if Exchange is closing
			Exchange exchange	= details.getExchange();
			Set<TimedTask> tSet	= eTaskStatus.get( exchange );
			if( tSet == null ){
				LOGGER.warn("Closing time isn't configured for Exchange [{}].", exchange);
				continue;
			}
				
			long nowMillis 		= TimeUtil.currentMillis();
			TimedTask timeTask	= TimedTask.getTask(nowMillis, details.getOpenMillis(), details.getCloseMillis() );
			boolean isClosing	= timeTask.isClosing();
			if( !isClosing ) continue;
				
			boolean alreadySent	= tSet.contains(timeTask);
			if( alreadySent ) continue;
				
			AdminClosingEvent event= new AdminClosingEvent( exchange, timeTask );
			tSet.add( timeTask );
			inDispatcher.enqueue(event);
				
			LOGGER.info("Sent closing time event [{}].", event);
				
		}
		
	}
	


	@Override
	public final void stop( ){
		keepRunning = false;
		service.shutdown();
	}
	
	
}
