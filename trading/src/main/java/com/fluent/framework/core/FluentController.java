package com.fluent.framework.core;

import org.slf4j.*;

import java.util.concurrent.*;

import com.fluent.framework.dispatcher.core.BackoffStrategy;
import com.fluent.framework.dispatcher.core.ParkingBackoffStrategy;
import com.fluent.framework.dispatcher.in.InputEventDispatcher;
import com.fluent.framework.dispatcher.out.OutputEventDispatcher;

import static java.util.concurrent.TimeUnit.*;
import static com.fluent.framework.core.FluentLocale.State.*;
import static com.fluent.framework.utility.ContainerUtil.*;


public final class FluentController implements FluentService{
	
	private final long time;
	private final TimeUnit unit;
	private final Object LOCK;
	private final InputEventDispatcher input;
	private final OutputEventDispatcher output;
		
	private final static String NAME        = FluentController.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
    public FluentController( InputEventDispatcher input, OutputEventDispatcher output ){
    	this( FIVE, SECONDS, input, output );
    }
    
    
	public FluentController( long time, TimeUnit unit, InputEventDispatcher input, OutputEventDispatcher output ){
		this.time	= time;
		this.unit	= unit;
		this.input	= input;
		this.output	= output;
		this.LOCK	= new Object();
	}

	@Override
	public final String name( ){
		return NAME;
	}
	
	@Override
	public final void init( ){
				
		try{
		
			synchronized( LOCK ){
			
				FluentContext.setState( RECOVERY );
				LOGGER.debug("Starting Event Controller, container is in [{}] state. Dispatches will be given [{}] [{}] for recovery.", FluentContext.getState(), time, unit );
			
				input.startDispatch();
				output.startDispatch();
		
				BackoffStrategy bOff	= new ParkingBackoffStrategy( ONE, SECONDS );
				long sleepDuration		= MILLISECONDS.convert( time, unit );
				long startTime 		 	= System.currentTimeMillis();
					
				while( (System.currentTimeMillis() - startTime ) < sleepDuration ){
					bOff.apply();
				}
			
				FluentContext.setState( RUNNING );
				LOGGER.debug("Successfully completed recovery, container is in [{}] state!", FluentContext.getState() );
				
			}
			
		}catch( Exception e ){
			LOGGER.warn("Exception while performing recovery.");
			LOGGER.warn("Exception", e);
		}
				
	}
	

	@Override
	public void stop( ){
		
		try{
			synchronized( LOCK ){
				input.stopDispatch( );
				output.stopDispatch( );
				LOGGER.debug("Successfully stopped {}.", NAME);
			}
			
		}catch( Exception e ){
			LOGGER.warn("Exception while stopping {}.", NAME);
			LOGGER.warn("Exception", e);
		}
	
	}
	
		
	
}
