package com.fluent.etrading.framework.events.out.response;

import org.slf4j.*;

import com.fluent.etrading.framework.core.*;
import com.fluent.etrading.framework.dispatcher.out.*;
import com.fluent.etrading.framework.events.core.*;


public class ResponseEventAdaptor implements FluentService, FluentOutputEventListener{


	private final FluentOutputEventType supported;
	
    private final static String NAME        = ResponseEventAdaptor.class.getSimpleName();
    private final static Logger LOGGER      = LoggerFactory.getLogger( NAME );


    public ResponseEventAdaptor(  ){
    	this.supported = FluentOutputEventType.EVENT_TO_TRADER;
    }


    @Override
	public final String name( ){
		return NAME;
	}


    @Override
    public final void init( ){
        OutputEventDispatcher.add( this );
        LOGGER.debug( "Successfully initialized [{}], will listen for [{}] and send it to Trader.", NAME, supported );
    }


    @Override
    public final boolean isSupported( final FluentOutputEventType type ){
        return ( FluentOutputEventType.EVENT_TO_TRADER == type );
    }


    @Override
    public final void update( final FluentOutputEvent event ){

    	String message		= event.toJSON( );
        LOGGER.debug( "TO TRADER >> {}", message );
		
	}

    
    @Override
    public final void stop( ){
        OutputEventDispatcher.remove( this );
        LOGGER.debug( "Successfully stopped [{}].", NAME );
    }
    

}
