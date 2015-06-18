package com.fluent.framework.core;

import java.util.Arrays;
import java.util.concurrent.atomic.*;

import static com.fluent.framework.core.FluentContext.State.*;
import static com.fluent.framework.util.FluentUtil.*;


public final class FluentContext{
	
    public enum Region{
        NA,
        EMEA;

        public static Region getRegion( ){
            String name     = EMPTY;
            Region region   = null;
            String prop     = "fluent.framework.region";

            try{
                name    = System.getProperty( prop );
                region  = Region.valueOf( name );

            }catch( Exception e ){
                printUsageAndExit( prop, name, Arrays.deepToString( Region.values() ) );
            }

            return region;
        }

    }

    
    public enum Environment{

        SIMULATION,
        DEV,
        UAT,
        PROD;

        public static Environment getEnvironment( ){
            String name         = EMPTY;
            Environment env     = null;
            String prop         = "fluent.framework.environment";

            try{
                name    = System.getProperty( prop );
                env		= Environment.valueOf( name );

            }catch( Exception e ){
                printUsageAndExit( prop, name, Arrays.deepToString( Environment.values() ) );
            }

            return env;
        }

    }

    
    public enum Instance{
        
    	FLUENT_OUTRIGHT,
        FLUENT_STRATEGY,
        FLUENT_EXCHANGE,
    	FLUENT_MARKETDATA;

        public static Instance getInstance( ){
            String name         = EMPTY;
            Instance instance   = null;
            String prop         = "fluent.framework.instance";

            try{
                name        = System.getProperty( prop );
                instance    = Instance.valueOf( name );

            }catch( Exception e ){
                printUsageAndExit( prop, name, Arrays.deepToString( Instance.values() ) );
            }

            return instance;
        }

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
        
        builder.append( L_BRACKET );
        builder.append( "Environment:" ).append( getEnvironment() );
        builder.append( ", Region:" ).append( getRegion() );
        builder.append( ", Instance:" ).append( getInstance() );
        builder.append( ", State:" ).append( FluentContext.getState() );
        builder.append( ", Process:" ).append( getFullProcessName() );
        builder.append( R_BRACKET );

        return builder.toString();

    }
	

    private static void printUsageAndExit( String propName, String propValue, String choices ){

        StringBuilder usage = new StringBuilder( TWO * SIXTY_FOUR );
        usage.append( "[ERROR while starting Fluent Framework]" );
        usage.append( NEWLINE );
        usage.append( propName ).append( COLON ).append( propValue ).append(" is NOT valid!");
        usage.append( NEWLINE );
        usage.append( "Must specify one of these as a VM argument (-D): " ).append( choices );
        usage.append( NEWLINE );

        System.err.println( usage.toString() );
        System.exit( ONE );

    }
    
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
