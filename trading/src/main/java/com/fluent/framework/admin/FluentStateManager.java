package com.fluent.framework.admin;

import org.slf4j.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.fluent.framework.config.FluentConfiguration;
import com.fluent.framework.core.*;
import com.fluent.framework.internal.*;
import com.fluent.framework.util.FluentThreadFactory;
import com.fluent.framework.util.TimeUtil;
import com.fluent.framework.core.FluentContext.Environment;
import com.fluent.framework.core.FluentContext.FluentState;
import com.fluent.framework.events.in.FluentInboundEvent;
import com.fluent.framework.events.in.InboundEventDispatcher;

import static java.util.concurrent.TimeUnit.*;
import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.core.FluentContext.*;
import static com.fluent.framework.core.FluentContext.FluentState.*;


public final class FluentStateManager implements Runnable, FluentStartable{

	private volatile boolean keepRunning;
	
	private final int delay;
	private final TimeUnit unit;
	private final FluentConfiguration config;
	private final ScheduledExecutorService service;
		
	private final static AtomicReference<FluentState> APP_STATE;
	
	private final static int DEFAULT_DELAY		= 5;
	private final static TimeUnit DEFAULT_UNIT	= SECONDS;
	private final static String NAME			= FluentStateManager.class.getSimpleName();
    private final static Logger LOGGER     		= LoggerFactory.getLogger( NAME );

    
    static{
    	APP_STATE = new AtomicReference<>( STOPPED );
    }

    
    public FluentStateManager( FluentConfiguration config ){
    	this( DEFAULT_DELAY, DEFAULT_UNIT, config );
    }
    
    
    public FluentStateManager( int delay, TimeUnit unit, FluentConfiguration config ){
    	
    	this.delay	 	= delay;
    	this.unit		= unit;
    	this.config		= config;
    	this.service	= Executors.newSingleThreadScheduledExecutor( new FluentThreadFactory(NAME) );
        
    }

    
	
	@Override
	public final String name( ){
		return NAME;
	}

	
	@Override
	public final void init( ){
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
		FluentInboundEvent metro = new MetronomeEvent( secondsToClose );
		
		InboundEventDispatcher.enqueue( metro );
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
	
	
	public static final String getFrameworkInfo( ){

        StringBuilder builder  = new StringBuilder( TWO * SIXTY_FOUR );
        
        builder.append( L_BRACKET );
        builder.append( "AppName:" ).append( getAppName() );
        builder.append( ", Environment:" ).append( getEnvironment() );
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
