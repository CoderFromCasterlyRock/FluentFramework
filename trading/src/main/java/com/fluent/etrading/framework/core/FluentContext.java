package com.fluent.etrading.framework.core;

import java.util.concurrent.atomic.*;

import static com.fluent.etrading.framework.core.FluentLocale.*;
import static com.fluent.etrading.framework.core.FluentContext.State.*;


public final class FluentContext{
	
	
	public enum State{
		
		INITIALIZING 	("Initializing"),
		RECOVERY		("Recovering"),
		PAUSED			("Paused"),
		CANCEL_ONLY		("Cancel Only"),
		RUNNING			("Running");
		
		private final String description;
		
		private State( String description ){
			this.description = description;
		}
		
		public final String getDescription( ){
			return description;
		}
		
	}
	

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
	
	

}
