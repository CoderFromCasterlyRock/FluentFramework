package com.fluent.etrading.framework.core;

import java.lang.management.*;
import java.util.concurrent.atomic.*;

import static com.fluent.etrading.framework.core.FluentLocale.*;
import static com.fluent.etrading.framework.core.FluentLocale.State.*;
import static com.fluent.etrading.framework.utility.ContainerUtil.*;


public final class FluentContext{

	private final static AtomicReference<State> STATE;
		
	static{
		STATE = new AtomicReference<State>( INITIALIZING );
	}
	
	
	public static final boolean isProd( ){
        return ( Environment.PROD == getEnvironment() );
    }
	
	
	public static final boolean isReady( ){
		return ( State.RUNNING == STATE.get() );
	}
		
	
	public static final State getState( ){
		return STATE.get();
	}
	
	
	public static final State setState( State newState ){
		return STATE.getAndSet( newState );
	}
	
	
	public static final Region getRegion( ){
        return Region.getRegion();
    }

	
	public static final Environment getEnvironment( ){
        return Environment.getEnvironment();
    }

	
	public static final Instance getInstance( ){
        return Instance.getInstance();
    }

    
	public static final String getContainerInfo( ){

        StringBuilder builder  = new StringBuilder( TWO * SIXTY_FOUR );
        String processName     = ManagementFactory.getRuntimeMXBean().getName();

        builder.append( L_BRACKET );
        builder.append( "Instance:" ).append( getInstance() );
        builder.append( ", Environment:" ).append( getEnvironment() );
        builder.append( ", Region:" ).append( getRegion() );
        builder.append( ", State:" ).append( getState() );
        builder.append( ", Process:" ).append( processName );
        builder.append( R_BRACKET );

        return builder.toString();

    }

}
