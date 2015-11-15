package com.fluent.framework.core;

import java.util.Arrays;
import com.typesafe.config.Config;
import com.fluent.framework.config.*;

import static com.fluent.framework.util.FluentUtil.*;


public final class FluentContext{
	
	
	public enum Role{
	
		PRIMARY,
		SECONDARY;
		
		public static Role getRole( Config configuration ){

			Role role	    = null;
        	String name     = EMPTY;
            String prop     = "role";
            String path     = ConfigManager.FRAMEWORK_SECTION_KEY;

            try{
            	name    = configuration.getString( path + prop );
                role	= Role.valueOf( name );

            }catch( Exception e ){
                printUsageAndExit( prop, name, path, Arrays.deepToString( Role.values() ) );
            }

            return role;
		}
		


	}
	
	
	public enum Region{
    	
        NY,
        CHI,
        LON;

        public static Region getRegion( Config configuration ){
            
        	Region region   = null;
        	String name     = EMPTY;
            String prop     = "region";
            String path     = ConfigManager.FRAMEWORK_SECTION_KEY;

            try{
            	name    = configuration.getString( path + prop );
                region  = Region.valueOf( name );

            }catch( Exception e ){
                printUsageAndExit( prop, name, path, Arrays.deepToString( Region.values() ) );
            }

            return region;
        }

    }

    
    public enum Environment{

        SIMULATION,
        DEV,
        UAT,
        PROD;

        public static Environment getEnvironment( Config configuration ){
            
        	Environment env     = null;
        	String name         = EMPTY;
            String prop         = "environment";
            String path     	= ConfigManager.FRAMEWORK_SECTION_KEY;

            try{
                name    = configuration.getString( path + prop );
                env		= Environment.valueOf( name );

            }catch( Exception e ){
                printUsageAndExit( prop, name, path, Arrays.deepToString( Environment.values() ) );
            }

            return env;
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
	
	
    private final static void printUsageAndExit( String propName, String propValue, String propLocation, String choices ){

        StringBuilder usage = new StringBuilder( TWO * SIXTY_FOUR );
        usage.append( "[ERROR while starting Fluent Framework]" );
        usage.append( NEWLINE );
        usage.append( propName ).append( COLON ).append( propValue ).append(" is NOT valid!");
        usage.append( NEWLINE );
        usage.append( "Must specify " ).append( propLocation ).append( propName );
        usage.append( " = " ).append( choices );
        usage.append( NEWLINE );

        System.err.println( usage.toString() );
        System.exit( ONE );

    }
    


}
