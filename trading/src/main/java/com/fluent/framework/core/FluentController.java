package com.fluent.framework.core;

import org.slf4j.*;

import java.util.concurrent.*;

import com.fluent.framework.events.in.*;
import com.fluent.framework.events.out.*;

import static java.util.concurrent.TimeUnit.*;
import static com.fluent.framework.core.FluentContext.State.*;
import static com.fluent.framework.util.FluentUtil.*;


public final class FluentController implements FluentService{
	
	private final long time;
	private final TimeUnit unit;
	private final Object lock;
	private final InboundEventDispatcher input;
	private final OutboundEventDispatcher output;
		
	private final static String NAME        = FluentController.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
    public FluentController( InboundEventDispatcher input, OutboundEventDispatcher output ){
    	this( FIVE, SECONDS, input, output );
    }
    
    
	public FluentController( long time, TimeUnit unit, InboundEventDispatcher input, OutboundEventDispatcher output ){
		this.time	= time;
		this.unit	= unit;
		this.input	= input;
		this.output	= output;
		this.lock	= new Object();
	}

	
	@Override
	public final String name( ){
		return NAME;
	}
	
	
	@Override
	public final void init( ){
				
		try{
		
			synchronized( lock ){
			
				FluentContext.setState( RECOVERY );
				LOGGER.debug("Starting Event Controller, container is in [{}] state. Dispatches will be given [{}] [{}] for recovery.", FluentContext.getState(), time, unit );
			
				input.init();
				output.init();
						
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
			synchronized( lock ){
				input.stop( );
				output.stop( );
				LOGGER.debug("Successfully stopped {}.", NAME);
			}
			
		}catch( Exception e ){
			LOGGER.warn("Exception while stopping {}.", NAME);
			LOGGER.warn("Exception", e);
		}
	
	}
	
		
	
}
