package com.fluent.framework.core;

import org.slf4j.*;

import com.fluent.framework.admin.FluentStateManager;
import com.fluent.framework.events.core.FluentInboundEvent;
import com.fluent.framework.events.core.FluentInboundListener;
import com.fluent.framework.events.core.FluentInboundType;
import com.fluent.framework.events.dispatch.*;
import com.fluent.framework.internal.MetronomeEvent;
import com.fluent.framework.util.TimeUtil;

import static com.fluent.framework.util.FluentUtil.*;
import static com.fluent.framework.core.FluentContext.FluentState.*;


public final class FluentController implements FluentInboundListener, FluentService{
	
	
	private final FluentStateManager stateManager;
	private final InboundEventDispatcher input;
	private final OutboundEventDispatcher output;
		
	private final static String NAME        = FluentController.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );

    
    
	public FluentController( FluentConfiguration config ){
		
		this.stateManager	= new FluentStateManager( config );
		this.input			= new InboundEventDispatcher( );
		this.output			= new OutboundEventDispatcher( );
	}

	
	@Override
	public final String name( ){
		return NAME;
	}
	
	
	@Override
	public final boolean isSupported( FluentInboundType type ){
		return FluentInboundType.METRONOME_EVENT == type;
	}


	@Override
	public final boolean update( FluentInboundEvent event ){
		handleMetronomeEvent( event );
		return false;
	}
	

	@Override
	public final void init( ){
				
		try{
		
			long startTime 	= TimeUtil.currentMillis( );
			
			FluentStateManager.setState( INITIALIZING );
			LOGGER.debug("Attempting to START Fluent Framework {}.", FluentStateManager.getFrameworkInfo() );
			
			//Runtime.getRuntime().addShutdownHook(hook);
			startServices( );
						
			FluentStateManager.setState( RUNNING );
			long timeTaken 	= TimeUtil.currentMillis( ) - startTime;
			
			LOGGER.info( "Successfully STARTED Fluent Framework in [{}] ms.", timeTaken );
			LOGGER.info( "************************************************************** {}", NEWLINE );


        }catch( Exception e ){
        	LOGGER.error( "Fatal error while starting Fluent Framework." );
            LOGGER.error( "Exception: ", e );
            LOGGER.info( "************************************************************** {}", NEWLINE );
        	
            System.exit( ZERO );
            
        }

				
	}
	
	
	protected final void startServices( ){
		
		InboundEventDispatcher.register( this );
		
		output.init();
		input.init();
		stateManager.init();
	}
	
	
	protected final void stopServices( ){
		
		input.stop();
		output.stop();
		stateManager.stop();
		
	}
	
	protected final void handleMetronomeEvent( FluentInboundEvent event ){
		
		MetronomeEvent metroEvent 	= (MetronomeEvent) event;
		long secondsToClose			= metroEvent.getSecondsToClose();
		
		if( secondsToClose > 10 ){
			LOGGER.debug("Metronome event arrives, we have [{}] seconds to close.", secondsToClose );
			return;
		}
		
		LOGGER.info("STOPPING as we only have [{}] seconds to close.", secondsToClose );
		stop();
		
	}
	
	

	@Override
	public void stop( ){
		
		try{
			
			stopServices();
			
			LOGGER.debug("Successfully stopped {}.", NAME);
			
		}catch( Exception e ){
			LOGGER.warn("Exception while stopping {}.", NAME);
			LOGGER.warn("Exception", e);
		}
	
	}

		
	
}
