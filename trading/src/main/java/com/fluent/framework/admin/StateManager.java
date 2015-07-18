package com.fluent.framework.admin;

import org.slf4j.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.fluent.framework.collection.FluentThreadFactory;
import com.fluent.framework.config.*;
import com.fluent.framework.core.*;
import com.fluent.framework.util.*;
import com.fluent.framework.core.FluentContext.Environment;
import com.fluent.framework.core.FluentContext.FluentState;
import com.fluent.framework.events.in.InEvent;
import com.fluent.framework.events.in.InEventDispatcher;

import static java.util.concurrent.TimeUnit.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.util.FluentToolkit.*;
import static com.fluent.framework.core.FluentContext.*;
import static com.fluent.framework.core.FluentContext.FluentState.*;


public final class StateManager implements Runnable, FluentService{

	private volatile boolean keepRunning;
	
	private final int delay;
	private final TimeUnit unit;
	private final ConfigManager config;
	private final InEventDispatcher inDispatcher;
	private final ScheduledExecutorService service;
		
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
    	this.service		= Executors.newSingleThreadScheduledExecutor( new FluentThreadFactory(NAME) );
        
    }

    
	
	@Override
	public final String name( ){
		return NAME;
	}

	
	@Override
	public final void start( ){
		keepRunning = true;
		service.scheduleAtFixedRate( this, delay, delay, unit );
		LOGGER.info("Successfully started Application state manager, generating Metronome events every {} {}.", delay, unit );
	
	}
	
	
	@Override
	public final void run( ){
		
		if( keepRunning ){
			sendMetronomeEvent( );
		}
	
	}

	
	protected final void sendMetronomeEvent( ){
		long secondsToClose		 = getSecondsToClose( );
		InEvent metro = new MetronomeEvent( secondsToClose );
		
		inDispatcher.enqueue( metro );
	}
	
	
	protected final long getSecondsToClose( ){
		
		long closeSeconds	= ZERO;
		
		try{
		
			long nowMillis 		= TimeUtil.currentMillis( );
			long closeMillis 	= config.getAppCloseTime();
			long timeRemaining 	= closeMillis - nowMillis;
			closeSeconds		= SECONDS.convert(timeRemaining, MILLISECONDS);
			
		}catch( Exception e ){
			LOGGER.info("Exception: ", e);
		}
		
		return closeSeconds;
	}
	
	
	public final boolean isWithinTradingDay( ){
		return isWithinTradingDay( TimeUtil.currentMillis() );
	}
	
	
	protected final boolean isWithinTradingDay( long nowMillis ){
		
		long openMillis 	= config.getAppOpenTime();
		long closeMillis 	= config.getAppCloseTime();
		
		return ( openMillis < nowMillis && nowMillis < closeMillis );
	}
	
	
	public static final boolean isProd( ){
        return ( Environment.PROD == getEnvironment() );
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
	
	
	public final static String getFrameworkInfo( ){

        StringBuilder builder  = new StringBuilder( TWO * SIXTY_FOUR );
        
        builder.append( L_BRACKET );
        builder.append( "Environment:" ).append( getEnvironment() );
        builder.append( ", Region:" ).append( getRegion() );
        builder.append( ", Instance:" ).append( getInstance() );
        builder.append( ", State:" ).append( getState() );
        builder.append( ", Process:" ).append( getFullProcessName() );
        builder.append( R_BRACKET );

        return builder.toString();

    }
	

	@Override
	public final String toString( ){
		return getFrameworkInfo();
	}
	

	@Override
	public final void stop( ){
		keepRunning = false;
		service.shutdown();
	}
	
	
}
