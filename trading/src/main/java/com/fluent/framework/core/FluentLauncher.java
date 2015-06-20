package com.fluent.framework.core;

import org.slf4j.*;
import java.lang.Thread.*;


public final class FluentLauncher{

	
	private final static String NAME	= FluentLauncher.class.getSimpleName();
	private final static Logger LOGGER	= LoggerFactory.getLogger( NAME );

	   
	static{
		
		Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler(){
			@Override
			public void uncaughtException( Thread thread, Throwable ex ){
				LOGGER.warn("CAUGHT unhandled exception in Thread [{}]", thread.getName() );
				LOGGER.warn("Exception: ", ex );
			}
		});
		
	}

    
	public static void main( String args [] ){

       	FluentConfiguration config 		= new FluentConfiguration( );
       	FluentController controller		= new FluentController( config );
       	controller.init();

    }


}
