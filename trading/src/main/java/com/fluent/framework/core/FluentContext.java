package com.fluent.framework.core;

import java.util.Arrays;

import static com.fluent.framework.util.FluentUtil.*;


public final class FluentContext{
	
	
	
	public enum APP{
	
		NAME;
		
		public static String getName( ){

			String prop		= "fluent.framework.appname";
			String name		= System.getProperty( prop );
                
			if( isBlank(name) ){
               	StringBuilder usage = new StringBuilder( TWO * SIXTY_FOUR );
               	usage.append( "[ERROR while starting Fluent Framework]" );
               	usage.append( NEWLINE );
               	usage.append( prop ).append( COLON ).append( name ).append(" is NOT valid!");
               	usage.append( NEWLINE );
               	usage.append( "Must specify Application name as a VM argument (-D): " );
               	usage.append( NEWLINE );

               	System.err.println( usage.toString() );
                System.exit( ONE );
            }

    		return name;
		}
		


	}
	
	
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
        FLUENT_STRATEGY;
        
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

    
	public enum FluentState{
		
		INITIALIZING 	("Initializing"),
		WARMING_UP		("Warming up"),
		RUNNING			("Running"),
		PAUSED			("Paused"),
		CANCEL_ONLY		("Cancel Only"),
		STOPPING		("Stopping"),
    	STOPPED			("Stopped");
		
		private final String description;
		
		private FluentState( String description ){
			this.description = description;
		}
		
		public final String getDescription( ){
			return description;
		}
		
	}
	
	
	public final static String getAppName( ){
		return APP.getName();
	}
	
	
	
	public final static  Region getRegion( ){
        return Region.getRegion();
    }

	
	public final static  Environment getEnvironment( ){
        return Environment.getEnvironment();
    }

	
	public final static  Instance getInstance( ){
        return Instance.getInstance();
    }


    private final static void printUsageAndExit( String propName, String propValue, String choices ){

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
    


}
