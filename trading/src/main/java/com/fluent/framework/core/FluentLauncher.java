package com.fluent.framework.core;

import org.slf4j.*;

import com.fluent.framework.config.FluentConfiguration;

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
       	
       	Runtime.getRuntime().addShutdownHook( new FluentShutdownThread(controller) );
       	controller.init();

    }


	
	public final static class FluentShutdownThread extends Thread{

		private final FluentController controller;
		
		public FluentShutdownThread( FluentController controller ){
			this.controller = controller;
		}
	
		
		@Override
		public final void run( ){
			LOGGER.info("Shutdown hook called, will attempt to stop all services");
			controller.stop();
			LOGGER.info("Shutdown hook executed successfully.");
		}
	
	}

	
}
