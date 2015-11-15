package com.fluent.framework.algo;

import org.slf4j.*;

import com.fluent.framework.admin.*;
import com.fluent.framework.core.*;
import com.fluent.framework.events.in.*;


public abstract class FluentAlgoManager implements InListener, FluentLifecycle{

	private final String name;
	private final FluentServiceManager services;
	
    private final static String NAME        = FluentAlgoManager.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    public FluentAlgoManager( String name, FluentServiceManager services ){
        this.name	 	= name;
    	this.services	= services;
    }

    
    protected abstract void handleNewStrategy( InEvent inputEvent );
    protected abstract void handleModifyStrategy( InEvent inputEvent );
    protected abstract void handleCancelStrategy( InEvent inputEvent );
    protected abstract void handleExecutionReport( InEvent inputEvent );
    protected abstract void handleMarketMessage( InEvent inputEvent );
    protected abstract void handleMetronomeEvent( InEvent inputEvent );
    
    
    @Override
    public final String name( ){
        return name;
    }
    
	
    @Override
    public final boolean isSupported( final InType type ){
        return true;
    }
    

    @Override
    public final void start( ){
        services.getInDispatcher().register( this );
        LOGGER.info("[{}] initialized, listening for [{}].", name, InType.values() );
    }


    @Override
    public final boolean inUpdate( final InEvent inputEvent ){

    	if( !StateManager.isRunning() ){
    		LOGGER.warn("Discarding event [{}] as we are currently in [{}] state!", inputEvent, StateManager.getState() );
    		return false;
    	}
    	
    	InType type 	= inputEvent.getType( );

        switch( type ){

            case METRONOME_EVENT:
                handleMetronomeEvent( inputEvent );
            break;

            case NEW_STRATEGY:
                handleNewStrategy( inputEvent );
            break;

            case MODIFY_STRATEGY:
            	handleModifyStrategy( inputEvent );
            break;

            case CANCEL_STRATEGY:
            	handleCancelStrategy( inputEvent );
            break;

            case EXECUTION_REPORT:
                handleExecutionReport( inputEvent );
            break;

            case MARKET_DATA:
                handleMarketMessage( inputEvent );
            break;

            default:
                LOGGER.warn( "Input event of type [{}] is unsupported.", type );
        }

        return true;
        
    }

    
    
    @Override
    public final void stop(){
    	services.getInDispatcher().deregister( this );
        LOGGER.info("Stopped [{}].", NAME );
    }


}

